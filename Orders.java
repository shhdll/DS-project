public class Orders {
    private int orderId;
    private Customers customer; 
    private LinkedList<Products> productList;
    private double totalPrice;
    private String orderDate;
    private String status; // "Pending", "Shipped", "Delivered", "Canceled"  //enum 
     /*Or public enum OrderStatus {
    PENDING,
    SHIPPED,
    DELIVERED,
    CANCELED 
    } */

    public Orders(int orderId, Customers customer, LinkedList<Products> productList, String orderDate) {
        this.orderId = orderId;
        this.customer = customer;
        this.productList = productList;
        this.orderDate = orderDate;
        this.status = "Pending"; // default status
    }
     //Operations

    // 1 Cancel order
    public void cancelOrder() {
        if (!status.equalsIgnoreCase("Delivered")) {
            status = "Canceled";
            System.out.println("Order " + orderId + " has been canceled.");
        } else {
            System.out.println("Delivered orders cannot be canceled.");
        }
    }

    // 2 Update order status
    public void updateStatus(String newStatus) {
        status = newStatus;
        System.out.println("Order " + orderId + " status updated to: " + newStatus);
    }

    // 3 Search order by ID 

    // getters and setters
    public int getOrderId() { return orderId; }
    public Customers getCustomer() { return customer; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public LinkedList<Products> getProductList() { return productList; }
}


