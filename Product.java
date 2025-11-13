public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;
    private LinkedList<Review> reviews; // Each product has its own review

    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new LinkedList<Review>();
    }

    public void addReview(Review r) { // add review to a specific product
        reviews.insert(r);
    }

    // update product
    public void updateProduct(String name, double price, int stock) { 
        if (name != null && name.length() > 0) // Prevents empty and whitespace "" only strings
        {
            this.name = name;
        }
        if (price > 0) {
            this.price = price;
        }
        if (stock >= 0) {
            this.stock = stock;
        }
    }

    // getters and setters
    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public LinkedList<Review> getReviews() {
        return reviews;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void displayProductDetails() {
        System.out.println("Product ID: " + productId);
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
        System.out.println("Stock: " + stock);

        System.out.println("Reviews:");
        if (reviews.empty()) {
            System.out.println("There are no reviews available.");
        } else {
            reviews.findFirst();
            while (!reviews.last()) {
                Review r = reviews.retrieve();
                r.displayReviews();
                reviews.findNext();
            }
            Review lastReview = reviews.retrieve();
            lastReview.displayReviews();
        }
    }
}
