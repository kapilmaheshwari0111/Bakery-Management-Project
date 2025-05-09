import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;

import java.io.File;
import java.io.FileOutputStream;

public class InvoiceGenerator {
    public static void generateInvoice(String orderId, String customerName, String product, int quantity, double price, String status) {
        String filePath = "invoices/Invoice_" + orderId + ".pdf";
        try {
            // Create directory if not exists
            File dir = new File("invoices");
            if (!dir.exists()) {
                dir.mkdir();
            }

            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            document.add(new Paragraph("🧾 Bakery Invoice").setBold().setFontSize(18));

            // Order Details
            document.add(new Paragraph("Order ID: " + orderId));
            document.add(new Paragraph("Customer: " + customerName));
            document.add(new Paragraph("Product: " + product));
            document.add(new Paragraph("Quantity: " + quantity));
            document.add(new Paragraph("Total Price: $" + (quantity * price)));
            document.add(new Paragraph("Order Status: " + status));

            // Footer
            document.add(new Paragraph("\nThank you for shopping with us!").setItalic());

            document.close();
            System.out.println("Invoice generated: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
