package persistence;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Recipe;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonRecipeDao implements RecipeDao {
    private String filePath;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonRecipeDao(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Recipe> loadRecipes() {
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<List<Recipe>>(){}.getType();
            List<Recipe> recipes = gson.fromJson(reader, listType);
            return recipes != null ? recipes : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void saveRecipes(List<Recipe> recipes) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(recipes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
