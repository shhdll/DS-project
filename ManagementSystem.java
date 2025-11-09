
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System data
        Customers customers = new Customers();
        Products products = new Products();
        Orders orders = new Orders();
        Reviews reviews = new Reviews();

        // File paths
        String customersFile = "dataset/customers.csv";
        String productsFile = "dataset/prodcuts.csv";
        String ordersFile = "dataSet/orders.csv";
        String reviewsFile = "dataset/reviews.csv";

        // Load data from CSV files
        loadCustomers(customersFile, customers);
        loadProducts(productsFile, products);
        loadOrders(ordersFile, customers, products);
        loadReviews(reviewsFile, reviews, customers, products);

        // Main menu
        boolean running = true;
        while (running) {
            System.out.println("\n **** Welcome to E-Commerce Management **** ");
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
                    // Add Product
                    System.out.println("\n* ========== ADD PRODUCT ========== *");
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
                    boolean added = products.addProduct(newProduct);
                    if (added) {
                        System.out.println(" Product '" + pname + "' added successfully!");
                    } else {
                        System.out.println("Product ID " + pid + " already exists!");
                    }
                    break;

                case 2:
                    // Add Customer
                    System.out.println("\n* ========== ADD CUSTOMER ========== *");
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
                    System.out.println("\n* ========== PLACE NEW ORDER ========== *");
                    System.out.println("\n--- AVAILABLE PRODUCTS ---");
                    products.displayAllProducts();

                    System.out.print("Enter Customer ID: ");
                    int orderCustomerId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Order ID: ");
                    int newOrderId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Order Date (YYYY-MM-DD): ");
                    String newOrderDate = sc.nextLine();

                    // Create product list for the order
                    LinkedList<Product> selectedProducts = new LinkedList<>();
                    boolean addingProducts = true;

                    System.out.println("\n--- ADD PRODUCTS TO ORDER ---");
                    while (addingProducts) {
                        System.out.print("Enter Product ID from list above (-1 to checkout): ");
                        int productId = sc.nextInt();
                        sc.nextLine();

                        if (productId == -1) {
                            addingProducts = false;
                        } else {
                            Product product = products.findProductById(productId);
                            if (product != null) {
                                selectedProducts.insert(product);
                                System.out.println(
                                        "Added: " + product.getName() + " | Price: " + product.getPrice() + " SAR\"");
                            } else {
                                System.out.println("Product ID " + productId + " not found");
                            }
                        }
                    }

                    if (!selectedProducts.empty()) {
                        customers.placeOrder(orderCustomerId, newOrderId, selectedProducts, newOrderDate);
                        System.out.println("Order placed successfully!");
                    } else {
                        System.out.println("No products added. Order cancelled.");
                    }
                    break;

                case 4:
                    // All orders for a specific customer
                    System.out.println("\n* ========== VIEW CUSTOMER ORDERS ========== *");

                    System.out.print("Enter Customer ID to view orders: ");
                    int viewCid = sc.nextInt();
                    sc.nextLine();
                    customers.viewOrderHistory(viewCid);
                    break;

                case 5:
                    // Add Review for Product
                    System.out.println("\n* ========== ADD PRODUCT REVIEW ========== *");
                    // Get review details
                    System.out.print("Enter Review ID: ");
                    int newReviewId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Product ID: ");
                    int reviewProductId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Customer ID: ");
                    int reviewCustomerId = sc.nextInt();
                    sc.nextLine();
                    // ONLY CHECK: Product exists
                    Product product = products.findProductById(reviewProductId);
                    if (product == null) {
                        System.out.println("Product does not exist!");
                        break;
                    }

                    System.out.print("Enter Rating (1-5 stars): ");
                    int newRating = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Review Comment: ");
                    String newComment = sc.nextLine();

                    try {
                        // Create and add the review
                        Review newReview = new Review(newReviewId, reviewProductId, newRating, reviewCustomerId,
                                newComment);
                        products.addReview(reviewProductId, newReview);

                        System.out.println("\n Review added successfully!");
                        System.out.println("Product ID: " + reviewProductId + " | Rating: " + newRating + " stars");

                    } catch (InvalidRatingException e) {
                        System.out.println("\n ERROR: " + e.getMessage());
                        System.out.println("Please enter a rating between 1 to 5 stars.");
                    }
                    break;

                case 6:
                    // Extract Reviews from Specific Customer
                    System.out.println("\n* ========== CUSTOMER REVIEWS  ========== *");
                    System.out.print("Enter Customer ID: ");
                    int reviewCid = sc.nextInt();
                    sc.nextLine();
                    customers.extractCustomerReviews(reviewCid);
                    break;

                case 7:
                    // Show Top 3 Products by Average Rating
                    System.out.println("\n* ========== TOP 3 PRODUCTS BY RATING  ========== *");
                    System.out.println("--- Show Top 3 Products by Average Rating ---");
                    products.Top3Products();
                    System.out.println("Function to sort and display top 3 products goes here.");
                    break;

                case 8:
                    // Show orders within a specified date range
                    System.out.println("\n* ========== ORDERS BETWEEN DATES  ========== *");
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDate = sc.nextLine();
                    System.out.print("Enter end date (YYYY-MM-DD): ");
                    String endDate = sc.nextLine();

                    System.out.println("\n--- Orders between " + startDate + " and " + endDate + " ---");
                    String ordersResult = orders.showOrdersBetween(startDate, endDate);

                    if (ordersResult.isEmpty()) {
                        System.out.println("No orders found in this date range.");
                    } else {
                        System.out.println(ordersResult);
                    }
                    break;

                case 9:
                    // Common Reviewed Products Between Two Customers
                    System.out.println("\n* ========== COMMON HIGHLY-RATED PRODUCTS  ========== *");
                    System.out.print("Enter Customer ID 1: ");
                    int c1 = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Customer ID 2: ");
                    int c2 = sc.nextInt();
                    sc.nextLine();

                    products.commonProducts(c1, c2);
                    break;

                case 10:
                    // Exit program
                    running = false;
                    System.out.println("\n* ========== THANK YOU  ========== *");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        sc.close();
    }

    // CSV loading methods
    // Load customers, registers each customer
    private static void loadCustomers(String filePath, Customers customers) {
        try {
            File F = new File(filePath);
            Scanner FS = new Scanner(F);
            int tmpId;
            String tmpName, tmpEmail, tmpData;
            FS.nextLine(); // for skipping the header
            while (FS.hasNextLine()) {
                tmpData = FS.nextLine();
                int firstComma = tmpData.indexOf(',');
                int secondComma = tmpData.indexOf(',', firstComma + 1);
                String idString = tmpData.substring(0, firstComma);
                tmpName = tmpData.substring(firstComma + 1, secondComma);
                tmpEmail = tmpData.substring(secondComma + 1);
                tmpId = Integer.parseInt(idString);
                customers.registerCustomer(tmpId, tmpName, tmpEmail);
            }
            FS.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        // System.out.println("Loading customers...");
    }

    // Load products, creates Product objects and adds them to Products list
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
    }

    // Load orders, link them to products and customers
    public static Orders loadOrders(String filename, Customers customers, Products products) {
        Orders orders = new Orders();

        try (Scanner sc = new Scanner(new File(filename))) {
            sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                int orderId = Integer.parseInt(parts[0].trim());
                int customerId = Integer.parseInt(parts[1].trim());
                String productIdsStr = parts[2].replace("\"", "").trim(); // remove quotes
                double totalPrice = Double.parseDouble(parts[3].trim());
                String orderDate = parts[4].trim();
                String status = parts[5].trim();

                // Create list of products
                LinkedList<Product> orderProducts = new LinkedList<>();
                String[] productIds = productIdsStr.split(";");
                for (String pidStr : productIds) {
                    int productId = Integer.parseInt(pidStr.trim());
                    Product p = products.findProductById(productId);
                    if (p != null) {
                        orderProducts.insert(p);
                    }
                }

                // Create order object
                Order newOrder = new Order(orderId, customerId, orderProducts, orderDate);
                newOrder.setStatus(status); // assuming you have a setStatus method
                orders.getOrderList().insert(newOrder);

                // Link order to customer
                Customer customer = customers.findCustomerById(customerId);
                if (customer != null) {
                    customer.getOrders().insert(newOrder);
                } else {
                    System.out.println("Customer ID " + customerId + " not found for order " + orderId);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }

        return orders;
    }

    // Load reviews, and assign to both products and customers
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
                // Attach the review to both customer and product
                assignReviewToCustomerAndProduct(r, customers, products);
            }

            FS.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidRatingException e) {
            System.out.println("Error: " + e.getMessage());

        }
    }

    // Link a review to its corresponding customer and product
    private static void assignReviewToCustomerAndProduct(Review r, Customers customers, Products products) {
        // Find the matching customer
        Customer customer = customers.findCustomerById(r.getCustomerId());
        if (customer != null) {
            customer.addReview(r);
        } else {
            System.out.println("Customer ID " + r.getCustomerId() + " not found for review " + r.getReviewId());
        }

        // Find the matching product
        Product product = products.findProductById(r.getProductId());
        if (product != null) {
            product.addReview(r);
        } else {
            System.out.println(" Product ID " + r.getProductId() + " not found for review " + r.getReviewId());
        }
    }

}
