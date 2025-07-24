package view;

import model.Ingredient;
import model.Recipe;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class RecipeDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> typeCombo;
    private JTextArea ingredientsArea;
    private JTextArea instructionsArea;
    private JLabel imagePreview;
    private String imagePath = "";
    private Recipe result = null;

    private static final String[] TYPES = {"starter", "main dish", "dessert", "salad", "soup"};

    public RecipeDialog(JFrame parent, Recipe recipe) {
        super(parent, recipe == null ? "Add Recipe" : "Edit Recipe", true);
        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        JPanel formPanel = new JPanel(new GridLayout(7, 1, 4, 4));
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Type:"));
        typeCombo = new JComboBox<>(TYPES);
        formPanel.add(typeCombo);

        formPanel.add(new JLabel("Ingredients (one per line, name:amount):"));
        ingredientsArea = new JTextArea(3, 30);
        JScrollPane ingScroll = new JScrollPane(ingredientsArea);
        formPanel.add(ingScroll);

        formPanel.add(new JLabel("Instructions:"));
        instructionsArea = new JTextArea(3, 30);
        JScrollPane instScroll = new JScrollPane(instructionsArea);
        formPanel.add(instScroll);

        // Image select
        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(120, 80));
        JButton btnChoose = new JButton("Choose image");
        btnChoose.addActionListener(this::onChooseImage);
        imgPanel.add(btnChoose);
        imgPanel.add(imagePreview);

        add(formPanel, BorderLayout.CENTER);
        add(imgPanel, BorderLayout.EAST);

        // Buttons
        JPanel btnPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        btnOK.addActionListener(this::onOK);
        btnCancel.addActionListener(e -> setVisible(false));
        btnPanel.add(btnOK);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // Fill if edit
        if (recipe != null) {
            nameField.setText(recipe.getName());
            typeCombo.setSelectedItem(recipe.getType());
            StringBuilder sb = new StringBuilder();
            for (Ingredient ing : recipe.getIngredients()) {
                sb.append(ing.getName()).append(":").append(ing.getAmount()).append("\n");
            }
            ingredientsArea.setText(sb.toString());
            instructionsArea.setText(recipe.getInstructions());
            imagePath = recipe.getImagePath();
            if (imagePath != null && !imagePath.isEmpty())
                imagePreview.setIcon(ImageUtils.getScaledImage(imagePath, 120, 80));
        }
    }

    private void onChooseImage(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            imagePath = chooser.getSelectedFile().getAbsolutePath();
            imagePreview.setIcon(ImageUtils.getScaledImage(imagePath, 120, 80));
        }
    }

    private void onOK(ActionEvent e) {
        String name = nameField.getText().trim();
        String type = typeCombo.getSelectedItem().toString();
        String ingText = ingredientsArea.getText().trim();
        String inst = instructionsArea.getText().trim();

        if (name.isEmpty() || ingText.isEmpty() || inst.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        // Parse ingredients
        List<Ingredient> ingredients = new ArrayList<>();
        for (String line : ingText.split("\\n")) {
            String[] parts = line.split(":");
            if (parts.length == 2)
                ingredients.add(new Ingredient(parts[0].trim(), parts[1].trim()));
        }
        if (ingredients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least one ingredient.");
            return;
        }
        result = new Recipe(0, name, type, imagePath, ingredients, inst);
        setVisible(false);
    }

    public Recipe getRecipe() {
        return result;
    }
}
