public class Customers {  //customers represent a single customer, so maybe rename to singular? 
    private int customerId;
    private String name;
    private String email;
    private LinkedList<Orders> orders;  //linkedlist because it has easier insert and remove

    public Customers(int customerId, String name, String email) { 
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.orders = new LinkedList<>();
}

// Operations:

    //1 Register new customer
    public static Customers registerNewCustomer(int customerId, String name, String email) {
        return new Customers(customerId, name, email);
    }

    //2 Place a new order for a specific customer
     public void placeNewOrder(Orders order) {  //adds the new order to the end
        if (orders == null) {  //checks if orders is not initialized. won’t happen because of constructor?
            orders = new LinkedList<>();
        }

        // move to end then insert 
        if (orders.empty()) {  //empty
            orders.insert(order);
        } else {  //not empty
            orders.findFirst();
            while (!orders.last()) {  
                orders.findNext();
            }
            orders.insert(order);
        }
    }
    //3 View order history
    
    // getters and setters
    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LinkedList<Orders> getOrders() { return orders; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }




}
