public class Products {
    private LinkedList<Product> allProducts = new LinkedList<>(); // list for all products 

    // add new product to allProducts list
    public void addProduct(Product p) {
        if (findProductById(p.getProductId()) == null) { // check if product with same ID already exists
            allProducts.insert(p);
            System.out.println("Product with ID " + p.getProductId() + " has been added.");
        } else // product with same ID exists
            System.out.println("Product with ID " + p.getProductId() + " already exists.");
    }

    // remove product
    public void removeProduct(int productId) {
    if (allProducts.empty()) {
        System.out.println("No products available to remove.");
        return;
        }

    allProducts.findFirst();
     while (true) {
        Product p = allProducts.retrieve();
        if (p.getProductId() == productId) {
            allProducts.remove();
            System.out.println("Product with ID " + productId + " has been removed.");
            return; // Found and removed 
        }

        if (allProducts.last()) {
            System.out.println("Product with ID " + productId + " not found.");
            break; // Exit loop if last element reached
        }

        allProducts.findNext(); 
     }
    }

    // search for a product by ID
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

    // search for a product by Name (i think it's not useful)?
    public Product findProductByName(String name) {
        if (allProducts.empty())
            return null; 

        allProducts.findFirst();
        while (true) {
            Product current2 = allProducts.retrieve();
            if (current2.getName().equals(name)) {
                return current2; 
            }

            if (allProducts.last()) 
                break;
            allProducts.findNext();
        }
        return null; 
    }

    // update product details
    public void updateProducts(Product p , int id){
        Product target = findProductById(id); // check if product with same ID exists
        if(target != null){
            target.updateProduct(p.getName(), p.getPrice(), p.getStock());
            System.out.println("Product with ID " + id + " has been updated.");
        } else // product with same ID does not exist
            System.out.println("Product with ID " + id + " does not exist.");
    }

    // to Track out-of-stock products 
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
                outProduct.displayProductDetails();
                if (outOfStockProducts.last()) {
                    break;
                }
                outOfStockProducts.findNext();
            }
        }
    }

    // add review to a product
    public void addReview(int productId, Review r) {
        Product p = findProductById(productId); // find the product to which the review belongs
        if (p != null) { 
            p.addReview(r);
            System.out.println("Review added for Product ID " + productId);
        } else {
            System.out.println("Product with ID " + productId + " not found. Review not added.");
        }
    }

    // top 3 products by average rating
    public void Top3Products() {
    if (allProducts.empty()) {
        System.out.println("No products available.");
        return;
    }

    System.out.println(" ★Top 3 Products by Rating★ ");
    
    allProducts.findFirst();
    int count = 0;
    
    while (count < 3) {
        Product currentProduct = allProducts.retrieve();
        double avrRating = calculateAverageRating(currentProduct);
        
        if (avrRating > 0) {
          System.out.println((count + 1) + "- " + currentProduct.getName());
            System.out.println("   Rating: " + "★ " + avrRating + " out of 5");
            System.out.println("   Price: " + currentProduct.getPrice() + " SAR");
            System.out.println();
            count++;
        }
        if (allProducts.last()) 
        break;
        allProducts.findNext();
        }
    
        if (count == 0) {
        System.out.println("No products with ratings available.");
        }
    }

    private double calculateAverageRating(Product p) {
        LinkedList<Review> allreviews = p.getReviews();
        if (allreviews.empty()) {
            return 0;
        }

        double sum = 0;
        int count = 0;
        allreviews.findFirst();
        while (true) {
            Review currentReview = allreviews.retrieve();
            sum += currentReview.getRating();
            count++;
            if (allreviews.last()) {
                break;
            }
            allreviews.findNext();
        }
        return sum / count;
    }
    
    //to display a list of common that have been reviewed by two customers
    public void commonProducts(int cust1, int cust2) {
    System.out.println("Common products for customers " + cust1 + " & " + cust2 + ":");
    
    int count = 0;
    
    allProducts.findFirst();
    while (true) {
        Product p = allProducts.retrieve();
        double rating = calculateAverageRating(p);
        
        if (rating > 4.0 && hasCustomerReviewed(p, cust1) && hasCustomerReviewed(p, cust2)) {
            count++;
            System.out.println(count + ". " + p.getName() + " - " + rating + " out of 5 ");
        }
        
        if (allProducts.last()) 
        break;
        allProducts.findNext();
    }
    
    if (count == 0) {
        System.out.println("No common products with rating above 4.0 found between these customers.");
    }
    }

private boolean hasCustomerReviewed(Product p, int customerId) {
    LinkedList<Review> reviews = p.getReviews();
    if (reviews.empty()) return false;
    
    reviews.findFirst();
    while (true) {
        if (reviews.retrieve().getCustomerId() == customerId) {
            return true;
        }
        if (reviews.last()) break;
        reviews.findNext();
    }
    return false;
    }  

public void displayAllProducts() {
    if (allProducts.empty()) {
        System.out.println("No products available.");
        return;
    }

System.out.println(" ★ All Products ★ ");    
    allProducts.findFirst();
    int count = 0;
    
    while (true) {
        Product p = allProducts.retrieve();
        count++;
        System.out.println(count + ". " + p.getName() + " - " + p.getPrice() + " SAR - Stock: " + p.getStock());
        
        if (allProducts.last()) 
        break;
        allProducts.findNext();
    }
    
    System.out.println("Total: " + count + " Products");
    }
}
