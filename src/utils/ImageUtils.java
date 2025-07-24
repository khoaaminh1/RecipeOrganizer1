package utils;

import javax.swing.*;
import java.awt.*;

public class ImageUtils {
    public static ImageIcon getScaledImage(String path, int width, int height) {
        if (path == null || path.isEmpty()) return null;
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}
