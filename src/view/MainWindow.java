package view;

import controller.RecipeManager;
import model.Recipe;
import persistence.JsonRecipeDao;
import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main application window for Recipe Organizer.
 * Uses BackgroundPanel to display an image as the window's background.
 */
public class MainWindow extends JFrame {
    private RecipeManager manager;                   // Handles recipe logic (load/save)
    private DefaultListModel<Recipe> listModel = new DefaultListModel<>(); // List model for JList
    private JList<Recipe> recipeList;                // The left recipe list UI

    // Filter & sort UI
    private JComboBox<String> filterTypeCombo;
    private JButton btnSortName;
    private String currentFile = "src/recipes_sample.json"; // Currently opened file
    private boolean sortByNameAsc = true;               // Sort direction

    // Detail fields for layout
    private JLabel nameLabel, typeLabel, idLabel, imgLabel;
    private JTextArea ingArea, instrArea;

    private static final String[] ALL_TYPES = {"All", "starter", "main dish", "dessert", "salad", "soup"};

    /**
     * Build and show the main window.
     */
    public MainWindow() {
        // ------------------ MENU BAR ------------------
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // Open (choose JSON file)
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.addActionListener(e -> openRecipeJsonFile());
        fileMenu.add(openItem);

        // New (reset to sample data)
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> newRecipeFile());
        fileMenu.add(newItem);

