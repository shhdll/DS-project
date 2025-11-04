public class Order extends Node<Order>{
    private int orderId;
    private int Ocustomer; 
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
 
    public Order(int orderId, int customer, LinkedList<Products> productList, String orderDate) {
        this.orderId = orderId;
        this.Ocustomer = customer;
        this.productList = productList;
        this.orderDate = orderDate;
        this.status = "Pending"; // default status
    } 
     // getters and setters
    public int getOrderId() { return orderId; }
    public int getOcustomer() { return Ocustomer; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public LinkedList<Products> getProductList() { return productList; }
    public void setStatus(String s){status=s;}
    
}
