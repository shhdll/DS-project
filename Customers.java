public class Customers {

    private LinkedList<Customer> allCustomers;

    public Customers() {
        allCustomers = new LinkedList<Customer>();
    }

    // Operations:
    // 1 Register new customer
    public void registerCustomer(int customerId, String name, String email) {
        if (findCustomerById(customerId) != null) {
            System.out.println("Customer already exists");
            return;
        }

        Customer newCustomer = new Customer(customerId, name, email);
        allCustomers.insert(newCustomer);
        // System.out.println("Customer registered successfully");
    }

    // 2 Place a new order for a specific customer
    public void placeOrder(int customerId, int orderId, AVLTree<Product> productList, String orderDate) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }

        Order newOrder = new Order(orderId, customerId, productList, orderDate);
        customer.getOrders().insert(newOrder);
        System.out.println("Order placed successfully");
    }

    // 3 View order history
    public void viewOrderHistory(int customerId) {
        // a: customer is not found
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }
        // b: customer has no orders
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
            System.out.println(
                    "  Order ID: " + o.getOrderId() + ", Status: " + o.getStatus() + ", Date: " + o.getOrderDate());
            tmp = tmp.next;
        }
    }

    // Find customer by ID
    public Customer findCustomerById(int customerId) {
        if (allCustomers.empty())
            return null;

        Node<Customer> temp = allCustomers.getHead();
        while (temp != null) {
            if (temp.data.getCustomerId() == customerId)
                return temp.data;
            temp = temp.next;
        }
        return null;
    }

    public String toString() {
        String result = "";

        if (allCustomers.empty()) {
            return "No customers found.";
        }

        allCustomers.findFirst();
        while (true) {
            result += allCustomers.retrieve().toString() + "\n";
            if (allCustomers.last())
                break;
            allCustomers.findNext();
        }

        return result;
    }

    // 4 Extract customer reviews
    public void extractCustomerReviews(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }

        System.out.println(" Reviews by Customer " + customerId);
        Node<Review> tmp = customer.getReviews().getHead();
        while (tmp != null) {  //most efficient linear data structure possible.
            Review r = tmp.data;
            System.out.println("- Product " + r.getProductId() + ": " +
                    r.getRating() + " - " + r.getComment());
            tmp = tmp.next;
        }
    }

}