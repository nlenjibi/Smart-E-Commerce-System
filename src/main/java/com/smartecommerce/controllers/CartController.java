package com.smartecommerce.controllers;

import com.smartcommerce.app.SessionManager;
import com.smartcommerce.model.CartItem;
import com.smartcommerce.model.Order;
import com.smartcommerce.service.CartService;
import com.smartcommerce.service.OrderService;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CartController - Optimized controller for the shopping cart.
 *
 * OPTIMIZATIONS:
 * - UI caching to prevent full reloads on quantity changes
 * - Async operations for better responsiveness
 * - Memory-efficient event handling
 * - Smooth animations for better UX
 * - Debouncing for rapid clicks
 * - Configurable tax rate
 * - Better error handling
 *
 * Responsibilities:
 * - Display cart items dynamically from CartService
 * - Handle quantity updates (increment/decrement) with optimized rendering
 * - Remove items from cart with animations
 * - Calculate totals (subtotal, tax, total)
 * - Navigate to checkout or back to landing page
 * - Clear cart functionality
 */
public class CartController {

    private static final Logger LOGGER = Logger.getLogger(CartController.class.getName());
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10% tax - configurable
    private static final int DEBOUNCE_DELAY_MS = 300; // Debounce delay for rapid clicks
    private static final int ANIMATION_DURATION_MS = 200;

    @FXML private VBox cartItemsContainer;
    @FXML private VBox emptyCartMessage;
    @FXML private Label lblItemCount;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTax;
    @FXML private Label lblTotal;
    @FXML private Button btnCheckout;
    @FXML private Button btnClearCart;
    @FXML private Button btnHome;
    @FXML private Button btnContinueShopping;

    private CartService cartService;
    private OrderService orderService;

    // Cache UI components to avoid recreating on every update
    private final Map<Integer, VBox> cartItemCards = new HashMap<>();
    private final Map<Integer, Label> quantityLabels = new HashMap<>();
    private final Map<Integer, Label> subtotalLabels = new HashMap<>();

    // Debounce timer for preventing rapid operations
    private final Map<Integer, Long> lastUpdateTime = new HashMap<>();

    public CartController() {
        this.cartService = CartService.getInstance();
        this.orderService = new OrderService();
    }

    @FXML
    private void initialize() {
        try {
            LOGGER.info("Initializing CartController...");

            // Setup event handlers
            setupEventHandlers();

            // Load cart items asynchronously for better performance
            loadCartItemsAsync();

            LOGGER.info("CartController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing CartController", e);
            showNotification("Initialization Error", "Failed to initialize cart view", Alert.AlertType.ERROR);
        }
    }

    /**
     * Setup all event handlers
     */
    private void setupEventHandlers() {
        if (btnCheckout != null) {
            btnCheckout.setOnAction(e -> onCheckout());
        }

        if (btnClearCart != null) {
            btnClearCart.setOnAction(e -> onClearCart());
        }

        if (btnHome != null) {
            btnHome.setOnAction(e -> onGoHome());
        }

        if (btnContinueShopping != null) {
            btnContinueShopping.setOnAction(e -> onGoHome());
        }
    }

