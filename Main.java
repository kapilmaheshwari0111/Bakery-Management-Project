import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set Background Image
        setContentPane(new BackgroundPanel());
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);
        
        // Button Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setOpaque(false); // Make panel transparent

        JButton manageOrders = new JButton("📦 Manage Orders");
        JButton manageInventory = new JButton("📊 Manage Inventory");
        JButton logoutButton = new JButton("🚪 Logout");

        manageOrders.setFont(new Font("Arial", Font.BOLD, 14));
        manageInventory.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));

        manageOrders.addActionListener(e ->{
            new ManageOrders().setVisible(true);
        }); 
        manageInventory.addActionListener(e ->{
            new ManageInventory().setVisible(true);
        });

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

        // Table setup
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Order ID", "Customer", "Product", "Quantity", "Total Price", "Status"});
        ordersTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        loadOrders();

        add(scrollPane, BorderLayout.CENTER);
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
}


class ManageInventory extends JFrame {
    private JTable inventoryTable;
    private DefaultTableModel model;

    public ManageInventory() {
        setTitle("Manage Inventory");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Item ID", "Item Name", "Quantity", "Unit Price"});
        inventoryTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(inventoryTable);

        loadInventory();

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadInventory() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        backgroundImage = new ImageIcon("bakery.jpg").getImage(); // Load image
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}


