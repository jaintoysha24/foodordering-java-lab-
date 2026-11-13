package foodordering;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Main application window.
 * Demonstrates: GUI (Swing), MVC separation, Exception Handling.
 */
public class FoodOrderingFrame extends JFrame {

    // ── Data layer ───────────────────────────────────────────────
    private final MenuRepository menu = new MenuRepository();
    private final Cart           cart = new Cart();

    // ── Menu table ───────────────────────────────────────────────
    private final DefaultTableModel menuModel = new DefaultTableModel(
            new Object[]{"ID", "Item Name", "Category", "Type", "Price (Rs.)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        @Override public Class<?> getColumnClass(int c)       { return c == 0 ? Integer.class : String.class; }
    };
    private final JTable     menuTable  = new JTable(menuModel);
    private final JComboBox<String> categoryCombo = new JComboBox<>();
    private final JTextField searchField = UIHelper.textField(16);

    // ── Cart table ───────────────────────────────────────────────
    private final DefaultTableModel cartModel = new DefaultTableModel(
            new Object[]{"Item Name", "Qty", "Unit Price", "Line Total"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable     cartTable = new JTable(cartModel);
    private final JSpinner   qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));

    // ── Customer & payment ───────────────────────────────────────
    private final JTextField customerField = UIHelper.textField(18);
    private final JTextField phoneField    = UIHelper.textField(13);
    private final JComboBox<String> paymentCombo = new JComboBox<>(
            new String[]{"Cash", "UPI", "Card", "Net Banking"});

    // ── Totals ───────────────────────────────────────────────────
    private final JLabel subtotalLbl  = totalsLabel("Subtotal : Rs.     0.00");
    private final JLabel taxLbl       = totalsLabel("GST (5%) : Rs.     0.00");
    private final JLabel totalLbl     = totalsLabel("TOTAL    : Rs.     0.00");
    private final JLabel cartBadge    = new JLabel("");

    // ── Status bar ───────────────────────────────────────────────
    private final JLabel statusBar = new JLabel("  Ready");

    // ─────────────────────────────────────────────────────────────

    public FoodOrderingFrame() {
        setTitle("Foodie's Restaurant — Order Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1150, 700));
        setLocationRelativeTo(null);

        buildUI();
        populateCategories();
        refreshMenu();
        refreshCart();
        pack();
        setSize(1200, 760);
    }

    // ══════════════════════════════════════════════════════════════
    //  LAYOUT ASSEMBLY
    // ══════════════════════════════════════════════════════════════

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UIConstants.BG_PAGE);
        setContentPane(root);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildCenter(),  BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Header ───────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.BG_HEADER);
        header.setBorder(new EmptyBorder(14, 22, 14, 22));

        JLabel brand = new JLabel("🍽  FOODIE'S RESTAURANT");
        brand.setFont(UIConstants.FONT_TITLE);
        brand.setForeground(UIConstants.TEXT_ON_DARK);

        JLabel sub = new JLabel("Food Ordering System  ·  Indore");
        sub.setFont(UIConstants.FONT_SMALL);
        sub.setForeground(new Color(0x94A3B8));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(brand);
        left.add(sub);

