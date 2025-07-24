package model;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String type;
    private String imagePath;
    private List<Ingredient> ingredients;
    private String instructions;

    public Recipe(int id, String name, String type, String imagePath, List<Ingredient> ingredients, String instructions) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public List<Ingredient> getIngredients() { return ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    @Override
    public String toString() {
        return name;
    }
}
