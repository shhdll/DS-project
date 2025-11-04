import java.io.*;
import java.util.Scanner;

public class ManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System data
        Customers customers = new Customers();
        Products products = new Products();
        Orders orders = new Orders();

        // File paths
        String customersFile = "dataset/customers.csv";
        String productsFile = "dataset/products.csv";
        String ordersFile = "dataset/orders.csv";
        String reviewsFile = "dataset/reviews.csv";

        // 1 Load data from CSV files
        loadCustomers(customersFile, customers);
        loadProducts(productsFile, products);
        loadOrders(ordersFile, orders, customers, products);
        loadReviews(reviewsFile, customers, products);

        // 2 Main menu
        boolean running = true;
        while (running) {
            System.out.println("\n=== E-Commerce Management Menu ===");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Product");
            System.out.println("3. Place Order");
            System.out.println("4. Add Review");
            System.out.println("5. View Customer Orders");
            System.out.println("6. View Product Details");
            System.out.println("7. Exit");
            System.out.print("Select an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    // Add customer
                    System.out.print("Customer ID: ");
                    int cid = sc.nextInt(); sc.nextLine();
                    System.out.print("Name: ");
                    String cname = sc.nextLine();
                    System.out.print("Email: ");
                    String cemail = sc.nextLine();
                    customers.registerCustomer(cid, cname, cemail);
                    break;

                case 2:
                    // Add product
                    System.out.print("Product ID: ");
                    int pid = sc.nextInt(); sc.nextLine();
                    System.out.print("Name: ");
                    String pname = sc.nextLine();
                    System.out.print("Price: ");
                    double price = sc.nextDouble(); sc.nextLine();
                    System.out.print("Stock: ");
                    int stock = sc.nextInt(); sc.nextLine();
                    Product newProduct = new Product(pid, pname, price, stock);
                    products.addProduct(newProduct);
                    break;

                case 3:
                    // Place order
                    // TODO: Implement order creation
                    System.out.println("Place order functionality goes here.");
                    break;

                case 4:
                    // Add review
                    // TODO: Implement review creation
                    System.out.println("Add review functionality goes here.");
                    break;

                case 5:
                    // View customer orders
                    System.out.print("Customer ID: ");
                    int viewCid = sc.nextInt(); sc.nextLine();
                    customers.viewOrderHistory(viewCid);
                    break;

                case 6:
                    // View product details
                    System.out.print("Product ID: ");
                    int viewPid = sc.nextInt(); sc.nextLine();
                    Product p = products.findProductById(viewPid);
                    if (p != null) {
                        p.displayProductDetails();
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 7:
                    running = false;
                    System.out.println("Thank you! see you soon");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        sc.close();
    }

    // CSV loading methods
    private static void loadCustomers(String filePath, Customers customers) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String email = parts[2].trim();
                customers.registerCustomer(id, name, email);
            }
        } catch (Exception e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    private static void loadProducts(String filePath, Products products) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                int stock = Integer.parseInt(parts[3].trim());
                Product p = new Product(id, name, price, stock);
                products.addProduct(p);
            }
        } catch (Exception e) {
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    private static void loadOrders(String filePath, Orders orders, Customers customers, Products products) {
        // TODO: Implement reading orders from CSV and adding to customer orders
    }

    private static void loadReviews(String filePath, Customers customers, Products products) {
        // TODO: Implement reading reviews from CSV and adding to products/customers
    }
}