        // Cart item-count badge in header
        cartBadge.setFont(UIConstants.FONT_BTN);
        cartBadge.setForeground(UIConstants.TEXT_ON_DARK);
        cartBadge.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(left,       BorderLayout.WEST);
        header.add(cartBadge,  BorderLayout.EAST);
        return header;
    }

    // ── Centre split ─────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2, 12, 0));
        center.setBackground(UIConstants.BG_PAGE);
        center.setBorder(new EmptyBorder(14, 14, 10, 14));
        center.add(buildMenuPanel());
        center.add(buildCartPanel());
        return center;
    }

    // ── Menu panel ───────────────────────────────────────────────

    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        // Top bar: title + filter controls
        JPanel top = new JPanel(new BorderLayout(8, 4));
        top.setOpaque(false);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        JLabel title = UIHelper.sectionLabel("Available Menu");
        titleRow.add(title);
        top.add(titleRow, BorderLayout.NORTH);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setOpaque(false);
        styleCombo(categoryCombo);
        categoryCombo.addActionListener(e -> refreshMenu());

        searchField.setToolTipText("Search by name…");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { refreshMenu(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { refreshMenu(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshMenu(); }
        });

        filters.add(new JLabel("Category:"));
        filters.add(categoryCombo);
        filters.add(Box.createHorizontalStrut(8));
        filters.add(new JLabel("Search:"));
        filters.add(searchField);
        top.add(filters, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);

        // Table
        styleTable(menuTable);
        menuTable.setDefaultRenderer(Object.class, new FoodTableRenderer(3));
        menuTable.getColumnModel().getColumn(0).setPreferredWidth(42);
        menuTable.getColumnModel().getColumn(1).setPreferredWidth(190);
        menuTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        menuTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        menuTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane scroll = styledScroll(menuTable);
        panel.add(scroll, BorderLayout.CENTER);

        // Bottom: quantity + add button
        JPanel bot = UIHelper.card(new FlowLayout(FlowLayout.LEFT, 10, 6));
        bot.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, UIConstants.DIVIDER),
            new EmptyBorder(8, 10, 8, 10)));
        bot.setBackground(UIConstants.BG_CARD);

        JLabel qLbl = new JLabel("Quantity:");
        qLbl.setFont(UIConstants.FONT_BODY);
        styleSpinner(qtySpinner);

        JButton addBtn = UIHelper.primaryButton("＋ Add to Cart");
        addBtn.addActionListener(e -> addSelectedItem());

        bot.add(qLbl);
        bot.add(qtySpinner);
        bot.add(Box.createHorizontalStrut(10));
        bot.add(addBtn);
        panel.add(bot, BorderLayout.SOUTH);

        return panel;
    }

    // ── Cart panel ───────────────────────────────────────────────

    private JPanel buildCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);

        JLabel title = UIHelper.sectionLabel("Your Cart");
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleRow.setOpaque(false);
        titleRow.add(title);
        panel.add(titleRow, BorderLayout.NORTH);

        // Cart table
        styleTable(cartTable);
        cartTable.setDefaultRenderer(Object.class, new FoodTableRenderer(-1));
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(190);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(45);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(90);
        panel.add(styledScroll(cartTable), BorderLayout.CENTER);

        // South: cart controls + totals + customer + checkout
        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setOpaque(false);

        // Cart action buttons
        JPanel cartBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        cartBtns.setOpaque(false);
        JButton removeBtn = UIHelper.dangerButton("Remove");
        JButton updateBtn = UIHelper.secondaryButton("Update Qty");
        JButton clearBtn  = UIHelper.secondaryButton("Clear Cart");
        removeBtn.addActionListener(e -> removeSelectedItem());
        updateBtn.addActionListener(e -> updateQuantity());
        clearBtn.addActionListener(e  -> clearCart());
        cartBtns.add(removeBtn);
        cartBtns.add(updateBtn);
        cartBtns.add(clearBtn);
        south.add(cartBtns, BorderLayout.NORTH);

        // Totals card
        JPanel totalsCard = UIHelper.card(new GridLayout(3, 1, 2, 2));
        totalsCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_CARD, 1, true),
            new EmptyBorder(10, 14, 10, 14)));
        subtotalLbl.setFont(UIConstants.FONT_BODY);
        taxLbl.setFont(UIConstants.FONT_BODY);
        totalLbl.setFont(UIConstants.FONT_TOTAL);
        totalLbl.setForeground(UIConstants.ACCENT);
        totalsCard.add(subtotalLbl);
        totalsCard.add(taxLbl);
        totalsCard.add(totalLbl);
        south.add(totalsCard, BorderLayout.CENTER);

        // Customer details + checkout
        JPanel bottomCard = UIHelper.card(new GridBagLayout());
        bottomCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_CARD, 1, true),
            new EmptyBorder(12, 14, 12, 14)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets  = new Insets(4, 4, 4, 4);
        gc.anchor  = GridBagConstraints.WEST;
        gc.fill    = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0;
        bottomCard.add(formLabel("Customer :"), gc);
        gc.gridx = 1; gc.weightx = 1;
        bottomCard.add(customerField, gc);

        gc.gridx = 0; gc.gridy = 1; gc.weightx = 0;
        bottomCard.add(formLabel("Phone :"), gc);
        gc.gridx = 1; gc.weightx = 1;
        bottomCard.add(phoneField, gc);

        gc.gridx = 0; gc.gridy = 2; gc.weightx = 0;
        bottomCard.add(formLabel("Payment :"), gc);
        gc.gridx = 1; gc.weightx = 1;
        styleCombo(paymentCombo);
        bottomCard.add(paymentCombo, gc);

        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        gc.fill  = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 4, 4, 4);
        JButton confirmBtn = UIHelper.primaryButton("✔  Confirm & Place Order");
        confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmBtn.addActionListener(e -> checkout());
        bottomCard.add(confirmBtn, gc);

        south.add(bottomCard, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    // ── Status bar ───────────────────────────────────────────────

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UIConstants.BG_HEADER);
        bar.setBorder(new EmptyBorder(5, 16, 5, 16));
        statusBar.setFont(UIConstants.FONT_SMALL);
        statusBar.setForeground(new Color(0x94A3B8));
        bar.add(statusBar, BorderLayout.WEST);
        JLabel copy = new JLabel("© 2025 Foodie's Restaurant  ");
        copy.setFont(UIConstants.FONT_SMALL);
        copy.setForeground(new Color(0x64748B));
        bar.add(copy, BorderLayout.EAST);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════
    //  DATA / LOGIC
    // ══════════════════════════════════════════════════════════════

    private void populateCategories() {
        for (String cat : menu.getCategories()) categoryCombo.addItem(cat);
    }

    private void refreshMenu() {
        menuModel.setRowCount(0);
        String cat    = (String) categoryCombo.getSelectedItem();
        String filter = searchField.getText().trim().toLowerCase();

        for (FoodItem item : menu.getItemsByCategory(cat)) {
            if (!filter.isEmpty() && !item.getName().toLowerCase().contains(filter)) continue;
            menuModel.addRow(new Object[]{
                item.getId(),
                item.getName(),
                item.getCategory(),
                item.getFoodType(),
                String.format("%.2f", item.getPrice())
            });
        }
        setStatus("Showing " + menuModel.getRowCount() + " items.");
    }

    private void addSelectedItem() {
        int row = menuTable.getSelectedRow();
        if (row < 0) { warn("Select an item from the menu first."); return; }

        int id = (Integer) menuModel.getValueAt(row, 0);
        FoodItem item = findById(id);
        try {
            int qty = (Integer) qtySpinner.getValue();
            cart.addItem(item, qty);
            refreshCart();
            setStatus("Added " + item.getName() + " × " + qty + " to cart.");
        } catch (IllegalArgumentException ex) { warn(ex.getMessage()); }
    }

    private void removeSelectedItem() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { warn("Select a cart item to remove."); return; }
        String name = (String) cartModel.getValueAt(row, 0);
        for (FoodItem f : cart.getItems().keySet()) {
            if (f.getName().equals(name)) { cart.removeItem(f); break; }
        }
        refreshCart();
        setStatus("Removed " + name + " from cart.");
    }

    private void updateQuantity() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { warn("Select a cart item to update."); return; }
        String name = (String) cartModel.getValueAt(row, 0);
        String input = JOptionPane.showInputDialog(this, "New quantity (1–50):", "Update Quantity",
                JOptionPane.QUESTION_MESSAGE);
        if (input == null) return;
        try {
            int qty = Integer.parseInt(input.trim());
            for (FoodItem f : cart.getItems().keySet()) {
                if (f.getName().equals(name)) { cart.updateQuantity(f, qty); break; }
            }
            refreshCart();
            setStatus("Quantity updated.");
        } catch (NumberFormatException ex)       { warn("Please enter a whole number."); }
          catch (IllegalArgumentException ex)    { warn(ex.getMessage()); }
    }

    private void clearCart() {
        if (cart.isEmpty()) return;
        int c = JOptionPane.showConfirmDialog(this,
            "Remove all items from the cart?", "Clear Cart", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) { cart.clear(); refreshCart(); setStatus("Cart cleared."); }
    }

    private void checkout() {
        if (cart.isEmpty())                     { warn("Your cart is empty."); return; }
        if (customerField.getText().isBlank())  { warn("Enter the customer's name."); customerField.requestFocus(); return; }

        String cust = customerField.getText().trim();
        int c = JOptionPane.showConfirmDialog(this,
            "Confirm order for " + cust + "?\nTotal: Rs. " +
            String.format("%.2f", cart.calculateSubtotal() * 1.05),
            "Confirm Order", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;

        try {
            Order order = new Order(cust,
                                    phoneField.getText().trim(),
                                    (String) paymentCombo.getSelectedItem(),
                                    cart);
            Path receipt = order.saveReceipt();

            // Show receipt dialog
            JTextArea area = new JTextArea(order.createReceiptText(), 24, 54);
            area.setEditable(false);
            area.setFont(UIConstants.FONT_MONO);
            area.setBackground(UIConstants.BG_PAGE);
            JScrollPane sp = new JScrollPane(area);
            sp.setBorder(new EmptyBorder(0,0,0,0));
            JOptionPane.showMessageDialog(this, sp,
                "Order Confirmed — " + order.getOrderId(),
                JOptionPane.INFORMATION_MESSAGE);

            JOptionPane.showMessageDialog(this,
                "<html><b>Receipt saved:</b><br>" + receipt.toAbsolutePath() + "</html>",
                "Saved", JOptionPane.INFORMATION_MESSAGE);

            cart.clear();
            refreshCart();
            customerField.setText("");
            phoneField.setText("");
            setStatus("Order " + order.getOrderId() + " placed successfully.");

        } catch (IllegalArgumentException ex) { warn(ex.getMessage()); }
          catch (IOException ex)              { warn("Receipt could not be saved: " + ex.getMessage()); }
    }

    // ══════════════════════════════════════════════════════════════
    //  REFRESH HELPERS
    // ══════════════════════════════════════════════════════════════

    private void refreshCart() {
        cartModel.setRowCount(0);
        for (Map.Entry<FoodItem, Integer> e : cart.getItems().entrySet()) {
            FoodItem f = e.getKey(); int qty = e.getValue();
            cartModel.addRow(new Object[]{
                f.getName(), qty,
                String.format("Rs. %.2f", f.getPrice()),
                String.format("Rs. %.2f", f.getPrice() * qty)
            });
        }
        double sub = cart.calculateSubtotal();
        double tax = sub * 0.05;
        subtotalLbl.setText(String.format("Subtotal :  Rs. %8.2f", sub));
        taxLbl.setText(      String.format("GST (5%%) :  Rs. %8.2f", tax));
        totalLbl.setText(    String.format("TOTAL    :  Rs. %8.2f", sub + tax));

        int count = cart.getTotalItemCount();
        cartBadge.setText(count > 0 ? "🛒  " + count + " item" + (count > 1 ? "s" : "") + " in cart" : "Cart is empty");
    }

    // ══════════════════════════════════════════════════════════════
    //  STYLE HELPERS
    // ══════════════════════════════════════════════════════════════

    private void styleTable(JTable t) {
        t.setFont(UIConstants.FONT_BODY);
        t.setRowHeight(32);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setBackground(UIConstants.BG_CARD);
        t.setSelectionBackground(UIConstants.TABLE_SEL_BG);
        t.setSelectionForeground(UIConstants.TABLE_SEL_FG);
        t.getTableHeader().setFont(UIConstants.FONT_BTN);
        t.getTableHeader().setBackground(UIConstants.TABLE_HEADER);
        t.getTableHeader().setForeground(UIConstants.TEXT_PRIMARY);
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, UIConstants.DIVIDER));
        t.getTableHeader().setReorderingAllowed(false);
    }

    private JScrollPane styledScroll(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(new LineBorder(UIConstants.BORDER_CARD, 1, true));
        sp.getViewport().setBackground(UIConstants.BG_CARD);
        return sp;
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(UIConstants.FONT_BODY);
        cb.setBackground(Color.WHITE);
    }

    private void styleSpinner(JSpinner s) {
        s.setFont(UIConstants.FONT_BODY);
        s.setPreferredSize(new Dimension(68, 30));
    }

    private JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BODY);
        l.setForeground(UIConstants.TEXT_SECONDARY);
        return l;
    }

    private static JLabel totalsLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.FONT_BODY);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private FoodItem findById(int id) {
        for (FoodItem f : menu.getAllItems()) if (f.getId() == id) return f;
        throw new IllegalArgumentException("Item not found.");
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Notice", JOptionPane.WARNING_MESSAGE);
    }

    private void setStatus(String msg) {
        statusBar.setText("  " + msg);
    }
}
