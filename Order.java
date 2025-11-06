public class Order{
    private int orderId;
    private int customerID; 
    private LinkedList<Product> productList;
    private double totalPrice;
    private String orderDate;
    private String status; // "Pending", "Shipped", "Delivered", "Canceled"  //enum 
 
 
    public Order(int orderId, int customer, LinkedList<Product> productList, String orderDate) {
        this.orderId = orderId;
        this.customerID = customer;
        this.productList = productList;
        this.orderDate = orderDate;
        this.status = "Pending"; // default status
    } 
     // getters and setters
    public int getOrderId() { return orderId; }
    public int getOcustomer() { return customerID; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public LinkedList<Product> getProductList() { return productList; }
    public void setStatus(String s){status=s;}
    
}
