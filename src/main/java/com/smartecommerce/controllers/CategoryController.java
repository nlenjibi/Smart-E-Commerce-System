package com.smartecommerce.controllers;

import com.smartecommerce.dao.CategoryDAO;
import com.smartecommerce.models.Category;
import com.smartecommerce.utils.UIUtils;
import com.smartecommerce.utils.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

/**
 * CategoryController manages the category management UI with CRUD operations
 * Optimized with search, validation, and better UX
 */
public class CategoryController {

    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String> colName;
    @FXML private TableColumn<Category, String> colDescription;

    @FXML private TextField txtCategoryName;
    @FXML private TextArea txtDescription;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;
    @FXML private Label lblStatus;

    private CategoryDAO categoryDAO;
    private ObservableList<Category> categoryList;
    private FilteredList<Category> filteredList;
    private Category selectedCategory;

    public CategoryController() {
        this.categoryDAO = new CategoryDAO();
        this.categoryList = FXCollections.observableArrayList();
        this.filteredList = new FilteredList<>(categoryList, p -> true);
    }

    @FXML
    public void initialize() {
        if (colId != null) {
            // Set up table columns
            colId.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

            // Load categories
            loadCategories();

            // Set up row selection listener
            categoryTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            selectedCategory = newSelection;
                            txtCategoryName.setText(newSelection.getCategoryName());
                            txtDescription.setText(newSelection.getDescription());
                            btnUpdate.setDisable(false);
                            btnDelete.setDisable(false);
                        }
                    });

            // Set up double-click to edit
            categoryTable.setRowFactory(tv -> {
                TableRow<Category> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        handleUpdate();
                    }
                });
                return row;
            });

            // Initial button states
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            // Set placeholder text
            categoryTable.setPlaceholder(new Label("No categories found. Click 'Add Category' to create one."));
        }
    }

    private void loadCategories() {
        try {
            categoryList.clear();
            List<Category> categories = categoryDAO.findAll();

            if (categories != null) {
                categoryList.addAll(categories);
                categoryTable.setItems(categoryList);
                showStatus("✅ Loaded " + categories.size() + " categories", "success");
            } else {
                showStatus("⚠️ No categories found", "warning");
            }
        } catch (Exception e) {
            showError("❌ Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            String categoryName = txtCategoryName.getText().trim();

            // Check for duplicate category name
            if (isDuplicateCategoryName(categoryName, null)) {
                showError("❌ Category '" + categoryName + "' already exists!");
                return;
            }

            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setDescription(txtDescription.getText().trim());

            try {
                if (categoryDAO.create(category)) {
                    showStatus("✅ Category '" + categoryName + "' added successfully!", "success");
                    clearFields();
                    loadCategories();
                } else {
                    showError("❌ Failed to add category. Please try again.");
                }
            } catch (Exception e) {
                showError("❌ Error adding category: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedCategory == null) {
            showError("❌ Please select a category to update");
            return;
        }

        if (!validateInput()) {
            return;
        }

        String categoryName = txtCategoryName.getText().trim();

        // Check for duplicate category name (excluding current category)
        if (isDuplicateCategoryName(categoryName, selectedCategory.getCategoryId())) {
            showError("❌ Category '" + categoryName + "' already exists!");
            return;
        }

        // Confirm update
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Update Category");
        confirmAlert.setContentText("Are you sure you want to update '" + selectedCategory.getCategoryName() + "'?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedCategory.setCategoryName(categoryName);
            selectedCategory.setDescription(txtDescription.getText().trim());

            try {
                if (categoryDAO.update(selectedCategory)) {
                    showStatus("✅ Category updated successfully!", "success");
                    clearFields();
                    loadCategories();
                } else {
                    showError("❌ Failed to update category. Please try again.");
                }
            } catch (Exception e) {
                showError("❌ Error updating category: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedCategory == null) {
            showError("❌ Please select a category to delete");
            return;
        }

        // Enhanced confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("⚠️ Confirm Delete");
        alert.setHeaderText("Delete Category: " + selectedCategory.getCategoryName());
        alert.setContentText("Are you sure you want to delete this category?\n\n" +
                "⚠️ Warning: This action cannot be undone!\n" +
                "Note: Products using this category may be affected.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (categoryDAO.delete(selectedCategory.getCategoryId())) {
                    showStatus("✅ Category '" + selectedCategory.getCategoryName() + "' deleted successfully!", "success");
                    clearFields();
                    loadCategories();
                } else {
                    showError("❌ Failed to delete category. It may be in use by products.");
                }
            } catch (Exception e) {
                showError("❌ Error deleting category: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh() {
        clearFields();
        loadCategories();
        showStatus("🔄 Data refreshed", "info");
    }

    private boolean validateInput() {
        String categoryName = txtCategoryName.getText().trim();

        // Check if category name is empty using ValidationUtil
        if (!ValidationUtil.isNotEmpty(categoryName)) {
            showError("❌ Category name is required!");
            txtCategoryName.requestFocus();
            return false;
        }

        // Check length using ValidationUtil
        if (!ValidationUtil.isStringLengthValid(categoryName, 2, 100)) {
            showError("❌ Category name must be between 2 and 100 characters!");
            txtCategoryName.requestFocus();
            return false;
        }

        // Check for valid characters (alphanumeric, spaces, hyphens, ampersands)
        if (!categoryName.matches("^[a-zA-Z0-9 &-]+$")) {
            showError("❌ Category name can only contain letters, numbers, spaces, hyphens, and ampersands!");
            txtCategoryName.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Check if a category name already exists (case-insensitive)
     * @param categoryName The name to check
     * @param excludeId Category ID to exclude from check (for updates)
     * @return true if duplicate found, false otherwise
     */
    private boolean isDuplicateCategoryName(String categoryName, Integer excludeId) {
        return categoryList.stream()
                .filter(cat -> excludeId == null || cat.getCategoryId() != excludeId)
                .anyMatch(cat -> cat.getCategoryName().equalsIgnoreCase(categoryName));
    }

    private void clearFields() {
        txtCategoryName.clear();
        txtDescription.clear();
        selectedCategory = null;
        categoryTable.getSelectionModel().clearSelection();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        lblStatus.setText("");
    }

    /**
     * Show status message with color coding
     * @param message The message to display
     * @param type Message type: "success", "error", "warning", "info"
     */
    private void showStatus(String message, String type) {
        if (lblStatus != null) {
            switch (type.toLowerCase()) {
                case "success":
                    UIUtils.showSuccess(lblStatus, message);
                    break;
                case "error":
                    UIUtils.showError(lblStatus, message);
                    break;
                case "warning":
                    UIUtils.showWarning(lblStatus, message);
                    break;
                case "info":
                    UIUtils.showInfo(lblStatus, message);
                    break;
                default:
                    lblStatus.setText(message);
            }
        }
    }

    private void showError(String message) {
        showStatus(message, "error");
    }
}

