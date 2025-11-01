public class Customer {
    private int customerId;
    private String name;
    private String email;
    private LinkedList<Orders> orders;  //linkedlist because it has easier insert and remove

    public Customer(int customerId, String name, String email) { 
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.orders = new LinkedList<>();
    }
 // getters and setters
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LinkedList<Orders> getOrders() { return orders; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

}
