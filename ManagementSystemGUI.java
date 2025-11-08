
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ManagementSystemGUI {

    private static final Color DARK_BLUE = new Color(0x00, 0x00, 0x4d);
    private static final String DATASET_PATH = "dataset/";

    // 1. Custom Data Structure 
    public interface List<T> {

        public boolean empty();

        public boolean last();

        public boolean full();

        public void findFirst();

        public void findNext();

        public T retrieve();

        public void update(T val);

        public void insert(T val);

        public void remove();

        public boolean find(T key);

        public int size();
    }

    public static class Node<T> {

        public T data;
        public Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public static class LinkedList<T> implements List<T> {

        private Node<T> head;
        private Node<T> current;

        public LinkedList() {
            head = current = null;
        }

        @Override
        public boolean empty() {
            return head == null;
        }

        @Override
        public boolean last() {
            return current != null && current.next == null;
        }

        @Override
        public boolean full() {
            return false;
        }

        @Override
        public void findFirst() {
            current = head;
        }

        @Override
        public void findNext() {
            current = current.next;
        }

        @Override
        public T retrieve() {
            return current != null ? current.data : null;
        }

        @Override
        public void update(T val) {
            if (current != null) {
                current.data = val;
            }
        }

        @Override
        public void insert(T val) {
            Node<T> tmp;
            if (empty()) {
                current = head = new Node<>(val);
            } else {
                tmp = current.next;
                current.next = new Node<>(val);
                current = current.next;
                current.next = tmp;
            }
        }

        @Override
        public void remove() {
            if (current == null) {
                return;
            }
            if (current == head) {
                head = head.next;
            } else {
                Node<T> tmp = head;
                while (tmp.next != current) {
                    tmp = tmp.next;
                }
                tmp.next = current.next;
            }

            if (current.next == null) {
                current = head;
            } else {
                current = current.next;
            }
        }

        @Override
        public boolean find(T key) {
            Node<T> tmp = head;
            while (tmp != null) {
                if (tmp.data.equals(key)) {
                    current = tmp;
                    return true;
                }
                tmp = tmp.next;
            }
            return false;
        }

        @Override
        public int size() {
            int count = 0;
            Node<T> tmp = head;
            while (tmp != null) {
                count++;
                tmp = tmp.next;
            }
            return count;
        }

        public Node<T> getHead() {
            return head;
        }

        public Node<T> getCurrent() {
            return current;
        }

        public Node<T> getNext() {
            return current != null ? current.next : null;
        }

        public LinkedList<T> deepCopy() {
            LinkedList<T> new_list = new LinkedList<>();

            // 1. Handle the empty case
            if (this.empty()) {
                return new_list;
            }

            // 2. Traverse the original list
            this.findFirst();
            while (true) {
                T data = this.retrieve();
                if (data != null) {
                    // 3. Insert the item into the new list
                    new_list.insert(data);
                }

                if (this.last()) {
                    break;
                }
                this.findNext();
            }

            // 4. Reset the current pointer for the new list to the beginning
            new_list.findFirst();
            return new_list;
        }
    }

    // Custom Exceptions
    public static class InvalidRatingException extends Exception {

        public InvalidRatingException(String message) {
            super(message);
        }
    }

    public static class InvalidStatusException extends Exception {

        public InvalidStatusException(String message) {
            super(message);
        }
    }

    // Minimal Product Class
    public static class Product {

        private final int productId;
        private String name;
        private double price;
        private int stock;
        private LinkedList<Review> reviews = new LinkedList<>();

        public Product(int id, String name, double price, int stock) {
            this.productId = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public int getProductId() {
            return productId;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public LinkedList<Review> getReviews() {
            return reviews;
        }

        public void addReview(Review r) {
            reviews.insert(r);
        }

        public void updateProduct(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public void displayProductDetails() {
            System.out.println("ID: " + productId + ", Name: " + name + ", Price: " + price + ", Stock: " + stock);
        }
    }

    // Minimal Review Class
    public static class Review {

        private final int reviewId;
        private final int productId;
        private final int customerId;
        private int rating;
        private String comment;

        public Review(int reviewId, int productId, int rating, int customerId, String comment) throws InvalidRatingException {
            if (rating < 1 || rating > 5) {
                throw new InvalidRatingException("Rating must be between 1 and 5.");
            }
            this.reviewId = reviewId;
            this.productId = productId;
            this.rating = rating;
            this.customerId = customerId;
            this.comment = comment;
        }

        public int getReviewId() {
            return reviewId;
        }

        public int getProductId() {
            return productId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public int getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public void edit(int newRating, String newComment) throws InvalidRatingException {
            if (newRating < 1 || newRating > 5) {
                throw new InvalidRatingException("Rating must be between 1 and 5.");
            }
            this.rating = newRating;
            this.comment = newComment;
        }
    }

    // Minimal Order Class
    public static class Order {

        private final int orderId;
        private final int customerId;
        private final LinkedList<Product> productList;
        private final String orderDate;
        private String status = "Pending";

        public Order(int orderId, int customerId, LinkedList<Product> productList, String orderDate) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.productList = productList;
            this.orderDate = orderDate;
        }

        public int getOrderId() {
            return orderId;
        }

        public int getOcustomer() {
            return customerId;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public String getStatus() {
            return status;
        }

        public LinkedList<Product> getProductList() {
            return productList;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getTotalPrice() {
            if (productList.empty()) {
                return 0.0;
            }
            double total = 0;
            productList.findFirst();
            while (true) {
                Product p = productList.retrieve();
                if (p != null) {
                    total += p.getPrice();
                }
                if (productList.last()) {
                    break;
                }
                productList.findNext();
            }
            return total;
        }

        @Override
        public String toString() {
            return String.format("Order #%d | Date: %s | Total: $%.2f | Status: %s | Customer ID: %d",
                    orderId, orderDate, getTotalPrice(), status, customerId);
        }
    }

    // Minimal CustomerRecord Class
    public static class CustomerRecord {

        private final int customerId;
        private final String name;
        private final String email;
        private final LinkedList<Order> orders = new LinkedList<>();
        private final LinkedList<Review> reviews = new LinkedList<>();

        public CustomerRecord(int id, String name, String email) {
            this.customerId = id;
            this.name = name;
            this.email = email;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }

        public LinkedList<Order> getOrders() {
            return orders;
        }

        public LinkedList<Review> getReviews() {
            return reviews;
        }

        public void addReview(Review r) {
            reviews.insert(r);
        }

        @Override
        public String toString() {
            return String.format("ID: %d | Name: %s | Email: %s", customerId, name, email);
        }
    }

    // 2. System Management Classes
    private class Customers {

        private LinkedList<CustomerRecord> allCustomers = new LinkedList<>();

        public void registerCustomer(int customerId, String name, String email) {
            if (findCustomerById(customerId) != null) {
                JOptionPane.showMessageDialog(frame, "Customer ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            CustomerRecord newCustomer = new CustomerRecord(customerId, name, email);
            allCustomers.insert(newCustomer);
        }

        public void placeOrder(int customerId, int orderId, LinkedList<Product> productList, String orderDate) {
            CustomerRecord customer = findCustomerById(customerId);
            if (customer == null) {
                JOptionPane.showMessageDialog(frame, "Customer ID not found :(", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Order newOrder = new Order(orderId, customerId, productList, orderDate);
            customer.getOrders().insert(newOrder);
            orders.getOrderList().insert(newOrder);
        }

        public String viewOrderHistory(int customerId) {
            CustomerRecord customer = findCustomerById(customerId);
            if (customer == null) {
                return "Customer ID " + customerId + " not found :(";
            }

            LinkedList<Order> customerOrders = customer.getOrders();
            if (customerOrders.empty()) {
                return "Customer " + customer.getName() + " has no orders.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("--- Order History for Customer ID ").append(customerId).append(" ---\n");

            customerOrders.findFirst();
            while (true) {
                Order o = customerOrders.retrieve();
                if (o != null) {
                    sb.append("  Order ID: ").append(o.getOrderId())
                            .append(", Status: ").append(o.getStatus())
                            .append(", Date: ").append(o.getOrderDate())
                            .append(", Total: $").append(String.format("%.2f", o.getTotalPrice())).append("\n");
                }

                if (customerOrders.last()) {
                    break;
                }
                customerOrders.findNext();
            }
            return sb.toString();
        }

        public CustomerRecord findCustomerById(int customerId) {
            if (allCustomers.empty()) {
                return null;
            }

            Node<CustomerRecord> temp = allCustomers.getHead();
            while (temp != null) {
                if (temp.data.getCustomerId() == customerId) {
                    return temp.data;
                }
                temp = temp.next;
            }
            return null;
        }

        public String extractCustomerReviews(int customerId) {
            CustomerRecord customer = findCustomerById(customerId);
            if (customer == null) {
                return "Customer ID " + customerId + " not found.";
            }

            LinkedList<Review> customerReviews = customer.getReviews();
            if (customerReviews.empty()) {
                return "Customer " + customer.getName() + " has no reviews.";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("--- Reviews by Customer ").append(customer.getName()).append(" (ID: ").append(customerId).append(") ---\n");

            customerReviews.findFirst();
            while (true) {
                Review r = customerReviews.retrieve();
                if (r != null) {
                    Product p = products.findProductById(r.getProductId());
                    String pName = p != null ? p.getName() : "Product " + r.getProductId();

                    sb.append("- ").append(pName).append(":\n")
                            .append("  Review ID: ").append(r.getReviewId()).append("\n")
                            .append("  Rating: ").append(r.getRating()).append(" / 5\n")
                            .append("  Comment: \"").append(r.getComment()).append("\"\n");
                }

                if (customerReviews.last()) {
                    break;
                }
                customerReviews.findNext();
            }
            return sb.toString();
        }
    }

    private class Products {

        private LinkedList<Product> allProducts = new LinkedList<>();

        public void addProduct(Product p) {
            if (findProductById(p.getProductId()) == null) {
                allProducts.insert(p);
                JOptionPane.showMessageDialog(frame, "Product added successfully: " + p.getName());
            } else {
                JOptionPane.showMessageDialog(frame, "Product ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public Product findProductById(int productId) {
            if (allProducts.empty()) {
                return null;
            }

            allProducts.findFirst();
            while (true) {
                Product current1 = allProducts.retrieve();
                if (current1 != null && current1.getProductId() == productId) {
                    return current1;
                }

                if (allProducts.last()) {
                    break;
                }
                allProducts.findNext();
            }
            return null;
        }

        public void addReview(int productId, Review r) {
            Product p = findProductById(productId);
            if (p != null) {
                p.addReview(r);
                CustomerRecord customer = customers.findCustomerById(r.getCustomerId());
                if (customer != null) {
                    customer.addReview(r);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Product with ID " + productId + " not found. Review not added.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private double calculateAverageRating(Product p) {
            LinkedList<Review> allreviews = p.getReviews();
            if (allreviews.empty()) {
                return 0;
            }

            double sum = 0;
            int count = 0;
            allreviews.findFirst();
            while (true) {
                Review currentReview = allreviews.retrieve();
                if (currentReview != null) {
                    sum += currentReview.getRating();
                    count++;
                }
                if (allreviews.last()) {
                    break;
                }
                allreviews.findNext();
            }
            return sum / count;
        }

        private boolean hasCustomerReviewed(Product p, int customerId) {
            LinkedList<Review> reviews = p.getReviews();
            if (reviews.empty()) {
                return false;
            }

            reviews.findFirst();
            while (true) {
                Review r = reviews.retrieve();
                if (r != null && r.getCustomerId() == customerId) {
                    return true;
                }
                if (reviews.last()) {
                    break;
                }
                reviews.findNext();
            }
            return false;
        }

        public String getTop3Products() {
            if (allProducts.empty()) {
                return "No products available.";
            }

            // FIX: DECLARE AS CONCRETE ArrayList<Product>
            ArrayList<Product> sortableProducts = new ArrayList<>();

            // 1. EXTRACT: Use custom LinkedList traversal
            allProducts.findFirst();
            while (true) {
                Product p = allProducts.retrieve();
                if (p != null) {
                    sortableProducts.add(p);
                }
                if (allProducts.last()) {
                    break;
                }
                allProducts.findNext();
            }

            if (sortableProducts.isEmpty()) {
                return "No products available.";
            }
            // 2. SORT
            sortableProducts.sort(Comparator.comparingDouble(this::calculateAverageRating).reversed());

            StringBuilder sb = new StringBuilder();
            sb.append("--- ★Top 3 Products by Rating★ ---\n");

            int count = 0;
            for (Product p : sortableProducts) {
                double avgRating = calculateAverageRating(p);

                if (avgRating > 0) {
                    sb.append(count + 1).append(". ").append(p.getName()).append("\n")
                            .append("    Rating: ").append(String.format("%.1f", avgRating)).append(" out of 5\n")
                            .append("    Price: $").append(String.format("%.2f", p.getPrice())).append("\n");
                    count++;
                }
                if (count >= 3) {
                    break;
                }
            }

            if (count == 0) {
                return "No products with ratings available.";
            }
            return sb.toString();
        }

        // Option 9: Common Reviewed Products Between Two Customers 
        public String commonProducts(int cust1, int cust2) {
            StringBuilder sb = new StringBuilder();

            CustomerRecord c1 = customers.findCustomerById(cust1);
            CustomerRecord c2 = customers.findCustomerById(cust2);

            String c1Name = c1 != null ? c1.getName() : "ID " + cust1;
            String c2Name = c2 != null ? c2.getName() : "ID " + cust2;

            sb.append("--- Common Highly-Rated Products (Rating > 4.0) ---\n");
            sb.append("Customers ").append(c1Name).append(" & ").append(c2Name).append(" both reviewed:\n");

            int count = 0;

            if (!allProducts.empty()) {
                allProducts.findFirst();
                while (true) {
                    Product p = allProducts.retrieve();

                    if (p != null) {
                        double rating = calculateAverageRating(p);

                        if (rating > 4.0 && hasCustomerReviewed(p, cust1) && hasCustomerReviewed(p, cust2)) {
                            count++;
                            sb.append(count).append(". ").append(p.getName())
                                    .append(" - ").append(String.format("%.1f", rating)).append(" out of 5\n");
                        }
                    }

                    if (allProducts.last()) {
                        break;
                    }
                    allProducts.findNext();
                }
            }

            if (count == 0) {
                sb.append("No common products with rating above 4.0 found between these customers.");
            }
            return sb.toString();
        }

        public String getAllProductsForOrder() {
            if (allProducts.empty()) {
                return "No products available.";
            }

            StringBuilder sb = new StringBuilder();
            allProducts.findFirst();
            while (true) {
                Product p = allProducts.retrieve();
                if (p != null) {
                    sb.append(String.format("ID %d: %s | Price: $%.2f | Stock: %d\n",
                            p.getProductId(), p.getName(), p.getPrice(), p.getStock()));
                }
                if (allProducts.last()) {
                    break;
                }
                allProducts.findNext();
            }
            return sb.toString();
        }
    }

    private class Orders {

        private LinkedList<Order> orderList = new LinkedList<>();

        public Orders() {
        }

        public String showOrdersBetween(String start, String end) {
            Node<Order> current = orderList.getHead();
            StringBuilder sb = new StringBuilder();

            boolean found = false;

            while (current != null) {
                Order o = current.data;
                String date = o.getOrderDate();

                if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                    sb.append(o.toString()).append("\n");
                    found = true;
                }
                current = current.next;
            }

            if (!found) {
                return "No orders found in this date range.";
            }
            return sb.toString();
        }

        public LinkedList<Order> getOrderList() {
            return orderList;
        }
    }

    private class Reviews {

        private LinkedList<Review> reviewList = new LinkedList<>();

        public Reviews() {
        }

        public void addReview(Review r) {
            reviewList.insert(r);
        }
    }

    // 3. GUI and Main Class Fields/Methods
    private final Customers customers;
    private final Products products;
    private final Orders orders;
    private final Reviews reviews;

    private JFrame frame;
    private JPanel cards;
    private CardLayout cardLayout;

    // Field to hold the JTextArea for the Place Order product list (for refresh)
    private JTextArea orderProductListArea;

    private static ManagementSystemGUI instance;

    private static ManagementSystemGUI getInstance() {
        return instance;
    }

    public ManagementSystemGUI() {
        instance = this;
        customers = new Customers();
        products = new Products();
        orders = new Orders();
        reviews = new Reviews();

        loadData();

        createAndShowGUI();
    }

    private void loadData() {
        loadCustomers(DATASET_PATH + "customers.csv", customers);
        loadProducts(DATASET_PATH + "prodcuts.csv", products);
        loadOrders(DATASET_PATH + "orders.csv", customers, products);
        loadReviews(DATASET_PATH + "reviews.csv", reviews, customers, products);
    }

    private static void loadCustomers(String filePath, Customers customers) {
        try (Scanner FS = new Scanner(new File(filePath))) {
            FS.nextLine(); // skip header
            while (FS.hasNextLine()) {
                String tmpData = FS.nextLine();
                String[] parts = tmpData.split(",");
                if (parts.length >= 3) {
                    try {
                        int tmpId = Integer.parseInt(parts[0].trim());
                        String tmpName = parts[1].trim();
                        String tmpEmail = parts[2].trim();
                        CustomerRecord newCustomer = new CustomerRecord(tmpId, tmpName, tmpEmail);
                        customers.allCustomers.insert(newCustomer);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed customer line: " + tmpData);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Customers file not found at " + filePath);
        }
    }

    private static void loadProducts(String filePath, Products products) {
        try (Scanner FS = new Scanner(new File(filePath))) {
            FS.nextLine(); // skip header
            while (FS.hasNextLine()) {
                String tmpData = FS.nextLine();
                String[] parts = tmpData.split(",");
                if (parts.length >= 4) {
                    try {
                        int tmpId = Integer.parseInt(parts[0].trim());
                        String tmpName = parts[1].trim();
                        double tmpPrice = Double.parseDouble(parts[2].trim());
                        int tmpStock = Integer.parseInt(parts[3].trim());
                        Product p = new Product(tmpId, tmpName, tmpPrice, tmpStock);
                        products.allProducts.insert(p);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping malformed product line: " + tmpData);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Products file not found at " + filePath);
        }
    }

    public static void loadOrders(String filename, Customers customers, Products products) {
        Orders globalOrders = ManagementSystemGUI.getInstance().orders;

        try (Scanner sc = new Scanner(new File(filename))) {
            sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                int firstComma = line.indexOf(',');
                int secondComma = line.indexOf(',', firstComma + 1);
                int thirdComma = line.indexOf(',', secondComma + 1);

                if (firstComma == -1 || secondComma == -1 || thirdComma == -1) {
                    continue;
                }

                try {
                    String part1 = line.substring(0, firstComma).trim();
                    String part2 = line.substring(firstComma + 1, secondComma).trim();
                    String productIdsStr = line.substring(secondComma + 1, thirdComma).replace("\"", "").trim();
                    String remaining = line.substring(thirdComma + 1).trim();

                    String[] remainingParts = remaining.split(",", 3);

                    if (remainingParts.length < 3) {
                        continue;
                    }

                    int orderId = Integer.parseInt(part1);
                    int customerId = Integer.parseInt(part2);
                    String orderDate = remainingParts[1].trim();
                    String status = remainingParts[2].trim();

                    LinkedList<Product> orderProducts = new LinkedList<>();
                    String[] productIds = productIdsStr.split(";");

                    for (String pidStr : productIds) {
                        int productId = Integer.parseInt(pidStr.trim());
                        Product p = products.findProductById(productId);
                        if (p != null) {
                            orderProducts.insert(p);
                        } else {
                            System.err.println("Product ID " + productId + " not found for order " + orderId);
                        }
                    }

                    if (orderProducts.empty()) {
                        continue;
                    }

                    Order newOrder = new Order(orderId, customerId, orderProducts, orderDate);
                    newOrder.setStatus(status);
                    globalOrders.getOrderList().insert(newOrder);

                    CustomerRecord customer = customers.findCustomerById(customerId);
                    if (customer != null) {
                        customer.getOrders().insert(newOrder);
                    } else {
                        System.err.println("Customer ID " + customerId + " not found for order " + orderId);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing order line: " + line + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    private static void loadReviews(String filePath, Reviews reviews, Customers customers, Products products) {
        try (Scanner FS = new Scanner(new File(filePath))) {
            FS.nextLine(); // skip header

            while (FS.hasNextLine()) {
                String tmpData = FS.nextLine();
                String[] parts = tmpData.split(",", 5);

                if (parts.length >= 5) {
                    try {
                        int reviewId = Integer.parseInt(parts[0].trim());
                        int productId = Integer.parseInt(parts[1].trim());
                        int customerId = Integer.parseInt(parts[2].trim());
                        int rating = Integer.parseInt(parts[3].trim());
                        String comment = parts[4].replaceAll("^\"|\"$", "");

                        Review r = new Review(reviewId, productId, rating, customerId, comment);
                        reviews.addReview(r);
                        assignReviewToCustomerAndProduct(r, customers, products);
                    } catch (NumberFormatException | InvalidRatingException e) {
                        System.err.println("Skipping malformed review line: " + tmpData + " - " + e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Reviews file not found at " + filePath);
        }
    }

    private static void assignReviewToCustomerAndProduct(Review r, Customers customers, Products products) {
        CustomerRecord customer = customers.findCustomerById(r.getCustomerId());
        if (customer != null) {
            customer.addReview(r);
        } else {
            System.err.println("Customer ID " + r.getCustomerId() + " not found for review " + r.getReviewId());
        }

        Product product = products.findProductById(r.getProductId());
        if (product != null) {
            product.addReview(r);
        } else {
            System.err.println("Product ID " + r.getProductId() + " not found for review " + r.getReviewId());
        }
    }

    // Helper method to refresh the product list JTextArea for the Place Order screen
    private void refreshProductListArea() {
        if (orderProductListArea != null) {
            orderProductListArea.setText(products.getAllProductsForOrder());
        }
    }

    // NEW: Helper method to create a JComboBox from a LinkedList
    private <T> JComboBox<String> createIdSelector(LinkedList<T> list, java.util.function.Function<T, String> extractor) {
        ArrayList<String> items = new ArrayList<>();
        if (!list.empty()) {
            list.findFirst();
            while (true) {
                T data = list.retrieve();
                if (data != null) {
                    items.add(extractor.apply(data));
                }
                if (list.last()) {
                    break;
                }
                list.findNext();
            }
        }
        return new JComboBox<>(items.toArray(new String[0]));
    }

    // NEW: Helper to extract ID from selection string like "ID: 101 - Laptop A"
    private int extractIdFromString(String selectedItem) {
        try {
            if (selectedItem != null && selectedItem.contains("ID: ")) {
                // Find the ID part between "ID: " and the next space or dash
                int start = selectedItem.indexOf("ID: ") + 4;
                int end = selectedItem.indexOf(" -", start);
                if (end == -1) {
                    end = selectedItem.length();
                }
                String idPart = selectedItem.substring(start, end);
                return Integer.parseInt(idPart.trim());
            } else if (selectedItem != null) {
                // Handle cases where the item is just an ID string
                return Integer.parseInt(selectedItem.trim());
            }
        } catch (Exception e) {
            // Error handling for malformed string
        }
        return -1;
    }

    // 4. GUI Component Methods
    private void createAndShowGUI() {
        frame = new JFrame("E-Commerce Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(DARK_BLUE);

        // Header
        try {
            Image img = ImageIO.read(new File("images/readme_header.png"));
            JLabel headerLabel = new JLabel(new ImageIcon(img));
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(headerLabel);
        } catch (IOException e) {
            JLabel fallback = new JLabel("E-Commerce Management System", SwingConstants.CENTER);
            fallback.setOpaque(true);
            fallback.setBackground(DARK_BLUE);
            fallback.setForeground(Color.WHITE);
            fallback.setFont(new Font("Arial", Font.BOLD, 28));
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            fallback.setMaximumSize(new Dimension(800, 100));
            container.add(fallback);
        }

        // Buttons Setup
        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        buttonPanel.setBackground(DARK_BLUE);
        buttonPanel.setMaximumSize(new Dimension(700, 225));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addProductBtn = createMenuButton("1. Add Product");
        JButton addCustomerBtn = createMenuButton("2. Add Customer");
        JButton placeOrderBtn = createMenuButton("3. Place New Order");
        JButton viewOrdersBtn = createMenuButton("4. View Customer Orders");
        JButton addReviewBtn = createMenuButton("5. Add Review for Product");
        JButton extractReviewsBtn = createMenuButton("6. Extract Customer Reviews");
        JButton topProductsBtn = createMenuButton("7. Top 3 Products by Avg Rating");
        JButton ordersBetweenDatesBtn = createMenuButton("8. Orders Between Two Dates");
        JButton commonProductsBtn = createMenuButton("9. Common Reviewed Products");
        JButton exitBtn = createMenuButton("10. Exit");

        buttonPanel.add(addProductBtn);
        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(viewOrdersBtn);
        buttonPanel.add(addReviewBtn);
        buttonPanel.add(extractReviewsBtn);
        buttonPanel.add(topProductsBtn);
        buttonPanel.add(ordersBetweenDatesBtn);
        buttonPanel.add(commonProductsBtn);
        buttonPanel.add(exitBtn);

        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(buttonPanel);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setPreferredSize(new Dimension(950, 450));
        cards.setBackground(DARK_BLUE);

        cards.add(createWelcomePanel(), "home");
        cards.add(createAddProductPanel(), "addProduct");
        cards.add(createAddCustomerPanel(), "addCustomer");
        cards.add(createPlaceOrderPanel(), "placeOrder");
        // FIX: The functionality of View Orders is now completely moved to the button listener below.
        cards.add(createFillerPanel("Click 'View Customer Orders' to select an ID from the list.", "viewOrders"));
        cards.add(createAddReviewPanel(), "addReview");
        cards.add(createFillerPanel("Click 'Extract Customer Reviews' to select an ID from the list.", "extractReviews"));
        cards.add(createFillerPanel("Click 'Top 3 Products by Avg Rating' on the menu to view results.", "topProducts"));
        cards.add(createOrdersBetweenDatesPanel(), "ordersBetween");
        cards.add(createFillerPanel("Click 'Common Reviewed Products' to select two IDs from the list.", "commonProducts"));

        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(cards);

        // Action Listeners
        addProductBtn.addActionListener(e -> cardLayout.show(cards, "addProduct"));
        addCustomerBtn.addActionListener(e -> cardLayout.show(cards, "addCustomer"));

        // Option 3: Refresh product list before showing Place Order panel
        placeOrderBtn.addActionListener(e -> {
            refreshProductListArea();
            cardLayout.show(cards, "placeOrder");
        });

        // FIX: Option 4 - View Customer Orders (using list selector)
        viewOrdersBtn.addActionListener(e -> {
            JComboBox<String> customerSelector = createIdSelector(
                    customers.allCustomers,
                    c -> String.format("ID: %d - %s", c.getCustomerId(), c.getName())
            );

            int option = JOptionPane.showConfirmDialog(frame, customerSelector, "Select Customer to View Orders:", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION && customerSelector.getSelectedItem() != null) {
                try {
                    int id = extractIdFromString((String) customerSelector.getSelectedItem());
                    if (id != -1) {
                        String orderData = customers.viewOrderHistory(id);
                        Dimension wideSize = new Dimension(900, 300);
                        String resultCardName = "ordersResultSingle_" + System.currentTimeMillis();
                        JPanel resultsPanel = createLargeTextResultsPanel(orderData, "Order History for Customer " + id, wideSize);
                        cards.add(resultsPanel, resultCardName);
                        cardLayout.show(cards, resultCardName);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error processing selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addReviewBtn.addActionListener(e -> cardLayout.show(cards, "addReview"));

        // FIX: Option 6 - Extract Customer Reviews (using list selector)
        extractReviewsBtn.addActionListener(e -> {
            JComboBox<String> customerSelector = createIdSelector(
                    customers.allCustomers,
                    c -> String.format("ID: %d - %s", c.getCustomerId(), c.getName())
            );

            int option = JOptionPane.showConfirmDialog(frame, customerSelector, "Select Customer to Extract Reviews For:", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION && customerSelector.getSelectedItem() != null) {
                try {
                    int id = extractIdFromString((String) customerSelector.getSelectedItem());
                    if (id != -1) {
                        String reviewData = customers.extractCustomerReviews(id);
                        Dimension wideSize = new Dimension(900, 300);
                        String resultCardName = "reviewsResult_" + System.currentTimeMillis();
                        JPanel resultsPanel = createLargeTextResultsPanel(reviewData, "Reviews by Customer " + id, wideSize);
                        cards.add(resultsPanel, resultCardName);
                        cardLayout.show(cards, resultCardName);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error processing selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        topProductsBtn.addActionListener(e -> {
            String topData = products.getTop3Products();
            Dimension wideSize = new Dimension(900, 300);
            String resultCardName = "topProductsResult_" + System.currentTimeMillis();
            JPanel resultsPanel = createLargeTextResultsPanel(topData, "Top 3 Products by Rating", wideSize);
            cards.add(resultsPanel, resultCardName);
            cardLayout.show(cards, resultCardName);
        });

        ordersBetweenDatesBtn.addActionListener(e -> cardLayout.show(cards, "ordersBetween"));

        // FIX: Option 9 - Common Reviewed Products (using list selectors)
        commonProductsBtn.addActionListener(e -> {
            JComboBox<String> customerSelector1 = createIdSelector(
                    customers.allCustomers,
                    c -> String.format("ID: %d - %s", c.getCustomerId(), c.getName())
            );
            JComboBox<String> customerSelector2 = createIdSelector(
                    customers.allCustomers,
                    c -> String.format("ID: %d - %s", c.getCustomerId(), c.getName())
            );

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Customer 1:"));
            panel.add(customerSelector1);
            panel.add(new JLabel("Customer 2:"));
            panel.add(customerSelector2);

            int option = JOptionPane.showConfirmDialog(frame, panel, "Select Two Customers", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int id1 = extractIdFromString((String) customerSelector1.getSelectedItem());
                    int id2 = extractIdFromString((String) customerSelector2.getSelectedItem());

                    if (id1 != -1 && id2 != -1) {
                        String commonData = products.commonProducts(id1, id2);
                        String resultCardName = "commonProductsResult_" + System.currentTimeMillis();
                        Dimension wideSize = new Dimension(900, 300);
                        JPanel resultsPanel = createLargeTextResultsPanel(commonData, "Common Reviewed Products", wideSize);
                        cards.add(resultsPanel, resultCardName);
                        cardLayout.show(cards, resultCardName);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid selection for one or both customers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error processing selection.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        exitBtn.addActionListener(e -> frame.dispose());

        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(cards, "home");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(DARK_BLUE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        return btn;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BLUE);
        JLabel instructions = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to your E-commerce System!"
                + "<br><br>Manage products, track orders, and monitor customers."
                + "</div></html>", SwingConstants.CENTER);

        instructions.setForeground(Color.WHITE);
        instructions.setFont(new Font("Arial", Font.ITALIC, 18));
        instructions.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        panel.add(instructions, BorderLayout.CENTER);
        return panel;
    }

    // New: Reusable Filler Panel (for menu options that use dialogs)
    private JPanel createFillerPanel(String message, String name) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel infoLabel = new JLabel(message);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(infoLabel, gbc);
        JPanel filler = new JPanel();
        filler.setBackground(DARK_BLUE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(filler, gbc);
        return panel;
    }

    // 1. Add Product
    private JPanel createAddProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 35);
        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);
        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(fieldSize);
        nameField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(priceLabel, gbc);
        JTextField priceField = new JTextField();
        priceField.setPreferredSize(fieldSize);
        priceField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(priceField, gbc);
        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setForeground(Color.WHITE);
        stockLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(stockLabel, gbc);
        JTextField stockField = new JTextField();
        stockField.setPreferredSize(fieldSize);
        stockField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(stockField, gbc);
        JButton submit = new JButton("Add Product");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                products.addProduct(new Product(id, name, price, stock));
                idField.setText("");
                nameField.setText("");
                priceField.setText("");
                stockField.setText("");
                cardLayout.show(cards, "home");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid input. Check ID (int), Price (double), and Stock (int).", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    // 2. Add Customer
    private JPanel createAddCustomerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 35);
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(idLabel, gbc);
        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(fieldSize);
        nameField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emailLabel, gbc);
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        emailField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(emailField, gbc);
        JButton submit = new JButton("Add Customer");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                customers.registerCustomer(id, name, email);
                idField.setText("");
                nameField.setText("");
                emailField.setText("");
                cardLayout.show(cards, "home");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid Customer ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    // 3. Place New Order
    private JPanel createPlaceOrderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Dimension fieldSize = new Dimension(150, 25);

        // Fields that need direct text input
        JTextField customerIdField = new JTextField();
        JTextField orderIdField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField productIdField = new JTextField(); // Temporary ID input or ID selection

        customerIdField.setPreferredSize(fieldSize);
        orderIdField.setPreferredSize(fieldSize);
        dateField.setPreferredSize(fieldSize);
        productIdField.setPreferredSize(fieldSize);

        customerIdField.setFont(fieldFont);
        orderIdField.setFont(fieldFont);
        dateField.setFont(fieldFont);
        productIdField.setFont(fieldFont);

        // Uses the class field for refresh capability
        orderProductListArea = new JTextArea();
        orderProductListArea.setEditable(false);
        orderProductListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane productScrollPane = new JScrollPane(orderProductListArea);
        productScrollPane.setPreferredSize(new Dimension(300, 200));

        JTextArea selectedProductsArea = new JTextArea(5, 20);
        selectedProductsArea.setEditable(false);
        selectedProductsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane selectedScrollPane = new JScrollPane(selectedProductsArea);

        LinkedList<Product> selectedProducts = new LinkedList<>();

        // New: Customer ID selector button
        JButton selectCustomerBtn = new JButton("Select Customer ID");

        // New: Product ID selector button
        JButton selectProductBtn = new JButton("Select Product ID");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel customerLabel = new JLabel("Customer ID:");
        customerLabel.setForeground(Color.WHITE);
        panel.add(customerLabel, gbc);

        gbc.gridx = 1;
        customerIdField.setPreferredSize(new Dimension(50, 25)); // Make it smaller to hold the final ID
        panel.add(customerIdField, gbc);

        gbc.gridx = 2;
        panel.add(selectCustomerBtn, gbc);

        // Row 1: Order ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel orderLabel = new JLabel("Order ID:");
        orderLabel.setForeground(Color.WHITE);
        panel.add(orderLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(orderIdField, gbc);

        // Row 2: Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setForeground(Color.WHITE);
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(dateField, gbc);

        // Row 3: Add Product controls
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel addLabel = new JLabel("Product ID to Add:");
        addLabel.setForeground(Color.WHITE);
        panel.add(addLabel, gbc);

        gbc.gridx = 1;
        productIdField.setPreferredSize(new Dimension(50, 25)); // Make it smaller to hold the final ID
        panel.add(productIdField, gbc);

        JButton addProductToOrderBtn = new JButton("Add to Cart");
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(addProductToOrderBtn, gbc);

        // Row 4: List Viewers
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        JLabel availableLabel = new JLabel("Available Products (ID | Name | Price | Stock):");
        availableLabel.setForeground(Color.WHITE);
        panel.add(availableLabel, gbc);
        gbc.gridy = 5;
        panel.add(productScrollPane, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        JLabel selectedLabel = new JLabel("Selected Products:");
        selectedLabel.setForeground(Color.WHITE);
        panel.add(selectedLabel, gbc);
        gbc.gridy = 5;
        panel.add(selectedScrollPane, gbc);

        JButton checkoutBtn = new JButton("Place Order (Checkout)");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(checkoutBtn, gbc);

        // Action listeners for selectors
        selectCustomerBtn.addActionListener(e -> {
            JComboBox<String> customerSelector = createIdSelector(
                    customers.allCustomers,
                    c -> String.format("ID: %d - %s", c.getCustomerId(), c.getName())
            );
            int option = JOptionPane.showConfirmDialog(frame, customerSelector, "Select Customer ID:", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION && customerSelector.getSelectedItem() != null) {
                int id = extractIdFromString((String) customerSelector.getSelectedItem());
                if (id != -1) {
                    customerIdField.setText(String.valueOf(id));
                }
            }
        });

        selectProductBtn.addActionListener(e -> {
            JComboBox<String> productSelector = createIdSelector(
                    products.allProducts,
                    p -> String.format("ID: %d - %s", p.getProductId(), p.getName())
            );
            int option = JOptionPane.showConfirmDialog(frame, productSelector, "Select Product ID:", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION && productSelector.getSelectedItem() != null) {
                int id = extractIdFromString((String) productSelector.getSelectedItem());
                if (id != -1) {
                    productIdField.setText(String.valueOf(id));
                }
            }
        });

        addProductToOrderBtn.addActionListener(e -> {
            try {
                int pId = Integer.parseInt(productIdField.getText().trim());
                Product p = products.findProductById(pId);
                if (p != null) {
                    selectedProducts.insert(p);
                    String productEntry = String.format("ID %d: %s ($%.2f)\n", p.getProductId(), p.getName(), p.getPrice());
                    selectedProductsArea.append(productEntry);
                    productIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Product ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Product ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        checkoutBtn.addActionListener(e -> {
            try {
                int cId = Integer.parseInt(customerIdField.getText().trim());
                int oId = Integer.parseInt(orderIdField.getText().trim());
                String date = dateField.getText().trim();

                if (selectedProducts.empty()) {
                    JOptionPane.showMessageDialog(frame, "Please add products before checking out.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    LocalDate.parse(date);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                customers.placeOrder(cId, oId, selectedProducts.deepCopy(), date);
                JOptionPane.showMessageDialog(frame, "Order placed successfully for Customer ID: " + cId);

                customerIdField.setText("");
                orderIdField.setText("");
                dateField.setText(LocalDate.now().toString());
                selectedProducts.current = selectedProducts.head = null;
                selectedProductsArea.setText("");
                cardLayout.show(cards, "home");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Customer ID or Order ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // 4. View Customer Orders - Now handled by the main menu listener and a filler panel
    private JPanel createViewCustomerOrdersPanel() {
        return createFillerPanel("Click 'View Customer Orders' to select an ID from the list.", "viewOrders");
    }

    // 5. Add Review for Product
    private JPanel createAddReviewPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST; // Changed anchor for left alignment
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font fieldFont = new Font("Arial", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(70, 30); // Smaller field for displaying ID

        // Fields to display the selected ID
        JTextField reviewIdField = new JTextField();
        JTextField productIdField = new JTextField();
        JTextField customerIdField = new JTextField();
        JTextField ratingField = new JTextField();
        JTextArea commentArea = new JTextArea(3, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);

        reviewIdField.setPreferredSize(fieldSize);
        productIdField.setPreferredSize(fieldSize);
        customerIdField.setPreferredSize(fieldSize);
        ratingField.setPreferredSize(fieldSize);

        // Buttons for selection
        JButton selectProductBtn = new JButton("Select Product ID");
        JButton selectCustomerBtn = new JButton("Select Customer ID");
        JButton viewProductsBtn = new JButton("View All Products (Reference)");

        // Row 0: Review ID
        JLabel revIdLabel = new JLabel("Review ID:");
        revIdLabel.setForeground(Color.WHITE);
        revIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(revIdLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(reviewIdField, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Row 1: Product ID Selector
        JLabel prodIdLabel = new JLabel("Product ID:");
        prodIdLabel.setForeground(Color.WHITE);
        prodIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(prodIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(productIdField, gbc);
        gbc.gridx = 2;
        panel.add(selectProductBtn, gbc);

        // Row 2: Customer ID Selector
        JLabel custIdLabel = new JLabel("Customer ID:");
        custIdLabel.setForeground(Color.WHITE);
        custIdLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(custIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(customerIdField, gbc);
        gbc.gridx = 2;
        panel.add(selectCustomerBtn, gbc);

        // Row 3: Rating
        JLabel ratingLabel = new JLabel("Rating (1-5 Stars):");
        ratingLabel.setForeground(Color.WHITE);
        ratingLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(ratingLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(ratingField, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Row 4: Comment
        JLabel commentLabel = new JLabel("Comment:");
        commentLabel.setForeground(Color.WHITE);
        commentLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(commentLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(commentScrollPane, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Row 5: Submit and View button
        JButton submit = new JButton("Add Review");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 2;
        gbc.gridy = 5;
        panel.add(submit, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(viewProductsBtn, gbc);

        // Listeners for selectors
        selectCustomerBtn.addActionListener(e -> {
            JComboBox<String> customerSelector = createIdSelector(
                    customers.allCustomers,
                    c -> String.format("ID: %d - %s", c.getCustomerId(), c.getName())
            );
            int option = JOptionPane.showConfirmDialog(frame, customerSelector, "Select Customer ID:", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION && customerSelector.getSelectedItem() != null) {
                int id = extractIdFromString((String) customerSelector.getSelectedItem());
                if (id != -1) {
                    customerIdField.setText(String.valueOf(id));
                }
            }
        });

        selectProductBtn.addActionListener(e -> {
            JComboBox<String> productSelector = createIdSelector(
                    products.allProducts,
                    p -> String.format("ID: %d - %s", p.getProductId(), p.getName())
            );
            int option = JOptionPane.showConfirmDialog(frame, productSelector, "Select Product ID:", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION && productSelector.getSelectedItem() != null) {
                int id = extractIdFromString((String) productSelector.getSelectedItem());
                if (id != -1) {
                    productIdField.setText(String.valueOf(id));
                }
            }
        });

        // Listener for view button (shows the products list in a separate window/card)
        viewProductsBtn.addActionListener(e -> {
            String allProductsData = products.getAllProductsForOrder();
            Dimension wideSize = new Dimension(700, 300);
            String resultCardName = "allProductsResult_" + System.currentTimeMillis();

            JPanel resultsPanel = createLargeTextResultsPanel(allProductsData, "All Available Products", wideSize);
            cards.add(resultsPanel, resultCardName);
            cardLayout.show(cards, resultCardName);
        });

        // Submit listener
        submit.addActionListener(e -> {
            try {
                int reviewId = Integer.parseInt(reviewIdField.getText().trim());
                int productId = Integer.parseInt(productIdField.getText().trim());
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                int rating = Integer.parseInt(ratingField.getText().trim());
                String comment = commentArea.getText().trim();
                if (rating < 1 || rating > 5) {
                    throw new InvalidRatingException("Rating must be between 1 and 5 stars.");
                }
                Review newReview = new Review(reviewId, productId, rating, customerId, comment);
                products.addReview(productId, newReview);
                JOptionPane.showMessageDialog(frame, "Review added successfully!");
                reviewIdField.setText("");
                productIdField.setText("");
                customerIdField.setText("");
                ratingField.setText("");
                commentArea.setText("");
                cardLayout.show(cards, "home");
            } catch (InvalidRatingException ex) {
                JOptionPane.showMessageDialog(frame, "ERROR: " + ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid input. Check IDs, Rating (int), and use selectors for IDs.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return panel;
    }

    // 6. Extract Reviews from Specific Customer - Now handled by the main menu listener and a filler panel
    private JPanel createExtractReviewsPanel() {
        return createFillerPanel("Click 'Extract Customer Reviews' to select an ID from the list.", "extractReviews");
    }

    // 7. Show Top 3 Products by Average Rating - Now handled by the main menu listener and a filler panel
    private JPanel createTopProductsPanel() {
        return createFillerPanel("Click 'Top 3 Products by Avg Rating' on the menu to view results.", "topProducts");
    }

    // 8. Orders Between Two Dates
    private JPanel createOrdersBetweenDatesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(300, 45);

        JLabel fromLabel = new JLabel("From Date (yyyy-mm-dd):");
        fromLabel.setForeground(Color.WHITE);
        fromLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(fromLabel, gbc);
        JTextField fromField = new JTextField();
        fromField.setPreferredSize(fieldSize);
        fromField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(fromField, gbc);
        JLabel toLabel = new JLabel("To Date (yyyy-mm-dd):");
        toLabel.setForeground(Color.WHITE);
        toLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(toLabel, gbc);
        JTextField toField = new JTextField();
        toField.setPreferredSize(fieldSize);
        toField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(toField, gbc);

        JButton showBtn = new JButton("Show Orders");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(showBtn, gbc);

        JPanel filler = new JPanel();
        filler.setBackground(DARK_BLUE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(filler, gbc);

        showBtn.addActionListener(e -> {
            String from = fromField.getText().trim();
            String to = toField.getText().trim();

            if (from.isEmpty() || to.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter both start and end dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    LocalDate.parse(from);
                    LocalDate.parse(to);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid Date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String orderList = orders.showOrdersBetween(from, to);
                Dimension wideSize = new Dimension(900, 200);
                String resultCardName = "ordersResult_" + System.currentTimeMillis();
                JPanel resultsPanel = createOrdersResultsPanel(orderList, from, to, wideSize);

                cards.add(resultsPanel, resultCardName);
                cardLayout.show(cards, resultCardName);
            }
        });
        return panel;
    }

    // 9. Common Reviewed Products Between Two Customers - Now handled by the main menu listener and a filler panel
    private JPanel createCommonProductsPanel() {
        return createFillerPanel("Click 'Common Reviewed Products' to select two IDs from the list.", "commonProducts");
    }

    // Reusable Large Results Panel
    private JPanel createLargeTextResultsPanel(String results, String title, Dimension scrollPaneSize) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font outputFont = new Font("Monospaced", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(titleLabel, gbc);

        JTextArea outputArea = new JTextArea(20, 90);
        outputArea.setText(results);
        outputArea.setEditable(false);
        outputArea.setFont(outputFont);
        outputArea.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(scrollPaneSize);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        JButton backBtn = new JButton("<< Back to Main Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(backBtn, gbc);

        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));
        return panel;
    }

    // 8. Orders Between Dates Results Panel
    private JPanel createOrdersResultsPanel(String results, String from, String to, Dimension scrollPaneSize) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font outputFont = new Font("Monospaced", Font.PLAIN, 16);
        JLabel titleLabel = new JLabel("Orders Found (" + from + " to " + to + ")");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(titleLabel, gbc);
        JTextArea outputArea = new JTextArea(12, 90);
        outputArea.setText(results);
        outputArea.setEditable(false);
        outputArea.setFont(outputFont);
        outputArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(scrollPaneSize);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);
        JButton backBtn = new JButton("<< Back to Main Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(backBtn, gbc);
        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagementSystemGUI::new);
    }
}
