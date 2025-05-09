import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;


class CustomerDashboard extends JFrame {
    public CustomerDashboard() {
        setTitle("Customer Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("background1.jpg"); 
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setFont(new Font("Serif", Font.BOLD, 28));

                String text = "📌 Customer Dashboard";
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int x = (getWidth() - textWidth) / 2; 
                int y = 50; 

                g2.setColor(new Color(50, 50, 50, 150)); 
                g2.drawString(text, x + 2, y + 2); 

                g2.setColor(Color.WHITE);
                g2.drawString(text, x, y);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension(getWidth(), 60));
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setOpaque(false); // 🔹 Make transparent
        panel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton viewProducts = new JButton("🛍 View Products");
        JButton myOrders = new JButton("📦 My Orders");
        JButton logoutButton = new JButton("🚪 Logout");

        // Button Actions
        viewProducts.addActionListener(e -> new ViewProducts().setVisible(true));
        myOrders.addActionListener(e -> new MyOrders().setVisible(true));
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You have been logged out!");
            dispose();
            new LoginPage().setVisible(true);
        });

        styleButton(viewProducts);
        styleButton(myOrders);
        styleButton(logoutButton);

        panel.add(viewProducts);
        panel.add(myOrders);
        panel.add(logoutButton);
        backgroundPanel.add(panel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(0, 123, 255)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public static void main(String[] args) {
        new CustomerDashboard();
    }
}

class ViewProducts extends JFrame {
    private JPanel productPanel;
    private JScrollPane scrollPane;
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalAmountLabel; 

    public ViewProducts() {
        setTitle("Available Products");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        productPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        scrollPane = new JScrollPane(productPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        loadProducts();

        add(scrollPane, BorderLayout.CENTER);
        add(createCartPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadProducts() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM inventory")) {

            while (rs.next()) {
                String productName = rs.getString("item_name");
                double price = rs.getDouble("unit_price");
                int stock = rs.getInt("quantity_available");
                String imagePath = rs.getString("image_path");

                if (stock > 0) {
                    productPanel.add(createProductCard(productName, price, imagePath, stock));
                }
            }
            productPanel.revalidate();
            productPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createProductCard(String name, double price, String imagePath, int quantity) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setPreferredSize(new Dimension(200, 280));

        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        JLabel nameLabel = new JLabel(name, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel priceLabel = new JLabel("₹" + price, JLabel.CENTER);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel quantityLabel = new JLabel("Available: " + quantity, JLabel.CENTER);
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton addToCartButton = new JButton("🛒 Add to Cart");
        addToCartButton.addActionListener(e -> addToCart(name, price, quantity, quantityLabel));

        card.add(imageLabel);
        card.add(nameLabel);
        card.add(priceLabel);
        card.add(quantityLabel);
        card.add(addToCartButton);

        return card;
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout());

        JLabel cartTitle = new JLabel("🛒 Your Cart", JLabel.CENTER);
        cartTitle.setFont(new Font("Arial", Font.BOLD, 16));

        cartTableModel = new DefaultTableModel(new Object[]{"Product", "Qty", "Price"}, 0);
        cartTable = new JTable(cartTableModel);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setPreferredSize(new Dimension(400, 100));

        totalAmountLabel = new JLabel("Total: ₹0.00", JLabel.CENTER); 
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton checkoutButton = new JButton("✅ Checkout");
        checkoutButton.addActionListener(e -> checkout());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalAmountLabel, BorderLayout.NORTH); // Show total price
        bottomPanel.add(checkoutButton, BorderLayout.SOUTH);

        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);

        return cartPanel;
    }

    private void addToCart(String productName, double price, int quantity,JLabel quantityLabel) {
        // Get the current stock from the label
        int stock = Integer.parseInt(quantityLabel.getText().replaceAll("[^0-9]", "")); 
    
        if (stock <= 0) {
            JOptionPane.showMessageDialog(this, "Out of Stock!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.productName.equals(productName)) {
                item.quantity++;
                item.totalPrice += price;
                found = true;
                break;
            }
        }
    
        if (!found) {
            cartItems.add(new CartItem(productName, 1, price));
        }
    
        updateCartTable();
        stock--; 
        quantityLabel.setText("Stock: " + stock); 
        updateTotalAmount(); 
    }
    

    private void updateCartTable() {
        cartTableModel.setRowCount(0);
        for (CartItem item : cartItems) {
            cartTableModel.addRow(new Object[]{item.productName, item.quantity, "₹" + item.totalPrice});
        }
    }

    private void updateStockInUI(JLabel quantityLabel, int newStock) {
        quantityLabel.setText("Available: " + newStock);
    }

    private void updateTotalAmount() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.totalPrice;
        }
        totalAmountLabel.setText("Total: ₹" + String.format("%.2f", total)); 
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Checkout Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
             PreparedStatement orderStmt = conn.prepareStatement("INSERT INTO orders (customer_name, product_name, quantity, total_price, order_status) VALUES (?, ?, ?, ?, 'Pending')")) {
    
            for (CartItem item : cartItems) {
                orderStmt.setString(1, "Guest");
                orderStmt.setString(2, item.productName);
                orderStmt.setInt(3, item.quantity);
                orderStmt.setDouble(4, item.totalPrice);
                orderStmt.executeUpdate();
            }
    
            String environmentalTip = getRandomEnvironmentalTip();
            JOptionPane.showMessageDialog(this, "Order Placed Successfully!\n" + environmentalTip, "Success", JOptionPane.INFORMATION_MESSAGE);
    
            cartItems.clear();
            updateCartTable();
            updateTotalAmount(); 
    
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Order Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getRandomEnvironmentalTip() {
        String[] tips = {
            "Did you know? Choosing plant-based bakery items reduces your carbon footprint!",
            "Over 30% of food is wasted globally. Store your bakery items properly to reduce waste.",
            "Supporting local bakeries helps reduce transportation emissions and supports the community.",
            "Recycling packaging can save energy and reduce pollution. Dispose of packaging responsibly!",
            "Your order contributes to SDG 12: Responsible Consumption & Production. Thank you for making a difference!"
        };
    
        Random random = new Random();
        return tips[random.nextInt(tips.length)];
    }
}


class CartItem {
    String productName;
    int quantity;
    double totalPrice;

    public CartItem(String productName, int quantity, double totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
