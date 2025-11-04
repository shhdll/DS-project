import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ManagementSystemGUI {

    private static final Color DARK_BLUE = new Color(0x00, 0x00, 0x4d);

    // Placeholder classes for compilation - replace with your actual classes
    private class Customers {
        public void registerCustomer(int id, String name, String email) {
            System.out.println("Customer registered: " + name);
        }
    }
    private class Products {
        public void addProduct(Product p) {
            System.out.println("Product added: " + p.name);
        }
    }
    private class Orders {
        // Placeholder for orders functionality
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
    }

    private Customers customers;
    private Products products;
    private Orders orders;

    private JFrame frame;
    private JPanel cards; // CardLayout pages
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

        // Container with vertical layout
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(DARK_BLUE);

        // Header image
        try {
            // Note: This path might cause issues if the image isn't available
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

        // Welcome message
        JLabel welcomeLabel = new JLabel(" "); //Welcome to the E-Commerce Management System
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        container.add(welcomeLabel);

        // Menu buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        buttonPanel.setBackground(DARK_BLUE);
        buttonPanel.setMaximumSize(new Dimension(700, 120));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton addCustomerBtn = createMenuButton("Add Customer");
        JButton addProductBtn = createMenuButton("Add Product");
        JButton placeOrderBtn = createMenuButton("Place Order");
        JButton addReviewBtn = createMenuButton("Add Review");
        JButton viewOrdersBtn = createMenuButton("View Customer Orders");
        JButton viewProductBtn = createMenuButton("View Product Details");

        buttonPanel.add(addCustomerBtn);
        buttonPanel.add(addProductBtn);
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(addReviewBtn);
        buttonPanel.add(viewOrdersBtn);
        buttonPanel.add(viewProductBtn);

        container.add(buttonPanel);

        // Exit button
        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(Color.WHITE);
        exitBtn.setForeground(DARK_BLUE);
        exitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        exitBtn.setMaximumSize(new Dimension(120, 35));
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(exitBtn);

        // Cards (pages)
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        cards.setPreferredSize(new Dimension(800, 400));
        cards.setBackground(DARK_BLUE);
        
        // --- MODIFICATION START ---
        
        // 1. Add the Welcome/Home panel FIRST
        cards.add(createWelcomePanel(), "home"); 

        // 2. Add other panels
        cards.add(createAddCustomerPanel(), "addCustomer");
        cards.add(createAddProductPanel(), "addProduct");
        cards.add(createPlaceholderPanel("Place Order not implemented yet."), "placeOrder");
        cards.add(createPlaceholderPanel("Add Review not implemented yet."), "addReview");
        cards.add(createPlaceholderPanel("View Customer Orders not implemented yet."), "viewOrders");
        cards.add(createPlaceholderPanel("View Product not implemented yet."), "viewProduct");

        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(cards);

        // Button actions to switch cards
        addCustomerBtn.addActionListener(e -> cardLayout.show(cards, "addCustomer"));
        addProductBtn.addActionListener(e -> cardLayout.show(cards, "addProduct"));
        placeOrderBtn.addActionListener(e -> cardLayout.show(cards, "placeOrder"));
        addReviewBtn.addActionListener(e -> cardLayout.show(cards, "addReview"));
        viewOrdersBtn.addActionListener(e -> cardLayout.show(cards, "viewOrders"));
        viewProductBtn.addActionListener(e -> cardLayout.show(cards, "viewProduct"));
        exitBtn.addActionListener(e -> frame.dispose());

        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // 3. Explicitly show the home panel at startup
        cardLayout.show(cards, "home");
        
        // --- MODIFICATION END ---
    }
    
    // --- NEW METHOD START ---
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_BLUE);
        
        JLabel instructions = new JLabel("<html><div style='text-align: center;'>"
                + "Welcome to your E-commerce System!"
                + "<br><br>Easily manage products, track orders, and monitor customer activity"
                + "</div></html>", SwingConstants.CENTER);
        
        instructions.setForeground(Color.WHITE);
        instructions.setFont(new Font("Arial", Font.ITALIC, 18));
        instructions.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0)); // Add some top padding
        
        panel.add(instructions, BorderLayout.CENTER);
        return panel;
    }
    // --- NEW METHOD END ---

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(DARK_BLUE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        return btn;
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
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 35);

        // Customer ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Customer ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField();
        idField.setFont(fieldFont);
        idField.setPreferredSize(fieldSize);
        panel.add(idField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField();
        nameField.setFont(fieldFont);
        nameField.setPreferredSize(fieldSize);
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(labelFont);
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField();
        emailField.setFont(fieldFont);
        emailField.setPreferredSize(fieldSize);
        panel.add(emailField, gbc);

        // Submit button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton submit = new JButton("Add Customer");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                customers.registerCustomer(id, name, email);
                JOptionPane.showMessageDialog(frame, "Customer added successfully.");
                
                // Clear fields after success
                idField.setText("");
                nameField.setText("");
                emailField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please ensure Customer ID is a number.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred while adding the customer.");
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
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);
        Dimension fieldSize = new Dimension(200, 35);

        // Product ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(labelFont);
        panel.add(idLabel, gbc);

        gbc.gridx = 1;
        JTextField idField = new JTextField();
        idField.setFont(fieldFont);
        idField.setPreferredSize(fieldSize);
        panel.add(idField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField();
        nameField.setFont(fieldFont);
        nameField.setPreferredSize(fieldSize);
        panel.add(nameField, gbc);

        // Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(labelFont);
        panel.add(priceLabel, gbc);

        gbc.gridx = 1;
        JTextField priceField = new JTextField();
        priceField.setFont(fieldFont);
        priceField.setPreferredSize(fieldSize);
        panel.add(priceField, gbc);

        // Stock
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setForeground(Color.WHITE);
        stockLabel.setFont(labelFont);
        panel.add(stockLabel, gbc);

        gbc.gridx = 1;
        JTextField stockField = new JTextField();
        stockField.setFont(fieldFont);
        stockField.setPreferredSize(fieldSize);
        panel.add(stockField, gbc);

        // Submit
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton submit = new JButton("Add Product");
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(submit, gbc);

        submit.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                Product p = new Product(id, name, price, stock);
                products.addProduct(p);
                JOptionPane.showMessageDialog(frame, "Product added successfully.");
                
                // Clear fields after success
                idField.setText("");
                nameField.setText("");
                priceField.setText("");
                stockField.setText("");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Check ID, Price, and Stock values.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred while adding the product.");
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagementSystemGUI::new);
    }
}