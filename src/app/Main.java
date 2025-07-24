package app;

import com.formdev.flatlaf.FlatLightLaf;
import view.MainWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Set FlatLaf for modern look
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf.");
        }
        // Run GUI on Event Dispatch Thread (recommended)
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