    /**
     * Load cart items asynchronously to prevent UI blocking
     */
    private void loadCartItemsAsync() {
        CompletableFuture.runAsync(() -> {
            try {
                List<CartItem> cartItems = cartService.getCartItems();

                Platform.runLater(() -> {
                    if (cartItems.isEmpty()) {
                        showEmptyCart();
                    } else {
                        hideEmptyCart();
                        renderCartItems(cartItems);
                        updateOrderSummary();
                    }
                    updateItemCount();
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error loading cart items asynchronously", e);
                Platform.runLater(() ->
                    showNotification("Error", "Failed to load cart items", Alert.AlertType.ERROR)
                );
            }
        });
    }

    /**
     * Load and display all cart items (kept for backwards compatibility)
     */
    private void loadCartItems() {
        loadCartItemsAsync();
    }

    /**
     * Render cart items with caching for better performance
     * Only creates new UI components when needed
     */
    private void renderCartItems(List<CartItem> cartItems) {
        // Track which product IDs are currently in cart
        Map<Integer, CartItem> currentItems = new HashMap<>();
        for (CartItem item : cartItems) {
            currentItems.put(item.getProduct().getProductId(), item);
        }

        // Remove cards for items no longer in cart
        cartItemCards.keySet().removeIf(productId -> {
            if (!currentItems.containsKey(productId)) {
                VBox card = cartItemCards.get(productId);
                animateRemoval(card, () -> cartItemsContainer.getChildren().remove(card));
                quantityLabels.remove(productId);
                subtotalLabels.remove(productId);
                return true;
            }
            return false;
        });

        // Clear container and re-add in correct order
        cartItemsContainer.getChildren().clear();

        for (CartItem item : cartItems) {
            int productId = item.getProduct().getProductId();
            VBox card;

            if (cartItemCards.containsKey(productId)) {
                // Use cached card and update values
                card = cartItemCards.get(productId);
                updateCartItemCard(item);
            } else {
                // Create new card
                card = createCartItemCard(item);
                cartItemCards.put(productId, card);
                animateAddition(card);
            }

            cartItemsContainer.getChildren().add(card);
        }
    }

    /**
     * Update existing cart item card without recreating it
     */
    private void updateCartItemCard(CartItem cartItem) {
        int productId = cartItem.getProduct().getProductId();

        // Update quantity label
        Label quantityLabel = quantityLabels.get(productId);
        if (quantityLabel != null) {
            quantityLabel.setText(String.valueOf(cartItem.getQuantity()));
            animateQuantityChange(quantityLabel);
        }

        // Update subtotal label
        Label subtotalLabel = subtotalLabels.get(productId);
        if (subtotalLabel != null) {
            subtotalLabel.setText("Subtotal: $" + formatPrice(cartItem.getSubtotal()));
            animateQuantityChange(subtotalLabel);
        }
    }

    /**
     * Create a cart item card UI component with caching and image loading
     */
    private VBox createCartItemCard(CartItem cartItem) {
        int productId = cartItem.getProduct().getProductId();

        VBox card = new VBox(12);
        card.getStyleClass().add("cart-item");
        card.setPadding(new Insets(16));

        // Top section: Image and product info
        HBox topSection = new HBox(16);
        topSection.setAlignment(Pos.CENTER_LEFT);

        // Product image container with actual image loading
        StackPane imageContainer = new StackPane();
        imageContainer.setPrefSize(80, 80);
        imageContainer.setMinSize(80, 80);
        imageContainer.setMaxSize(80, 80);
        imageContainer.setStyle("-fx-background-color: #e5e7eb; -fx-background-radius: 8px;");

        // Try to load product image
        String imageUrl = cartItem.getProduct().getImageUrl();
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            try {
                boolean isWebUrl = imageUrl.startsWith("http://") || imageUrl.startsWith("https://");
                final Image image;

                if (isWebUrl) {
                    image = new Image(imageUrl, 80, 80, true, true, true);
                } else {
                    image = loadLocalImage(imageUrl);
                }

                if (image != null && !image.isError()) {
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(80);
                    imageView.setFitHeight(80);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageContainer.getChildren().add(imageView);
                    StackPane.setAlignment(imageView, Pos.CENTER);
                } else {
                    addImagePlaceholder(imageContainer);
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to load image for product: " + cartItem.getProduct().getProductName());
                addImagePlaceholder(imageContainer);
            }
        } else {
            addImagePlaceholder(imageContainer);
        }

        // Product info
        VBox productInfo = new VBox(6);
        productInfo.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(productInfo, Priority.ALWAYS);

        Label productName = new Label(cartItem.getProduct().getProductName());
        productName.getStyleClass().add("product-name");
        productName.setWrapText(true);

        Label productPrice = new Label("$" + formatPrice(cartItem.getProduct().getPrice()));
        productPrice.getStyleClass().add("product-price");

        productInfo.getChildren().addAll(productName, productPrice);

        topSection.getChildren().addAll(imageContainer, productInfo);

        // Bottom section: Quantity controls and subtotal
        HBox bottomSection = new HBox(16);
        bottomSection.setAlignment(Pos.CENTER_LEFT);

        // Quantity controls
        HBox quantityControls = new HBox(8);
        quantityControls.setAlignment(Pos.CENTER_LEFT);
        quantityControls.getStyleClass().add("quantity-controls");

        Button btnDecrement = new Button("-");
        btnDecrement.getStyleClass().add("quantity-button");
        btnDecrement.setOnAction(e -> decrementQuantityOptimized(cartItem));

        Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
        quantityLabel.getStyleClass().add("quantity-label");
        quantityLabels.put(productId, quantityLabel); // Cache the label

        Button btnIncrement = new Button("+");
        btnIncrement.getStyleClass().add("quantity-button");
        btnIncrement.setOnAction(e -> incrementQuantityOptimized(cartItem));

        quantityControls.getChildren().addAll(btnDecrement, quantityLabel, btnIncrement);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Subtotal
        Label subtotalLabel = new Label("Subtotal: $" + formatPrice(cartItem.getSubtotal()));
        subtotalLabel.getStyleClass().add("product-price");
        subtotalLabels.put(productId, subtotalLabel); // Cache the label

        // Remove button
        Button btnRemove = new Button("Remove");
        btnRemove.getStyleClass().add("remove-button");
        btnRemove.setOnAction(e -> removeItemOptimized(cartItem));

        bottomSection.getChildren().addAll(quantityControls, spacer, subtotalLabel, btnRemove);

        card.getChildren().addAll(topSection, bottomSection);

        return card;
    }

    /**
     * Load local image with multiple fallback strategies
     */
    private Image loadLocalImage(String imagePath) {
        Image image = null;

        LOGGER.fine("Attempting to load local image: " + imagePath);

        // Strategy 1: Try as file path relative to project root
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists() && imageFile.isFile()) {
                String fileUrl = imageFile.toURI().toString();
                LOGGER.fine("Strategy 1: Loading from file system: " + fileUrl);
                image = new Image(fileUrl, 80, 80, true, true, true);
                if (!image.isError()) {
                    LOGGER.fine("✓ Successfully loaded from file system");
                    return image;
                }
            } else {
                LOGGER.fine("Strategy 1: File not found at: " + imageFile.getAbsolutePath());
            }
        } catch (Exception e) {
            LOGGER.fine("Strategy 1 failed: " + e.getMessage());
        }

        // Strategy 2: Try as resource from classpath with leading slash
        try {
            String resourcePath = "/" + imagePath;
            LOGGER.fine("Strategy 2: Trying classpath resource: " + resourcePath);
            InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
            if (resourceStream != null) {
                image = new Image(resourceStream, 80, 80, true, true);
                resourceStream.close();
                if (!image.isError()) {
                    LOGGER.fine("✓ Successfully loaded from classpath (with /)");
                    return image;
                }
            }
        } catch (Exception e) {
            LOGGER.fine("Strategy 2 failed: " + e.getMessage());
        }

        // Strategy 3: Try without leading slash
        try {
            LOGGER.fine("Strategy 3: Trying classpath resource: " + imagePath);
            InputStream resourceStream = getClass().getResourceAsStream(imagePath);
            if (resourceStream != null) {
                image = new Image(resourceStream, 80, 80, true, true);
                resourceStream.close();
                if (!image.isError()) {
                    LOGGER.fine("✓ Successfully loaded from classpath (no /)");
                    return image;
                }
            }
        } catch (Exception e) {
            LOGGER.fine("Strategy 3 failed: " + e.getMessage());
        }

        // Strategy 4: Try with file:// prefix
        try {
            if (!imagePath.startsWith("file://")) {
                String fileUrl = "file:///" + imagePath.replace("\\", "/");
                LOGGER.fine("Strategy 4: Trying file URL: " + fileUrl);
                image = new Image(fileUrl, 80, 80, true, true, true);
                if (!image.isError()) {
                    LOGGER.fine("✓ Successfully loaded with file:// prefix");
                    return image;
                }
            }
        } catch (Exception e) {
            LOGGER.fine("Strategy 4 failed: " + e.getMessage());
        }

        // Strategy 5: Try target/classes path (for Maven/Gradle builds)
        try {
            String targetPath = "target/classes/" + imagePath;
            File targetFile = new File(targetPath);
            if (targetFile.exists() && targetFile.isFile()) {
                String fileUrl = targetFile.toURI().toString();
                LOGGER.fine("Strategy 5: Loading from target/classes: " + fileUrl);
                image = new Image(fileUrl, 80, 80, true, true, true);
                if (!image.isError()) {
                    LOGGER.fine("✓ Successfully loaded from target/classes");
                    return image;
                }
            }
        } catch (Exception e) {
            LOGGER.fine("Strategy 5 failed: " + e.getMessage());
        }

        LOGGER.warning("All image loading strategies failed for: " + imagePath);
        return null;
    }

