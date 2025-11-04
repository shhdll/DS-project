import java.io.*;
import java.util.Scanner;

public class ManagementSystem {

    public static void main(String[] args) {
        Customers customers = new Customers();
        Products products = new Products();
        Scanner sc = new Scanner(System.in);

        // 1. Load Customers
        try (BufferedReader br = new BufferedReader(new FileReader("customers.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int customerId = Integer.parseInt(parts[0]);
                String name = parts[1];
                String email = parts[2];
                customers.registerCustomer(customerId, name, email);
            }
        } catch (IOException e) {
            System.out.println("Error reading customers.csv: " + e.getMessage());
        }

        // 2. Load Products
        try (BufferedReader br = new BufferedReader(new FileReader("products.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int productId = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                int stock = Integer.parseInt(parts[3]);
                Product p = new Product(productId, name, price, stock);
                products.addProduct(p);
            }
        } catch (IOException e) {
            System.out.println("Error reading products.csv: " + e.getMessage());
        }

        // 3. Load Orders
        try (BufferedReader br = new BufferedReader(new FileReader("orders.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int orderId = Integer.parseInt(parts[0]);
                int customerId = Integer.parseInt(parts[1]);
                String[] productIds = parts[2].split(";");
                LinkedList<Product> productList = new LinkedList<>();
                double total = 0;
                for (String pid : productIds) {
                    Product p = products.findProductById(Integer.parseInt(pid));
                    if (p != null) {
                        productList.insert(p);
                        total += p.getPrice();
                    }
                }
                String date = parts[4];
                String status = parts[5];
                Order o = new Order(orderId, customerId, productList, date);
                o.setStatus(status);
                o.setTotalPrice(total);
                customers.placeOrder(customerId, orderId, productList, date);
            }
        } catch (IOException e) {
            System.out.println("Error reading orders.csv: " + e.getMessage());
        }

        // 4. Load Reviews
        try (BufferedReader br = new BufferedReader(new FileReader("reviews.csv"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int productId = Integer.parseInt(parts[1]);
                int customerId = Integer.parseInt(parts[2]);
                int rating = Integer.parseInt(parts[3]);
                String comment = parts[4];
                try {
                    Review r = new Review(rating, comment);
                    products.addReview(r);
                    CustomerRecord c = customers.findCustomerById(customerId);
                    if (c != null) c.addReview(r);
                } catch (InvalidRatingException e) {
                    System.out.println("Invalid rating in CSV: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading reviews.csv: " + e.getMessage());
        }

        // 5. Menu
        while (true) {
            System.out.println("\n--- E-Commerce System Menu ---");
            System.out.println("1. View all customers");
            System.out.println("2. View all products");
            System.out.println("3. Place new order");
            System.out.println("4. Add product");
            System.out.println("5. Add customer");
            System.out.println("6. Add review");
            System.out.println("7. View order history for a customer");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    Node<CustomerRecord> tmp = customers.getAllCustomers().getHead();
                    while (tmp != null) {
                        System.out.println(tmp.data);
                        tmp = tmp.next;
                    }
                }
                case 2 -> {
                    Node<Product> tmp = products.getAllProducts().getHead();
                    while (tmp != null) {
                        tmp.data.displayProductDetails();
                        tmp = tmp.next;
                    }
                }
                case 3 -> {
                    System.out.print("Customer ID: ");
                    int cid = Integer.parseInt(sc.nextLine());
                    System.out.print("Order ID: ");
                    int oid = Integer.parseInt(sc.nextLine());
                    LinkedList<Product> orderProducts = new LinkedList<>();
                    System.out.print("Enter product IDs separated by ;: ");
                    String[] pids = sc.nextLine().split(";");
                    double total = 0;
                    for (String pid : pids) {
                        Product p = products.findProductById(Integer.parseInt(pid));
                        if (p != null) {
                            orderProducts.insert(p);
                            total += p.getPrice();
                        }
                    }
                    System.out.print("Enter order date (MM/DD/YYYY): ");
                    String date = sc.nextLine();
                    Order o = new Order(oid, cid, orderProducts, date);
                    o.setTotalPrice(total);
                    customers.placeOrder(cid, oid, orderProducts, date);
                }
                case 4 -> {
                    System.out.print("Product ID: ");
                    int pid = Integer.parseInt(sc.nextLine());
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Price: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print("Stock: ");
                    int stock = Integer.parseInt(sc.nextLine());
                    Product p = new Product(pid, name, price, stock);
                    products.addProduct(p);
                }
                case 5 -> {
                    System.out.print("Customer ID: ");
                    int cid = Integer.parseInt(sc.nextLine());
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    customers.registerCustomer(cid, name, email);
                }
                case 6 -> {
                    System.out.print("Customer ID: ");
                    int cid = Integer.parseInt(sc.nextLine());
                    System.out.print("Product ID: ");
                    int pid = Integer.parseInt(sc.nextLine());
                    System.out.print("Rating (1-5): ");
                    int rating = Integer.parseInt(sc.nextLine());
                    System.out.print("Comment: ");
                    String comment = sc.nextLine();
                    try {
                        Review r = new Review(rating, comment);
                        products.addReview(r);
                        CustomerRecord c = customers.findCustomerById(cid);
                        if (c != null) c.addReview(r);
                    } catch (InvalidRatingException e) {
                        System.out.println("Invalid rating: " + e.getMessage());
                    }
                }
                case 7 -> {
                    System.out.print("Customer ID: ");
                    int cid = Integer.parseInt(sc.nextLine());
                    customers.viewOrderHistory(cid);
                }
                case 8 -> {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option");
            }
        }
    }
}
