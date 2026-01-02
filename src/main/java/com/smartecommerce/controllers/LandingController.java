package com.smartecommerce.controllers;

import com.smartcommerce.app.SessionManager;
import com.smartcommerce.dao.CategoryDAO;
import com.smartcommerce.model.Category;
import com.smartcommerce.model.Product;
import com.smartcommerce.model.User;
import com.smartcommerce.service.CartService;
import com.smartcommerce.service.ProductService;
import com.smartcommerce.service.ViewedProductsTracker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LandingController - controller for the public landing page.
 * Responsibilities:
 * - Fetch and display products from database
 * - Manage product display and Add to Cart functionality
 * - Handle navigation (login, cart, search)
 * - Update cart badge count
 *
 * OPTIMIZED: Uses AsyncTaskManager for non-blocking data loading
 */
public class LandingController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger(LandingController.class.getName());
    private static final int PRODUCTS_PER_PAGE = 9; // 3x3 grid for optimization
    private static final int MAX_CATEGORIES_DISPLAY = 6; // Max categories on landing page
    private static final int MAX_RECENTLY_VIEWED = 6; // Max recently viewed products to display
    private static final int MAX_TOP_PURCHASED = 6; // Max top purchased products to display

    @FXML private TextField searchField;
    @FXML private Button btnLogin;
    @FXML private Button btnDashboard;
    @FXML private Button btnLogout;
    @FXML private Button btnAdmin;
    @FXML private Button btnCart;
    @FXML private Button btnShopNow;
    @FXML private VBox mainContent;
    @FXML private GridPane productGrid;

    // New sections
    @FXML private VBox categoriesSection;
    @FXML private HBox categoriesContainer;
    @FXML private VBox recentlyViewedSection;
    @FXML private HBox recentlyViewedContainer;
    @FXML private VBox topPurchasedSection;
    @FXML private HBox topPurchasedContainer;

    private CartService cartService;
    private ProductService productService;
    private CategoryDAO categoryDAO;

    // Store fetched products
    private List<Product> displayedProducts;

    // Track current filter
    private Integer currentCategoryFilter = null;

    public LandingController() {
        this.cartService = CartService.getInstance();
        this.productService = new ProductService();
        this.categoryDAO = new CategoryDAO();
    }

    @FXML
    private void initialize() {
        try {
            LOGGER.info("Initializing LandingController...");

            // Check if user is already logged in
            checkUserSession();

            // Load new sections
            loadCategoriesSection();
            loadRecentlyViewedSection();
            loadTopPurchasedSection();

            // Fetch products from database
            loadProductsFromDatabase();

            // Null-safety checks for all FXML components
            if (btnLogin != null) {
                btnLogin.setVisible(true);
                btnLogin.setOnAction(e -> onOpenLogin());
            } else {
                LOGGER.warning("btnLogin is null - FXML binding may have failed");
            }

            if (btnAdmin != null) {
                btnAdmin.setVisible(false);
            } else {
                LOGGER.warning("btnAdmin is null - FXML binding may have failed");
            }

            if (btnShopNow != null) {
                btnShopNow.setOnAction(e -> onShopNow());
            } else {
                LOGGER.warning("btnShopNow is null - FXML binding may have failed");
            }

            if (btnCart != null) {
                btnCart.setOnAction(e -> onOpenCart());
                updateCartBadge();
            } else {
                LOGGER.warning("btnCart is null - FXML binding may have failed");
            }

            // Add search functionality
            if (searchField != null) {
                searchField.setOnAction(e -> handleSearch());
            }

            LOGGER.info("LandingController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing LandingController", e);
            throw e;
        }
    }

    /**
     * Check if user has an active session and update UI accordingly
     */
    private void checkUserSession() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null && SessionManager.getInstance().isSessionValid()) {
                // User is logged in - show Dashboard and Logout buttons, hide Login
                if (btnLogin != null) {
                    btnLogin.setVisible(false);
                    btnLogin.setManaged(false);
                }

                if (btnDashboard != null) {
                    btnDashboard.setVisible(true);
                    btnDashboard.setManaged(true);
                    btnDashboard.setOnAction(evt -> returnToDashboard());
                }

                if (btnLogout != null) {
                    btnLogout.setVisible(true);
                    btnLogout.setManaged(true);
                    btnLogout.setOnAction(evt -> handleLogout());
                }

                LOGGER.info("User session detected: " + currentUser.getUsername() +
                           " (Role: " + currentUser.getRole() + ") - Showing Dashboard and Logout buttons");
            } else {
                // No active session - show Login button, hide Dashboard and Logout
                if (btnLogin != null) {
                    btnLogin.setVisible(true);
                    btnLogin.setManaged(true);
                    btnLogin.setOnAction(evt -> onOpenLogin());
                }

                if (btnDashboard != null) {
                    btnDashboard.setVisible(false);
                    btnDashboard.setManaged(false);
                }

                if (btnLogout != null) {
                    btnLogout.setVisible(false);
                    btnLogout.setManaged(false);
                }

                LOGGER.info("No active session - showing Login button");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error checking user session", e);
            // On error, default to login button only
            if (btnLogin != null) {
                btnLogin.setVisible(true);
                btnLogin.setManaged(true);
                btnLogin.setOnAction(evt -> onOpenLogin());
            }
            if (btnDashboard != null) {
                btnDashboard.setVisible(false);
                btnDashboard.setManaged(false);
            }
            if (btnLogout != null) {
                btnLogout.setVisible(false);
                btnLogout.setManaged(false);
            }
        }
    }

    /**
     * Return user to their appropriate dashboard based on role
     * Session is preserved - user remains logged in
     */
    private void returnToDashboard() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser == null || !SessionManager.getInstance().isSessionValid()) {
                LOGGER.warning("Session expired or invalid - redirecting to login");
                onOpenLogin();
                return;
            }

            String role = currentUser.getRole();
            String viewPath;
            String viewTitle;

            if ("ADMIN".equalsIgnoreCase(role)) {
                viewPath = "/com/smartcommerce/ui/views/admin/dashboard.fxml";
                viewTitle = "Smart E-Commerce System - Admin Dashboard";
                LOGGER.info("Returning admin user to dashboard: " + currentUser.getUsername());
            } else {
                viewPath = "/com/smartcommerce/ui/views/custormer/customer_dashboard.fxml";
                viewTitle = "Smart E-Commerce System - My Dashboard";
                LOGGER.info("Returning customer user to dashboard: " + currentUser.getUsername());
            }

            // Update activity timestamp
            SessionManager.getInstance().updateActivity();

            // Load appropriate dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Parent dashboardView = loader.load();

            Stage stage = (Stage) btnDashboard.getScene().getWindow();
            Scene scene = new Scene(dashboardView);
            stage.setScene(scene);
            stage.setTitle(viewTitle);

            LOGGER.info("User returned to dashboard successfully - session preserved");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error returning to dashboard", e);
            showNotification("Error",
                "Failed to load dashboard. Please try logging in again.",
                Alert.AlertType.ERROR);
        }
    }

    /**
     * Handle user logout - destroy session and update UI
     */
    @FXML
    private void handleLogout() {
        try {
            // Get username before destroying session
            User currentUser = SessionManager.getInstance().getCurrentUser();
            String username = currentUser != null ? currentUser.getUsername() : "unknown";

            LOGGER.info("User logging out: " + username);

            // Destroy the session
            SessionManager.getInstance().destroySession();

            LOGGER.info("Session destroyed for user: " + username);

            // Update UI to show Login button and hide Dashboard/Logout buttons
            if (btnLogin != null) {
                btnLogin.setVisible(true);
                btnLogin.setManaged(true);
            }

            if (btnDashboard != null) {
                btnDashboard.setVisible(false);
                btnDashboard.setManaged(false);
            }

            if (btnLogout != null) {
                btnLogout.setVisible(false);
                btnLogout.setManaged(false);
            }

            // Show logout confirmation
            showNotification("Logged Out",
                "You have been successfully logged out.",
                Alert.AlertType.INFORMATION);

            LOGGER.info("User logged out successfully, staying on landing page");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during logout", e);
            showNotification("Error",
                "An error occurred during logout.",
                Alert.AlertType.ERROR);
        }
    }

    /**
     * Load products from database and display them asynchronously
     * OPTIMIZED: Non-blocking async operation with loading feedback
     * Limits to first 9 products for performance
     */
    private void loadProductsFromDatabase() {
        LOGGER.info("Starting async product loading...");

        // Initialize loading overlay if mainContent is available
        if (mainContent != null) {
            initializeLoadingOverlay(mainContent, "Loading products...");
        }

        loadDataAsync(
            () -> {
                // Background task: Fetch products from database
                LOGGER.info("Fetching products from database in background...");
                updateProgress("Fetching products...");
                return productService.getAllProducts();
            },
            allProducts -> {
                // UI update: Process and display products
                if (allProducts == null || allProducts.isEmpty()) {
                    LOGGER.warning("No products found in database");
                    showNotification("No Products",
                        "No products available at the moment.",
                        Alert.AlertType.INFORMATION);
                    return;
                }

                // Limit to first 9 products for optimization (3x3 grid)
                int limit = Math.min(allProducts.size(), PRODUCTS_PER_PAGE);
                displayedProducts = allProducts.subList(0, limit);

                LOGGER.info("Loaded " + displayedProducts.size() + " products from database");

                // Display products in grid
                displayProducts();
            },
            throwable -> {
                // Error handling
                LOGGER.log(Level.SEVERE, "Error loading products from database", throwable);
                showNotification("Error",
                    "Failed to load products from database: " + throwable.getMessage(),
                    Alert.AlertType.ERROR);
            }
        );
    }

    /**
     * Display products dynamically in the product grid
     */
    private void displayProducts() {
        if (productGrid == null) {
            LOGGER.warning("Product grid is null, cannot display products");
            return;
        }

        // Clear existing products
        productGrid.getChildren().clear();

        if (displayedProducts == null || displayedProducts.isEmpty()) {
            // Show no products message
            Label noProductsLabel = new Label("No products available");
            noProductsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
            productGrid.add(noProductsLabel, 0, 0);
            return;
        }

        // Display products in grid (3 columns)
        int row = 0;
        int col = 0;

        for (int i = 0; i < displayedProducts.size(); i++) {
            Product product = displayedProducts.get(i);

            // Create product card
            VBox productCard = createProductCard(product);

            // Add to grid
            productGrid.add(productCard, col, row);

            // Update column and row
            col++;
            if (col >= 3) {
                col = 0;
                row++;
            }
        }

        // Format price labels after display
        formatPriceLabels();

        LOGGER.info("Displayed " + displayedProducts.size() + " products in grid");
    }

    /**
     * Create a product card UI component with image and stock information
     */
    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.getStyleClass().add("product-card");
        card.setMaxWidth(Double.MAX_VALUE); // Fill available width
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(10));

        // Product image container with explicit styling
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(150);
        imageContainer.setPrefWidth(200);
        imageContainer.setMinHeight(150);
        imageContainer.setMinWidth(200);
        imageContainer.setMaxHeight(150);
        imageContainer.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #e5e7eb; -fx-border-width: 1px;");

        // Try to load product image or use placeholder
        if (product.getImageUrl() != null && !product.getImageUrl().trim().isEmpty()) {
            String imageUrl = product.getImageUrl().trim();
            LOGGER.info("=== Loading image for: " + product.getProductName());
            LOGGER.info("    URL: " + imageUrl);

            // Determine if URL is web or local and handle accordingly
            boolean isWebUrl = imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
            LOGGER.info("    URL Type: " + (isWebUrl ? "WEB URL (External)" : "Local/Resource"));

            try {
                final Image image;

                if (isWebUrl) {
                    // Web URL - load directly
                    image = new Image(imageUrl, 200, 150, true, true, true);
                } else {
                    // Local file path - try multiple loading strategies
                    image = loadLocalImage(imageUrl, product.getProductName());
                }

                if (image == null) {
                    LOGGER.warning("    ✗ Could not load image, using placeholder");
                    addImagePlaceholder(imageContainer);
                }

                // Check if image creation failed immediately
                if (image != null && image.isError()) {
                    LOGGER.severe("    ✗ Image creation failed immediately!");
                    Exception ex = image.getException();
                    if (ex != null) {
                        LOGGER.severe("    Error: " + ex.getMessage());
                    }
                    addImagePlaceholder(imageContainer);
                } else if (image != null) {
                    // Image loaded successfully - create ImageView
                    final ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageView.setCache(true);

                    // Add to container immediately
                    imageContainer.getChildren().add(imageView);
                    StackPane.setAlignment(imageView, Pos.CENTER);

                    LOGGER.info("    ✓ ImageView created and added");
                    LOGGER.info("    Initial image error state: " + image.isError());
                    LOGGER.info("    Image object created: YES");

                    // Track if placeholder was shown
                    final boolean[] placeholderShown = {false};

                    // Keep final reference for lambda
                    final Image finalImage = image;
                    final String finalImageUrl = imageUrl;

                    // Monitor error property
                    image.errorProperty().addListener((obs, wasError, isError) -> {
                        if (isError && !placeholderShown[0]) {
                            placeholderShown[0] = true;
                            Exception ex = finalImage.getException();
                            LOGGER.severe("    ✗✗✗ IMAGE ERROR DETECTED ✗✗✗");
                            LOGGER.severe("    Product: " + product.getProductName());
                            LOGGER.severe("    URL: " + finalImageUrl);
                            if (ex != null) {
                                LOGGER.severe("    Exception: " + ex.getClass().getName());
                                LOGGER.severe("    Message: " + ex.getMessage());
                                ex.printStackTrace();
                            }
                            javafx.application.Platform.runLater(() -> {
                                if (imageContainer.getChildren().contains(imageView)) {
                                    imageContainer.getChildren().remove(imageView);
                                }
                                addImagePlaceholder(imageContainer);
                            });
                        }
                    });

                    // Monitor progress property
                    image.progressProperty().addListener((obs, oldVal, newVal) -> {
                        double progress = newVal.doubleValue();
                        if (progress >= 1.0 && !placeholderShown[0]) {
                            LOGGER.info("    ✓✓✓ SUCCESS: Image loaded 100% ✓✓✓");
                            LOGGER.info("    Product: " + product.getProductName());
                            LOGGER.info("    Actual size: " + finalImage.getWidth() + "x" + finalImage.getHeight());
                            LOGGER.info("    ImageView visible: " + imageView.isVisible());
                            LOGGER.info("    ImageView in scene: " + (imageView.getScene() != null));
                        } else if (progress > 0 && progress < 1.0) {
                            LOGGER.info("    Loading: " + (int)(progress * 100) + "%");
                        }
                    });
                }

            } catch (Exception e) {
                LOGGER.severe("    ✗ EXCEPTION creating Image:");
                LOGGER.severe("    Type: " + e.getClass().getName());
                LOGGER.severe("    Message: " + e.getMessage());
                e.printStackTrace();
                addImagePlaceholder(imageContainer);
            }
        } else {
            // No image URL - show placeholder
            LOGGER.info("=== No image URL for: " + product.getProductName());
            addImagePlaceholder(imageContainer);
        }

        // Stock badge overlay
        if (product.getStockQuantity() <= 0) {
            Label outOfStockBadge = new Label("OUT OF STOCK");
            outOfStockBadge.setStyle(
                "-fx-background-color: rgba(220, 38, 38, 0.9); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 4 8; " +
                "-fx-font-size: 10px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 3;"
            );
            StackPane.setAlignment(outOfStockBadge, Pos.TOP_RIGHT);
            StackPane.setMargin(outOfStockBadge, new Insets(8));
            imageContainer.getChildren().add(outOfStockBadge);
        } else if (product.getStockQuantity() <= 10) {
            Label lowStockBadge = new Label("LOW STOCK");
            lowStockBadge.setStyle(
                "-fx-background-color: rgba(245, 158, 11, 0.9); " +
                "-fx-text-fill: white; " +
                "-fx-padding: 4 8; " +
                "-fx-font-size: 10px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 3;"
            );
            StackPane.setAlignment(lowStockBadge, Pos.TOP_RIGHT);
            StackPane.setMargin(lowStockBadge, new Insets(8));
            imageContainer.getChildren().add(lowStockBadge);
        }

        // Product name
        Label nameLabel = new Label(product.getProductName());
        nameLabel.getStyleClass().addAll("product-name");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(Double.MAX_VALUE);

        // Price and stock info container
        HBox infoBox = new HBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // Product price
        Label priceLabel = new Label(product.getPrice().toString());
        priceLabel.getStyleClass().add("price");

        // Stock quantity label
        Label stockLabel = new Label();
        if (product.getStockQuantity() > 0) {
            stockLabel.setText(product.getStockQuantity() + " in stock");
            stockLabel.setStyle("-fx-text-fill: #10b981; -fx-font-size: 11px;");
        } else {
            stockLabel.setText("Out of stock");
            stockLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 11px; -fx-font-weight: bold;");
        }

        infoBox.getChildren().addAll(priceLabel, stockLabel);

        // Add to Cart button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.getStyleClass().add("add-to-cart-btn");
        addToCartBtn.setMaxWidth(Double.MAX_VALUE);

        // Disable button if out of stock
        if (product.getStockQuantity() <= 0) {
            addToCartBtn.setDisable(true);
            addToCartBtn.setText("Out of Stock");
            addToCartBtn.setStyle("-fx-background-color: #9ca3af; -fx-text-fill: white;");
        } else {
            // Set event handler for this specific product
            final Product currentProduct = product;
            addToCartBtn.setOnAction(e -> addProductToCart(currentProduct));
        }

        // Add all components to card
        card.getChildren().addAll(imageContainer, nameLabel, infoBox, addToCartBtn);

        return card;
    }

    /**
     * Load local image with multiple fallback strategies
     * Tries: 1) File path from project root, 2) Resource from classpath, 3) Absolute file path
     */
    private Image loadLocalImage(String imagePath, String productName) {
        Image image = null;

        // Strategy 1: Try as file path relative to project root
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists() && imageFile.isFile()) {
                String fileUrl = imageFile.toURI().toString();
                LOGGER.info("    Strategy 1: Loading from file system: " + fileUrl);
                image = new Image(fileUrl, 200, 150, true, true, true);
                if (!image.isError()) {
                    LOGGER.info("    ✓ Successfully loaded from file system");
                    return image;
                }
            } else {
                LOGGER.fine("    Strategy 1: File not found at: " + imageFile.getAbsolutePath());
            }
        } catch (Exception e) {
            LOGGER.fine("    Strategy 1 failed: " + e.getMessage());
        }

        // Strategy 2: Try as resource from classpath
        try {
            // Try loading as resource with leading slash
            String resourcePath = "/" + imagePath;
            InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
            if (resourceStream != null) {
                LOGGER.info("    Strategy 2: Loading from classpath: " + resourcePath);
                image = new Image(resourceStream, 200, 150, true, true);
                resourceStream.close();
                if (!image.isError()) {
                    LOGGER.info("    ✓ Successfully loaded from classpath");
                    return image;
                }
            } else {
                LOGGER.fine("    Strategy 2: Resource not found: " + resourcePath);
            }
        } catch (Exception e) {
            LOGGER.fine("    Strategy 2 failed: " + e.getMessage());
        }

        // Strategy 3: Try without leading slash
        try {
            InputStream resourceStream = getClass().getResourceAsStream(imagePath);
            if (resourceStream != null) {
                LOGGER.info("    Strategy 3: Loading from classpath (no slash): " + imagePath);
                image = new Image(resourceStream, 200, 150, true, true);
                resourceStream.close();
                if (!image.isError()) {
                    LOGGER.info("    ✓ Successfully loaded from classpath");
                    return image;
                }
            }
        } catch (Exception e) {
            LOGGER.fine("    Strategy 3 failed: " + e.getMessage());
        }

        // Strategy 4: Try with "file://" prefix
        try {
            if (!imagePath.startsWith("file://")) {
                String fileUrl = "file:///" + imagePath.replace("\\", "/");
                LOGGER.info("    Strategy 4: Trying with file:// prefix: " + fileUrl);
                image = new Image(fileUrl, 200, 150, true, true, true);
                if (!image.isError()) {
                    LOGGER.info("    ✓ Successfully loaded with file:// prefix");
                    return image;
                }
            }
        } catch (Exception e) {
            LOGGER.fine("    Strategy 4 failed: " + e.getMessage());
        }

        LOGGER.warning("    ✗ All loading strategies failed for: " + imagePath);
        return null;
    }

    /**
     * Add a placeholder image when product image is not available
     */
    private void addImagePlaceholder(StackPane container) {
        VBox placeholder = new VBox(5);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-background-color: #e5e7eb;");
        placeholder.setPrefHeight(150);
        placeholder.setPrefWidth(200);
        placeholder.setMinHeight(150);
        placeholder.setMinWidth(200);

        Label iconLabel = new Label("📦");
        iconLabel.setStyle("-fx-font-size: 48px;");

        Label textLabel = new Label("No Image");
        textLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280; -fx-font-weight: bold;");

        placeholder.getChildren().addAll(iconLabel, textLabel);
        container.getChildren().add(placeholder);
        StackPane.setAlignment(placeholder, Pos.CENTER);
    }

    /**
     * Helper method to add dollar signs to price labels programmatically
     */
    private void formatPriceLabels() {
        if (mainContent != null) {
            formatPriceLabelsRecursive(mainContent);
        }
    }

    private void formatPriceLabelsRecursive(Node node) {
        if (node instanceof Label) {
            Label label = (Label) node;
            if (label.getStyleClass().contains("price")) {
                String currentText = label.getText();
                if (!currentText.startsWith("$")) {
                    label.setText("$" + currentText);
                }
            }
        } else if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                formatPriceLabelsRecursive(child);
            }
        }
    }

    /**
     * Add product to cart and show confirmation
     * Validates stock availability before adding
     */
    private void addProductToCart(Product product) {
        try {
            // Track product view
            trackProductView(product);

            // Check stock availability
            if (product.getStockQuantity() <= 0) {
                showNotification("Out of Stock",
                    product.getProductName() + " is currently out of stock.",
                    Alert.AlertType.WARNING);
                return;
            }

            cartService.addProduct(product);
            updateCartBadge();

            // Show success notification with stock info
            String message = product.getProductName() + " added to cart!\n";
            if (product.getStockQuantity() <= 10) {
                message += "Only " + product.getStockQuantity() + " left in stock!";
            }

            showNotification("Success", message, Alert.AlertType.INFORMATION);

            LOGGER.info("Added to cart: " + product.getProductName() + " (Stock: " + product.getStockQuantity() + ")");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product to cart", e);
            showNotification("Error",
                "Failed to add product to cart",
                Alert.AlertType.ERROR);
        }
    }

    /**
     * Update cart badge with current item count
     */
    private void updateCartBadge() {
        if (btnCart != null) {
            int itemCount = cartService.getTotalItems();
            btnCart.setText("Cart (" + itemCount + ")");
        }
    }

    /**
     * Handle search functionality
     */
    @FXML
    private void handleSearch() {
        try {
            String searchTerm = searchField.getText();
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                LOGGER.info("Searching for: " + searchTerm);

                // Search products in database
                List<Product> searchResults = productService.searchProducts(searchTerm);

                if (searchResults == null || searchResults.isEmpty()) {
                    showNotification("No Results",
                        "No products found matching: " + searchTerm,
                        Alert.AlertType.INFORMATION);
                    return;
                }

                // Limit search results for optimization
                int limit = Math.min(searchResults.size(), PRODUCTS_PER_PAGE);
                displayedProducts = searchResults.subList(0, limit);

                // Display search results
                displayProducts();

                showNotification("Search Results",
                    "Found " + searchResults.size() + " product(s) for: " + searchTerm,
                    Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling search", e);
        }
    }

    @FXML
    private void onShopNow() {
        try {
            LOGGER.info("Shop Now clicked - scrolling to products");
            // Scroll to product grid
            if (productGrid != null && mainContent != null) {
                // Request focus on product grid to scroll it into view
                productGrid.requestFocus();
                showNotification("Products",
                    "Browse our featured products below!",
                    Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling Shop Now action", e);
        }
    }

    @FXML
    private void onOpenCart() {
        try {
            LOGGER.info("Opening cart view");

            if (cartService.isEmpty()) {
                showNotification("Cart Empty",
                    "Your cart is empty. Add some products first!",
                    Alert.AlertType.INFORMATION);
                return;
            }

            // Load cart view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/custormer/cart.fxml"));
            Parent cartView = loader.load();

            Stage stage = (Stage) btnCart.getScene().getWindow();
            Scene scene = new Scene(cartView);
            stage.setScene(scene);
            stage.setTitle("Shopping Cart");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening cart view", e);
            showNotification("Error",
                "Failed to open cart view",
                Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onOpenLogin() {
        try {
            LOGGER.info("Opening login view");

            // Load login view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/login.fxml"));
            Parent loginView = loader.load();

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
            stage.setTitle("Login");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error opening login view", e);
            showNotification("Error",
                "Failed to open login view",
                Alert.AlertType.ERROR);
        }
    }

    /**
     * Show notification dialog
     */
    private void showNotification(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Public getter for search field
     */
    public TextField getSearchField() {
        return searchField;
    }

    /**
     * Public getter for main content container
     */
    public VBox getMainContent() {
        return mainContent;
    }

    /**
     * Refresh products - reload from database
     */
    public void refreshProducts() {
        loadProductsFromDatabase();
    }

    /**
     * Load Categories Section - Display max 6 categories with filtering capability
     */
    private void loadCategoriesSection() {
        try {
            if (categoriesContainer == null) {
                LOGGER.warning("categoriesContainer is null - FXML binding may have failed");
                return;
            }

            // Clear existing categories
            categoriesContainer.getChildren().clear();

            // Fetch categories from database
            List<Category> categories = categoryDAO.findAll();

            if (categories == null || categories.isEmpty()) {
                LOGGER.warning("No categories found in database");
                if (categoriesSection != null) {
                    categoriesSection.setVisible(false);
                    categoriesSection.setManaged(false);
                }
                return;
            }

            // Limit to MAX_CATEGORIES_DISPLAY
            int limit = Math.min(categories.size(), MAX_CATEGORIES_DISPLAY);
            List<Category> displayCategories = categories.subList(0, limit);

            // Create category cards
            for (Category category : displayCategories) {
                VBox categoryCard = createCategoryCard(category);
                categoriesContainer.getChildren().add(categoryCard);
            }

            // Add "View All Categories" button if more categories exist
            if (categories.size() > MAX_CATEGORIES_DISPLAY) {
                Button viewAllBtn = new Button("View All Categories");
                viewAllBtn.getStyleClass().add("view-all-btn");
                viewAllBtn.setOnAction(e -> handleViewAllCategories());
                categoriesContainer.getChildren().add(viewAllBtn);
            }

            // Show the section
            if (categoriesSection != null) {
                categoriesSection.setVisible(true);
                categoriesSection.setManaged(true);
            }

            LOGGER.info("Loaded " + displayCategories.size() + " categories");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading categories section", e);
        }
    }

    /**
     * Create a category card UI component
     */
    private VBox createCategoryCard(Category category) {
        VBox card = new VBox(8);
        card.getStyleClass().add("category-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setMinWidth(120);
        card.setMaxWidth(150);

        // Category icon/emoji (simple text-based)
        Label iconLabel = new Label(getCategoryIcon(category.getCategoryName()));
        iconLabel.setStyle("-fx-font-size: 36px;");

        // Category name
        Label nameLabel = new Label(category.getCategoryName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(130);
        nameLabel.setAlignment(Pos.CENTER);

        // Add click handler
        card.setOnMouseClicked(e -> handleCategoryClick(category));
        card.setStyle(card.getStyle() + "-fx-cursor: hand;");

        card.getChildren().addAll(iconLabel, nameLabel);

        return card;
    }

    /**
     * Get icon/emoji for category (simple mapping)
     */
    private String getCategoryIcon(String categoryName) {
        String name = categoryName.toLowerCase();
        if (name.contains("electron") || name.contains("tech")) return "💻";
        if (name.contains("fashion") || name.contains("cloth")) return "👕";
        if (name.contains("book")) return "📚";
        if (name.contains("home") || name.contains("garden")) return "🏠";
        if (name.contains("sport")) return "⚽";
        if (name.contains("food") || name.contains("grocery")) return "🍎";
        if (name.contains("toy")) return "🧸";
        if (name.contains("beauty") || name.contains("health")) return "💄";
        return "🏷️"; // Default
    }

    /**
     * Handle category click - filter products by category
     */
    private void handleCategoryClick(Category category) {
        try {
            LOGGER.info("Category clicked: " + category.getCategoryName());
            currentCategoryFilter = category.getCategoryId();

            // Fetch products by category
            List<Product> categoryProducts = productService.getProductsByCategory(category.getCategoryId());

            if (categoryProducts == null || categoryProducts.isEmpty()) {
                showNotification("No Products",
                    "No products found in " + category.getCategoryName(),
                    Alert.AlertType.INFORMATION);
                return;
            }

            // Limit for optimization
            int limit = Math.min(categoryProducts.size(), PRODUCTS_PER_PAGE);
            displayedProducts = categoryProducts.subList(0, limit);

            // Display filtered products
            displayProducts();

            showNotification("Category Filter",
                "Showing " + categoryProducts.size() + " product(s) in " + category.getCategoryName(),
                Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling category click", e);
        }
    }

    /**
     * Handle "View All Categories" button click
     */
    private void handleViewAllCategories() {
        try {
            LOGGER.info("View All Categories clicked");

            // Fetch all categories
            List<Category> allCategories = categoryDAO.findAll();

            // Create dialog or show all categories
            StringBuilder message = new StringBuilder("All Categories:\n\n");
            for (Category cat : allCategories) {
                message.append("• ").append(cat.getCategoryName()).append("\n");
            }

            showNotification("All Categories", message.toString(), Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error viewing all categories", e);
        }
    }

    /**
     * Load Recently Viewed Section - Display products recently viewed by user
     * Visible only to logged-in users
     */
    private void loadRecentlyViewedSection() {
        try {
            if (recentlyViewedContainer == null) {
                LOGGER.warning("recentlyViewedContainer is null - FXML binding may have failed");
                return;
            }

            // Clear existing content
            recentlyViewedContainer.getChildren().clear();

            // Check if user is logged in
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser == null || !SessionManager.getInstance().isSessionValid()) {
                // Hide section for guests
                if (recentlyViewedSection != null) {
                    recentlyViewedSection.setVisible(false);
                    recentlyViewedSection.setManaged(false);
                }
                return;
            }

            // Get recently viewed products for this user
            List<Product> recentlyViewed = ViewedProductsTracker.getRecentlyViewedProducts(
                currentUser.getUserId(), MAX_RECENTLY_VIEWED, productService
            );

            if (recentlyViewed == null || recentlyViewed.isEmpty()) {
                // Hide section if no viewed products
                if (recentlyViewedSection != null) {
                    recentlyViewedSection.setVisible(false);
                    recentlyViewedSection.setManaged(false);
                }
                return;
            }

            // Create product cards for recently viewed
            for (Product product : recentlyViewed) {
                VBox productCard = createCompactProductCard(product);
                recentlyViewedContainer.getChildren().add(productCard);
            }

            // Show the section
            if (recentlyViewedSection != null) {
                recentlyViewedSection.setVisible(true);
                recentlyViewedSection.setManaged(true);
            }

            LOGGER.info("Loaded " + recentlyViewed.size() + " recently viewed products");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading recently viewed section", e);
        }
    }

    /**
     * Load Top Purchased Section - Display products with highest purchase frequency
     * Visible to all users
     */
    private void loadTopPurchasedSection() {
        try {
            if (topPurchasedContainer == null) {
                LOGGER.warning("topPurchasedContainer is null - FXML binding may have failed");
                return;
            }

            // Clear existing content
            topPurchasedContainer.getChildren().clear();

            // Get top purchased products
            List<Product> topPurchased = productService.getTopPurchasedProducts(MAX_TOP_PURCHASED);

            if (topPurchased == null || topPurchased.isEmpty()) {
                // Hide section if no purchased products
                if (topPurchasedSection != null) {
                    topPurchasedSection.setVisible(false);
                    topPurchasedSection.setManaged(false);
                }
                return;
            }

            // Create product cards for top purchased
            for (Product product : topPurchased) {
                VBox productCard = createCompactProductCard(product);
                topPurchasedContainer.getChildren().add(productCard);
            }

            // Show the section
            if (topPurchasedSection != null) {
                topPurchasedSection.setVisible(true);
                topPurchasedSection.setManaged(true);
            }

            LOGGER.info("Loaded " + topPurchased.size() + " top purchased products");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading top purchased section", e);
        }
    }

    /**
     * Create a compact product card for horizontal sections
     */
    private VBox createCompactProductCard(Product product) {
        VBox card = new VBox(6);
        card.getStyleClass().add("compact-product-card");
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(10));
        card.setMinWidth(150);
        card.setMaxWidth(180);

        // Product image container
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(120);
        imageContainer.setPrefWidth(150);
        imageContainer.setMinHeight(120);
        imageContainer.setMinWidth(150);
        imageContainer.setMaxHeight(120);
        imageContainer.setStyle("-fx-background-color: #f3f4f6; -fx-border-color: #e5e7eb; -fx-border-width: 1px;");

        // Try to load product image
        if (product.getImageUrl() != null && !product.getImageUrl().trim().isEmpty()) {
            String imageUrl = product.getImageUrl().trim();
            boolean isWebUrl = imageUrl.startsWith("http://") || imageUrl.startsWith("https://");

            try {
                final Image image;
                if (isWebUrl) {
                    image = new Image(imageUrl, 150, 120, true, true, true);
                } else {
                    image = loadLocalImage(imageUrl, product.getProductName());
                }

                if (image != null && !image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(120);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageContainer.getChildren().add(imageView);
                } else {
                    addImagePlaceholder(imageContainer);
                }
            } catch (Exception e) {
                addImagePlaceholder(imageContainer);
            }
        } else {
            addImagePlaceholder(imageContainer);
        }

        // Product name (truncated)
        Label nameLabel = new Label(product.getProductName());
        nameLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #1f2937;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(150);
        nameLabel.setMaxHeight(35);

        // Price
        Label priceLabel = new Label("$" + product.getPrice().toString());
        priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #059669;");

        // Add click handler to track view and show details
        card.setOnMouseClicked(e -> {
            trackProductView(product);
            addProductToCart(product);
        });
        card.setStyle(card.getStyle() + "-fx-cursor: hand;");

        card.getChildren().addAll(imageContainer, nameLabel, priceLabel);

        return card;
    }

    /**
     * Track product view for logged-in users
     */
    private void trackProductView(Product product) {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null && SessionManager.getInstance().isSessionValid()) {
                ViewedProductsTracker.addViewedProduct(currentUser.getUserId(), product.getProductId());
                LOGGER.info("Tracked view for product: " + product.getProductName() + " by user: " + currentUser.getUsername());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error tracking product view", e);
        }
    }
}


