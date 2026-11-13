package foodordering;

import javax.swing.*;

/** Application entry point. Demonstrates Multithreading via SwingUtilities.invokeLater. */
public class Main {
    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (multithreading concept)
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new FoodOrderingFrame().setVisible(true);
        });
    }
}
