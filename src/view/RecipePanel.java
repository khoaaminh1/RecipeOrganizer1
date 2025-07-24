package view;

import model.Recipe;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Panel to display a recipe thumbnail and name.
 */
public class RecipePanel extends JPanel {
    public RecipePanel(Recipe recipe) {
        setLayout(new BorderLayout());
        JLabel nameLabel = new JLabel(recipe.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(nameLabel, BorderLayout.SOUTH);

        if (recipe.getImagePath() != null && !recipe.getImagePath().isEmpty()) {
            JLabel imgLabel = new JLabel(ImageUtils.getScaledImage(recipe.getImagePath(), 200, 120));
            add(imgLabel, BorderLayout.CENTER);
        }
    }
}
