package persistence;

import model.Recipe;

import java.io.*;
import java.util.*;

public class TextRecipeDao {
    private String filePath;

    public TextRecipeDao(String filePath) {
        this.filePath = filePath;
    }

    public List<Recipe> loadRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String type = parts[2];
                    String ingredients = parts[3];
                    String recipe = parts[4];
                    recipes.add(new Recipe(id, name, type, ingredients, recipe));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return recipes;
    }

    public void saveRecipes(List<Recipe> recipes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Recipe r : recipes) {
                writer.write(r.getId() + "|" + r.getName() + "|" + r.getType() + "|" + r.getIngredients() + "|" + r.getRecipe());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
