package controller;

import model.Recipe;
import persistence.TextRecipeDao;

import java.util.List;

public class RecipeManager {
    private TextRecipeDao dao;
    private List<Recipe> recipes;

    public RecipeManager(TextRecipeDao dao) {
        this.dao = dao;
        this.recipes = dao.loadRecipes();
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        try {
            dao.saveRecipes(recipes);
        } catch (Exception e) {
            System.err.println("Error saving recipes: " + e.getMessage());
        }
    }

    public void updateRecipe(int index, Recipe recipe) {
        recipes.set(index, recipe);
        try {
            dao.saveRecipes(recipes);
        } catch (Exception e) {
            System.err.println("Error saving recipes: " + e.getMessage());
        }
    }

    public void deleteRecipe(int index) {
        recipes.remove(index);
        try {
            dao.saveRecipes(recipes);
        } catch (Exception e) {
            System.err.println("Error saving recipes: " + e.getMessage());
        }
    }
}
