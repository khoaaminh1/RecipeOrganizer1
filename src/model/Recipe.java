package model;

public class Recipe {
    private int id;
    private String name;
    private String type;
    private String ingredients;
    private String recipe;

    public Recipe(int id, String name, String type, String ingredients, String recipe) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.ingredients = ingredients;
        this.recipe = recipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }
}
