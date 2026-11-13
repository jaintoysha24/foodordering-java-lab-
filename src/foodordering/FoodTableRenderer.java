package foodordering;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom cell renderer – alternating row colours and food-type badges.
 * Demonstrates: GUI customisation, Polymorphism (via renderer chain).
 */
public class FoodTableRenderer extends DefaultTableCellRenderer {
    private final int typeColumn;      // column index that holds "Veg / Non-Veg"
    private final int badgeColumn;     // -1 if no badge needed

    public FoodTableRenderer(int typeColumn) {
        this.typeColumn  = typeColumn;
        this.badgeColumn = typeColumn;
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        setFont(UIConstants.FONT_BODY);

        if (isSelected) {
            c.setBackground(UIConstants.TABLE_SEL_BG);
            c.setForeground(UIConstants.TABLE_SEL_FG);
        } else {
            c.setBackground(row % 2 == 0 ? Color.WHITE : UIConstants.ROW_ALT);
            c.setForeground(UIConstants.TEXT_PRIMARY);
        }

        // Colour the food-type cell text
        if (col == badgeColumn && value != null) {
            String v = value.toString();
            if (v.contains("Veg") && !v.contains("Non")) {
                setForeground(UIConstants.VEG_COLOR);
                setFont(UIConstants.FONT_BTN);
            } else if (v.contains("Non")) {
                setForeground(UIConstants.NONVEG_COLOR);
                setFont(UIConstants.FONT_BTN);
            }
        }

        setBorder(new javax.swing.border.EmptyBorder(4, 8, 4, 8));
        return c;
    }
}
