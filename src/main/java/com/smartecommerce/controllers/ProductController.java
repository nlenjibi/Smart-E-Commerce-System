package com.smartecommerce.controllers;

import com.smartcommerce.dao.CategoryDAO;
import com.smartcommerce.model.Category;
import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * ProductController manages the product management UI
 */
public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProductId;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, BigDecimal> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TableColumn<Product, String> colStatus;

    @FXML private TextField txtProductName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtPrice;
    @FXML private TextField txtImageUrl;
    @FXML private ComboBox<Category> cmbCategory;
    @FXML private TextField txtSearch;
    @FXML private Button btnAddProduct;
    @FXML private Button btnEditProduct;
    @FXML private Button btnDeleteProduct;
    @FXML private Button btnSearch;
    @FXML private Label lblStatus;
    @FXML private Label lblTotalProducts;
    @FXML private Label lblInStock;
    @FXML private Label lblLowStock;
    @FXML private Label lblOutOfStock;

    private ProductService productService;
    private CategoryDAO categoryDAO;
    private ObservableList<Product> productList;
    private Product selectedProduct;

    public ProductController() {
        this.productService = new ProductService();
        this.categoryDAO = new CategoryDAO();
        this.productList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        if (colProductId != null) {
            colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
            colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            // Note: stock and status columns not initialized - Product model doesn't have these fields yet

            // Load categories
            loadCategories();

            // Load products
            loadProducts();

            // Update statistics
            updateStatistics();

            // Selection listener
            productTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            selectedProduct = newSelection;
                            populateFields(newSelection);
                        }
                    });
        }
    }

    private void loadCategories() {
        List<Category> categories = categoryDAO.findAll();
        cmbCategory.setItems(FXCollections.observableArrayList(categories));
    }

    private void loadProducts() {
        productList.clear();
        List<Product> products = productService.getAllProducts();
        productList.addAll(products);
        productTable.setItems(productList);
        updateStatistics();
    }

    private void populateFields(Product product) {
        txtProductName.setText(product.getProductName());
        txtDescription.setText(product.getDescription());
        txtPrice.setText(product.getPrice().toString());

        // Select category in combo box
        for (Category cat : cmbCategory.getItems()) {
            if (cat.getCategoryId() == product.getCategoryId()) {
                cmbCategory.setValue(cat);
                break;
            }
        }
    }

    @FXML
    private void handleAdd() {
        if (validateInput()) {
            Product product = new Product();
            product.setProductName(txtProductName.getText());
            product.setDescription(txtDescription.getText());
            product.setPrice(new BigDecimal(txtPrice.getText()));
            product.setCategoryId(cmbCategory.getValue().getCategoryId());

            if (productService.addProduct(product)) {
                showStatus("Product added successfully!");
                clearFields();
                loadProducts();
            } else {
                showError("Failed to add product");
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedProduct != null && validateInput()) {
            selectedProduct.setProductName(txtProductName.getText());
            selectedProduct.setDescription(txtDescription.getText());
            selectedProduct.setPrice(new BigDecimal(txtPrice.getText()));
            selectedProduct.setCategoryId(cmbCategory.getValue().getCategoryId());
            selectedProduct.setImageUrl(txtImageUrl.getText().trim().isEmpty() ? null : txtImageUrl.getText().trim());

            if (productService.updateProduct(selectedProduct)) {
                showStatus("Product updated successfully!");
                clearFields();
                loadProducts();
            } else {
                showError("Failed to update product");
            }
        } else {
            showError("Please select a product to update");
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Product");
            alert.setContentText("Are you sure you want to delete: " + selectedProduct.getProductName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                if (productService.deleteProduct(selectedProduct.getProductId())) {
                    showStatus("Product deleted successfully!");
                    clearFields();
                    loadProducts();
                } else {
                    showError("Failed to delete product");
                }
            }
        } else {
            showError("Please select a product to delete");
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = txtSearch.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<Product> results = productService.searchProducts(searchTerm);
            productList.clear();
            productList.addAll(results);
            showStatus("Found " + results.size() + " products");
        } else {
            loadProducts();
        }
    }

    @FXML
    private void handleRefresh() {
        loadProducts();
        clearFields();
    }

    private boolean validateInput() {
        if (txtProductName.getText().trim().isEmpty()) {
            showError("Product name is required");
            return false;
        }
        if (txtPrice.getText().trim().isEmpty()) {
            showError("Price is required");
            return false;
        }
        try {
            BigDecimal price = new BigDecimal(txtPrice.getText());
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                showError("Price must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid price format");
            return false;
        }
        if (cmbCategory.getValue() == null) {
            showError("Please select a category");
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtProductName.clear();
        txtDescription.clear();
        txtPrice.clear();
        txtImageUrl.clear();
        cmbCategory.setValue(null);
        txtSearch.clear();
        selectedProduct = null;
        productTable.getSelectionModel().clearSelection();
    }

    private void showStatus(String message) {
        if (lblStatus != null) {
            lblStatus.setText(message);
            lblStatus.setStyle("-fx-text-fill: green;");
        }
    }

    private void showError(String message) {
        if (lblStatus != null) {
            lblStatus.setText(message);
            lblStatus.setStyle("-fx-text-fill: red;");
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Update product statistics labels
     */
    private void updateStatistics() {
        int total = productList.size();
        // Note: Product model doesn't have stock field yet
        // Using placeholder values for now
        int inStock = total; // All products assumed in stock
        int lowStock = 0;
        int outOfStock = 0;


        if (lblTotalProducts != null) lblTotalProducts.setText(String.valueOf(total));
        if (lblInStock != null) lblInStock.setText(String.valueOf(inStock));
        if (lblLowStock != null) lblLowStock.setText(String.valueOf(lowStock));
        if (lblOutOfStock != null) lblOutOfStock.setText(String.valueOf(outOfStock));
    }

    /**
     * Handle Add Product button click (new name to match FXML)
     */
    @FXML
    private void handleAddProduct() {
        handleAdd();
    }

    /**
     * Handle Edit Product button click (new name to match FXML)
     */
    @FXML
    private void handleEditProduct() {
        handleUpdate();
    }

    /**
     * Handle Delete Product button click (new name to match FXML)
     */
    @FXML
    private void handleDeleteProduct() {
        handleDelete();
    }
}

