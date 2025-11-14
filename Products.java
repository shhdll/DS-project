public class Products {

    private LinkedList<Product> allProducts = new LinkedList<>(); // list for all products

    // add new product to allProducts list
    public boolean addProduct(Product p) {
        if (findProductById(p.getProductId()) == null) {
            allProducts.insert(p);
            return true;
        }
        return false;
    }

    // remove product
    public boolean removeProduct(int productId) {
        if (allProducts.empty()) {
            return false; // nothing to remove
        }

        allProducts.findFirst();
        while (true) {
            Product p = allProducts.retrieve();
            if (p.getProductId() == productId) {
                allProducts.remove();
                return true; // successfully removed
            }

            if (allProducts.last()) {
                break; // reached end
            }

            allProducts.findNext();
        }

        return false; // not found
    }

    // search for a product by ID
    public Product findProductById(int productId) {
        if (allProducts.empty()) {
            return null;
        }

        allProducts.findFirst();
        while (true) {
            Product current1 = allProducts.retrieve();
            if (current1.getProductId() == productId) {
                return current1;
            }

            if (allProducts.last()) {
                break;
            }
            allProducts.findNext();
        }
        return null;
    }

    // search for a product by Name 
    public Product findProductByName(String name) {
        if (allProducts.empty()) {
            return null;
        }

        allProducts.findFirst();
        while (true) {
            Product current2 = allProducts.retrieve();
            if (current2.getName().equals(name)) {
                return current2;
            }

            if (allProducts.last()) {
                break;
            }
            allProducts.findNext();
        }
        return null;
    }

    // update product details
    public void updateProducts(Product p, int id) {
        Product target = findProductById(id); // check if product with same ID exists
        if (target != null) {
            target.updateProduct(p.getName(), p.getPrice(), p.getStock());
            System.out.println("Product with ID " + id + " has been updated.");
        } else // product with same ID does not exist
        {
            System.out.println("Product with ID " + id + " does not exist.");
        }
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

        
        Product first = null, second = null, third = null;
        double firstRate = 0, secondRate = 0, thirdRate = 0;

        allProducts.findFirst();
        while (true) {
            Product current = allProducts.retrieve();
            double rate = calculateAverageRating(current);

            if (rate > 0) {
              
                if (rate > firstRate) {
                    third = second;
                    thirdRate = secondRate;
                    second = first;
                    secondRate = firstRate;
                    first = current;
                    firstRate = rate;
                } else if (rate > secondRate) {
                    third = second;
                    thirdRate = secondRate;
                    second = current;
                    secondRate = rate;
                } else if (rate > thirdRate) {
                    third = current;
                    thirdRate = rate;
                }
            }

            if (allProducts.last())
                break;
            allProducts.findNext();
        }

       
        System.out.println(" Top 3 Products by Rating ");

        if (first != null) {
            System.out.println("1- " + first.getName());
            System.out.println("   Rating: " + String.format("%.1f", firstRate) + " out of 5");
            System.out.println("   Price: " + first.getPrice() + " SAR");
            System.out.println();
        }
        if (second != null) {
            System.out.println("2- " + second.getName());
            System.out.println("   Rating: " + String.format("%.1f", secondRate) + " out of 5");
            System.out.println("   Price: " + second.getPrice() + " SAR");
            System.out.println();
        }
        if (third != null) {
            System.out.println("3- " + third.getName());
            System.out.println("   Rating: " + String.format("%.1f", thirdRate) + " out of 5");
            System.out.println("   Price: " + third.getPrice() + " SAR");
            System.out.println();
        }

        if (first == null) {
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

    // to display a list of common that have been reviewed by two customers
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

            if (allProducts.last()) {
                break;
            }
            allProducts.findNext();
        }

        if (count == 0) {
            System.out.println("No common products with rating above 4.0 found between these customers.");
        }
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

    public void displayAllProducts() {
        if (allProducts.empty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("=== All Products with Details ===");
        allProducts.findFirst();
        int count = 0;

        while (true) {
            Product p = allProducts.retrieve();
            count++;
            System.out.println(count + ". " + p.getName());
            System.out.println("   ID: " + p.getProductId() + ", Price: " + p.getPrice() + " SAR"
                    + ", Stock: " + p.getStock());

            // Show average rating if available
            double avgRating = calculateAverageRating(p);
            if (avgRating > 0) {
                System.out.println("   Average Rating: " + String.format("%.1f", avgRating));
            }
            System.out.println();

            if (allProducts.last()) {
                break;
            }
            allProducts.findNext();
        }

        System.out.println("Total: " + count + " Products");
    }
}
