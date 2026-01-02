package com.smartecommerce.controllers;

import com.smartcommerce.app.SessionManager;
import com.smartcommerce.dao.UserDAO;
import com.smartcommerce.model.Order;
import com.smartcommerce.model.Product;
import com.smartcommerce.model.User;
import com.smartcommerce.service.OrderService;
import com.smartcommerce.service.ProductService;
import com.smartcommerce.utils.AsyncTaskManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * DashboardController - main controller for the admin dashboard UI.
 *
 * Security Rule: ADMIN ONLY ACCESS - Customers must never access this view
 *
 * Responsibilities:
 * - Enforce admin-only access restriction
 * - Initialize topbar, sidebar, and default center content
 * - Connect sub-controllers for navigation and theme management
 * - Provide hooks for opening modals and switching views
 * - Manage theme switching and sidebar collapse via style class toggles
 *
 * OPTIMIZED: Uses AsyncTaskManager for non-blocking data loading
 */
public class DashboardController extends BaseController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    private static final String FX_CONTROLLER = "fxController";
    private static final String SIDEBAR_COLLAPSED = "sidebar-collapsed";
    private static final String DARK_MODE = "dark-mode";
    private static final String ADMIN_PATH= "/com/smartcommerce/ui/views/custormer/customer_dashboard.fxml";

    @FXML public BorderPane root;
    @FXML public StackPane mainStack;
    @FXML public AnchorPane modalContainer;
    @FXML public VBox contentVBox;
    @FXML public Label ordersValue;
    @FXML public Label revenueValue;
    @FXML public Label productsValue;
    @FXML public Label usersValue;
    @FXML public Label quickInsight;

    // Additional UI elements referenced from FXML
    @FXML public Label pageTitle;
    @FXML public Label adminNameLabel;
    @FXML public Label adminRoleLabel;
    @FXML public javafx.scene.layout.HBox summaryCards;

    // Chart containers
    @FXML public StackPane salesChartContainer;
    @FXML public StackPane ordersChartContainer;
    @FXML public StackPane categoriesChartContainer;
    @FXML public StackPane userGrowthChartContainer;


    // Sub-controller references
    private SidebarController sidebarController;
    private TopbarController topbarController;

    // Services for loading real data
    private final OrderService orderService;
    private final ProductService productService;
    private final UserDAO userDAO;

    public DashboardController() {
        this.orderService = new OrderService();
        this.productService = new ProductService();
        this.userDAO = new UserDAO();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Security Check: Verify admin access
        if (!checkAdminAccess()) {
            LOGGER.severe("Access denied: Non-admin user attempted to access admin dashboard");
            showAccessDeniedAndRedirect();
            return;
        }

        // Initialize loading overlay
        if (mainStack != null) {
            initializeLoadingOverlay(mainStack, "Loading dashboard data...");
        }

        // Load real data from database asynchronously
        loadRealDashboardDataAsync();

        // Connect sub-controllers after a short delay to ensure FXML is fully loaded
        Platform.runLater(this::connectSubControllers);

        LOGGER.info("Admin Dashboard initialized successfully with async data loading");
    }

    /**
     * Load real data from database asynchronously and populate dashboard statistics
     * OPTIMIZED: Uses parallel async loading to prevent UI freezing
     */
    private void loadRealDashboardDataAsync() {
        showLoadingState("Loading dashboard statistics...");

        // Load data asynchronously
        AsyncTaskManager.runAsync(() -> {
            // Fetch all data in background
            List<Order> allOrders = orderService.getAllOrders();
            List<Product> allProducts = productService.getAllProducts();
            List<User> allUsers = userDAO.findAll();

            return new Object[] { allOrders, allProducts, allUsers };
        }, result -> {
            // UI update on JavaFX thread
            Object[] data = (Object[]) result;
            @SuppressWarnings("unchecked")
            List<Order> allOrders = (List<Order>) data[0];
            @SuppressWarnings("unchecked")
            List<Product> allProducts = (List<Product>) data[1];
            @SuppressWarnings("unchecked")
            List<User> allUsers = (List<User>) data[2];

                    // Calculate statistics
                    int totalOrders = allOrders.size();
                    int pendingOrders = (int) allOrders.stream()
                            .filter(order -> "PENDING".equalsIgnoreCase(String.valueOf(order.getStatus())))
                            .count();

                    BigDecimal totalRevenue = allOrders.stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int totalProducts = allProducts.size();
                    int totalUsers = allUsers != null ? allUsers.size() : 0;

                    // Format and display data
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

                    safeSetText(ordersValue, numberFormat.format(totalOrders));
                    safeSetText(revenueValue, currencyFormat.format(totalRevenue));
                    safeSetText(productsValue, numberFormat.format(totalProducts));
                    safeSetText(usersValue, numberFormat.format(totalUsers));

                    // Set quick insight message with real data
                    String insightMessage = pendingOrders > 0
                            ? "You have " + pendingOrders + " pending order" + (pendingOrders > 1 ? "s" : "")
                            : "All orders are up to date! 🎉";
                    safeSetText(quickInsight, insightMessage);

            LOGGER.info(String.format("Dashboard data loaded async: Orders=%d, Revenue=%s, Products=%d, Users=%d",
                    totalOrders,
                    currencyFormat.format(totalRevenue),
                    totalProducts,
                    totalUsers));

            // Initialize charts with loaded data
            initializeCharts(allOrders, allProducts, allUsers);

            hideLoadingState();
            showCompletionState("Dashboard loaded successfully");
        }, throwable -> {
            LOGGER.log(Level.SEVERE, "Failed to load dashboard data", throwable);
            hideLoadingState();
            handleAsyncError(throwable);
            loadPlaceholderData();
        });
    }

    /**
     * Load placeholder data if database is unavailable
     */
    private void loadPlaceholderData() {
        safeSetText(ordersValue, "1,234");
        safeSetText(revenueValue, "$24,560");
        safeSetText(productsValue, "320");
        safeSetText(usersValue, "1,002");
        safeSetText(quickInsight, "You have 12 pending orders");
        LOGGER.warning("Using placeholder data - database connection may have failed");
    }

    /**
     * Connect to sidebar and topbar controllers for navigation and theme management
     */
    private void connectSubControllers() {
        try {
            // Get sidebar controller from fx:include
            Node sidebarNode = root.getLeft();
            if (sidebarNode != null && sidebarNode.getProperties().containsKey(FX_CONTROLLER)) {
                sidebarController = (SidebarController) sidebarNode.getProperties().get(FX_CONTROLLER);
                if (sidebarController != null) {
                    sidebarController.setDashboardController(this);
                    LOGGER.info("Sidebar controller connected");
                }
            }

            // Get topbar controller from fx:include
            Node topbarNode = root.getTop();
            if (topbarNode != null && topbarNode.getProperties().containsKey(FX_CONTROLLER)) {
                topbarController = (TopbarController) topbarNode.getProperties().get(FX_CONTROLLER);
                if (topbarController != null) {
                    topbarController.setDashboardController(this);
                    LOGGER.info("Topbar controller connected");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to connect sub-controllers", e);
        }
    }

    /**
     * Security check: Verify that the current user is an admin
     */
    private boolean checkAdminAccess() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            LOGGER.warning("No user in session - access denied");
            return false;
        }

        String role = currentUser.getRole();
        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

        if (!isAdmin) {
            LOGGER.warning("User %s with role %s attempted admin access".formatted(currentUser.getUsername(), role));
        }

        return isAdmin;
    }

    /**
     * Show access denied message and redirect to appropriate page
     */
    private void showAccessDeniedAndRedirect() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Access Denied");
            alert.setHeaderText("Unauthorized Access");
            alert.setContentText("You do not have permission to access the admin dashboard.\n" +
                    "Only administrators can access this area.");
            alert.showAndWait();

            // Redirect to customer dashboard or login
            redirectToAppropriateView();
        });
    }

    /**
     * Redirect to the appropriate view based on user role
     */
    private void redirectToAppropriateView() {
        try {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            Stage stage = (Stage) (root != null ? root.getScene().getWindow() : null);

            if (stage == null) return;

            String viewPath;
            String title;

            if (currentUser != null && "CUSTOMER".equalsIgnoreCase(currentUser.getRole())) {
                // Redirect to customer dashboard
                viewPath = ADMIN_PATH;
                title = "Smart E-Commerce System - My Dashboard";
            } else {
                // Redirect to login
                viewPath = "/com/smartcommerce/ui/views/login.fxml";
                title = "Smart E-Commerce System - Login";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to redirect user", e);
        }
    }

    // Helper that guards against null @FXML injections
    private void safeSetText(Label label, String text) {
        if (label != null) {
            label.setText(text);
        }
    }

    /**
     * Load the default dashboard view (summary cards and tables)
     * Reloads the entire dashboard to show hero section and stats
     */
    public void loadDashboardView() {
        try {
            // Reload the entire dashboard scene
            Stage stage = (Stage) root.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/admin/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Scene scene = new Scene(dashboardRoot);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Admin Dashboard");

            LOGGER.info("Dashboard view reloaded successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to reload dashboard", e);
            // Fallback: just refresh data
            loadRealDashboardDataAsync();
            updatePageTitle("Dashboard Overview");
        }
    }

    /**
     * Update the page title in the topbar
     */
    public void updatePageTitle(String title) {
        if (topbarController != null) {
            topbarController.setPageTitle(title);
        }
    }

    /**
     * Loads an FXML view into the main content area center. Replaces existing center children.
     * Sub-controllers should implement any data binding after load.
     */
    public void loadCenterView(String fxmlResource) {
        try {
            URL res = getClass().getResource(fxmlResource);
            if (res == null) {
                LOGGER.log(Level.WARNING, "FXML resource not found: {0}", fxmlResource);
                showPlaceholderView("Resource not found: " + fxmlResource);
                return;
            }
            FXMLLoader loader = new FXMLLoader(res);
            Node view = loader.load();

            // Clear all children and add only the new view
            // This removes the hero section and stats cards
            contentVBox.getChildren().clear();
            contentVBox.getChildren().add(view);

            LOGGER.log(Level.INFO, "Loaded view: {0}", fxmlResource);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load center view: %s".formatted(fxmlResource), e);
            showPlaceholderView("Error loading view: " + fxmlResource + "\n" + e.getMessage());
        }
    }

    /**
     * Show a placeholder view when the actual view cannot be loaded
     */
    private void showPlaceholderView(String message) {
        VBox placeholder = new VBox(20);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setStyle("-fx-padding: 40; -fx-background-color: #f8f9fa;");

        Label iconLabel = new Label("⚠️");
        iconLabel.setStyle("-fx-font-size: 48px;");

        Label titleLabel = new Label("View Not Available");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #495057;");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d; -fx-wrap-text: true; -fx-text-alignment: center;");
        messageLabel.setMaxWidth(500);

        javafx.scene.control.Button backButton = new javafx.scene.control.Button("Back to Dashboard");
        backButton.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 8px; -fx-cursor: hand; -fx-font-weight: 600;");
        backButton.setOnAction(e -> loadDashboardView());

        placeholder.getChildren().addAll(iconLabel, titleLabel, messageLabel, backButton);

        contentVBox.getChildren().clear();
        contentVBox.getChildren().add(placeholder);
    }

    /**
     * Opens a modal by loading the provided FXML inside the modal container and showing backdrop.
     * Logic to close the modal should be implemented inside the modal's controller.
     */
    public void openModal(String fxmlResource) {
        try {
            URL res = getClass().getResource(fxmlResource);
            if (res == null) {
                LOGGER.log(Level.WARNING, "Modal resource not found: {0}", fxmlResource);
                return;
            }
            Node modal = FXMLLoader.load(res);
            modalContainer.getChildren().clear();
            modalContainer.getChildren().add(modal);
            modalContainer.setVisible(true);
            modalContainer.setManaged(true);
            // show backdrop
            mainStack.lookupAll(".modal-backdrop").forEach(node -> {
                node.setVisible(true);
                node.setManaged(true);
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, () -> "Failed to open modal: " + fxmlResource);
        }
    }

    /**
     * Closes any open modal.
     */
    public void closeModal() {
        if (modalContainer == null || mainStack == null) return;
        modalContainer.getChildren().clear();
        modalContainer.setVisible(false);
        modalContainer.setManaged(false);
        mainStack.lookupAll(".modal-backdrop").forEach(node -> {
            node.setVisible(false);
            node.setManaged(false);
        });
    }

    // ----- UI helper hooks (for wiring from topbar/sidebar) -----

    /**
     * Toggle sidebar collapsed/expanded state by toggling a CSS class on the root node.
     * The actual sidebar <fx:include> should observe this class in CSS and adjust layout.
     */
    public void toggleSidebar() {
        if (root == null) return;
        if (root.getStyleClass().contains("sidebar-collapsed")) {
            root.getStyleClass().remove("sidebar-collapsed");
            LOGGER.info("Sidebar expanded");
        } else {
            root.getStyleClass().add("sidebar-collapsed");
            LOGGER.info("Sidebar collapsed");
        }
    }

    /**
     * Toggle between light and dark theme -- toggles a 'dark-mode' style class on the root.
     */
    public void toggleTheme() {
        if (root == null) return;
        if (root.getStyleClass().contains(DARK_MODE)) {
            root.getStyleClass().remove(DARK_MODE);
            LOGGER.info("Light theme activated");
        } else {
            root.getStyleClass().add(DARK_MODE);
            LOGGER.info("Dark theme activated");
        }
    }

    // ----- Authentication / authorization methods -----

    /**
     * Check whether a user is authenticated.
     */
    public boolean isAuthenticated() {
        return SessionManager.getInstance().getCurrentUser() != null;
    }

    /**
     * Check whether the current user is an admin.
     */
    public boolean isAdmin() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        return currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }

    /**
     * Handle Add Product button click
     */
    @FXML
    private void handleAddProduct() {
        LOGGER.info("Add Product button clicked");
        // Clear existing content first
        if (contentVBox != null) {
            contentVBox.getChildren().clear();
        }
        // Load the simple products view (working version)
        loadCenterView("/com/smartcommerce/ui/views/admin/products-simple.fxml");
        updatePageTitle("Product Management");
    }

    /**
     * Handle View Orders button click
     */
    @FXML
    private void handleViewOrders() {
        LOGGER.info("View Orders button clicked");
        loadCenterView("/com/smartcommerce/ui/views/admin/orders.fxml");
        updatePageTitle("Order Management");
    }

    /**
     * Handle View Analytics button click
     */
    @FXML
    private void handleViewAnalytics() {
        LOGGER.info("View Analytics button clicked");
        loadCenterView("/com/smartcommerce/ui/views/admin/analytics-simple.fxml");
        updatePageTitle("Analytics & Performance");
    }

    /**
     * Handle View All button click
     */
    @FXML
    private void handleViewAll() {
        LOGGER.info("View All button clicked");
        loadCenterView("/com/smartcommerce/ui/views/admin/orders.fxml");
        updatePageTitle("All Recent Activity");
    }

    /**
     * Handle Orders stat card click - navigate to orders page
     */
    @FXML
    private void handleOrdersCardClick() {
        LOGGER.info("Orders stat card clicked");
        loadCenterView("/com/smartcommerce/ui/views/admin/orders.fxml");
        updatePageTitle("Order Management");
    }

    /**
     * Handle Revenue stat card click - navigate to analytics/revenue page
     */
    @FXML
    private void handleRevenueCardClick() {
        LOGGER.info("Revenue stat card clicked");
        loadCenterView("/com/smartcommerce/ui/views/admin/analytics-simple.fxml");
        updatePageTitle("Revenue Analytics");
    }

    /**
     * Handle Products stat card click - navigate to products page
     */
    @FXML
    private void handleProductsCardClick() {
        LOGGER.info("Products stat card clicked");
        // Clear existing content first
        if (contentVBox != null) {
            contentVBox.getChildren().clear();
        }
        // Load the simple products view (working version)
        loadCenterView("/com/smartcommerce/ui/views/admin/products-simple.fxml");
        updatePageTitle("Product Management");
    }

    /**
     * Handle Users stat card click - navigate to users page
     */
    @FXML
    private void handleUsersCardClick() {
        LOGGER.info("Users stat card clicked");
        loadCenterView("/com/smartcommerce/ui/views/admin/users.fxml");
        updatePageTitle("User Management");
    }

    // ==================== CHART CREATION METHODS ====================

    /**
     * Initialize and populate all dashboard charts with real data
     */
    private void initializeCharts(List<Order> orders, List<Product> products, List<User> users) {
        Platform.runLater(() -> {
            createSalesRevenueChart(orders);
            createOrdersStatusChart(orders);
            createTopCategoriesChart(products);
            createUserGrowthChart(users);
        });
    }

    /**
     * Create Sales Revenue Trend Chart (Line Chart)
     */
    private void createSalesRevenueChart(List<Order> orders) {
        if (salesChartContainer == null) return;

        try {
            // Create axes
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Month");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Revenue ($)");

            // Create line chart
            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Monthly Revenue");
            lineChart.setLegendVisible(false);

            // Create data series
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Revenue");

            // Group orders by month and calculate revenue
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            double[] monthlyRevenue = new double[12];

            for (Order order : orders) {
                if (order.getOrderDate() != null) {
                    int month = order.getOrderDate().getMonthValue() - 1;
                    if (month >= 0 && month < 12) {
                        monthlyRevenue[month] += order.getTotalAmount().doubleValue();
                    }
                }
            }

            // Add data to chart (show last 6 months)
            int currentMonth = java.time.LocalDate.now().getMonthValue() - 1;
            for (int i = 0; i < 6; i++) {
                int monthIndex = (currentMonth - 5 + i + 12) % 12;
                series.getData().add(new XYChart.Data<>(months[monthIndex], monthlyRevenue[monthIndex]));
            }

            lineChart.getData().add(series);
            lineChart.setStyle("-fx-background-color: transparent;");

            salesChartContainer.getChildren().clear();
            salesChartContainer.getChildren().add(lineChart);

            LOGGER.info("Sales revenue chart created successfully");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to create sales chart", e);
        }
    }

    /**
     * Create Orders by Status Chart (Pie Chart)
     */
    private void createOrdersStatusChart(List<Order> orders) {
        if (ordersChartContainer == null) return;

        try {
            PieChart pieChart = new PieChart();
            pieChart.setTitle("Order Status Distribution");

            // Count orders by status
            Map<String, Long> statusCounts = orders.stream()
                .collect(Collectors.groupingBy(
                    order -> String.valueOf(order.getStatus()),
                    Collectors.counting()
                ));

            // Add data to pie chart
            for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
                PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
                pieChart.getData().add(slice);
            }

            pieChart.setLegendVisible(true);
            pieChart.setStyle("-fx-background-color: transparent;");

            ordersChartContainer.getChildren().clear();
            ordersChartContainer.getChildren().add(pieChart);

            LOGGER.info("Orders status chart created successfully");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to create orders chart", e);
        }
    }

    /**
     * Create Top Categories Chart (Bar Chart)
     */
    private void createTopCategoriesChart(List<Product> products) {
        if (categoriesChartContainer == null) return;

        try {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Category");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Number of Products");

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Products by Category");
            barChart.setLegendVisible(false);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Products");

            // Count products by category
            Map<String, Long> categoryCounts = products.stream()
                .filter(p -> p.getCategoryName() != null)
                .collect(Collectors.groupingBy(
                    Product::getCategoryName,
                    Collectors.counting()
                ));

            // Add top 5 categories
            categoryCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));

            barChart.getData().add(series);
            barChart.setStyle("-fx-background-color: transparent;");

            categoriesChartContainer.getChildren().clear();
            categoriesChartContainer.getChildren().add(barChart);

            LOGGER.info("Categories chart created successfully");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to create categories chart", e);
        }
    }

    /**
     * Create User Growth Chart (Area Chart)
     */
    private void createUserGrowthChart(List<User> users) {
        if (userGrowthChartContainer == null) return;

        try {
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Month");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("New Users");

            AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
            areaChart.setTitle("User Registration Trend");
            areaChart.setLegendVisible(false);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("New Users");

            // Group users by month
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            int[] monthlyUsers = new int[12];

            for (User user : users) {
                if (user.getCreatedAt() != null) {
                    int month = user.getCreatedAt().getMonthValue() - 1;
                    if (month >= 0 && month < 12) {
                        monthlyUsers[month]++;
                    }
                }
            }

            // Add data for last 6 months
            int currentMonth = java.time.LocalDate.now().getMonthValue() - 1;
            for (int i = 0; i < 6; i++) {
                int monthIndex = (currentMonth - 5 + i + 12) % 12;
                series.getData().add(new XYChart.Data<>(months[monthIndex], monthlyUsers[monthIndex]));
            }

            areaChart.getData().add(series);
            areaChart.setStyle("-fx-background-color: transparent;");

            userGrowthChartContainer.getChildren().clear();
            userGrowthChartContainer.getChildren().add(areaChart);

            LOGGER.info("User growth chart created successfully");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to create user growth chart", e);
        }
    }

}
