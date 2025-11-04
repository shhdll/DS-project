public class Products {
    private LinkedList<Product> allProducts = new LinkedList<>();

    // Add new product
    public void addProduct(Product p) {
        if (findProductById(p.getProductId()) == null) {
            allProducts.insert(p);
            System.out.println("Product with ID " + p.getProductId() + " has been added.");
        } else {
            System.out.println("Product with ID " + p.getProductId() + " already exists.");
        }
    }

    // Remove product
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
                return;
            }
            if (allProducts.last()) {
                System.out.println("Product with ID " + productId + " not found.");
                break;
            }
            allProducts.findNext();
        }
    }

    // Search product by ID
    public Product findProductById(int productId) {
        if (allProducts.empty()) return null;

        allProducts.findFirst();
        while (true) {
            Product p = allProducts.retrieve();
            if (p.getProductId() == productId) return p;
            if (allProducts.last()) break;
            allProducts.findNext();
        }
        return null;
    }

    // Search product by Name
    public Product findProductByName(String name) {
        if (allProducts.empty()) return null;

        allProducts.findFirst();
        while (true) {
            Product p = allProducts.retrieve();
            if (p.getName().equals(name)) return p;
            if (allProducts.last()) break;
            allProducts.findNext();
        }
        return null;
    }

    // Update product
    public void updateProduct(int id, String name, double price, int stock) {
        Product p = findProductById(id);
        if (p != null) {
            p.updateProduct(name, price, stock);
            System.out.println("Product with ID " + id + " has been updated.");
        } else {
            System.out.println("Product with ID " + id + " does not exist.");
        }
    }

    // Get out-of-stock products
    public void getOutOfStockProducts() {
        if (allProducts.empty()) {
            System.out.println("No products available.");
            return;
        }

        allProducts.findFirst();
        boolean found = false;
        while (true) {
            Product p = allProducts.retrieve();
            if (p.getStock() == 0) {
                p.displayProductDetails();
                found = true;
            }
            if (allProducts.last()) break;
            allProducts.findNext();
        }
        if (!found) System.out.println("No out-of-stock products.");
    }

    // Add review to a product
    public void addReview(int productId, Review r) {
        Product p = findProductById(productId);
        if (p != null) {
            p.addReview(r);
            System.out.println("Review added for Product ID " + productId);
        } else {
            System.out.println("Product with ID " + productId + " not found. Review not added.");
        }
    }
}
