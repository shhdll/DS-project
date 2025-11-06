public class Order {
    private int orderId;
    private int customerID;
    private LinkedList<Product> productList;
    private double totalPrice;
    private String orderDate;
    private String status; // "Pending", "Shipped", "Delivered", "Canceled" //enum

    public Order(int orderId, int customer, LinkedList<Product> productList, String orderDate) {
        this.orderId = orderId;
        this.customerID = customer;
        this.productList = productList;
        this.orderDate = orderDate;
        this.status = "Pending"; // default status
    }

    public void addProduct(Product P) {

        productList.insert(P);
    }

    // getters and setters
    public int getOrderId() {
        return orderId;
    }

    public int getOcustomer() {
        return customerID;
    }

    public double getTotalPrice() {
        return totalPrice;
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

    public void setStatus(String s) {
        status = s;
    }

    public void displayOrderDetails() {
        System.out.println("=== Order Details ===");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer ID: " + customerID);
        System.out.println("Date: " + orderDate);
        System.out.println("Status: " + status);
        System.out.println("Total Price: " + totalPrice);

        System.out.println("Products in this order:");
        if (productList.empty()) {
            System.out.println("No products");
        } else {
            Node<Product> tmp = productList.getHead();
            int count = 1;
            while (tmp != null) {
                Product p = tmp.data;
                System.out.println("  " + count + ". " + p.getName() + " - " + p.getPrice() + " SAR");
                tmp = tmp.next;
                count++;
            }
        }
        System.out.println("----------------------");
    }
}
