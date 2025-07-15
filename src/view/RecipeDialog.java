package view;

import model.Recipe;

import javax.swing.*;
import java.awt.*;

public class RecipeDialog extends JDialog {
    private JTextField idField = new JTextField(10);
    private JTextField nameField = new JTextField(20);
    private JComboBox<String> typeCombo = new JComboBox<>(new String[]{"STARTERS", "MAIN DISH", "DESSERT"});
    private JTextArea ingredientsArea = new JTextArea(5, 20);
    private JTextArea recipeArea = new JTextArea(5, 20);
    private boolean saved = false;

    public RecipeDialog(JFrame parent, Recipe recipe) {
        super(parent, "Recipe", true);
        setLayout(new BorderLayout());

        JPanel fields = new JPanel(new GridLayout(10, 1));
        fields.add(new JLabel("ID:"));
        fields.add(idField);
        fields.add(new JLabel("Name:"));
        fields.add(nameField);
        fields.add(new JLabel("Type:"));
        fields.add(typeCombo);
        fields.add(new JLabel("Ingredients:"));
        fields.add(new JScrollPane(ingredientsArea));
        fields.add(new JLabel("Recipe:"));
        fields.add(new JScrollPane(recipeArea));

        add(fields, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            saved = true;
            setVisible(false);
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> setVisible(false));

        JPanel buttons = new JPanel();
        buttons.add(saveButton);
        buttons.add(cancelButton);
        add(buttons, BorderLayout.SOUTH);

        if (recipe != null) {
            idField.setText(String.valueOf(recipe.getId()));
            nameField.setText(recipe.getName());
            typeCombo.setSelectedItem(recipe.getType());
            ingredientsArea.setText(recipe.getIngredients());
            recipeArea.setText(recipe.getRecipe());
        }

        pack();
        setLocationRelativeTo(parent);
    }

    public boolean isSaved() {
        return saved;
    }

    public Recipe getRecipe() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String ingredients = ingredientsArea.getText().trim();
            String recipe = recipeArea.getText().trim();
            return new Recipe(id, name, type, ingredients, recipe);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input! " + e.getMessage());
            return null;
        }
    }
}