    /**
     * Add a placeholder when image cannot be loaded
     */
    private void addImagePlaceholder(StackPane container) {
        Label iconLabel = new Label("📦");
        iconLabel.setStyle("-fx-font-size: 32px;");
        container.getChildren().add(iconLabel);
        StackPane.setAlignment(iconLabel, Pos.CENTER);
    }

    /**
     * Optimized increment quantity with debouncing and minimal UI updates
     */
    private void incrementQuantityOptimized(CartItem cartItem) {
        int productId = cartItem.getProduct().getProductId();

        // Debouncing: prevent rapid fire clicks
        if (!shouldProcessUpdate(productId)) {
            return;
        }

        cartItem.incrementQuantity();
        updateCartItemCard(cartItem);
        updateOrderSummary();
        updateItemCount();

        LOGGER.info("Incremented quantity for: " + cartItem.getProduct().getProductName());
    }

    /**
     * Optimized decrement quantity with debouncing and minimal UI updates
     */
    private void decrementQuantityOptimized(CartItem cartItem) {
        int productId = cartItem.getProduct().getProductId();

        // Debouncing: prevent rapid fire clicks
        if (!shouldProcessUpdate(productId)) {
            return;
        }

        if (cartItem.getQuantity() > 1) {
            cartItem.decrementQuantity();
            updateCartItemCard(cartItem);
            updateOrderSummary();
            updateItemCount();
            LOGGER.info("Decremented quantity for: " + cartItem.getProduct().getProductName());
        } else {
            // If quantity is 1, ask to remove
            removeItemOptimized(cartItem);
        }
    }

