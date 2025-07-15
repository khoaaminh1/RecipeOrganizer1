package persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Recipe;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class JsonRecipeDao implements RecipeDao {
    private static final String CONFIG_DIR = ".recipeorganizer";
    private static final String RECIPES_FILE = "recipes.json";
    private final Path configPath;
    private final Gson gson;

    public JsonRecipeDao() {
        String userHome = System.getProperty("user.home");
        configPath = Paths.get(userHome, CONFIG_DIR);
        gson = new Gson();
    }

    @Override
    public List<Recipe> loadRecipes() {
        Path filePath = configPath.resolve(RECIPES_FILE);
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(filePath)) {
            List<Recipe> recipes = gson.fromJson(reader, new TypeToken<List<Recipe>>(){}.getType());
            return recipes != null ? recipes : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveRecipes(List<Recipe> recipes) {
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath);
            }
            Path filePath = configPath.resolve(RECIPES_FILE);
            try (Writer writer = Files.newBufferedWriter(filePath)) {
                gson.toJson(recipes, writer);
            }
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }
}