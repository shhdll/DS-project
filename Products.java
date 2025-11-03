public class Products {
private LinkedList<Product> allProducts = new LinkedList<>(); // list for all products 


//add new product to allProducts list
    public void addProduct(Product p) {
    if(p.findProductById(p.getProductId()) == null){//check if product with same ID already exists
        allProducts.insert(product);
        System.out.println("Product with ID " + p.getProductId() + " has been added.");
    }
        else //product with same ID exists
        System.out.println("Product with ID " + p.getProductId() + " already exists.");
    }

//remove product
    public void removeProduct(int productId) {
        if (allProducts.empty()) {
         System.out.println("No products available to remove.");
        return; }
    
    allProducts.findFirst();
    while (true) {
        Products p = allProducts.retrieve();
        if (p.productId == productId) {
            allProducts.remove();
            System.out.println("Product with ID " + productId + " has been removed.");
            return; // Found and removed 
        }
        
        if (allProducts.last())
        system.out.println("Product with ID " + productId + " not found.");
         break;

        allProducts.findNext();
    }
    }

//search for a product by ID
public Product findProductById(int productId) {
    if (allProducts.empty())
     return null; 
    
    allProducts.findFirst();
    while (true) {
        Product current1 = allProducts.retrieve();
        if (current1.productId == productId) {
            return current1; 
        }
        
        if (allProducts.last()) 
        break;
        allProducts.findNext();
    }
    return null; 
}


//search for a product by Name (i think it's not useful)?
public Product findProductByName(String name) {
    if (allProducts.empty())
     return null; 
    
    allProducts.findFirst();
    while (true) {
        Product current2 = allProducts.retrieve();
        if (current2.name.equals(name)) {
            return current2; 
        }
        
        if (allProducts.last()) 
        break;
        allProducts.findNext();
    }
    return null; 
}


//update product details
public void updateProducts(Product p , int id){
    if(p.findProductById(id) != null){//check if product with same ID exists
       p.updateProduct(p.getName(), p.getPrice(), p.getStock());
       System.out.println("Product with ID " + id + " has been updated.");
    }

    else //product with same ID does not exist
        System.out.println("Product with ID " + id + " does not exist.");
}

//to Track out-of-stock products 
public void getOutOfStockProducts() {
    LinkedList<Product> outOfStockProducts = new LinkedList<>();
    if (allProducts.empty()) {
        return;
    }
    
    allProducts.findFirst();
    while (true) {
        Product aProduct = allProducts.retrieve();
        if (aProduct.getStock() == 0) {
            outOfStockProducts.insert(aProduct);
        }
        if (allProducts.last()) {
            break;
        }
        allProducts.findNext();
    }
    if (outOfStockProducts.empty()) {
        System.out.println("No out-of-stock products.");
    } else {
        System.out.println("Out-of-stock products:");
        outOfStockProducts.findFirst();
        while (true) {
            Product outProduct = outOfStockProducts.retrieve();
            outProduct.displayallProducts();
            if (outOfStockProducts.last()) {
                break;
            }
            outOfStockProducts.findNext();
        }
    }
}

//add review to a product
public void addReview(Reviews r) {
    Product p = findProductById(r.getProductId()); //find the product to which the review belongs
    if (p != null) { 
        p.addReview(r);
        System.out.println("Review added for Product ID " + r.getProductId());
    } else {
        System.out.println("Product with ID " + r.getProductId() + " not found. Review not added.");
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
        
    public void displayallProducts() {
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
                Reviews aReview = reviews.retrieve();
                aReview.displayReviews();
                reviews.findNext();
            }
           
            Reviews lastReview = reviews.retrieve(); // to display the last review
            lastReview.displayReviews();
        }
    }
}