public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;

    private LinkedList<Reviews> reviews; //Each product has its own review
    private static LinkedList<Products> allProducts = new LinkedList<>(); //static list for all products 

    public Product(int productId, String name, double price, int stock) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.reviews = new LinkedList<Reviews>(); }
            
            
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

}
