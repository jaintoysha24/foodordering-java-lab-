package foodordering;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Confirmed order – generates and saves a receipt.
 * Demonstrates: File Handling, Collections, Exception Handling.
 */
public class Order {
    private static final AtomicInteger COUNTER  = new AtomicInteger(1000);
    private static final double        TAX_RATE = 0.05;

    private final String                orderId;
    private final String                customerName;
    private final String                phoneNumber;
    private final Map<FoodItem, Integer> items;
    private final double                subtotal;
    private final double                tax;
    private final double                total;
    private final LocalDateTime         orderTime;
    private final String                paymentMode;

    public Order(String customerName, String phoneNumber, String paymentMode, Cart cart) {
        if (customerName == null || customerName.isBlank())
            throw new IllegalArgumentException("Customer name is required.");
        if (cart == null || cart.isEmpty())
            throw new IllegalArgumentException("Cannot place an empty order.");

        this.orderId      = "ORD" + COUNTER.incrementAndGet();
        this.customerName = customerName.trim();
        this.phoneNumber  = (phoneNumber == null || phoneNumber.isBlank()) ? "N/A" : phoneNumber.trim();
        this.paymentMode  = (paymentMode == null || paymentMode.isBlank()) ? "Cash" : paymentMode.trim();
        this.items        = new LinkedHashMap<>(cart.getItems());
        this.subtotal     = cart.calculateSubtotal();
        this.tax          = subtotal * TAX_RATE;
        this.total        = subtotal + tax;
        this.orderTime    = LocalDateTime.now();
    }

    public String getOrderId()      { return orderId; }
    public double getTotal()        { return total; }
    public String getCustomerName() { return customerName; }

    public String createReceiptText() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("=================================================\n");
        sb.append("             FOODIE'S RESTAURANT\n");
        sb.append("         Sarafa Bazaar, Indore - 452001\n");
        sb.append("         Ph: +91-731-000-0000  GSTIN: 23XXXXX\n");
        sb.append("=================================================\n");
        sb.append(String.format("  Order ID  : %s%n", orderId));
        sb.append(String.format("  Customer  : %s%n", customerName));
        sb.append(String.format("  Phone     : %s%n", phoneNumber));
        sb.append(String.format("  Date/Time : %s%n", orderTime.format(fmt)));
        sb.append(String.format("  Payment   : %s%n", paymentMode));
        sb.append("-------------------------------------------------\n");
        sb.append(String.format("  %-26s %4s %12s%n", "Item", "Qty", "Amount"));
        sb.append("-------------------------------------------------\n");

        for (Map.Entry<FoodItem, Integer> e : items.entrySet()) {
            FoodItem f   = e.getKey();
            int      qty = e.getValue();
            sb.append(String.format("  %-26s %4d  Rs.%8.2f%n",
                    truncate(f.getName(), 26), qty, f.getPrice() * qty));
        }

        sb.append("-------------------------------------------------\n");
        sb.append(String.format("  %-32s Rs.%8.2f%n", "Subtotal:",   subtotal));
        sb.append(String.format("  %-32s Rs.%8.2f%n", "GST  (5%) :", tax));
        sb.append(String.format("  %-32s Rs.%8.2f%n", "TOTAL      :", total));
        sb.append("=================================================\n");
        sb.append("         Thank you for dining with us!\n");
        sb.append("         Visit again  |  Rate us on Google\n");
        sb.append("=================================================\n");
        return sb.toString();
    }

    public Path saveReceipt() throws IOException {
        Path dir  = Path.of("receipts");
        Files.createDirectories(dir);
        Path file = dir.resolve("receipt-" + orderId + ".txt");
        Files.writeString(file, createReceiptText(), StandardCharsets.UTF_8);
        return file;
    }

    private String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
