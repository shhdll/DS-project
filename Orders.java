
public class Orders extends LinkedList<Order> {

    private LinkedList<Order> orderList;

    public Orders() {
        orderList = new LinkedList<Order>();

    }
    // Operations
    // 1 create order

    public void createOrder(int orderId, int customerID, LinkedList<Product> productList, String orderDate) {
        Order o = new Order(orderId, customerID, productList, orderDate);
        orderList.insert(o);
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

    // 3 Search order by ID
    public Order searchOrder(int ID) {
        Node<Order> tmp = orderList.getHead();
        while (tmp != null) {
            Order order = tmp.data;
            if (order.getOrderId() == ID) {
                return order;
            }
            tmp = tmp.next;
        }
        System.out.println("Order Not found");
        return null;
    }

    // All Orders between two dates
    public String showOrdersBetween(String start, String end) {
        Node<Order> current = orderList.getHead();
        StringBuilder sb = new StringBuilder();

        while (current != null) {
            Order o = current.data;
            String date = o.getOrderDate();

            if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                sb.append("Order ID: ").append(o.getOrderId())
                        .append(", Date: ").append(o.getOrderDate())
                        .append("\n");
            }

            current = current.next;
        }

        return sb.toString();
    }

    public void displayAllOrders() {
        System.out.println("=== All Orders ===");
        if (orderList.empty()) {
            System.out.println("No orders found.");
            return;
        }

        Node<Order> tmp = orderList.getHead();
        int count = 1;
        while (tmp != null) {
            Order order = tmp.data;
            System.out.println(count + ". Order ID: " + order.getOrderId() + ", Customer: " + order.getOcustomer() +
                    ", Date: " + order.getOrderDate() + ", Status: " + order.getStatus());
            tmp = tmp.next;
            count++;
        }
        System.out.println("Total: " + (count - 1) + " orders");
    }
}
