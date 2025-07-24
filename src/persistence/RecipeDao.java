package persistence;

import model.Recipe;

import java.util.List;

/**
 * Data access object interface for recipes.
 */
public interface RecipeDao {
    List<Recipe> loadRecipes();
    void saveRecipes(List<Recipe> recipes);
}
