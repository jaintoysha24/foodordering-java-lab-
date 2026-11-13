package foodordering;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Factory for reusable UI components with consistent styling.
 */
public final class UIHelper {
    private UIHelper() {}

    // ── Buttons ──────────────────────────────────────────────────

    public static JButton primaryButton(String text) {
        return styledButton(text, UIConstants.ACCENT, Color.WHITE, UIConstants.ACCENT_HOVER);
    }

    public static JButton secondaryButton(String text) {
        return styledButton(text, Color.WHITE, UIConstants.TEXT_PRIMARY, new Color(0xEEEEEE));
    }

    public static JButton dangerButton(String text) {
        return styledButton(text, UIConstants.DANGER, Color.WHITE, new Color(0xB91C1C));
    }

    private static JButton styledButton(String text, Color bg, Color fg, Color hover) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? (hovered ? hover : bg) : new Color(0xCCCCCC));
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(UIConstants.FONT_BTN);
        btn.setForeground(fg);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        return btn;
    }

    // ── Cards ────────────────────────────────────────────────────

    public static JPanel card(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(UIConstants.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_CARD, 1, true),
            new EmptyBorder(14, 14, 14, 14)
        ));
        return p;
    }

    // ── Labels ───────────────────────────────────────────────────

    public static JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_SUBHEAD);
        lbl.setForeground(UIConstants.TEXT_PRIMARY);
        return lbl;
    }

    public static JLabel badge(String text, Color bg) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lbl.setFont(UIConstants.FONT_SMALL);
        lbl.setForeground(Color.WHITE);
        lbl.setOpaque(false);
        lbl.setPreferredSize(new Dimension(80, 18));
        return lbl;
    }

    // ── Styled text field ────────────────────────────────────────

    public static JTextField textField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(UIConstants.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIConstants.BORDER_CARD, 1, true),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    // ── Divider ──────────────────────────────────────────────────

    public static JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.DIVIDER);
        return sep;
    }
}
