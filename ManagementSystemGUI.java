import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ManagementSystemGUI {

    private static final Color DARK_BLUE = new Color(0x00, 0x00, 0x4d);

    // --- Placeholder Classes (No changes here) ---
    private class Customers {
        public void registerCustomer(int id, String name, String email) {
            System.out.println("Customer registered: " + name);
        }
        public String viewOrderHistory(int customerId) {
             return "--- Viewing Order History for Customer ID: " + customerId + " ---\n"
                 + "Order #101: 2024-05-15, Total: $50.00\n"
                 + "Order #105: 2024-06-01, Total: $125.50\n"
                 + "Order #112: 2024-07-20, Total: $15.99";
        }
    }

    private class Products {
        public void addProduct(Product p) {
            System.out.println("Product added: " + p.name);
        }
        public Product findProductById(int productId) {
             return new Product(productId, "Placeholder Product", 99.99, 50); 
        }
    }

    private class Orders {
        public String showOrdersBetween(String start, String end) {
            return "--- Orders Between " + start + " and " + end + " ---\n"
                 + "Order 201 | 2024-03-01 | Customer 1\n"
                 + "Order 202 | 2024-03-10 | Customer 2\n"
                 + "Order 203 | 2024-04-05 | Customer 1\n"
                 + "Order 204 | 2024-04-22 | Customer 3\n"
                 + "Order 205 | 2024-04-28 | Customer 2\n"
                 + "Order 206 | 2024-05-01 | Customer 4";
        }
    }

    private class Product {
        int id;
        String name;
        double price;
        int stock;

        public Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
        public String displayProductDetails() {
            return "--- Product Details ---\n" +
                   "ID: " + id + "\n" +
                   "Name: " + name + "\n" +
                   "Price: $" + String.format("%.2f", price) + "\n" +
                   "Stock: " + stock;
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

        // Buttons Setup
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBackground(DARK_BLUE);
        buttonPanel.setMaximumSize(new Dimension(700, 180));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addCustomerBtn = createMenuButton("1. Add Customer");
        JButton addProductBtn = createMenuButton("2. Add Product");
        JButton placeOrderBtn = createMenuButton("3. Place Order");
        JButton addReviewBtn = createMenuButton("4. Add Review");
        JButton viewOrdersBtn = createMenuButton("5. View Customer Orders");
        JButton viewProductDetailsBtn = createMenuButton("6. View Product Details"); 
        JButton ordersBetweenDatesBtn = createMenuButton("7. Orders Between Dates");

        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(addProductBtn);
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(addReviewBtn);
        buttonPanel.add(viewOrdersBtn);
        buttonPanel.add(viewProductDetailsBtn); 
        buttonPanel.add(ordersBetweenDatesBtn);
        buttonPanel.add(new JLabel()); 

        container.add(Box.createRigidArea(new Dimension(0, 15)));
        container.add(buttonPanel);

        // Exit Button
        JButton exitBtn = new JButton("8. Exit");
        exitBtn.setBackground(Color.WHITE);
        exitBtn.setForeground(DARK_BLUE);
        exitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        exitBtn.setMaximumSize(new Dimension(120, 35));
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(exitBtn);

        // Card layout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setPreferredSize(new Dimension(800, 400));
        cards.setBackground(DARK_BLUE);

        cards.add(createWelcomePanel(), "home");
        cards.add(createAddCustomerPanel(), "addCustomer");
        cards.add(createAddProductPanel(), "addProduct");
        cards.add(createPlaceholderPanel("Place Order (Option 3) not implemented yet."), "placeOrder");
        cards.add(createPlaceholderPanel("Add Review (Option 4) not implemented yet."), "addReview");
        cards.add(createViewCustomerOrdersPanel(), "viewOrders");
        cards.add(createViewProductDetailsPanel(), "viewProductDetails");
        cards.add(createOrdersBetweenDatesPanel(), "ordersBetween");

        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(cards);

        // Action Listeners
        addCustomerBtn.addActionListener(e -> cardLayout.show(cards, "addCustomer"));
        addProductBtn.addActionListener(e -> cardLayout.show(cards, "addProduct"));
        placeOrderBtn.addActionListener(e -> cardLayout.show(cards, "placeOrder"));
        addReviewBtn.addActionListener(e -> cardLayout.show(cards, "addReview"));
        viewOrdersBtn.addActionListener(e -> cardLayout.show(cards, "viewOrders"));
        viewProductDetailsBtn.addActionListener(e -> cardLayout.show(cards, "viewProductDetails"));
        ordersBetweenDatesBtn.addActionListener(e -> cardLayout.show(cards, "ordersBetween"));
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
        
        scrollPane.setVisible(false); // Hide initially

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
                scrollPane.setVisible(true); // Show on success
                panel.revalidate(); 
                panel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid Customer ID.");
                outputArea.setText("");
                scrollPane.setVisible(false); // Hide on error
            }
        });
        
        return panel;
    }
    
    private JPanel createViewProductDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(250, 40);

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
        
        JTextArea outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        outputArea.setFont(fieldFont);
        outputArea.setBackground(new Color(0x33, 0x33, 0x77)); 
        outputArea.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        panel.add(outputArea, gbc);
        outputArea.setVisible(false); // Hide initially

        JButton showBtn = new JButton("Show Details");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weighty = 0;
        panel.add(showBtn, gbc);

        showBtn.addActionListener(e -> {
             outputArea.setText(""); 
             try {
                int id = Integer.parseInt(idField.getText().trim());
                Product p = products.findProductById(id);
                if (p != null) {
                    outputArea.setText(p.displayProductDetails()); 
                    JOptionPane.showMessageDialog(frame, "Product details displayed below.");
                    outputArea.setVisible(true); // Show on success
                    panel.revalidate(); 
                    panel.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Product not found.");
                    outputArea.setVisible(false); // Hide on error
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error: Invalid Product ID.");
                outputArea.setVisible(false); // Hide on error
            }
        });

        return panel;
    }

    /**
     * Creates the resulting panel showing the order list.
     */
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

        JTextArea outputArea = new JTextArea(12, 45);
        outputArea.setText(results);
        outputArea.setEditable(false);
        outputArea.setFont(outputFont);
        outputArea.setForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(outputArea);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weighty = 1.0; // Allow scroll pane to take vertical space
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


    private JPanel createOrdersBetweenDatesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(DARK_BLUE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        
        // Fixed size for text fields
        Dimension fieldSize = new Dimension(300, 45); 

        // Date input fields
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
        
        // Show Orders Button
        JButton showBtn = new JButton("Show Orders");
        showBtn.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 0; 
        panel.add(showBtn, gbc);
        
        // Empty space filler - this is needed to ensure the panel takes up space
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
                
                // 1. Create a dynamic card name
                String resultCardName = "ordersResult_" + System.currentTimeMillis();
                
                // 2. Create the new results panel
                JPanel resultsPanel = createOrdersResultsPanel(orderList, from, to);
                
                // 3. Add the new panel to the card layout
                cards.add(resultsPanel, resultCardName);
                
                // 4. Switch to the new card
                cardLayout.show(cards, resultCardName);
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagementSystemGUI::new);
    }
}