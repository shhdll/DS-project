public class Orders {

    private AVLTree<Order> orderList = new AVLTree<>();

    // Operations
    // 1 create order
    public void createOrder(int orderId, int customerID, AVLTree<Product> productList, String orderDate) {
        Order o = new Order(orderId, customerID, productList, orderDate);
        orderList.insert(o.getOrderId(),o);
    }

    // 2 Cancel order
    public void cancelOrder(Order o) {
        if (!o.getStatus().equalsIgnoreCase("Delivered")) {
            o.setStatus("Canceled");
            System.out.println("Order " + o.getOrderId() + " has been canceled.");
        } else {
            System.out.println("Delivered orders cannot be canceled.");
        }
    }

    // 3 Update order status
    public void updateStatus(Order o, String newStatus) throws InvalidStatusException {
        switch (newStatus) {
            case "Pending":
            case "Shipped":
            case "Delivered":
            case "Canceled":
                o.setStatus(newStatus);
                System.out.println("Order " + o.getOrderId() + " status updated to: " + newStatus);
                break;
            default:
                System.out.println("Status not valid! ");
                throw new InvalidStatusException(
                        "Invalid status: " + newStatus + ". Must be Pending, Shipped, Delivered, or Canceled.");

        }

    }

    // 4 Search order by ID
    public Order searchOrder(int ID) {
        if (orderList.findkey(ID)) {
            return orderList.retrieve();   // return order
        }
        System.out.println("Order not found.");
        return null;
    }

    // 5 All Orders between two dates
    public String showOrdersBetween(String start, String end) {
        StringBuilder sb = new StringBuilder();
        showBetweenRec(orderList.getRoot(), start, end, sb);
        return sb.toString();
    }

    private void showBetweenRec(AVLNode<Order> node, String start, String end, StringBuilder sb) {
        if (node == null) return;

        showBetweenRec(node.left, start, end, sb);

        Order o = node.data;
        if (o.getOrderDate().compareTo(start) >= 0 &&
            o.getOrderDate().compareTo(end) <= 0) {

            sb.append("Order ID: ").append(o.getOrderId())
              .append(", Date: ").append(o.getOrderDate())
              .append("\n");
        }

        showBetweenRec(node.right, start, end, sb);
    }

    public void displayAllOrders() {
        if (orderList.empty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("=== All Orders ===");
        printOrdersInOrder(orderList.getRoot());
    }

    private void printOrdersInOrder(AVLNode<Order> node) {
        if (node == null) return;

        printOrdersInOrder(node.left);

        Order o = node.data;
        System.out.println("Order ID: " + o.getOrderId() +
                ", Customer: " + o.getOcustomer() +
                ", Date: " + o.getOrderDate() +
                ", Status: " + o.getStatus());

        printOrdersInOrder(node.right);
    }
    public AVLTree<Order> getOrderList() {
        return orderList;
    }

    // 6 Cancel an order by order ID
    public boolean cancelOrder(int orderId) {
        if (orderList.findkey(orderId)) {
       
            Order order = (Order) orderList.current.data; 

            if (order.getStatus().equalsIgnoreCase("Cancelled")) {
                return false; // Already cancelled
            }
            order.setStatus("Cancelled");
    
            return true;
        }
        return false;
    }

    // 7 Update order status by order ID 
    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (orderList.findkey(orderId)) {
            Order o = orderList.retrieve();
            o.setStatus(newStatus);
            return true;
        }
        return false;
    }

}
