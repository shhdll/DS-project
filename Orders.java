public class Orders extends LinkedList<Order> {
   private LinkedList<Order> orderList;
    public Orders() {
        orderList = new LinkedList<Order>();
        
    }
     //Operations
    // 1 create order 
    public void createOrder(int orderId, int Ocustomer, LinkedList<Products> productList, String orderDate){
        Order o = new Order( orderId, Ocustomer, productList, orderDate);
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
    public void updateStatus(Order o , String newStatus) throws InvalidStatusException{
        switch(newStatus){
            case "Pending":
            case "Shipped":
            case "Delivered":
            case "Canceled":
                o.setStatus(newStatus);
                 System.out.println("Order " + o.getOrderId() + " status updated to: " + newStatus);
                break;
            default:
                System.out.println("Status not valid! ");
                 throw new InvalidStatusException("Invalid status: " + newStatus + ". Must be Pending, Shipped, Delivered, or Canceled.");

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


    

}