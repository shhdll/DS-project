import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ManagementSystemGUI {

    private static final Color DARK_BLUE = new Color(0x00, 0x00, 0x4d);

    // --- Helper Data Structures (now static for easy access) ---
    private record Product(int id, String name, double price, int stock) {
        public String displayProductDetails() {
            return "--- Product Details ---\n" +
                   "ID: " + id + "\n" +
                   "Name: " + name + "\n" +
                   "Price: $" + String.format("%.2f", price) + "\n" +
                   "Stock: " + stock;
        }
    }

    private record Review(int reviewId, int productId, int customerId, int rating, String comment) {}

    // --- Placeholder Classes updated for new functionality ---
    private class Customers {
        public void registerCustomer(int id, String name, String email) {
            System.out.println("Customer registered: " + name);
        }
        public String viewOrderHistory(int customerId) {
            // NOTE: This now calls a file reading utility to be more realistic, 
            // but the file data in orders.csv is complex (productIds are a list), 
            // so this remains a placeholder for simplicity in this step.
            return "--- Viewing Order History for Customer ID: " + customerId + " ---\n"
                 + "Order #101: 2024-05-15, Total: $50.00 (Placeholder)\n"
                 + "Order #105: 2024-06-01, Total: $125.50 (Placeholder)\n"
                 + "Order #112: 2024-07-20, Total: $15.99 (Placeholder)";
        }
        public String getCustomerReviews(int customerId) {
            // Implementation for Option 6 (extract reviews) should be similar to getTop3Products
            // but for now, we leave the original placeholder to focus on Option 7
            return "--- Reviews by Customer " + customerId + " ---\n"
                 + "Product A: Rating 5/5, Comment: Great service! (Placeholder)\n"
                 + "Product B: Rating 4/5, Comment: Could be better. (Placeholder)\n"
                 + "Product C: Rating 5/5, Comment: Best buy this year. (Placeholder)";
        }
        public String getCommonReviewedProducts(int cId1, int cId2) {
            return "--- Common Highly-Rated Products (RATING > 4) ---\n"
                 + "Customers " + cId1 + " and " + cId2 + " both loved:\n"
                 + "ID 10: Premium Coffee Maker (Placeholder)\n"
                 + "ID 45: Wireless Headset Pro (Placeholder)";
        }
    }

    private class Products {
        private final Map<Integer, Product> productMap;

        public Products() {
            // Load products upon initialization
            this.productMap = loadProductsFromFile("prodcuts.csv");
        }
        
        // UTILITY: Loads products from the file
        private Map<Integer, Product> loadProductsFromFile(String filename) {
            Map<Integer, Product> map = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                br.readLine(); // Skip header
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 4) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            double price = Double.parseDouble(parts[2].trim());
                            int stock = Integer.parseInt(parts[3].trim());
                            map.put(id, new Product(id, name, price, stock));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed product line: " + line);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not load products file: " + e.getMessage());
            }
            return map;
        }
        
        // UTILITY: Loads reviews from the file
        private List<Review> loadReviewsFromFile(String filename) {
            List<Review> reviews = new java.util.ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                br.readLine(); // Skip header
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 5) {
                        try {
                            int reviewId = Integer.parseInt(parts[0].trim());
                            int productId = Integer.parseInt(parts[1].trim());
                            int customerId = Integer.parseInt(parts[2].trim());
                            int rating = Integer.parseInt(parts[3].trim());
                            String comment = parts[4].replaceAll("^\"|\"$", ""); // Remove quotes from comment
                            reviews.add(new Review(reviewId, productId, customerId, rating, comment));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping malformed review line: " + line);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not load reviews file: " + e.getMessage());
            }
            return reviews;
        }

        public void addProduct(Product p) {
            productMap.put(p.id(), p);
            System.out.println("Product added (in-memory): " + p.name());
        }
        
        public Product findProductById(int productId) {
             return productMap.getOrDefault(productId, new Product(0, "Not Found", 0.0, 0)); 
        }

        // Option 7: Dynamically calculate Top 3 Products by Avg Rating
        public String getTop3Products() {
            List<Review> reviews = loadReviewsFromFile("reviews.csv");
            
            // 1. Group reviews by productId and calculate the average rating
            Map<Integer, Double> avgRatings = reviews.stream()
                .collect(Collectors.groupingBy(
                    Review::productId,
                    Collectors.averagingInt(Review::rating)
                ));
            
            // 2. Map the average ratings to a list of (Product, Average Rating) pairs
            List<Map.Entry<Integer, Double>> topRatedList = avgRatings.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());

            // 3. Format the output string
            StringBuilder sb = new StringBuilder();
            sb.append("--- Top 3 Rated Products ---\n");
            
            int rank = 1;
            for (Map.Entry<Integer, Double> entry : topRatedList) {
                Product p = productMap.get(entry.getKey());
                if (p != null) {
                    sb.append(rank).append(". ")
                      .append(p.name())
                      .append(String.format(" (%.1f Average Rating)\n", entry.getValue()));
                    rank++;
                }
            }
            
            return sb.toString();
        }
    }

    private class Orders {
        public String showOrdersBetween(String start, String end) {
            // NOTE: This remains a placeholder for simplicity due to the complex 
            // nature of productIds in orders.csv
            return "--- Orders Between " + start + " and " + end + " ---\n"
                 + "Order 201 | 2024-03-01 | Customer 1 (Placeholder)\n"
                 + "Order 202 | 2024-03-10 | Customer 2 (Placeholder)\n"
                 + "Order 203 | 2024-04-05 | Customer 1 (Placeholder)\n";
        }
    }
    // -----------------------------------------------------------

    private Customers customers;
    private Products products;
    private Orders orders;

    private JFrame frame;
    private JPanel cards;
    private CardLayout cardLayout;

    public ManagementSystemGUI() {
        customers = new Customers();
        products = new Products();
        orders = new Orders();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("E-Commerce Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(DARK_BLUE);

        // Header
        try {
            // Note: This file path will fail unless the image exists at that exact location
            Image img = ImageIO.read(new File("C:\\Users\\shaha\\DS-project\\images\\readme_header.png"));
            JLabel headerLabel = new JLabel(new ImageIcon(img));
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(headerLabel);
        } catch (IOException e) {
            JLabel fallback = new JLabel("E-Commerce Management System", SwingConstants.CENTER);
            fallback.setOpaque(true);
            fallback.setBackground(DARK_BLUE);
            fallback.setForeground(Color.WHITE);
            fallback.setFont(new Font("Arial", Font.BOLD, 28));
            fallback.setAlignmentX(Component.CENTER_ALIGNMENT);
            fallback.setMaximumSize(new Dimension(800, 100));
            container.add(fallback);
        }

        // --- Buttons Setup (5x2 grid for 10 options) ---
        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        buttonPanel.setBackground(DARK_BLUE);
        buttonPanel.setMaximumSize(new Dimension(700, 225));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addProductBtn = createMenuButton("1. Add Product");
        JButton addCustomerBtn = createMenuButton("2. Add Customer");
        JButton placeOrderBtn = createMenuButton("3. Place New Order");
        JButton viewOrdersBtn = createMenuButton("4. View Customer Orders");
        JButton addReviewBtn = createMenuButton("5. Add Review for Product");
        JButton extractReviewsBtn = createMenuButton("6. Extract Customer Reviews");
        JButton topProductsBtn = createMenuButton("7. Top 3 Products by Avg Rating");
        JButton ordersBetweenDatesBtn = createMenuButton("8. Orders Between Two Dates");
        JButton commonProductsBtn = createMenuButton("9. Common Reviewed Products");
        JButton exitBtn = createMenuButton("10. Exit");

        buttonPanel.add(addProductBtn);
        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(viewOrdersBtn);
        buttonPanel.add(addReviewBtn);
        buttonPanel.add(extractReviewsBtn);
        buttonPanel.add(topProductsBtn);
        buttonPanel.add(ordersBetweenDatesBtn);
        buttonPanel.add(commonProductsBtn);
        buttonPanel.add(exitBtn); 
        
        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(buttonPanel);
        
        // Card layout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setPreferredSize(new Dimension(800, 400));
        cards.setBackground(DARK_BLUE);

        cards.add(createWelcomePanel(), "home");
        cards.add(createAddProductPanel(), "addProduct"); 
        cards.add(createAddCustomerPanel(), "addCustomer"); 
        cards.add(createPlaceholderPanel("3. Place Order not implemented yet."), "placeOrder");
        cards.add(createViewCustomerOrdersPanel(), "viewOrders"); 
        cards.add(createPlaceholderPanel("5. Add Review not implemented yet."), "addReview");
        cards.add(createExtractReviewsPanel(), "extractReviews"); 
        cards.add(createTopProductsPanel(), "topProducts");       
        cards.add(createOrdersBetweenDatesPanel(), "ordersBetween");
        cards.add(createCommonProductsPanel(), "commonProducts");


        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(cards);

        // Action Listeners
        addProductBtn.addActionListener(e -> cardLayout.show(cards, "addProduct"));
        addCustomerBtn.addActionListener(e -> cardLayout.show(cards, "addCustomer"));
        placeOrderBtn.addActionListener(e -> cardLayout.show(cards, "placeOrder"));
        viewOrdersBtn.addActionListener(e -> cardLayout.show(cards, "viewOrders"));
        addReviewBtn.addActionListener(e -> cardLayout.show(cards, "addReview"));
        extractReviewsBtn.addActionListener(e -> cardLayout.show(cards, "extractReviews"));
        topProductsBtn.addActionListener(e -> cardLayout.show(cards, "topProducts"));
        ordersBetweenDatesBtn.addActionListener(e -> cardLayout.show(cards, "ordersBetween"));
        commonProductsBtn.addActionListener(e -> cardLayout.show(cards, "commonProducts"));
        exitBtn.addActionListener(e -> frame.dispose());

        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        cardLayout.show(cards, "home");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(DARK_BLUE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        return btn;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BLUE);

        JLabel instructions = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to your E-commerce System!"
                + "<br><br>Manage products, track orders, and monitor customers."
                + "</div></html>", SwingConstants.CENTER);

        instructions.setForeground(Color.WHITE);
        instructions.setFont(new Font("Arial", Font.ITALIC, 18));
        instructions.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        panel.add(instructions, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BLUE);
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // --- 1. Add Product ---
    private JPanel createAddProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 35);

        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(idLabel, gbc);

        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        nameField.setPreferredSize(fieldSize);
        nameField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(priceLabel, gbc);

        JTextField priceField = new JTextField();
        priceField.setPreferredSize(fieldSize);
        priceField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setForeground(Color.WHITE);
        stockLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(stockLabel, gbc);

        JTextField stockField = new JTextField();
        stockField.setPreferredSize(fieldSize);
        stockField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(stockField, gbc);

        JButton submit = new JButton("Add Product");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                products.addProduct(new Product(id, name, price, stock));
                JOptionPane.showMessageDialog(frame, "Product added successfully.");
                idField.setText("");
                nameField.setText("");
                priceField.setText("");
                stockField.setText("");
                cardLayout.show(cards, "home");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid input.");
            }
        });

        return panel;
    }

    // --- 2. Add Customer ---
    private JPanel createAddCustomerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 35);

        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(idLabel, gbc);

        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField();
        nameField.setPreferredSize(fieldSize);
        nameField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(emailLabel, gbc);

        JTextField emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);
        emailField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        JButton submit = new JButton("Add Customer");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                customers.registerCustomer(id, name, email);
                JOptionPane.showMessageDialog(frame, "Customer added successfully.");
                idField.setText("");
                nameField.setText("");
                emailField.setText("");
                cardLayout.show(cards, "home");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid input.");
            }
        });

        return panel;
    }

    // --- 4. View Customer Orders ---
    private JPanel createViewCustomerOrdersPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(250, 40);

        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(idLabel, gbc);

        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        JTextArea outputArea = new JTextArea(10, 35);
        outputArea.setEditable(false);
        outputArea.setFont(fieldFont);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        scrollPane.setVisible(false);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH; 
        gbc.weighty = 1.0; 
        panel.add(scrollPane, gbc);

        JButton showBtn = new JButton("Show Orders");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(showBtn, gbc);
        
        showBtn.addActionListener(e -> {
              try {
                int id = Integer.parseInt(idField.getText().trim());
                String orderData = customers.viewOrderHistory(id);
                outputArea.setText(orderData);
                JOptionPane.showMessageDialog(frame, "Orders displayed below.");
                scrollPane.setVisible(true); 
                panel.revalidate(); 
                panel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid Customer ID.");
                outputArea.setText("");
                scrollPane.setVisible(false); 
            }
        });
        
        return panel;
    }
    
    // --- 6. Extract Reviews from Specific Customer ---
    private JPanel createExtractReviewsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Dimension fieldSize = new Dimension(250, 40);

        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(idLabel, gbc);

        JTextField idField = new JTextField();
        idField.setPreferredSize(fieldSize);
        idField.setFont(labelFont);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        
        JButton showBtn = new JButton("Extract Reviews");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(showBtn, gbc);
        
        JPanel filler = new JPanel();
        filler.setBackground(DARK_BLUE);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(filler, gbc);

        showBtn.addActionListener(e -> {
              try {
                int id = Integer.parseInt(idField.getText().trim());
                String reviewData = customers.getCustomerReviews(id);
                
                String resultCardName = "reviewsResult_" + System.currentTimeMillis();
                JPanel resultsPanel = createLargeTextResultsPanel(reviewData, "Reviews by Customer " + id);
                
                cards.add(resultsPanel, resultCardName);
                cardLayout.show(cards, resultCardName);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid Customer ID.");
            }
        });
        
        return panel;
    }
    
    // --- 7. Show Top 3 Products by Average Rating ---
    private JPanel createTopProductsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel infoLabel = new JLabel("Click below to see the current Top 3 Products (from files).");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(infoLabel, gbc);

        JButton showBtn = new JButton("Show Top 3 Products");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(showBtn, gbc);
        
        JPanel filler = new JPanel();
        filler.setBackground(DARK_BLUE);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(filler, gbc);

        showBtn.addActionListener(e -> {
            String topData = products.getTop3Products();
            String resultCardName = "topProductsResult_" + System.currentTimeMillis();
            
            JPanel resultsPanel = createLargeTextResultsPanel(topData, "Top 3 Products by Rating");
            
            cards.add(resultsPanel, resultCardName);
            cardLayout.show(cards, resultCardName);
        });

        return panel;
    }
    
    // --- 8. Orders Between Two Dates ---
    private JPanel createOrdersBetweenDatesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        
        Dimension fieldSize = new Dimension(300, 45); 

        JLabel fromLabel = new JLabel("From Date (yyyy-mm-dd):");
        fromLabel.setForeground(Color.WHITE);
        fromLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(fromLabel, gbc);

        JTextField fromField = new JTextField();
        fromField.setPreferredSize(fieldSize);
        fromField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(fromField, gbc);

        JLabel toLabel = new JLabel("To Date (yyyy-mm-dd):");
        toLabel.setForeground(Color.WHITE);
        toLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(toLabel, gbc);

        JTextField toField = new JTextField();
        toField.setPreferredSize(fieldSize);
        toField.setFont(fieldFont);
        gbc.gridx = 1;
        panel.add(toField, gbc);
        
        JButton showBtn = new JButton("Show Orders");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 0; 
        panel.add(showBtn, gbc);
        
        JPanel filler = new JPanel();
        filler.setBackground(DARK_BLUE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(filler, gbc);

        showBtn.addActionListener(e -> {
            String from = fromField.getText().trim();
            String to = toField.getText().trim();
            
            if (from.isEmpty() || to.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter both start and end dates.");
            } else {
                String orderList = orders.showOrdersBetween(from, to);
                
                String resultCardName = "ordersResult_" + System.currentTimeMillis();
                JPanel resultsPanel = createOrdersResultsPanel(orderList, from, to);
                
                cards.add(resultsPanel, resultCardName);
                cardLayout.show(cards, resultCardName);
            }
        });

        return panel;
    }
    
    // --- 9. Common Reviewed Products Between Two Customers ---
    private JPanel createCommonProductsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Dimension fieldSize = new Dimension(150, 40);

        JLabel id1Label = new JLabel("Customer ID 1:");
        id1Label.setForeground(Color.WHITE);
        id1Label.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(id1Label, gbc);

        JTextField id1Field = new JTextField();
        id1Field.setPreferredSize(fieldSize);
        id1Field.setFont(labelFont);
        gbc.gridx = 1;
        panel.add(id1Field, gbc);
        
        JLabel id2Label = new JLabel("Customer ID 2:");
        id2Label.setForeground(Color.WHITE);
        id2Label.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(id2Label, gbc);

        JTextField id2Field = new JTextField();
        id2Field.setPreferredSize(fieldSize);
        id2Field.setFont(labelFont);
        gbc.gridx = 1;
        panel.add(id2Field, gbc);
        
        JButton showBtn = new JButton("Find Common Products");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(showBtn, gbc);
        
        JPanel filler = new JPanel();
        filler.setBackground(DARK_BLUE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(filler, gbc);


        showBtn.addActionListener(e -> {
              try {
                int id1 = Integer.parseInt(id1Field.getText().trim());
                int id2 = Integer.parseInt(id2Field.getText().trim());
                
                String commonData = customers.getCommonReviewedProducts(id1, id2);
                
                String resultCardName = "commonProductsResult_" + System.currentTimeMillis();
                JPanel resultsPanel = createLargeTextResultsPanel(commonData, "Common Reviewed Products");
                
                cards.add(resultsPanel, resultCardName);
                cardLayout.show(cards, resultCardName);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid Customer ID(s).");
            }
        });
        
        return panel;
    }
    
    // --- Reusable Large Results Panel (for Options 6, 7, 9) ---
    private JPanel createLargeTextResultsPanel(String results, String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        
        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font outputFont = new Font("Monospaced", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(titleLabel, gbc);

        // FIX: Increased width to 100 columns and set preferred size explicitly
        JTextArea outputArea = new JTextArea(20, 100); 
        outputArea.setText(results);
        outputArea.setEditable(false);
        outputArea.setFont(outputFont);
        outputArea.setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(750, 300)); // Explicit size hint

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        JButton backBtn = new JButton("<< Back to Main Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(backBtn, gbc);

        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));

        return panel;
    }

    // --- 8. Orders Between Dates Results Panel (Kept specific) ---
    private JPanel createOrdersResultsPanel(String results, String from, String to) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;

        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font outputFont = new Font("Monospaced", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel("Orders Found (" + from + " to " + to + ")");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(titleLabel, gbc);

        // FIX: Increased width to 100 columns and set preferred size explicitly
        JTextArea outputArea = new JTextArea(12, 100); 
        outputArea.setText(results);
        outputArea.setEditable(false);
        outputArea.setFont(outputFont);
        outputArea.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(750, 200)); // Explicit size hint

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        JButton backBtn = new JButton("<< Back to Main Menu");
        backBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(backBtn, gbc);

        backBtn.addActionListener(e -> cardLayout.show(cards, "home"));

        return panel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagementSystemGUI::new);
    }
}