public class Customers {

    private AVLTree<Customer> allCustomers;

    public Customers() {
        allCustomers = new AVLTree<>();
    }

    // Operations:
    // 1 Register new customer
    public void registerCustomer(int customerId, String name, String email) {
        if (findCustomerById(customerId) != null) {
            System.out.println("Customer already exists");
            return;
        }

        Customer newCustomer = new Customer(customerId, name, email);
        allCustomers.insert(customerId,newCustomer);
        // System.out.println("Customer registered successfully");
    }

    // 2 Place a new order for a specific customer
    public void placeOrder(int customerId, int orderId, AVLTree<Product> productList, String orderDate) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }

        Order newOrder = new Order(orderId, customerId, productList, orderDate);
        customer.getOrders().insert(newOrder.getOrderId(),newOrder);
        System.out.println("Order placed successfully");
    }

    // 3 View order history
    public void viewOrderHistory(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }

        AVLTree<Order> orders = customer.getOrders();
        if (orders.getRoot() == null) {
            System.out.println("Customer has no orders");
            return;
        }

        System.out.println("Order history for customer ID " + customerId + ":");
        inOrderPrintOrders(orders.getRoot());
    }

    private void inOrderPrintOrders(AVLNode<Order> node) {
        if (node == null) return;
        inOrderPrintOrders(node.left);
        Order o = node.data;
        System.out.println(
                "  Order ID: " + o.getOrderId() + ", Status: " + o.getStatus() + ", Date: " + o.getOrderDate());
        inOrderPrintOrders(node.right);
    }

     public Customer findCustomerById(int customerId) {
    return findCustomerRec(allCustomers.getRoot(), customerId);
}

private Customer findCustomerRec(AVLNode<Customer> node, int customerId) {
    if (node == null) return null; // not found

    if (node.data.getCustomerId() == customerId) {
        return node.data;
    } else if (customerId < node.data.getCustomerId()) {
        return findCustomerRec(node.left, customerId);
    } else {
        return findCustomerRec(node.right, customerId);
    }
}


      public String toString() {
        StringBuilder sb = new StringBuilder();
        inOrderPrintCustomers(allCustomers.getRoot(), sb);
        return sb.length() == 0 ? "No customers found." : sb.toString();
    }
    private void inOrderPrintCustomers(AVLNode<Customer> node, StringBuilder sb) {
        if (node == null) return;
        inOrderPrintCustomers(node.left, sb);
        sb.append(node.data.toString()).append("\n");
        inOrderPrintCustomers(node.right, sb);
    }

    // 4 Extract customer reviews
    public void extractCustomerReviews(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }

        System.out.println(" Reviews by Customer " + customerId);
        AVLTree<Review> reviews = customer.getReviews();
        inOrderPrintReviews(reviews.getRoot());
    }

    private void inOrderPrintReviews(AVLNode<Review> node) {
        if (node == null) return;
        inOrderPrintReviews(node.left);
        Review r = node.data;
        System.out.println("- Product " + r.getProductId() + ": " + r.getRating() + " - " + r.getComment());
        inOrderPrintReviews(node.right);
    }

}