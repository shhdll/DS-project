public class Products {
 private int productId;
 private String name;
 private double price;
 private int stock;

 private LinkedList<Reviews> reviews; //Each product has its own review
 private static LinkedList<Products> allProducts = new LinkedList<>(); //static list for all products 

    public Products(int productId, String name, double price, int stock) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.reviews = new LinkedList<Reviews>(); }

//add new product to static list
    public static void addProduct(int productId, String name, double price, int stock) {
        Products newProduct = new Products(productId, name, price, stock);
        allProducts.insert(newProduct); 
    }


//update product 
    public void updateProduct(String name, double price, int stock) {
        if(name != null && name.length()>0) //Prevents empty and whitespace "" only  strings 
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

//remove product
    public static void removeProduct(int productId) {
        if (allProducts.empty()) return;
    
    allProducts.findFirst();
    while (true) {
        Products current = allProducts.retrieve();
        if (current.productId == productId) {
            allProducts.remove();
            return; // Found and removed > exit
        }
        
        if (allProducts.last())
         break;

        allProducts.findNext();
    }
    }

//search by id , "should be by name also?"
public static Products findProductById(int productId) {
    if (allProducts.empty())
     return null; //null?
    
    allProducts.findFirst();
    while (true) {
        Products current = allProducts.retrieve();
        if (current.productId == productId) {
            return current; 
        }
        
        if (allProducts.last()) 
        break;
        allProducts.findNext();
    }
    return null; //null?
}

//Track out-of-stock products ......... 





 
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