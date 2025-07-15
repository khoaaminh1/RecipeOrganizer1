package view;

import model.Recipe;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel {
    private List<Recipe> recipes;
    private final String[] columnNames = { "ID", "Name", "Type", "Ingredients", "Recipe" };

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return recipes != null ? recipes.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Recipe r = recipes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.getId();
            case 1 -> r.getName();
            case 2 -> r.getType();
            case 3 -> r.getIngredients();
            case 4 -> r.getRecipe();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
