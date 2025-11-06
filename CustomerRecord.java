public class CustomerRecord {

    private int customerId;
    private String name;
    private String email;
    private LinkedList<Order> orders; // one customer can have many orders
    private LinkedList<Review> reviews; // one customer can have many reviews

    // Constructor
    public CustomerRecord(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.orders = new LinkedList<>();
        reviews = new LinkedList<>();
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

    public LinkedList<Order> getOrders() {
        return orders;
    }

    public LinkedList<Review> getReviews() {
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

    public void setOrders(LinkedList<Order> orders) {
        this.orders = orders;
    }

    public void setReviews(LinkedList<Review> reviews) {
        this.reviews = reviews;
    }

    public void addOrder(Order o) {
        orders.insert(o);
    }

    public void addReview(Review r) {
        reviews.insert(r);
    }

    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Number of Orders: " + orders.size());
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name + ", Email: " + email;
    }

}
