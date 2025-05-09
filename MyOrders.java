import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyOrders extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel model;

    public MyOrders() {
        setTitle("My Orders");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Order ID", "Product", "Quantity", "Total Price", "Status"});
        ordersTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        loadOrders("Guest");  

        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadOrders(String customerName) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3332/bakery", "root", "jaykap31");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE customer_name = ?")) {

            stmt.setString(1, customerName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_price"),
                        rs.getString("order_status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load orders!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
