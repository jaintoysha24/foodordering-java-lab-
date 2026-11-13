package foodordering;

import java.awt.*;

/**
 * Centralised palette and typography constants.
 * One place to change the entire look of the application.
 */
public final class UIConstants {
    private UIConstants() {}

    // ── Palette ──────────────────────────────────────────────────
    public static final Color BG_PAGE       = new Color(0xF7F8FA);
    public static final Color BG_CARD       = Color.WHITE;
    public static final Color BG_HEADER     = new Color(0x1A2340);   // deep navy
    public static final Color BG_SIDEBAR    = new Color(0x232D45);   // slightly lighter navy
    public static final Color ACCENT        = new Color(0xE8641A);   // warm orange
    public static final Color ACCENT_HOVER  = new Color(0xCC5510);
    public static final Color ACCENT_LIGHT  = new Color(0xFFF0E6);
    public static final Color TEXT_PRIMARY  = new Color(0x1A1A2E);
    public static final Color TEXT_SECONDARY= new Color(0x6B7280);
    public static final Color TEXT_ON_DARK  = new Color(0xF0F4FF);
    public static final Color DIVIDER       = new Color(0xE5E7EB);
    public static final Color ROW_ALT       = new Color(0xF9FAFB);
    public static final Color SUCCESS       = new Color(0x16A34A);
    public static final Color DANGER        = new Color(0xDC2626);
    public static final Color VEG_COLOR     = new Color(0x16A34A);
    public static final Color NONVEG_COLOR  = new Color(0xDC2626);
    public static final Color TABLE_HEADER  = new Color(0xF1F5F9);
    public static final Color TABLE_SEL_BG  = new Color(0xFFF0E6);
    public static final Color TABLE_SEL_FG  = TEXT_PRIMARY;
    public static final Color BORDER_CARD   = new Color(0xE2E8F0);

    // ── Typography ───────────────────────────────────────────────
    public static final Font  FONT_HEADING  = new Font("SansSerif", Font.BOLD,  22);
    public static final Font  FONT_SUBHEAD  = new Font("SansSerif", Font.BOLD,  14);
    public static final Font  FONT_BODY     = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font  FONT_SMALL    = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font  FONT_MONO     = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    public static final Font  FONT_TITLE    = new Font("SansSerif", Font.BOLD,  28);
    public static final Font  FONT_BTN      = new Font("SansSerif", Font.BOLD,  13);
    public static final Font  FONT_TOTAL    = new Font("SansSerif", Font.BOLD,  16);
}
