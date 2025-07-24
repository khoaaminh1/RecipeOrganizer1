package view;

import javax.swing.*;
import java.awt.*;

/**
 * Custom JPanel to display a background image stretched to the panel size.
 */
public class BackgroundPanel extends JPanel {
    private Image bgImage;

    /**
     * Constructor: load background image from given path.
     * @param imagePath path to the image file (relative to project root)
     */
    public BackgroundPanel(String imagePath) {
        this.bgImage = new ImageIcon(imagePath).getImage();
    }

    /**
     * Paint the background image stretched to the panel size.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
