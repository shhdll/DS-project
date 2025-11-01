public class Order {
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
 
    public Order(int orderId, Customers customer, LinkedList<Products> productList, String orderDate) {
        this.orderId = orderId;
        this.customer = customer;
        this.productList = productList;
        this.orderDate = orderDate;
        this.status = "Pending"; // default status
    } 
     // getters and setters
    public int getOrderId() { return orderId; }
    public Customers getCustomer() { return customer; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public LinkedList<Products> getProductList() { return productList; }
}
