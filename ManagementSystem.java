import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System data10
        Customers customers = new Customers();
        Products products = new Products();
        Orders orders = new Orders();
        Reviews reviews = new Reviews();

        // File paths
        String customersFile = "dataset/customers.csv";
        String productsFile = "dataset/prodcuts.csv";
        String ordersFile = "dataSet/orders.csv";/// erorrrr the system didnt read this !
        String reviewsFile = "dataset/reviews.csv";

        // 1 Load data from CSV files
        loadCustomers(customersFile, customers);
        loadProducts(productsFile, products);
        loadOrders(ordersFile, orders, customers, products);
        loadReviews(reviewsFile,reviews, customers, products);
       
        // 2 Main menu
        boolean running = true;
        while (running) {
            System.out.println("\n=== Welcome to E-Commerce Management ===");
            System.out.println("--------------------------------");                                 
            System.out.println("1. Add Product");
            System.out.println("2. Add Customer");
            System.out.println("3. Place New Order");
            System.out.println("4. View Customer Orders");
            System.out.println("5. Add Review for Product"); 
            System.out.println("6. Extract Reviews from Specific Customer");
            System.out.println("7. Show Top 3 Products by Average Rating");
            System.out.println("8. View All Orders Between Two Dates");
            System.out.println("9. Common Reviewed Products Between Two Customers");
            System.out.println("10. Exit");
            System.out.println("--------------------------------");                                 
            System.out.print("Select an option: ");
            
            int choice;
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline
            } else {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine(); // consume invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    // Add Product (Updated menu order, logic remains the same)
                    System.out.print("Product ID: ");
                    int pid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Name: ");
                    String pname = sc.nextLine();
                    System.out.print("Price: ");
                    double price = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();
                    Product newProduct = new Product(pid, pname, price, stock);
                    products.addProduct(newProduct);
                    break;

                case 2:
                    // Add Customer (Updated menu order, logic remains the same)
                    System.out.print("Customer ID: ");
                    int cid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Name: ");
                    String cname = sc.nextLine();
                    System.out.print("Email: ");
                    String cemail = sc.nextLine();
                    customers.registerCustomer(cid, cname, cemail);
                    break;
                
                case 3:
                    // Place New Order
                    System.out.println("--- Place New Order ---");
                    System.out.println("Place order functionality goes here.");
                    break;

                case 4:
                    // View Customer Orders
                    System.out.print("Enter Customer ID to view orders: ");
                    int viewCid = sc.nextInt();
                    sc.nextLine();
                    customers.viewOrderHistory(viewCid);
                    break;
                
                case 5:
                    // Add Review for Product
                    System.out.println("--- Add Review for Product ---");
                    System.out.println("Add review functionality goes here.");
                    break;
                
                case 6:
                    // Extract Reviews from Specific Customer (New Option)
                    System.out.println("--- Extract Reviews from Specific Customer ---");
                    System.out.print("Enter Customer ID: ");
                    int reviewCid = sc.nextInt();
                    sc.nextLine();
                    // customers.showReviews(reviewCid); // Placeholder call
                    System.out.println("Showing all reviews written by Customer " + reviewCid + ".");
                    break;

                case 7:
                    // Show Top 3 Products by Average Rating (New Option)
                    System.out.println("--- Show Top 3 Products by Average Rating ---");
                    // products.showTopRated(); // Placeholder call
                    System.out.println("Function to sort and display top 3 products goes here.");
                    break;

                case 8:
                    // View All Orders Between Two Dates
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter end date (YYYY-MM-DD): ");
                    String endDate = sc.nextLine();
                    System.out.println("\nOrders between " + startDate + " and " + endDate + ":");
                    orders.showOrdersBetween(startDate, endDate);
                    break;

                case 9:
                    // Common Reviewed Products Between Two Customers (New Option)
                    System.out.println("--- Common Reviewed Products (Rating > 4) ---");
                    System.out.print("Enter Customer ID 1: ");
                    int c1 = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Customer ID 2: ");
                    int c2 = sc.nextInt();
                    sc.nextLine();
                    // logic to find common products...
                    System.out.println("Finding common highly-rated products reviewed by Customer " + c1 + " and Customer " + c2 + ".");
                    break;

                case 10:
                    running = false;
                    System.out.println("Thank you! see you soon");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        sc.close();
    }

    // CSV loading methods (UNCHANGED, assuming necessary classes (Product, Customers, Orders, etc.) exist elsewhere)
    private static void loadCustomers(String filePath, Customers customers) {
       try {
            File F = new File(filePath);
            Scanner FS = new Scanner(F);
            int tmpId ;
            String tmpName , tmpEmail,tmpData;
             FS.nextLine(); // for skipping the header 
            while (FS.hasNextLine()) {
                tmpData = FS.nextLine();
                int firstComma = tmpData.indexOf(',');
                int secondComma = tmpData.indexOf(',', firstComma + 1);
                String idString = tmpData.substring(0, firstComma);
                tmpName = tmpData.substring(firstComma + 1, secondComma);
                tmpEmail = tmpData.substring(secondComma + 1);
                tmpId= Integer.parseInt(idString);
                customers.registerCustomer(tmpId, tmpName, tmpEmail);
            }
            FS.close();
       } catch (FileNotFoundException e) {
        System.out.println(e.getMessage());
       } 
        //System.out.println("Loading customers...");
    }

    private static void loadProducts(String filePath, Products products) {
        try {
            File F = new File(filePath);
            Scanner FS = new Scanner(F);
            int tmpId, tmpStock;
            double tmpPrice;
            String tmpName, tmpData;
            FS.nextLine(); // skip header
            while (FS.hasNextLine()) {
                tmpData = FS.nextLine();
                int firstComma = tmpData.indexOf(',');
                int secondComma = tmpData.indexOf(',', firstComma + 1);
                int thirdComma = tmpData.indexOf(',', secondComma + 1);

                String idString = tmpData.substring(0, firstComma);
                tmpName = tmpData.substring(firstComma + 1, secondComma);
                String priceString = tmpData.substring(secondComma + 1, thirdComma);
                String stockString = tmpData.substring(thirdComma + 1);
                tmpId = Integer.parseInt(idString);
                tmpPrice = Double.parseDouble(priceString);
                tmpStock = Integer.parseInt(stockString);
                Product p = new Product(tmpId, tmpName, tmpPrice, tmpStock);
                products.addProduct(p);
            }
            FS.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("Loading products...");
    }

    private static void loadOrders(String filePath, Orders orders, Customers customers, Products products) {
        try {
            File F = new File(filePath);
            Scanner FS = new Scanner(F);
            int orderId,customerId;
            String productIds,orderDate,status,tmpData;
            double totalPrice;
            LinkedList<Product> orderProducts = new LinkedList<>();

            FS.nextLine(); // skip header
            while (FS.hasNext()) {
                tmpData = FS.nextLine();
                int firstComma = tmpData.indexOf(',');
                int secondComma = tmpData.indexOf(',', firstComma + 1);
                int thirdComma = tmpData.indexOf(',', secondComma + 1);
                int fourthComma = tmpData.indexOf(',', thirdComma + 1);
                int fifthComma = tmpData.indexOf(',', fourthComma + 1);
                orderId = Integer.parseInt(tmpData.substring(0, firstComma));
                customerId = Integer.parseInt(tmpData.substring(firstComma + 1, secondComma));

                // Extract the product IDs string between quotes
                int quoteStart = tmpData.indexOf('"');
                int quoteEnd = tmpData.indexOf('"', quoteStart + 1);
                String productIdsText = tmpData.substring(quoteStart + 1, quoteEnd);
                int k = 0;
                while (k < productIdsText.length()) {
                    int index = productIdsText.indexOf(';', k);
                    if (index == -1) 
                        index = productIdsText.length();
                        String productIdText = productIdsText.substring(k, index);
                        int productId = Integer.parseInt(productIdText);
                        Product found = products.findProductById(productId);
                        if (found != null) {
                            products.addProduct(found);
                        }

                        k = index + 1;
                }
                totalPrice = Double.parseDouble(tmpData.substring(thirdComma + 1, fourthComma));
                orderDate = tmpData.substring(fourthComma + 1, fifthComma);
                status = tmpData.substring(fifthComma + 1);
                orders.createOrder(orderId,customerId,orderProducts,orderDate);
            }
        } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());

        }
        // TODO: Implement reading orders from CSV and adding to customer orders
       // System.out.println("Loading orders...");
    }

    private static void loadReviews(String filePath, Reviews reviews, Customers customers, Products products) {
        try {
        File F = new File(filePath);
        Scanner FS = new Scanner(F);
        FS.nextLine(); // skip header

        while (FS.hasNextLine()) {
            String tmpData = FS.nextLine();

            int firstComma = tmpData.indexOf(',');
            int secondComma = tmpData.indexOf(',', firstComma + 1);
            int thirdComma = tmpData.indexOf(',', secondComma + 1);
            int fourthComma = tmpData.indexOf(',', thirdComma + 1);

            int reviewId = Integer.parseInt(tmpData.substring(0, firstComma));
            int productId = Integer.parseInt(tmpData.substring(firstComma + 1, secondComma));
            int customerId = Integer.parseInt(tmpData.substring(secondComma + 1, thirdComma));
            int rating = Integer.parseInt(tmpData.substring(thirdComma + 1, fourthComma));

            String comment = tmpData.substring(fourthComma + 1);

            Review r = new Review(reviewId, productId, rating, customerId, comment);
            reviews.addReview(reviewId, productId, rating, customerId, comment);
            //Attach the review to both customer and product
            assignReviewToCustomerAndProduct(r, customers, products);
        }

        FS.close();

    } catch (FileNotFoundException e) {
        System.out.println("Error: " + e.getMessage());
    }
    catch (InvalidRatingException e){
                System.out.println("Error: " + e.getMessage());

    }
        // TODO: Implement reading reviews from CSV and adding to products/customers
        //System.out.println("Loading reviews...");
    }
    
    
    private static void assignReviewToCustomerAndProduct(Review r, Customers customers, Products products) {
    // 1️⃣ Find the matching customer
    CustomerRecord customer = customers.findCustomerById(r.getCustomerId());
    if (customer != null) {
        customer.addReview(r);
    } else {
        System.out.println("Customer ID " + r.getCustomerId() + " not found for review " + r.getReviewId());
    }

    // 2️⃣ Find the matching product
    Product product = products.findProductById(r.getProductId());
    if (product != null) {
        product.addReview(r);
    } else {
        System.out.println(" Product ID " + r.getProductId() + " not found for review " + r.getReviewId());
    }
}

}