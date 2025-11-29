
public class Products {

    private AVLTree<Product> allProducts = new AVLTree<>();

    // add new product to allProducts list
    public boolean addProduct(Product p) {
        return allProducts.insert(p.getProductId(), p);
    }

    // remove product
    public boolean removeProduct(int productId) {
        return allProducts.removeKey(productId);
    }

    // search for a product by ID
    public Product findProductById(int productId) {
        if (allProducts.findkey(productId)) {
            return allProducts.retrieve();
        }
        return null;
    }

    // new for phase 2
    public void getProductsInPriceRange(double minPrice, double maxPrice) {
        System.out.println("=== Products in Price Range [" + minPrice + " - " + maxPrice + "] ===");

        if (allProducts.empty()) {
            System.out.println("No products found in this price range.");
            return;
        }

        boolean found = getProductsInPriceRangeRec(allProducts.root, minPrice, maxPrice);

        if (!found) {
            System.out.println("No products found in this price range.");
        }
    }

    private boolean getProductsInPriceRangeRec(AVLNode<Product> node, double minPrice, double maxPrice) {
        if (node == null) {
            return false;
        }

        boolean found = false;

        if (getProductsInPriceRangeRec(node.left, minPrice, maxPrice)) {
            found = true;
        }

        Product product = node.data;
        double price = product.getPrice();
        if (price >= minPrice && price <= maxPrice) {
            System.out.println("ID: " + product.getProductId()
                    + ", Name: " + product.getName()
                    + ", Price: " + product.getPrice()
                    + ", Stock: " + product.getStock());
            found = true;
        }

        if (getProductsInPriceRangeRec(node.right, minPrice, maxPrice)) {
            found = true;
        }

        return found;
    }

    // search for a product by Name
    public Product findProductByName(String name) {
        if (allProducts.empty()) {
            return null;
        }

        // Use AVL tree traversal to search by name
        return findProductByNameRec(allProducts.getRoot(), name);
    }

    // Recursive helper method for name search using AVL tree traversal
    private Product findProductByNameRec(AVLNode<Product> node, String name) {
        if (node == null) {
            return null;
        }

        // Check left subtree
        Product leftResult = findProductByNameRec(node.left, name);
        if (leftResult != null) {
            return leftResult;
        }

        // Check current node
        Product currentProduct = node.data;
        if (currentProduct.getName().equalsIgnoreCase(name)) {
            return currentProduct;
        }

        // Check right subtree
        Product rightResult = findProductByNameRec(node.right, name);
        if (rightResult != null) {
            return rightResult;
        }

        return null;
    }

    // update product details
    public boolean updateProductDetails(int productId, String name, double price, int stock) {
        Product product = findProductById(productId);
        if (product != null) {
            product.updateProduct(name, price, stock);
            return true;
        }
        return false;
    }

    // to Track out-of-stock products
    public void getOutOfStockProducts() {
        System.out.println("=== Out-of-Stock Products ===");

        if (allProducts.empty()) {
            System.out.println("No out-of-stock products.");
            return;
        }

        boolean found = getOutOfStockProductsRec(allProducts.root);

        if (!found) {
            System.out.println("No out-of-stock products.");
        }
    }

    private boolean getOutOfStockProductsRec(AVLNode<Product> node) {
        if (node == null) {
            return false;
        }

        boolean found = false;

        if (getOutOfStockProductsRec(node.left)) {
            found = true;
        }

        Product p = node.data;
        if (p.getStock() == 0) {
            p.displayProductDetails();
            found = true;
        }

        if (getOutOfStockProductsRec(node.right)) {
            found = true;
        }

        return found;
    }

    // add review to a product
    public void addReview(int productId, Review r) {
        Product p = findProductById(productId);
        if (p != null) {
            p.addReview(r);
            System.out.println("Review added.");
        } else {
            System.out.println("Review not added.");
        }
    }

    // top 3 Highest Rated Products
    public void Top3Products() {
        System.out.println("=== Top 3 Highest Rated Products ===");

        if (allProducts.empty()) {
            System.out.println("No products available.");
            return;
        }

        Product[] topProducts = new Product[3];
        double[] topRatings = new double[3];

        findTopRatedProductsRec(allProducts.root, topProducts, topRatings);

        boolean found = false;
        for (int i = 0; i < 3; i++) {
            if (topProducts[i] != null) {
                System.out.println((i + 1) + ". " + topProducts[i].getName()
                        + " - Rating: " + String.format("%.1f", topRatings[i]) + "/5"
                        + " - Price: " + topProducts[i].getPrice() + " SAR"
                        + " - Reviews: " + getReviewCount(topProducts[i]));
                found = true;
            }
        }

        if (!found) {
            System.out.println("No products with reviews available.");
        }
    }

    private void findTopRatedProductsRec(AVLNode<Product> node, Product[] topProducts, double[] topRatings) {
        if (node == null) {
            return;
        }

        // In-order traversal
        findTopRatedProductsRec(node.left, topProducts, topRatings);

        Product product = node.data;
        double rating = calculateAverageRating(product);

        if (rating > 0) {
            for (int i = 0; i < 3; i++) {
                if (topProducts[i] == null || rating > topRatings[i]) {
                    for (int j = 2; j > i; j--) {
                        topProducts[j] = topProducts[j - 1];
                        topRatings[j] = topRatings[j - 1];
                    }
                    topProducts[i] = product;
                    topRatings[i] = rating;
                    break;
                }
            }
        }

        findTopRatedProductsRec(node.right, topProducts, topRatings);
    }

