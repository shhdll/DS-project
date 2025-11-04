public class Customers  {  
    
    private LinkedList<CustomerRecord> allCustomers;

    public Customers() {
        allCustomers = new LinkedList<CustomerRecord>();
    }

   // Operations:
   //1 Register new customer
   public void registerCustomer(int customerId, String name, String email) {
        if (findCustomerById(customerId) != null) {
            System.out.println("Customer already exists");
            return;
        }

        CustomerRecord newCustomer = new CustomerRecord(customerId, name, email);
        allCustomers.insert(newCustomer);
        System.out.println("Customer registered successfully");
    }


    //2 Place a new order for a specific customer
    public void placeOrder(int customerId, int orderId, LinkedList<Products> productList, String orderDate) {
        CustomerRecord customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }

        Order newOrder = new Order(orderId, customerId, productList, orderDate);
        customer.getOrders().insert(newOrder);
        System.out.println("Order placed successfully");
    }

    //3 View order history
    public void viewOrderHistory(int customerId) {
        //a: customer is not found
        CustomerRecord customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }
        //b: customer has no orders
        LinkedList<Order> orders = customer.getOrders();
        if (orders.empty()) {
            System.out.println("Customer has no orders");
            return;
        }
        // display history
        System.out.println("Order history for customer ID " + customerId + ":");
        Node<Order> tmp = orders.getHead();
        while (tmp != null) {
            Order o = tmp.data;
            System.out.println("  Order ID: " + o.getOrderId() + ", Status: " + o.getStatus() + ", Date: " + o.getOrderDate());
            tmp = tmp.next;
        }
    }

     //Find customer by ID
    public CustomerRecord findCustomerById(int customerId) {
        if (allCustomers.empty())
            return null;

        Node<CustomerRecord> temp = allCustomers.getHead();
        while (temp != null) {
            if (temp.data.getCustomerId() == customerId)
                return temp.data;
            temp = temp.next;
        }
        return null;
    }
   

}