        // Close (exit app)
        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        fileMenu.add(closeItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // ------------------ DATA LAYER ------------------
        // Always load from sample file on app start
        manager = new RecipeManager(new JsonRecipeDao(currentFile));

        setTitle("Recipe Organizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 700);
        setLocationRelativeTo(null); // Center

        // ------------------ MAIN LAYOUT WITH BACKGROUND ------------------
        // Use BackgroundPanel for background image
        BackgroundPanel rootPanel = new BackgroundPanel("resources/images/default_bg.jpg");
        rootPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20,20,20,20);

        // Main border panel, white, with border & rounded corners
        JPanel mainPanel = new JPanel(new BorderLayout(18, 12));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225,225,225), 1, true),
            BorderFactory.createEmptyBorder(20,20,20,20)
        ));
        mainPanel.setPreferredSize(new Dimension(950, 610));

        // ------------------ HEADER ------------------
        JLabel titleLabel = new JLabel("Recipe Organizer", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(0,51,102));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0,0,16,0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ------------------ LEFT: FILTER + LIST ------------------
        JPanel leftPanel = new JPanel(new BorderLayout(10,8));
        leftPanel.setOpaque(false); // transparent to show background

        // Filter & sort row
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterPanel.setOpaque(false);
        filterTypeCombo = new JComboBox<>(ALL_TYPES);
        filterTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        filterTypeCombo.addActionListener(e -> refreshRecipeList());
        btnSortName = new JButton("Sort by name");
        btnSortName.addActionListener(e -> {
            sortByNameAsc = !sortByNameAsc;
            btnSortName.setText(sortByNameAsc ? "Sort by name ▲" : "Sort by name ▼");
            refreshRecipeList();
        });
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(filterTypeCombo);
        filterPanel.add(btnSortName);
        leftPanel.add(filterPanel, BorderLayout.NORTH);

        // Recipe JList (custom renderer for thumb/type)
        recipeList = new JList<>(listModel);
        recipeList.setCellRenderer(new RecipeListRenderer());
        recipeList.setFixedCellHeight(70);
        JScrollPane listScroll = new JScrollPane(recipeList);
        listScroll.setPreferredSize(new Dimension(320, 480));
        leftPanel.add(listScroll, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ------------------ BOTTOM: BUTTONS ------------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(this::onAdd);
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(this::onEdit);
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(this::onDelete);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ------------------ CENTER: DETAIL PANEL ------------------
        JPanel detailPanel = new JPanel();
        detailPanel.setBackground(new Color(250,250,255, 240));
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,235), 1, true),
            BorderFactory.createEmptyBorder(20, 28, 20, 28)
        ));
        detailPanel.setLayout(new BorderLayout(0, 16));
        detailPanel.setPreferredSize(new Dimension(540, 520));

        // ---------- Top Row: Name / Type / ID ----------
        JPanel infoRow = new JPanel();
        infoRow.setLayout(new BoxLayout(infoRow, BoxLayout.X_AXIS));
        infoRow.setOpaque(false);

        nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        infoRow.add(nameLabel);
        infoRow.add(Box.createHorizontalGlue());
        infoRow.add(typeLabel);
        infoRow.add(Box.createHorizontalGlue());
        infoRow.add(idLabel);
        infoRow.add(Box.createHorizontalGlue());

        detailPanel.add(infoRow, BorderLayout.NORTH);

        // ---------- Center: Thumbnail image ----------
        imgLabel = new JLabel();
        imgLabel.setHorizontalAlignment(JLabel.CENTER);
        detailPanel.add(imgLabel, BorderLayout.CENTER);

        // ---------- Ingredients & Instructions ----------
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new GridLayout(2, 1, 0, 8));

        ingArea = new JTextArea();
        ingArea.setEditable(false);
        ingArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        ingArea.setLineWrap(true);
        ingArea.setWrapStyleWord(true);
        ingArea.setOpaque(false);
        ingArea.setBorder(BorderFactory.createTitledBorder("Ingredients"));

        instrArea = new JTextArea();
        instrArea.setEditable(false);
        instrArea.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        instrArea.setLineWrap(true);
        instrArea.setWrapStyleWord(true);
        instrArea.setOpaque(false);
        instrArea.setBorder(BorderFactory.createTitledBorder("Instructions"));

        textPanel.add(new JScrollPane(ingArea));
        textPanel.add(new JScrollPane(instrArea));
        detailPanel.add(textPanel, BorderLayout.SOUTH);

        mainPanel.add(detailPanel, BorderLayout.CENTER);

        // --------- List selection updates detail panel ---------
        recipeList.addListSelectionListener(e -> {
            Recipe selected = recipeList.getSelectedValue();
            if (selected != null) {
                nameLabel.setText("Name: " + selected.getName());
                typeLabel.setText("Type: " + selected.getType());
                idLabel.setText("ID: " + selected.getId());
                if (selected.getImagePath() != null && !selected.getImagePath().isEmpty())
                    imgLabel.setIcon(ImageUtils.getScaledImage(selected.getImagePath(), 120, 90));
                else
                    imgLabel.setIcon(null);

                StringBuilder sbIng = new StringBuilder();
                selected.getIngredients().forEach(ing -> sbIng.append("- ").append(ing.getName()).append(": ").append(ing.getAmount()).append("\n"));
                ingArea.setText(sbIng.toString());
                instrArea.setText(selected.getInstructions());
            } else {
                nameLabel.setText("Name:");
                typeLabel.setText("Type:");
                idLabel.setText("ID:");
                imgLabel.setIcon(null);
                ingArea.setText("");
                instrArea.setText("");
            }
        });

        // --- Final assembly ---
        rootPanel.add(mainPanel, gbc);
        setContentPane(rootPanel);

        // --- Load initial data ---
        refreshRecipeList();
    }

    /** Loads recipe list from manager and applies filter/sort. */
    private void refreshRecipeList() {
        listModel.clear();
        List<Recipe> recipes = manager.getAllRecipes();

        // Filter by type if needed
        String selectedType = Objects.requireNonNull(filterTypeCombo.getSelectedItem()).toString();
        if (!"All".equals(selectedType)) {
            recipes = recipes.stream().filter(r -> r.getType().equalsIgnoreCase(selectedType)).collect(Collectors.toList());
        }
        // Sort by name
        recipes.sort((a, b) -> sortByNameAsc
                ? a.getName().compareToIgnoreCase(b.getName())
                : b.getName().compareToIgnoreCase(a.getName()));

        for (Recipe recipe : recipes) {
            listModel.addElement(recipe);
        }
    }

    /** Handles Add button, shows dialog, saves if valid. */
    private void onAdd(ActionEvent e) {
        RecipeDialog dialog = new RecipeDialog(this, null);
        dialog.setVisible(true);
        Recipe recipe = dialog.getRecipe();
        if (recipe != null) {
            manager.addRecipe(recipe);
            refreshRecipeList();
        }
    }

    /** Handles Edit button, shows dialog, saves if valid. */
    private void onEdit(ActionEvent e) {
        int idx = recipeList.getSelectedIndex();
        if (idx >= 0) {
            RecipeDialog dialog = new RecipeDialog(this, listModel.get(idx));
            dialog.setVisible(true);
            Recipe edited = dialog.getRecipe();
            if (edited != null) {
                manager.updateRecipe(idx, edited);
                refreshRecipeList();
            }
        }
    }

    /** Handles Delete button, confirm & delete. */
    private void onDelete(ActionEvent e) {
        int idx = recipeList.getSelectedIndex();
        if (idx >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this recipe?");
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteRecipe(idx);
                refreshRecipeList();
            }
        }
    }

    /** File > Open: show chooser, load new JSON file. */
    private void openRecipeJsonFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open Recipe JSON File");
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = chooser.getSelectedFile().getAbsolutePath();
            currentFile = filePath;
            manager = new RecipeManager(new JsonRecipeDao(currentFile));
            refreshRecipeList();
        }
    }

    /** File > New: reload from sample file. */
    private void newRecipeFile() {
        currentFile = "recipes_sample.json";
        manager = new RecipeManager(new JsonRecipeDao(currentFile));
        refreshRecipeList();
    }

    /**
     * Custom renderer for JList. Shows thumbnail and type color.
     */
    private static class RecipeListRenderer extends JPanel implements ListCellRenderer<Recipe> {
        private JLabel iconLabel = new JLabel();
        private JLabel nameLabel = new JLabel();
        private JLabel typeLabel = new JLabel();

        public RecipeListRenderer() {
            setLayout(new BorderLayout(6,0));
            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.setOpaque(false);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            textPanel.add(nameLabel, BorderLayout.CENTER);
            textPanel.add(typeLabel, BorderLayout.SOUTH);

            add(iconLabel, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Recipe> list, Recipe value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value.getImagePath() != null && !value.getImagePath().isEmpty()) {
                iconLabel.setIcon(ImageUtils.getScaledImage(value.getImagePath(), 54, 42));
            } else {
                iconLabel.setIcon(null);
            }
            nameLabel.setText(value.getName());
            typeLabel.setText(value.getType());
            typeLabel.setForeground(getTypeColor(value.getType()));
            setBackground(isSelected ? new Color(220,240,255) : new Color(255,255,255,180));
            return this;
        }

        /** Returns type color for type label. */
        private Color getTypeColor(String type) {
            switch (type.toLowerCase()) {
                case "starter": return new Color(0, 128, 255);
                case "main dish": return new Color(85, 168, 52);
                case "dessert": return new Color(205, 87, 51);
                case "salad": return new Color(89, 191, 81);
                case "soup": return new Color(232, 188, 39);
                default: return Color.DARK_GRAY;
            }
        }
    }

    /** The entry point for launching the app */
    public static void main(String[] args) {
        // Optional: FlatLaf look
        // try { UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf()); } catch (Exception e) {}
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
