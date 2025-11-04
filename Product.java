public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;
    private LinkedList<Reviews> reviews; //Each product has its own review

    public Product(int productId, String name, double price, int stock) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.reviews = new LinkedList<Reviews(); }

    public void addReview(Reviews review) { //add review to a specific product
        reviews.insert(review);
    }
            
     //update product 
    public void updateProduct(String name, double price, int stock) { //should we allow updating productId?
        if(name != null && name.length()>0) //Prevents empty and whitespace "" only strings 
        {
            this.name = name;
        }
        if(price > 0) {
            this.price = price;
        }   
        if(stock >= 0) {
            this.stock = stock;
        }   
    }
       
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
    public LinkedList<Reviews> getReviews() {
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

    public void display ProductDetails() {
        System.out.println("Product ID: " + productId);
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
        System.out.println("Stock: " + stock);
        System.out.println("Reviews:");
       if (reviews.empty()) {
            System.out.println("there is no reviews available.");
        } else {
            reviews.findFirst();
         while (!reviews.last()) {
                Reviews currentReview = reviews.retrieve();
                currentReview.displayReviews();
                reviews.findNext();
            }
           
            Reviews lastReview = reviews.retrieve(); // to display the last review
            lastReview.displayReviews();
        }
    }

}