    /**
     * Optimized remove item with animation
     */
    private void removeItemOptimized(CartItem cartItem) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Item");
        confirm.setHeaderText(null);
        confirm.setContentText("Remove " + cartItem.getProduct().getProductName() + " from cart?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int productId = cartItem.getProduct().getProductId();
                cartService.removeProduct(productId);

                // Reload asynchronously
                loadCartItemsAsync();

                showNotification("Success", "Item removed from cart", Alert.AlertType.INFORMATION);
                LOGGER.info("Removed from cart: " + cartItem.getProduct().getProductName());
            }
        });
    }

    /**
     * Check if enough time has passed since last update (debouncing)
     */
    private boolean shouldProcessUpdate(int productId) {
        long currentTime = System.currentTimeMillis();
        Long lastTime = lastUpdateTime.get(productId);

        if (lastTime == null || (currentTime - lastTime) >= DEBOUNCE_DELAY_MS) {
            lastUpdateTime.put(productId, currentTime);
            return true;
        }

        return false;
    }

    /**
     * Legacy increment method (kept for backwards compatibility)
     */
    private void incrementQuantity(CartItem cartItem) {
        incrementQuantityOptimized(cartItem);
    }

    /**
     * Legacy decrement method (kept for backwards compatibility)
     */
    private void decrementQuantity(CartItem cartItem) {
        decrementQuantityOptimized(cartItem);
    }

    /**
     * Legacy remove method (kept for backwards compatibility)
     */
    private void removeItem(CartItem cartItem) {
        removeItemOptimized(cartItem);
    }

    /**
     * Update order summary (subtotal, tax, total)
     */
    private void updateOrderSummary() {
        BigDecimal subtotal = cartService.getTotalPrice();
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        BigDecimal total = subtotal.add(tax);

        if (lblSubtotal != null) {
            lblSubtotal.setText("$" + formatPrice(subtotal));
        }

        if (lblTax != null) {
            lblTax.setText("$" + formatPrice(tax));
        }

        if (lblTotal != null) {
            lblTotal.setText("$" + formatPrice(total));
        }
    }

    /**
     * Format price with proper decimal places
     */
    private String formatPrice(BigDecimal price) {
        return price.setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * Animate addition of a new cart item
     */
    private void animateAddition(VBox card) {
        card.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(ANIMATION_DURATION_MS), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Animate removal of a cart item
     */
    private void animateRemoval(VBox card, Runnable onComplete) {
        FadeTransition fade = new FadeTransition(Duration.millis(ANIMATION_DURATION_MS), card);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            if (onComplete != null) {
                onComplete.run();
            }
        });
        fade.play();
    }

    /**
     * Animate quantity change with scale effect
     */
    private void animateQuantityChange(Label label) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(150), label);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    /**
     * Update item count label
     */
    private void updateItemCount() {
        if (lblItemCount != null) {
            int count = cartService.getTotalItems();
            lblItemCount.setText(count + (count == 1 ? " item" : " items"));
        }
    }

    /**
     * Show empty cart message
     */
    private void showEmptyCart() {
        if (cartItemsContainer != null) {
            cartItemsContainer.setVisible(false);
            cartItemsContainer.setManaged(false);
        }

        if (emptyCartMessage != null) {
            emptyCartMessage.setVisible(true);
            emptyCartMessage.setManaged(true);
        }

        if (btnCheckout != null) {
            btnCheckout.setDisable(true);
        }

        if (btnClearCart != null) {
            btnClearCart.setDisable(true);
        }

        // Set summary to zero
        updateOrderSummary();
    }

    /**
     * Hide empty cart message
     */
    private void hideEmptyCart() {
        if (cartItemsContainer != null) {
            cartItemsContainer.setVisible(true);
            cartItemsContainer.setManaged(true);
        }

        if (emptyCartMessage != null) {
            emptyCartMessage.setVisible(false);
            emptyCartMessage.setManaged(false);
        }

        if (btnCheckout != null) {
            btnCheckout.setDisable(false);
        }

        if (btnClearCart != null) {
            btnClearCart.setDisable(false);
        }
    }

    /**
     * Clear cart handler
     */
    @FXML
    private void onClearCart() {
        if (cartService.isEmpty()) {
            showNotification("Cart Empty", "Your cart is already empty", Alert.AlertType.INFORMATION);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear Cart");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to clear all items from your cart?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                cartService.clearCart();
                loadCartItems();
                showNotification("Success", "Cart cleared successfully", Alert.AlertType.INFORMATION);
                LOGGER.info("Cart cleared");
            }
        });
    }

    /**
     * Calculate total amount including tax
     */
    private BigDecimal calculateTotal() {
        BigDecimal subtotal = cartService.getTotalPrice();
        BigDecimal tax = subtotal.multiply(TAX_RATE);
        BigDecimal total = subtotal.add(tax);
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Checkout handler - Creates order when user is authenticated
     */
    @FXML
    private void onCheckout() {
        boolean authenticated = SessionManager.getInstance().isAuthenticated();

        if (!authenticated) {
            // Redirect to login
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Required");
            alert.setHeaderText(null);
            alert.setContentText("Please login to proceed with checkout");
            alert.showAndWait();

            navigateToLogin();
        } else {
            // Check if cart is empty
            List<CartItem> cartItems = cartService.getCartItems();
            if (cartItems.isEmpty()) {
                showNotification("Empty Cart",
                    "Your cart is empty. Add some items before checking out.",
                    Alert.AlertType.WARNING);
                return;
            }

            // Calculate total amount
            BigDecimal totalAmount = calculateTotal();

            // Get current user ID
            Integer userId = SessionManager.getInstance().getUserId();

            if (userId == null) {
                showNotification("Error",
                    "Unable to identify user. Please login again.",
                    Alert.AlertType.ERROR);
                return;
            }

            // Create order
            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus("PENDING");

            // Show confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Order");
            confirmAlert.setHeaderText("Complete Your Purchase");
            confirmAlert.setContentText(
                "Total Amount: $" + formatPrice(totalAmount) + "\n" +
                "Items: " + cartItems.size() + "\n\n" +
                "Do you want to complete this order?"
            );

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    processCheckout(order, cartItems);
                }
            });
        }
    }

    /**
     * Process the checkout - create order and clear cart
     */
    private void processCheckout(Order order, List<CartItem> cartItems) {
        try {
            LOGGER.info("Processing checkout for user: " + order.getUserId());

            // Create the order in database
            boolean orderCreated = orderService.createOrder(order);

            if (orderCreated) {
                LOGGER.info("Order created successfully with ID: " + order.getOrderId());

                // Clear the cart after successful order
                cartService.clearCart();

                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Order Successful");
                successAlert.setHeaderText("Thank You for Your Order!");
                successAlert.setContentText(
                    "Order ID: " + order.getOrderId() + "\n" +
                    "Total: $" + formatPrice(order.getTotalAmount()) + "\n" +
                    "Status: " + order.getStatus() + "\n\n" +
                    "You will receive a confirmation email shortly."
                );

                successAlert.showAndWait();

                // Refresh the cart display
                loadCartItemsAsync();

                // Navigate to home/landing page
                Platform.runLater(this::onGoHome);

            } else {
                LOGGER.severe("Failed to create order");
                showNotification("Order Failed",
                    "Unable to process your order. Please try again.",
                    Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing checkout", e);
            showNotification("Checkout Error",
                "An error occurred while processing your order: " + e.getMessage(),
                Alert.AlertType.ERROR);
        }
    }

    /**
     * Navigate to home/landing page with smooth transition
     */
    @FXML
    private void onGoHome() {
        try {
            LOGGER.info("Navigating to landing page");

            // Load landing page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/landing.fxml"));
            Parent landingView = loader.load();

            // Get current stage
            Stage stage = (Stage) btnHome.getScene().getWindow();

            // Create new scene
            Scene scene = new Scene(landingView);

            // Add fade transition for smooth navigation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), btnHome.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                stage.setScene(scene);
                stage.setTitle("SmartStore - Home");

                // Fade in the new scene
                landingView.setOpacity(0.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(150), landingView);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();

            LOGGER.info("Successfully navigated to landing page");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error navigating to landing page", e);
            showNotification("Navigation Error",
                "Unable to return to home page. Please try again.",
                Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during navigation", e);
            showNotification("Error",
                "An unexpected error occurred. Please restart the application.",
                Alert.AlertType.ERROR);
        }
    }

    /**
     * Navigate to login page with smooth transition
     */
    private void navigateToLogin() {
        try {
            LOGGER.info("Navigating to login page");

            // Load login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/login.fxml"));
            Parent loginView = loader.load();

            // Get current stage
            Stage stage = (Stage) btnCheckout.getScene().getWindow();

            // Create new scene
            Scene scene = new Scene(loginView);

            // Add fade transition for smooth navigation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), btnCheckout.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(event -> {
                stage.setScene(scene);
                stage.setTitle("Login - SmartStore");

                // Fade in the new scene
                loginView.setOpacity(0.0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(150), loginView);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();

            LOGGER.info("Successfully navigated to login page");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error navigating to login page", e);
            showNotification("Navigation Error",
                "Unable to open login page. Please try again.",
                Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during navigation", e);
            showNotification("Error",
                "An unexpected error occurred. Please restart the application.",
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
}
