public class Products {
private LinkedList<Product> allProducts = new LinkedList<>(); // list for all products 


//add new product to allProducts list
    public void addProduct(Product p) {
    if(findProductById(p.getProductId()) == null){//check if product with same ID already exists
        allProducts.insert(p);
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
        Product p = allProducts.retrieve();
        if (p.getProductId() == productId) {
            allProducts.remove();
            System.out.println("Product with ID " + productId + " has been removed.");
            return; // Found and removed 
        }
        
        if (allProducts.last())
        System.out.println("Product with ID " + productId + " not found.");
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
        if (current1.getProductId() == productId) {
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

   
}