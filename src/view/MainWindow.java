package view;

import controller.RecipeManager;
import model.Recipe;
import persistence.TextRecipeDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Image;
import javax.swing.ImageIcon;

public class MainWindow extends JFrame {
    private RecipeManager recipeManager;
    private RecipeTableModel tableModel;
    private JTable recipeTable;
    private JTextField idField, nameField, ingredientField, searchField;
    private JComboBox<String> typeCombo;
    private JTextArea recipeArea;
    private JButton addButton, updateButton, deleteButton, searchButton;

    public MainWindow() {
        super("Recipe Management System");
        TextRecipeDao dao = new TextRecipeDao("src/view/database.txt");
        recipeManager = new RecipeManager(dao);
        tableModel = new RecipeTableModel();
        tableModel.setRecipes(recipeManager.getRecipes());
        setJMenuBar(createMenuBar());
        initComponents();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Recipe Organizer\nVersion 1.0"));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left panel: Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("ITEM ID:");
        idLabel.setForeground(Color.BLACK);
        idField = new JTextField(10);
        JLabel nameLabel = new JLabel("NAME:");
        nameLabel.setForeground(Color.BLACK);
        nameField = new JTextField(10);
        JLabel typeLabel = new JLabel("TYPE:");
        typeLabel.setForeground(Color.BLACK);
        typeCombo = new JComboBox<>(new String[]{"STARTERS", "MAIN DISH", "DESSERT"});
        JLabel ingredientLabel = new JLabel("INGRIDIENTS:");
        ingredientLabel.setForeground(Color.BLACK);
        ingredientField = new JTextField(15);
        JLabel recipeLabel = new JLabel("RECIPE:");
        recipeLabel.setForeground(Color.BLACK);
        recipeArea = new JTextArea(5, 15);
        JScrollPane recipeScroll = new JScrollPane(recipeArea);

        // Đặt màu chữ cho các button
        Color buttonTextColor = Color.BLACK;
        searchButton = new JButton("SEARCH");
        searchButton.setForeground(buttonTextColor);
        addButton = new JButton("ADD RECORD");
        addButton.setForeground(buttonTextColor);
        updateButton = new JButton("UPDATE RECORD");
        updateButton.setForeground(buttonTextColor);
        deleteButton = new JButton("DELETE RECORD");
        deleteButton.setForeground(buttonTextColor);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(idLabel, gbc);
        gbc.gridx = 1; formPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(nameLabel, gbc);
        gbc.gridx = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(typeLabel, gbc);
        gbc.gridx = 1; formPanel.add(typeCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(ingredientLabel, gbc);
        gbc.gridx = 1; formPanel.add(ingredientField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(recipeLabel, gbc);
        gbc.gridx = 1; formPanel.add(recipeScroll, gbc);

        // Search field
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Search:"), gbc);
        searchField = new JTextField(10);
        gbc.gridx = 1; formPanel.add(searchField, gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(searchButton, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        gbc.gridx = 1; gbc.gridy = 7; formPanel.add(buttonPanel, gbc);

        // GridBagLayout: căn đều và thêm "đệm" để đẩy các thành phần lên trên khi phóng to
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.weighty = 1; gbc.fill = GridBagConstraints.VERTICAL;
        formPanel.add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;

        // Table (right)
        recipeTable = new JTable(tableModel);
        recipeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        recipeTable.setShowGrid(true);
        recipeTable.setGridColor(Color.GRAY);
        recipeTable.setShowHorizontalLines(true);
        recipeTable.setShowVerticalLines(true);
        recipeTable.setForeground(Color.BLACK);
        // Đặt header in đậm
        javax.swing.table.JTableHeader header = recipeTable.getTableHeader();
        header.setForeground(Color.BLACK);
        header.setFont(header.getFont().deriveFont(java.awt.Font.BOLD));
        // Tooltip hiển thị full nội dung ô
        recipeTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    setToolTipText(value.toString());
                } else {
                    setToolTipText(null);
                }
                return c;
            }
        });
        // Custom renderer cho cột Recipe để wrap text và hiển thị full nội dung
        recipeTable.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea area = new JTextArea();
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setText(value != null ? value.toString() : "");
                area.setFont(table.getFont());
                area.setForeground(table.getForeground());
                area.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                area.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
                area.setEditable(false);
                area.setToolTipText(area.getText());
                return area;
            }
        });
        // Set preferred width cho từng cột
        recipeTable.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        recipeTable.getColumnModel().getColumn(1).setPreferredWidth(180); // Name
        recipeTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Type
        recipeTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Ingredients
        recipeTable.getColumnModel().getColumn(4).setPreferredWidth(400); // Recipe
        recipeTable.setRowHeight(48);
        JScrollPane tableScroll = new JScrollPane(recipeTable);
        tableScroll.setPreferredSize(new Dimension(500, 300));
        tableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tableScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // tableScroll.getViewport().setBackground(new java.awt.Color(245, 245, 245)); // bỏ màu nền cũ

        // Debug kiểm tra resource
        System.out.println("Resource: " + getClass().getResource("/img/background1.jpg"));
        // Load ảnh nền (ưu tiên resource, nếu không được thì thử đường dẫn tuyệt đối)
        Image bgImage = null;
        try {
            java.net.URL url = getClass().getResource("/img/background1.jpg");
            if (url != null) {
                bgImage = new ImageIcon(url).getImage();
            } else {
                // Nếu không tìm thấy resource, thử đường dẫn tuyệt đối (chỉ để test)
                String absPath = "/Users/tranminhkhoa/eclipse-workspace/RecipeOrganizer/src/img/background1.jpg";
                System.out.println("Thử load ảnh bằng đường dẫn tuyệt đối: " + absPath);
                bgImage = new ImageIcon(absPath).getImage();
            }
        } catch (Exception e) {
            System.out.println("Không tìm thấy ảnh nền: " + e.getMessage());
        }
        BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(tableScroll, BorderLayout.CENTER);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(bgPanel, BorderLayout.CENTER); // Thay vì add tableScroll trực tiếp
        add(mainPanel, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(this::onAdd);
        updateButton.addActionListener(this::onUpdate);
        deleteButton.addActionListener(this::onDelete);
        searchButton.addActionListener(this::onSearch);
        recipeTable.getSelectionModel().addListSelectionListener(e -> onTableSelect());

        pack();
        setLocationRelativeTo(null);
    }

    private void onAdd(ActionEvent e) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            String ingredients = ingredientField.getText().trim();
            String recipe = recipeArea.getText().trim();
            Recipe r = new Recipe(id, name, type, ingredients, recipe);
            recipeManager.addRecipe(r);
            tableModel.setRecipes(recipeManager.getRecipes());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! " + ex.getMessage());
        }
    }

    private void onUpdate(ActionEvent e) {
        int selected = recipeTable.getSelectedRow();
        if (selected != -1) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String type = (String) typeCombo.getSelectedItem();
                String ingredients = ingredientField.getText().trim();
                String recipe = recipeArea.getText().trim();
                Recipe r = new Recipe(id, name, type, ingredients, recipe);
                recipeManager.updateRecipe(selected, r);
                tableModel.setRecipes(recipeManager.getRecipes());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! " + ex.getMessage());
            }
        }
    }

    private void onDelete(ActionEvent e) {
        int selected = recipeTable.getSelectedRow();
        if (selected != -1) {
            recipeManager.deleteRecipe(selected);
            tableModel.setRecipes(recipeManager.getRecipes());
        }
    }

    private void onSearch(ActionEvent e) {
        String keyword = searchField.getText().trim().toLowerCase();
        List<Recipe> filtered = recipeManager.getRecipes().stream()
                .filter(r -> r.getName().toLowerCase().contains(keyword) || r.getIngredients().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        tableModel.setRecipes(filtered);
    }

    private void onTableSelect() {
        int selected = recipeTable.getSelectedRow();
        if (selected != -1) {
            Recipe r = recipeManager.getRecipes().get(selected);
            idField.setText(String.valueOf(r.getId()));
            nameField.setText(r.getName());
            typeCombo.setSelectedItem(r.getType());
            ingredientField.setText(r.getIngredients());
            recipeArea.setText(r.getRecipe());
        }
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    public BackgroundPanel(Image image) {
        this.backgroundImage = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
