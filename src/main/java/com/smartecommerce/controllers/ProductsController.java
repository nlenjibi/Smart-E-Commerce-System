package com.smartecommerce.controllers;

import com.smartecommerce.dao.CategoryDAO;
import com.smartecommerce.models.Category;
import com.smartecommerce.models.Product;
import com.smartecommerce.service.ProductService;
import com.smartecommerce.utils.UIUtils;
import com.smartecommerce.utils.ValidationUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProductsController - Full CRUD operations for product management
 * OPTIMIZED: Uses AsyncTaskManager for non-blocking data loading
 */
public class ProductsController extends BaseController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ProductsController.class.getName());

    @FXML private Label totalProductsLabel;
    @FXML private Label inStockLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label outOfStockLabel;
    @FXML private VBox contentContainer;

    private final ProductService productService;
    private final CategoryDAO categoryDAO;
    private final ObservableList<Product> productsList = FXCollections.observableArrayList();
    private TableView<Product> productsTable;

    public ProductsController() {
        this.productService = new ProductService();
        this.categoryDAO = new CategoryDAO();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing ProductsController with async loading...");

        // Initialize loading overlay if contentContainer is available
        if (contentContainer != null) {
            initializeLoadingOverlay(contentContainer, "Loading products...");
        }

        // Load product data asynchronously
        loadProductDataAsync();

        LOGGER.info("ProductsController initialized successfully");
    }

    /**
     * Load all products from database asynchronously and populate UI
     * OPTIMIZED: Non-blocking async operation with loading feedback
     */
    private void loadProductDataAsync() {
        loadDataAsync(
            () -> {
                // Background task: Fetch products from database
                updateProgress("Fetching products from database...");
                return productService.getAllProducts();
            },
            products -> {
                // UI update: Process and display products
                productsList.setAll(products);
                updateStatistics(products);
                LOGGER.info(String.format("Loaded %d products asynchronously", products.size()));
            },
            throwable -> {
                // Error handling
                LOGGER.log(Level.SEVERE, "Failed to load products asynchronously", throwable);
                showError("Failed to load products: " + throwable.getMessage());
            }
        );
    }

    /**
     * Reload product data (for refresh operations)
     */
    public void refreshProductData() {
        LOGGER.info("Refreshing product data...");
        productService.clearCache(); // Clear cache to force fresh load
        loadProductDataAsync();
    }

    /**
     * Update product statistics in the UI
     */
    private void updateStatistics(List<Product> products) {
        int total = products.size();
        long inStock = products.stream()
                .filter(p -> p.getStockQuantity() > 10)
                .count();
        long lowStock = products.stream()
                .filter(p -> p.getStockQuantity() > 0 && p.getStockQuantity() <= 10)
                .count();
        long outOfStock = products.stream()
                .filter(p -> p.getStockQuantity() == 0)
                .count();

        safeSetText(totalProductsLabel, String.valueOf(total));
        safeSetText(inStockLabel, String.valueOf(inStock));
        safeSetText(lowStockLabel, String.valueOf(lowStock));
        safeSetText(outOfStockLabel, String.valueOf(outOfStock));
    }

    /**
     * Handle View Products button - displays full products table
     */
    @FXML
    private void handleViewProducts() {
        LOGGER.info("View Products button clicked");

        if (contentContainer == null) {
            LOGGER.warning("Content container is null");
            return;
        }

        // Clear existing content
        contentContainer.getChildren().clear();

        // Create table view
        productsTable = createProductsTable();
        productsTable.setItems(productsList);

        VBox tableContainer = new VBox(15);
        tableContainer.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 12;");

        Label title = new Label("All Products");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Search and filter bar
        HBox controlBar = new HBox(10);
        controlBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(250);
        searchField.setOnKeyReleased(e -> searchProducts(searchField.getText()));

        Button btnAll = new Button("All");
        Button btnInStock = new Button("In Stock");
        Button btnLowStock = new Button("Low Stock");
        Button btnOutOfStock = new Button("Out of Stock");

        btnAll.setOnAction(e -> productsTable.setItems(FXCollections.observableArrayList(productsList)));
        btnInStock.setOnAction(e -> filterProducts("instock"));
        btnLowStock.setOnAction(e -> filterProducts("lowstock"));
        btnOutOfStock.setOnAction(e -> filterProducts("outofstock"));

        for (Button btn : new Button[]{btnAll, btnInStock, btnLowStock, btnOutOfStock}) {
            btn.setStyle("-fx-background-color: #e5e7eb; -fx-padding: 8 16; -fx-cursor: hand;");
        }

        controlBar.getChildren().addAll(searchField, new Label("|"), btnAll, btnInStock, btnLowStock, btnOutOfStock);

        tableContainer.getChildren().addAll(title, controlBar, productsTable);
        VBox.setMargin(productsTable, new Insets(10, 0, 0, 0));

        contentContainer.getChildren().add(tableContainer);
    }

    /**
     * Search products by name
     */
    private void searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            productsTable.setItems(FXCollections.observableArrayList(productsList));
            return;
        }

        List<Product> results = productService.searchProducts(searchTerm);
        productsTable.setItems(FXCollections.observableArrayList(results));
    }

    /**
     * Filter products by stock status
     */
    private void filterProducts(String filter) {
        List<Product> filtered;
        switch (filter.toLowerCase()) {
            case "instock":
                filtered = productsList.stream()
                        .filter(p -> p.getStockQuantity() > 10)
                        .toList();
                break;
            case "lowstock":
                filtered = productsList.stream()
                        .filter(p -> p.getStockQuantity() > 0 && p.getStockQuantity() <= 10)
                        .toList();
                break;
            case "outofstock":
                filtered = productsList.stream()
                        .filter(p -> p.getStockQuantity() == 0)
                        .toList();
                break;
            default:
                filtered = productsList;
        }
        productsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    /**
     * Create and configure the products table
     */
    private TableView<Product> createProductsTable() {
        TableView<Product> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<Product, Integer> colProductId = new TableColumn<>("ID");
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductId.setPrefWidth(60);

        TableColumn<Product, String> colName = new TableColumn<>("Product Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colName.setPrefWidth(200);

        TableColumn<Product, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            return new SimpleStringProperty(product.getCategoryName() != null ? product.getCategoryName() : "N/A");
        });
        colCategory.setPrefWidth(120);

        TableColumn<Product, String> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            BigDecimal price = product.getPrice();
            String formatted = UIUtils.formatCurrency(price);
            return new SimpleStringProperty(formatted);
        });
        colPrice.setPrefWidth(120);

        TableColumn<Product, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colStock.setPrefWidth(80);

        TableColumn<Product, Void> colActions = new TableColumn<>("Actions");
        colActions.setPrefWidth(220);
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnView = new Button("View");
            private final Button btnEdit = new Button("Edit");
            private final Button btnDelete = new Button("Delete");

            {
                btnView.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 5 10; -fx-cursor: hand; -fx-font-size: 11px;");
                btnEdit.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-padding: 5 10; -fx-cursor: hand; -fx-font-size: 11px;");
                btnDelete.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 5 10; -fx-cursor: hand; -fx-font-size: 11px;");

                btnView.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleViewProduct(product);
                });

                btnEdit.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleEditProduct(product);
                });

                btnDelete.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleDeleteProduct(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, btnView, btnEdit, btnDelete);
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(colProductId, colName, colCategory, colPrice, colStock, colActions);

        return table;
    }

    /**
     * Handle Add New Product button
     */
    @FXML
    private void handleAddProduct() {
        LOGGER.info("Add Product button clicked");
        showProductDialog(null, "Add New Product");
    }

    /**
     * Handle View Product
     */
    private void handleViewProduct(Product product) {
        LOGGER.info("Viewing product: " + product.getProductId());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Details");
        alert.setHeaderText(product.getProductName());

        String details = String.format(
            "Product ID: %d\n" +
            "Name: %s\n" +
            "Category: %s\n" +
            "Price: %s\n" +
            "Stock: %d\n" +
            "Description: %s\n" +
            "Image URL: %s",
            product.getProductId(),
            product.getProductName(),
            product.getCategoryName() != null ? product.getCategoryName() : "N/A",
            UIUtils.formatCurrency(product.getPrice()),
            product.getStockQuantity(),
            product.getDescription() != null ? product.getDescription() : "N/A",
            product.getImageUrl() != null ? product.getImageUrl() : "N/A"
        );

        alert.setContentText(details);
        alert.showAndWait();
    }

    /**
     * Handle Edit Product
     */
    private void handleEditProduct(Product product) {
        LOGGER.info("Editing product: " + product.getProductId());
        showProductDialog(product, "Edit Product");
    }

    /**
     * Handle Delete Product
     */
    private void handleDeleteProduct(Product product) {
        LOGGER.info("Deleting product: " + product.getProductId());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Product");
        confirmation.setHeaderText("Delete " + product.getProductName() + "?");
        confirmation.setContentText("Are you sure you want to delete this product? This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = productService.deleteProduct(product.getProductId());
            if (success) {
                LOGGER.info("Product deleted successfully");
                showSuccess("Product deleted successfully");
                refreshProductData(); // Refresh data
                if (productsTable != null) {
                    handleViewProducts(); // Refresh table view
                }
            } else {
                showError("Delete Failed", "Failed to delete product");
            }
        }
    }

    /**
     * Show Add/Edit Product Dialog
     */
    private void showProductDialog(Product product, String title) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(product == null ? "Enter product details" : "Edit product details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");
        if (product != null) nameField.setText(product.getProductName());

        // Load all categories from database
        List<Category> categories = categoryDAO.findAll();
        ComboBox<Category> categoryComboBox = new ComboBox<>();
        categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        categoryComboBox.setPromptText("Select Category");
        categoryComboBox.setPrefWidth(200);

        // Set selected category if editing
        if (product != null && product.getCategoryId() > 0) {
            Category selectedCategory = categories.stream()
                    .filter(cat -> cat.getCategoryId() == product.getCategoryId())
                    .findFirst()
                    .orElse(null);
            categoryComboBox.setValue(selectedCategory);
        }

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        if (product != null) priceField.setText(product.getPrice().toString());

        TextField stockField = new TextField();
        stockField.setPromptText("Stock Quantity");
        if (product != null) stockField.setText(String.valueOf(product.getStockQuantity()));

        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);
        if (product != null && product.getDescription() != null) descArea.setText(product.getDescription());

        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("Image URL (optional)");
        if (product != null && product.getImageUrl() != null) imageUrlField.setText(product.getImageUrl());

        grid.add(new Label("Product Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryComboBox, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Stock Quantity:"), 0, 3);
        grid.add(stockField, 1, 3);
        grid.add(new Label("Description:"), 0, 4);
        grid.add(descArea, 1, 4);
        grid.add(new Label("Image URL (Optional):"), 0, 5);
        grid.add(imageUrlField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    // Validate category selection
                    Category selectedCategory = categoryComboBox.getValue();
                    if (selectedCategory == null) {
                        UIUtils.showErrorAlert("Please select a category.");
                        return null;
                    }

                    // Validate required fields using ValidationUtil
                    String productName = nameField.getText();
                    if (!ValidationUtil.isNotEmpty(productName)) {
                        UIUtils.showErrorAlert("Product name is required.");
                        return null;
                    }

                    String priceText = priceField.getText();
                    if (!ValidationUtil.isValidPrice(priceText)) {
                        UIUtils.showErrorAlert("Please enter a valid price greater than 0.");
                        return null;
                    }

                    String stockText = stockField.getText();
                    if (!ValidationUtil.isNotEmpty(stockText) || !ValidationUtil.isNumeric(stockText)) {
                        UIUtils.showErrorAlert("Please enter a valid stock quantity.");
                        return null;
                    }

                    Product newProduct = product != null ? product : new Product();
                    newProduct.setProductName(productName.trim());
                    newProduct.setCategoryId(selectedCategory.getCategoryId());
                    newProduct.setPrice(new BigDecimal(priceText.trim()));
                    newProduct.setStockQuantity(Integer.parseInt(stockText.trim()));
                    newProduct.setDescription(UIUtils.isEmpty(descArea.getText())
                        ? null
                        : descArea.getText().trim());

                    // Handle image URL - convert empty string to null
                    String imageUrl = imageUrlField.getText();
                    newProduct.setImageUrl(UIUtils.isEmpty(imageUrl) ? null : imageUrl.trim());

                    return newProduct;
                } catch (NumberFormatException e) {
                    UIUtils.showErrorAlert("Please enter valid numeric values for Price and Stock.");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(prod -> {
            boolean success;
            if (product == null) {
                // Add new product
                success = productService.addProduct(prod);
            } else {
                // Update existing product
                success = productService.updateProduct(prod);
            }

            if (success) {
                String message = product == null ? "Product added successfully" : "Product updated successfully";
                LOGGER.info(message);
                showSuccess(message);
                refreshProductData(); // Refresh data
                if (productsTable != null) {
                    handleViewProducts(); // Refresh table view
                }
            } else {
                showError("Save Failed", "Failed to save product");
            }
        });
    }

    // Helper methods
    private void safeSetText(Label label, String text) {
        if (label != null) {
            label.setText(text);
        }
    }

    private void showError(String title, String message) {
        UIUtils.showAlert(Alert.AlertType.ERROR, title, null, message);
    }

    protected void showSuccess(String message) {
        UIUtils.showSuccessAlert(message);
    }
}

