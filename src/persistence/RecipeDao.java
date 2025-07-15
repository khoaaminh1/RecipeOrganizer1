package persistence;

import model.Recipe;
import java.util.List;

public interface RecipeDao {
    List<Recipe> loadRecipes() throws Exception;
    void saveRecipes(List<Recipe> recipes) throws Exception;
}