import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ManagementSystem {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // System data
        Customers customersobj = new Customers();
        Products productsobj = new Products();
        Orders ordersobj = new Orders();
        Reviews reviewsobj = new Reviews();

        // File paths
        String customersFile = "dataset/customers.csv";
        String productsFile = "dataset/prodcuts.csv";
        String ordersFile = "dataSet/orders.csv";
        String reviewsFile = "dataset/reviews.csv";

        // Load data from CSV files
        loadCustomers(customersFile, customersobj);
        loadProducts(productsFile, productsobj);
        loadOrders(ordersFile, customersobj, productsobj);
        loadReviews(reviewsFile, reviewsobj, customersobj, productsobj);

        // Main menu
        boolean running = true;
        while (running) {
            System.out.println("\n **** Welcome to E-Commerce Management **** ");
            System.out.println("--------------------------------");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Update Product Details");
            System.out.println("4. Add Customer");
            System.out.println("5. Place New Order");
            System.out.println("6. Cancel Order");
            System.out.println("7. Update Order Status");
            System.out.println("8. View Customer Orders");
            System.out.println("9. Add Review for Product");
            System.out.println("10. Edit Review");
            System.out.println("11. Extract Reviews from Specific Customer");
            System.out.println("12. Common Reviewed Products Between Two Customers");
            //Advanced queries
            System.out.println("13. View All Orders Between Two Dates");
            System.out.println("14. List All Products Within a Price Range");
            System.out.println("15. Show Top 3 Products by Average Rating");
            System.out.println("16. List All Customers Sorted Alphabetically");
            System.out.println("17. Show All Reviews for a Product (by ID)");

            System.out.println("18. Exit");
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

                // -------------------- PRODUCT MANAGEMENT --------------------
                case 1:
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
                    boolean added = productsobj.addProduct(newProduct);
                    appendProductToFile(productsFile, newProduct);
                    if (added) {
                        System.out.println("Product '" + pname + "' added successfully!");
                    } else {
                        System.out.println("Product ID " + pid + " already exists!");
                    }
                    break;

                case 2:
                    System.out.println("\n* ========== REMOVE PRODUCT ========== *");
                    System.out.print("Enter Product ID to remove: ");
                    int removePid = sc.nextInt();
                    sc.nextLine();
                    boolean removed = productsobj.removeProduct(removePid);
                    if (removed) {
                        System.out.println("Product ID " + removePid + " removed successfully!");
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;

                case 3:
                    System.out.println("\n* ========== UPDATE PRODUCT DETAILS ========== *");
                    System.out.print("Enter Product ID to update: ");
                    int updatePid = sc.nextInt();
                    sc.nextLine();
                    Product existingProduct = productsobj.findProductById(updatePid);
                    if (existingProduct == null) {
                        System.out.println("Product not found.");
                        break;
                    }
                    System.out.print("Enter new name (or press Enter to skip): ");
                    String newName = sc.nextLine();
                    if (!newName.isEmpty()) {
                        existingProduct.setName(newName);
                    }
                    System.out.print("Enter new price (or -1 to skip): ");
                    double newPrice = sc.nextDouble();
                    sc.nextLine();
                    if (newPrice >= 0) {
                        existingProduct.setPrice(newPrice);
                    }
                    System.out.print("Enter new stock (or -1 to skip): ");
                    int newStock = sc.nextInt();
                    sc.nextLine();
                    if (newStock >= 0) {
                        existingProduct.setStock(newStock);
                    }
                    System.out.println("Product updated successfully!");
                    break;

                // -------------------- CUSTOMER MANAGEMENT --------------------
                case 4:
                    System.out.println("\n* ========== ADD CUSTOMER ========== *");
                    System.out.print("Customer ID: ");
                    int cid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Name: ");
                    String cname = sc.nextLine();
                    System.out.print("Email: ");
                    String cemail = sc.nextLine();
                    Customer c = new Customer(cid, cname, cemail);
                    customersobj.registerCustomer(cid, cname, cemail);
                    appendCustomerToFile(customersFile, c);

                    break;

                // -------------------- ORDER MANAGEMENT --------------------
                case 5:
                    System.out.println("\n* ========== PLACE NEW ORDER ========== *");

                    // Show Out-of-Stock List before displaying available products
                    System.out.println("\n--- ATTENTION: OUT-OF-STOCK WARNING ---");
                    productsobj.getOutOfStockProducts();
                    System.out.println("---------------------------------------");

                    System.out.println("\n--- AVAILABLE PRODUCTS ---");
                    productsobj.displayAllProducts();

                    System.out.print("Enter Customer ID: ");
                    int orderCustomerId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Order ID: ");
                    int newOrderId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Order Date (YYYY-MM-DD): ");
                    String newOrderDate = sc.nextLine();

                    AVLTree<Product> selectedProducts = new AVLTree<>();
                    boolean addingProducts = true;
                    boolean stockError = false;

                    System.out.println("\n--- ADD PRODUCTS TO ORDER ---");
                    while (addingProducts) {
                        System.out.print("Enter Product ID (-1 to checkout): ");
                        int productId = sc.nextInt();
                        sc.nextLine();

                        if (productId == -1) {
                            addingProducts = false;
                        } else {
                            // Find the product in the central storage for stock check
                            Product realProduct = productsobj.findProductById(productId);

                            if (realProduct != null) {
                                if (realProduct.getStock() > 0) {
                                    // 1. Decrement Stock on the real product object
                                    realProduct.setStock(realProduct.getStock() - 1);

                                    // 2. Add product (instance) to the order list
                                    selectedProducts.insert(realProduct.getProductId(), realProduct);
                                    System.out.println("Added: " + realProduct.getName() + " (Stock remaining: " + realProduct.getStock() + ")");
                                } else {
                                    System.out.println("ERROR: Stock for " + realProduct.getName() + " is zero. Cannot add.");
                                    stockError = true;
                                }
                            } else {
                                System.out.println("Product not found.");
                            }
                        }
                    }

                    if (stockError) {
                        System.out.println("\nOrder cancelled due to stock errors.");
                    } else if (!selectedProducts.empty()) {
                        Order r = new Order(newOrderId, orderCustomerId, selectedProducts, newOrderDate);
                        appendOrderToFile(ordersFile, r);
                        customersobj.placeOrder(orderCustomerId, newOrderId, selectedProducts, newOrderDate);
                        System.out.println("\nOrder placed successfully!");
                    } else {
                        System.out.println("No products added. Order cancelled.");
                    }
                    break;
                case 6:
                    System.out.println("\n* ========== CANCEL ORDER ========== *");
                    System.out.print("Enter Order ID to cancel: ");
                    int cancelOrderId = sc.nextInt();
                    sc.nextLine();
                    boolean canceled = ordersobj.cancelOrder(cancelOrderId);
                    if (canceled) {
                        System.out.println("Order cancelled successfully!");
                    } else {
                        System.out.println("Order not found or already cancelled.");
                    }
                    break;

                case 7:
                    System.out.println("\n* ========== UPDATE ORDER STATUS ========== *");
                    System.out.print("Enter Order ID: ");
                    int updateOrderId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new status: ");
                    String newStatus = sc.nextLine();
                    boolean updated = ordersobj.updateOrderStatus(updateOrderId, newStatus);
                    if (updated) {
                        System.out.println("Order status updated successfully!");
                    } else {
                        System.out.println("Order not found.");
                    }
                    break;

                // -------------------- VIEW & REVIEW MANAGEMENT --------------------
                case 8:
                    System.out.println("\n* ========== VIEW CUSTOMER ORDERS ========== *");
                    System.out.print("Enter Customer ID: ");
                    int viewCid = sc.nextInt();
                    sc.nextLine();
                    customersobj.viewOrderHistory(viewCid);
                    break;

                case 9:
                    System.out.println("\n* ========== ADD PRODUCT REVIEW ========== *");
                    System.out.print("Enter Review ID: ");
                    int newReviewId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Product ID: ");
                    int reviewProductId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Customer ID: ");
                    int reviewCustomerId = sc.nextInt();
                    sc.nextLine();
                    Product product = productsobj.findProductById(reviewProductId);
                    if (product == null) {
                        System.out.println("Product does not exist!");
                        break;
                    }
                    System.out.print("Enter Rating (1-5): ");
                    int newRating = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Comment: ");
                    String newComment = sc.nextLine();

                    try {
                        Review newReview = new Review(newReviewId, reviewProductId, newRating, reviewCustomerId, newComment);
                        productsobj.addReview(reviewProductId, newReview);
                        reviewsobj.addReview(newReview);
                        appendReviewToFile(reviewsFile, newReview);
                        System.out.println("Review added successfully!");
                    } catch (InvalidRatingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 10:
                    System.out.println("\n* ========== EDIT REVIEW ========== *");
                    System.out.print("Enter Review ID: ");
                    int editReviewId = sc.nextInt();
                    sc.nextLine();
                    Review targetReview = reviewsobj.findReviewById(editReviewId);
                    if (targetReview == null) {
                        System.out.println("Review not found.");
                        break;
                    }
                    System.out.print("New Rating (1-5): ");
                    int updatedRating = sc.nextInt();
                    sc.nextLine();
                    System.out.print("New Comment: ");
                    String updatedComment = sc.nextLine();
                    try {
                        targetReview.edit(updatedRating, updatedComment);
                        System.out.println("Review updated successfully!");
                    } catch (InvalidRatingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 11:
                    System.out.println("\n* ========== CUSTOMER REVIEWS ========== *");
                    System.out.print("Enter Customer ID: ");
                    int reviewCid = sc.nextInt();
                    sc.nextLine();
                    customersobj.extractCustomerReviews(reviewCid);
                    break;

                // ADVANCED QUERIES 
                case 12:
                    System.out.println("\n* ========== FIND COMMON REVIEWED PRODUCTS ========== *");
                    showCommonReviewedProductsBetweenCustomers(productsobj, sc);
                    break;

                case 13:
                    System.out.println("\n* ========== ORDERS BETWEEN DATES ========== *");
                    System.out.print("Start Date (YYYY-MM-DD): ");
                    String startDate = sc.nextLine();
                    System.out.print("End Date (YYYY-MM-DD): ");
                    String endDate = sc.nextLine();
                    String ordersResult = ordersobj.showOrdersBetween(startDate, endDate);
                    if (ordersResult.isEmpty()) {
                        System.out.println("No orders found.");
                    } else {
                        System.out.println(ordersResult);
                    }
                    break;

                case 14: 
                    showProductsInPriceRange(productsobj, sc);
                    break;

                case 15: 
                    System.out.println("\n* ========== TOP 3 PRODUCTS ========== *");
                    productsobj.Top3Products();
                    break;

                case 16: 
                    customersobj.listCustomersAlphabetically();
                    break;

                case 17: 
                    showReviewsForProduct(productsobj, customersobj, sc);
                    break;

                case 18: 
                    running = false;
                    System.out.println("\n* ========== THANK YOU ========== *");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        sc.close();
    }

    private static void showCommonReviewedProductsBetweenCustomers(Products products, Scanner sc) {
        System.out.print("Enter Customer ID 1: ");
        int c1 = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Customer ID 2: ");
        int c2 = sc.nextInt();
        sc.nextLine();

        products.commonProducts(c1, c2);
    }

    private static void showProductsInPriceRange(Products products, Scanner sc) {
        System.out.println("\n* ========== PRODUCTS IN PRICE RANGE ========== *");
        System.out.print("Enter minimum price: ");
        double minPrice = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter maximum price: ");
        double maxPrice = sc.nextDouble();
        sc.nextLine();
        products.getProductsInPriceRange(minPrice, maxPrice);
    }

    private static void showReviewsForProduct(Products products, Customers customers, Scanner sc) {
        System.out.println("\n* ========== CUSTOMERS WHO REVIEWED PRODUCT ========== *");
        System.out.print("Enter Product ID to view reviews: ");
        int reviewedPid = sc.nextInt();
        sc.nextLine();
        products.commonProducts(reviewedPid, customers);
    }

    // ================= CSV LOADING METHODS =================
    private static void loadCustomers(String filePath, Customers customers) {
        try {
            File F = new File(filePath);
            Scanner FS = new Scanner(F);
            FS.nextLine(); // skip header
            while (FS.hasNextLine()) {
                String tmpData = FS.nextLine();
                int firstComma = tmpData.indexOf(',');
                int secondComma = tmpData.indexOf(',', firstComma + 1);
                int tmpId = Integer.parseInt(tmpData.substring(0, firstComma));
                String tmpName = tmpData.substring(firstComma + 1, secondComma);
                String tmpEmail = tmpData.substring(secondComma + 1);
                customers.registerCustomer(tmpId, tmpName, tmpEmail);
            }
            FS.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadProducts(String filePath, Products products) {
        try {
            File F = new File(filePath);
            Scanner FS = new Scanner(F);
            FS.nextLine();
            while (FS.hasNextLine()) {
                String tmpData = FS.nextLine();
                int firstComma = tmpData.indexOf(',');
                int secondComma = tmpData.indexOf(',', firstComma + 1);
                int thirdComma = tmpData.indexOf(',', secondComma + 1);
                int tmpId = Integer.parseInt(tmpData.substring(0, firstComma));
                String tmpName = tmpData.substring(firstComma + 1, secondComma);
                double tmpPrice = Double.parseDouble(tmpData.substring(secondComma + 1, thirdComma));
                int tmpStock = Integer.parseInt(tmpData.substring(thirdComma + 1));
                Product p = new Product(tmpId, tmpName, tmpPrice, tmpStock);
                products.addProduct(p);
            }
            FS.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

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
                String productIdsStr = parts[2].replace("\"", "").trim();
                double totalPrice = Double.parseDouble(parts[3].trim());
                String orderDate = parts[4].trim();
                String status = parts[5].trim();

                AVLTree<Product> orderProducts = new AVLTree<>();
                for (String pidStr : productIdsStr.split(";")) {
                    int productId = Integer.parseInt(pidStr.trim());
                    Product p = products.findProductById(productId);
                    if (p != null) {
                        orderProducts.insert(p.getProductId(), p);
                    }
                }

                Order newOrder = new Order(orderId, customerId, orderProducts, orderDate);
                newOrder.setStatus(status);
                orders.getOrderList().insert(newOrder.getOrderId(), newOrder);

                Customer customer = customers.findCustomerById(customerId);
                if (customer != null) {
                    customer.getOrders().insert(newOrder.getOrderId(), newOrder);
                } else {
                    System.out.println("Customer ID " + customerId + " not found for order " + orderId);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }

        return orders;
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
                reviews.addReview(r);
                assignReviewToCustomerAndProduct(r, customers, products);

            }

            FS.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InvalidRatingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void assignReviewToCustomerAndProduct(Review r, Customers customers, Products products) {
        Customer customer = customers.findCustomerById(r.getCustomerId());
        if (customer != null) {
            customer.addReview(r);
        } else {
            System.out.println("Customer ID " + r.getCustomerId() + " not found for review " + r.getReviewId());
        }

        Product product = products.findProductById(r.getProductId());
        if (product != null) {
            product.addReview(r);
        } else {
            System.out.println("Product ID " + r.getProductId() + " not found for review " + r.getReviewId());
        }
    }

    // writing methodes !
    private static void appendCustomerToFile(String filePath, Customer customer) {
        try (FileWriter fw = new FileWriter(filePath, true); // true for append mode
                 PrintWriter pw = new PrintWriter(fw)) {

            // Format: customerId,name,email
            String csvLine = String.format("%d,%s,%s", customer.getCustomerId(), customer.getName(), customer.getEmail());
            pw.println(csvLine);

        } catch (IOException e) {
            System.err.println("Error writing new customer to file: " + e.getMessage());
        }
    }

    private static void appendProductToFile(String filePath, Product product) {
        try (FileWriter fw = new FileWriter(filePath, true); PrintWriter pw = new PrintWriter(fw)) {

            // Format: productId,name,price,stock
            String csvLine = String.format("%d,%s,%.2f,%d", product.getProductId(), product.getName(), product.getPrice(), product.getStock());
            pw.println(csvLine);

        } catch (IOException e) {
            System.err.println("Error writing new product to file: " + e.getMessage());
        }
    }

    private static void appendOrderToFile(String filePath, Order order) {
        StringBuilder productIdsBuilder = new StringBuilder();
        double[] totalPrice = {0}; // small trick to allow modification inside recursion
        boolean[] first = {true};

        inorderProcess(order.getProductList().getRoot(), productIdsBuilder, totalPrice, first);

        String productIdsStr = productIdsBuilder.toString();

        try (FileWriter fw = new FileWriter(filePath, true); PrintWriter pw = new PrintWriter(fw)) {

            String csvLine = String.format(
                    "%d,%d,\"%s\",%.2f,%s,%s",
                    order.getOrderId(),
                    order.getOcustomer(),
                    productIdsStr,
                    totalPrice[0],
                    order.getOrderDate(),
                    order.getStatus()
            );

            pw.println(csvLine);

        } catch (IOException e) {
            System.err.println("Error writing new order to file: " + e.getMessage());
        }
    }

    private static void inorderProcess(AVLNode<Product> root, StringBuilder ids, double[] totalPrice, boolean[] first) {

        if (root == null) {
            return;
        }

        inorderProcess(root.left, ids, totalPrice, first);

        // Visit root (Product)
        Product p = root.data;
        totalPrice[0] += p.getPrice();

        if (!first[0]) {
            ids.append(";");
        }
        ids.append(p.getProductId());
        first[0] = false;

        inorderProcess(root.right, ids, totalPrice, first);
    }

    private static void appendReviewToFile(String filePath, Review review) {
        try (FileWriter fw = new FileWriter(filePath, true); PrintWriter pw = new PrintWriter(fw)) {

            String csvLine = String.format("%d,%d,%d,%d,%s", review.getReviewId(), review.getProductId(), review.getCustomerId(), review.getRating(), review.getComment());
            pw.println(csvLine);

        } catch (IOException e) {
            System.err.println("Error writing new review to file: " + e.getMessage());
        }
    }
}