    private int getReviewCount(Product product) {
        LinkedList<Review> reviews = product.getReviews();
        if (reviews.empty()) {
            return 0;
        }

        int count = 0;
        reviews.findFirst();
        while (!reviews.last()) {
            count++;
            reviews.findNext();
        }
        count++;
        return count;
    }

    private double calculateAverageRating(Product product) {
        LinkedList<Review> reviews = product.getReviews();
        if (reviews.empty()) {
            return 0.0;
        }

        double sum = 0;
        int count = 0;

        reviews.findFirst();
        while (!reviews.last()) {
            Review review = reviews.retrieve();
            sum += review.getRating();
            count++;
            reviews.findNext();
        }

        // Process last review
        Review lastReview = reviews.retrieve();
        sum += lastReview.getRating();
        count++;

        return sum / count;
    }

  // to display all customers who reviewed a specific product (sorted by customer ID)
public void commonProducts(int productId, Customers customers) {
    System.out.println("=== Customers Who Reviewed Product ID: " + productId + " ===");

    Product product = findProductById(productId);
    if (product == null) {
        System.out.println("Product not found.");
        return;
    }

    LinkedList<Review> reviews = product.getReviews();
    if (reviews.empty()) {
        System.out.println("No reviews for this product.");
        return;
    }

    int size = countReviews(reviews);
    Review[] reviewArray = new Review[size];

    // copy reviews from LinkedList to array
    fillReviewsArray(reviews, reviewArray);

    // sort array by customer ID (ascending)
    bubbleSortReviewsByCustomerId(reviewArray);

    System.out.println("Product: " + product.getName() +
            " (Avg Rating: " + String.format("%.1f", calculateAverageRating(product)) + "/5)");
    System.out.println("Reviewers (sorted by customer ID):");

    for (int i = 0; i < reviewArray.length; i++) {
        Review review = reviewArray[i];

        // lookup for customer details per review
        Customer reviewer = customers.findCustomerById(review.getCustomerId());
        String reviewerName = (reviewer != null) ? reviewer.getName() : "Unknown Customer";

        System.out.println((i + 1) + ". Customer: " + reviewerName
                + " (ID: " + review.getCustomerId()
                + "), Rating: " + review.getRating() + "/5"
                + ", Comment: " + review.getComment());
    }

    System.out.println("=== Total: " + reviewArray.length + " reviews ===");
}

// count how many reviews in the LinkedList
private int countReviews(LinkedList<Review> reviews) {
    if (reviews.empty()) {
        return 0;
    }

    int count = 0;
    reviews.findFirst();
    while (!reviews.last()) {
        count++;
        reviews.findNext();
    }
    count++; // last review
    return count;
}

// copy all reviews from LinkedList to array
private void fillReviewsArray(LinkedList<Review> reviews, Review[] arr) {
    if (reviews.empty()) {
        return;
    }

    int index = 0;
    reviews.findFirst();
    while (true) {
        arr[index++] = reviews.retrieve();
        if (reviews.last()) {
            break;
        }
        reviews.findNext();
    }
}

// bubble sort reviews array by customer ID (ascending)
private void bubbleSortReviewsByCustomerId(Review[] arr) {
    int n = arr.length;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j].getCustomerId() > arr[j + 1].getCustomerId()) {
                Review temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}
    public void commonProducts(int cust1, int cust2) {
        System.out.println("=== Common products for customers " + cust1 + " & " + cust2 + " (Rated > 4.0) ===");

        int count = 0;
        // recursive traversal to check every product 
        count = commonProductsRec(allProducts.getRoot(), cust1, cust2, count);

        if (count == 0) {
            System.out.println("No common products with rating above 4.0 found between these customers.");
        }
    }

    private int commonProductsRec(AVLNode<Product> node, int cust1, int cust2, int count) {
        if (node == null) {
            return count;
        }

        // Traverse left
        count = commonProductsRec(node.left, cust1, cust2, count);

        Product p = node.data;
        double rating = calculateAverageRating(p);

        if (rating > 4.0 && hasCustomerReviewed(p, cust1) && hasCustomerReviewed(p, cust2)) {
            count++;
            System.out.println(count + ". " + p.getName()
                    + " (ID: " + p.getProductId()
                    + ") - Rating: " + String.format("%.1f", rating) + " out of 5");
        }
        // Traverse right
        count = commonProductsRec(node.right, cust1, cust2, count);

        return count;
    }

    private boolean hasCustomerReviewed(Product p, int customerId) {
        LinkedList<Review> reviews = p.getReviews();
        if (reviews.empty()) {
            return false;
        }

        reviews.findFirst();
        while (true) {
            if (reviews.retrieve().getCustomerId() == customerId) {
                return true;
            }
            if (reviews.last()) {
                break;
            }
            reviews.findNext();
        }
        return false;
    }

    // to display all products
    public void displayAllProducts() {
        System.out.println("=== All Products (Sorted by ID) ===");

        if (allProducts.empty()) {
            System.out.println("No products available.");
            return;
        }

        displayAllProductsRec(allProducts.getRoot());
    }

    private void displayAllProductsRec(AVLNode<Product> node) {
        if (node == null) {
            return;
        }

        displayAllProductsRec(node.left);

        Product product = node.data;
        System.out.println("ID: " + product.getProductId()
                + ", Name: " + product.getName()
                + ", Price: " + product.getPrice() + " SAR"
                + ", Stock: " + product.getStock());

        displayAllProductsRec(node.right);
    }

}
