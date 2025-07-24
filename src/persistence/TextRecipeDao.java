package persistence;

import model.Recipe;

import java.util.List;

/**
 * A stub for text-based RecipeDao implementation.
 * (Not used, just for illustration)
 */
public class TextRecipeDao implements RecipeDao {
    @Override
    public List<Recipe> loadRecipes() { return null; }

    @Override
    public void saveRecipes(List<Recipe> recipes) { }
}
