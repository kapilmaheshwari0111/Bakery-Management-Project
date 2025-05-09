import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.*;
import java.math.*;
import javax.swing.table.DefaultTableModel;


public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JComboBox<String> roleBox;

    public LoginPage() {
        setTitle("Sustainable Bakery Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("background3.jpg"); 
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

    
        JLabel titleLabel = new JLabel("Sustainable Bakery Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

     
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("👤 Username:");
        usernameField = new JTextField(15);
        JLabel passLabel = new JLabel("🔒 Password:");
        passwordField = new JPasswordField(15);
        JLabel roleLabel = new JLabel("🎭 Login as:");
        String[] roles = {"Customer", "Admin"};
        roleBox = new JComboBox<>(roles);

        loginButton = new JButton("🚀 Login");
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        loginButton.addActionListener(e -> authenticateUser());

        registerButton = new JButton("📝 Register");
        registerButton.setBackground(new Color(30, 144, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        registerButton.addActionListener(e -> new RegistrationPage());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(userLabel, gbc);
        gbc.gridx = 1; panel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passLabel, gbc);
        gbc.gridx = 1; panel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(roleLabel, gbc);
        gbc.gridx = 1; panel.add(roleBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(loginButton, gbc);
        gbc.gridy = 4; panel.add(registerButton, gbc);

        backgroundPanel.add(panel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
        setVisible(true);
    }
    private void authenticateUser() {
        String username = usernameField.getText();
        String password = hashPassword(new String(passwordField.getPassword()));
        String role = (String) roleBox.getSelectedItem();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=? AND role=?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "✅ Login Successful!");
                if ("Admin".equals(role)) {
                    new AdminDashboard();
                } else {
                    new CustomerDashboard();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(password.getBytes());
                BigInteger no = new BigInteger(1, messageDigest);
                return no.toString(16);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }
    
        public static void main(String[] args) {
            new LoginPage();
        }
    }
    
    class RegistrationPage extends JFrame {
        public RegistrationPage() {
            setTitle("User Registration");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
    
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ignored) {}
    
            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    ImageIcon background = new ImageIcon("background6.jpg"); 
                    g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            };
            backgroundPanel.setLayout(new BorderLayout());
    
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            formPanel.setOpaque(false); 
    
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);
    
            JLabel userLabel = new JLabel("👤 Username:");
            JTextField userField = new JTextField(15);
            JLabel passLabel = new JLabel("🔒 Password:");
            JPasswordField passField = new JPasswordField(15);
            JLabel roleLabel = new JLabel("🎭 Role:");
            String[] roles = {"Customer", "Admin"};
            JComboBox<String> roleComboBox = new JComboBox<>(roles);
    
            JButton registerButton = new JButton("🚀 Register");
            registerButton.setFont(new Font("Arial", Font.BOLD, 14));
            registerButton.setBackground(new Color(34, 167, 240));
            registerButton.setForeground(Color.WHITE);
            registerButton.setFocusPainted(false);
            registerButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    
            registerButton.addActionListener(e -> {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                String hashedPassword = new LoginPage().hashPassword(password); 
    
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
                     PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashedPassword);
                    stmt.setString(3, role);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "✅ Registration Successful!");
                    dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
    
            gbc.gridx = 0; gbc.gridy = 0; formPanel.add(userLabel, gbc);
            gbc.gridx = 1; formPanel.add(userField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; formPanel.add(passLabel, gbc);
            gbc.gridx = 1; formPanel.add(passField, gbc);
            gbc.gridx = 0; gbc.gridy = 2; formPanel.add(roleLabel, gbc);
            gbc.gridx = 1; formPanel.add(roleComboBox, gbc);
            gbc.gridx = 1; gbc.gridy = 3; formPanel.add(registerButton, gbc);
    
            backgroundPanel.add(formPanel, BorderLayout.CENTER);
            setContentPane(backgroundPanel);
            setVisible(true);
        }
    
        public static void main(String[] args) {
            new RegistrationPage();
        }
    }
    
class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 20, 15, 20); // Spacing
        
        JButton manageOrders = new JButton("📦 Manage Orders");
        JButton manageInventory = new JButton("📊 Manage Inventory");
        JButton logoutButton = new JButton("🚪 Logout");
        
        Dimension buttonSize = new Dimension(250, 50); // Larger buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonColor = new Color(34, 167, 240);
        Color hoverColor = new Color(24, 144, 210);
        Color textColor = Color.WHITE;
        
        JButton[] buttons = {manageOrders, manageInventory, logoutButton};
        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setPreferredSize(buttonSize);
            btn.setBackground(buttonColor);
            btn.setForeground(textColor);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(hoverColor);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(buttonColor);
                }
            });
        
            panel.add(btn, gbc);
            gbc.gridy++; 
        }
        
        add(panel, BorderLayout.CENTER);
        
        manageOrders.addActionListener(e -> new ManageOrders().setVisible(true));
        manageInventory.addActionListener(e -> new ManageInventory().setVisible(true));
        logoutButton.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        panel.add(manageOrders);
        panel.add(manageInventory);
        panel.add(logoutButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}

