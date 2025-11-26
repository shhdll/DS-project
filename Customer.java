public class Customer {

    private int customerId;
    private String name;
    private String email;
    private AVLTree<Order> orders; // one customer can have many orders
    private AVLTree<Review> reviews; // one customer can have many reviews

    // Constructor
    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.orders = new AVLTree<>();
        reviews = new AVLTree<>();
    }

    // getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public AVLTree<Order> getOrders() {
        return orders;
    }

    public AVLTree<Review> getReviews() {
        return reviews;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOrders(AVLTree<Order> orders) {
        this.orders = orders;
    }

    public void setReviews(AVLTree<Review> reviews) {
        this.reviews = reviews;
    }

    public void addOrder(Order o) {
        orders.insert(o.getOrderId(),o);
    }

    public void addReview(Review r) {
        reviews.insert(r.getReviewId(),r);
    }

    public void displayCustomerInfo() {
    System.out.println("Customer ID: " + customerId);
    System.out.println("Name: " + name);
    System.out.println("Email: " + email);
    System.out.println("Number of Orders: " + countOrders(orders.getRoot()));
}

// Recursive helper to count nodes in AVLTree
private int countOrders(AVLNode<Order> node) {
    if (node == null) return 0;
    return 1 + countOrders(node.left) + countOrders(node.right);
}


    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name + ", Email: " + email;
    }

}
