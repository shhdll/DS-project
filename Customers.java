public class Customers {

    private AVLTree<Customer> allCustomers;

    public Customers() {
        allCustomers = new AVLTree<>();
    }

    // Operations:
    // 1 Register a new customer
    public void registerCustomer(int customerId, String name, String email) {
        if (findCustomerById(customerId) != null) {
            System.out.println("Customer already exists");
            return;
        }

        Customer newCustomer = new Customer(customerId, name, email);
        allCustomers.insert(customerId, newCustomer);
        //System.out.println("Customer registered successfully!");
    }

    // 2 Place a new order for a specific customer
    public void placeOrder(int customerId, int orderId, AVLTree<Product> productList, String orderDate) {
        Customer customer = findCustomerById(customerId);  // Find customer by ID
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }

        Order newOrder = new Order(orderId, customerId, productList, orderDate);
        customer.getOrders().insert(newOrder.getOrderId(), newOrder);
        System.out.println("Order placed successfully");
    }

    // 3 View order history
    public void viewOrderHistory(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found :(");
            return;
        }

        AVLTree<Order> orders = customer.getOrders();
        if (orders.getRoot() == null) {
            System.out.println("Customer has no orders");
            return;
        }

        System.out.println("Order history for customer ID " + customerId + ":");
        inOrderPrintOrders(orders.getRoot());
    }

    private void inOrderPrintOrders(AVLNode<Order> node) {
        if (node == null) {
            return;
        }
        inOrderPrintOrders(node.left); // Traverse left subtree first
        Order o = node.data;
        System.out.println(
                "  Order ID: " + o.getOrderId() + ", Status: " + o.getStatus() + ", Date: " + o.getOrderDate());
        inOrderPrintOrders(node.right); // Traverse right subtree
    }

    public Customer findCustomerById(int customerId) {
        return findCustomerRec(allCustomers.getRoot(), customerId);  // Recursive search starting from root
    }

     private Customer findCustomerRec(AVLNode<Customer> node, int customerId) {
        if (node == null) return null; // Base case: not found
        if (node.data.getCustomerId() == customerId) return node.data; // Found customer
        else if (customerId < node.data.getCustomerId()) return findCustomerRec(node.left, customerId); // Search left
        else return findCustomerRec(node.right, customerId); // Search right
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        inOrderPrintCustomers(allCustomers.getRoot(), sb);
        return sb.length() == 0 ? "No customers found." : sb.toString();
    }

    private void inOrderPrintCustomers(AVLNode<Customer> node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        inOrderPrintCustomers(node.left, sb);
        sb.append(node.data.toString()).append("\n");
        inOrderPrintCustomers(node.right, sb);
    }

    // 4 Extract customer reviews
    public void extractCustomerReviews(int customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found");
            return;
        }

        System.out.println(" Reviews by Customer " + customerId);
        AVLTree<Review> reviews = customer.getReviews();
        inOrderPrintReviews(reviews.getRoot());
    }

    private void inOrderPrintReviews(AVLNode<Review> node) {
        if (node == null) {
            return;
        }
        inOrderPrintReviews(node.left);
        Review r = node.data;
        System.out.println("- Product " + r.getProductId() + ": " + r.getRating() + " - " + r.getComment());
        inOrderPrintReviews(node.right);
    }

    // 5 List Customers Sorted Alphabetically
    public void listCustomersAlphabetically() {
        if (allCustomers.empty()) {
            System.out.println("No customers registered.");
            return;
        }
        // all customers into an array 
        Customer[] customerArray = new Customer[countNodes(allCustomers.getRoot())];
        inOrderGatherCustomers(allCustomers.getRoot(), customerArray, new int[]{0});

        // Sort the array alphabetically by name 
        bubbleSortByName(customerArray);

        // display the sorted list
        System.out.println("\n=== All Customers Sorted Alphabetically ===");
        for (int i = 0; i < customerArray.length; i++) {
            System.out.println((i + 1) + ". " + customerArray[i].toString());
        }
    }

    private int countNodes(AVLNode<Customer> node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private void inOrderGatherCustomers(AVLNode<Customer> node, Customer[] arr, int[] index) {
        if (node == null) {
            return;
        }

        inOrderGatherCustomers(node.left, arr, index);

        arr[index[0]++] = node.data;

        inOrderGatherCustomers(node.right, arr, index);
    }

    // Bubble sort 
    private void bubbleSortByName(Customer[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare customer names alphabetically
                if (arr[j].getName().compareToIgnoreCase(arr[j + 1].getName()) > 0) {
                    Customer temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

}