class ManageOrders extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel model;

    public ManageOrders() {
        setTitle("Manage Orders");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Order ID", "Customer", "Product", "Quantity", "Total Price", "Status"});
        ordersTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        loadOrders();

        JButton updateStatusButton = new JButton("Update Status");
        updateStatusButton.addActionListener(e -> updateOrderStatus());

        add(scrollPane, BorderLayout.CENTER);
        add(updateStatusButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void loadOrders() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_price"),
                        rs.getString("order_status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderStatus() {
        int selectedRow = ordersTable.getSelectedRow();
        if (selectedRow != -1) {
            String[] statuses = {"Pending", "Processing", "Shipped", "Delivered"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
            if (newStatus != null) {
                int orderId = (int) model.getValueAt(selectedRow, 0);
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
                     PreparedStatement stmt = conn.prepareStatement("UPDATE orders SET order_status = ? WHERE order_id = ?")) {
                    stmt.setString(1, newStatus);
                    stmt.setInt(2, orderId);
                    stmt.executeUpdate();
                    model.setValueAt(newStatus, selectedRow, 5);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ManageInventory extends JFrame {
    private JTable inventoryTable;
    private DefaultTableModel model;

    public ManageInventory() {
        setTitle("Manage Inventory");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Item ID", "Item Name", "Quantity", "Unit Price"});
        inventoryTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        loadInventory();

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> addItem());

        add(scrollPane, BorderLayout.CENTER);
        add(addItemButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void loadInventory() {
        System.out.println("Loading inventory..."); 
        
        DefaultTableModel model = (DefaultTableModel) inventoryTable.getModel();
        model.setRowCount(0); 
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM inventory")) {
    
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity_available"),
                        rs.getDouble("unit_price")
                });
            }
    
            System.out.println("Inventory loaded successfully."); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    private void addItem() {
        JTextField nameField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField priceField = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Item Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Unit Price:"));
        panel.add(priceField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31")) {
                String itemName = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                
                String checkQuery = "SELECT * FROM inventory WHERE item_name = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, itemName);
                    ResultSet rs = checkStmt.executeQuery();
                    
                    if (rs.next()) {
                        String updateQuery = "UPDATE inventory SET quantity_available = quantity_available + ? WHERE item_name = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, quantity);
                            updateStmt.setString(2, itemName);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        String insertQuery = "INSERT INTO inventory (item_name, quantity_available, unit_price) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, itemName);
                            insertStmt.setInt(2, quantity);
                            insertStmt.setDouble(3, price);
                            insertStmt.executeUpdate();
                        }
                    }
                }
                loadInventory(); // Refresh UI
                JOptionPane.showMessageDialog(this, "Inventory updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    


class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon("bakery.jpg").getImage(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
}

