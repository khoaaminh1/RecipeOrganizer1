package view;

import model.Recipe;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * TableModel for displaying recipes in a JTable.
 */
public class RecipeTableModel extends AbstractTableModel {
    private List<Recipe> recipes;
    private String[] columns = { "Name", "Ingredients", "Instructions" };

    public RecipeTableModel(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public int getRowCount() { return recipes.size(); }

    @Override
    public int getColumnCount() { return columns.length; }

    @Override
    public String getColumnName(int col) { return columns[col]; }

    @Override
    public Object getValueAt(int row, int col) {
        Recipe r = recipes.get(row);
        switch (col) {
            case 0: return r.getName();
            case 1: return r.getIngredients().size();
            case 2: return r.getInstructions();
            default: return "";
        }
    }
}
