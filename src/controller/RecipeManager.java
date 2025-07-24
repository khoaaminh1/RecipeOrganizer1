package controller;

import model.Recipe;
import persistence.RecipeDao;

import java.util.List;

public class RecipeManager {
    private RecipeDao dao;

    public RecipeManager(RecipeDao dao) {
        this.dao = dao;
    }

    public List<Recipe> getAllRecipes() {
        return dao.loadRecipes();
    }

    public void addRecipe(Recipe recipe) {
        List<Recipe> recipes = dao.loadRecipes();
        // ID tăng tự động:
        int nextId = recipes.stream().mapToInt(Recipe::getId).max().orElse(0) + 1;
        recipe.setId(nextId);
        recipes.add(recipe);
        dao.saveRecipes(recipes);
    }

    public void updateRecipe(int index, Recipe recipe) {
        List<Recipe> recipes = dao.loadRecipes();
        // Đảm bảo giữ đúng ID
        recipe.setId(recipes.get(index).getId());
        recipes.set(index, recipe);
        dao.saveRecipes(recipes);
    }

    public void deleteRecipe(int index) {
        List<Recipe> recipes = dao.loadRecipes();
        recipes.remove(index);
        // Sau khi xóa, cập nhật lại ID cho liên tục
        for (int i = 0; i < recipes.size(); i++) {
            recipes.get(i).setId(i + 1);
        }
        dao.saveRecipes(recipes);
    }
}
