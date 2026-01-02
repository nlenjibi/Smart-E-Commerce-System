package com.smartecommerce.app;

import com.smartcommerce.config.DatabaseConfig;
import com.smartcommerce.optimization.*;
import com.smartcommerce.performance.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * SmartEcommerceApp - Main JavaFX Application
 * Entry point for the Smart E-Commerce System
 */
public class SmartEcommerceApp extends Application {

    private BorderPane mainLayout;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // Show landing (public) page first
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/smartcommerce/ui/views/landing.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Make the scene background black
            root.setStyle("-fx-background-color: black;");

            primaryStage.setTitle("Smart E-Commerce System - Home");
            primaryStage.setScene(scene);
            // Landing page is responsive; allow resizing
            primaryStage.setResizable(true);

            // Add application icon (logo) with error handling
            try {
                Image icon = new Image(getClass().getResourceAsStream("/images/logo.jpg"));
                if (!icon.isError()) {
                    primaryStage.getIcons().add(icon);
                } else {
                    System.err.println("Warning: Logo image could not be loaded");
                }
            } catch (Exception iconEx) {
                System.err.println("Warning: Could not load application icon: " + iconEx.getMessage());
                // Continue without icon - not a critical error
            }

            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error loading landing page: " + e.getMessage());
            e.printStackTrace();

            // Fallback to old login screen if landing page fails
            loadFallbackLogin(primaryStage);
        }
    }

    /**
     * Fallback helper to load login page if landing page fails to load.
     */
    private void loadFallbackLogin(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/smartcommerce/ui/views/login.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Smart E-Commerce System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            System.err.println("Critical error loading login fallback: " + ex.getMessage());
            ex.printStackTrace();
            // As a last resort, load main application layout
            loadMainApplication(primaryStage);
        }
    }

    /**
     * Load main application (called after successful login)
     */
    private void loadMainApplication(Stage primaryStage) {
        primaryStage.setTitle("Smart E-Commerce System - Database Fundamentals");

        // Test database connection
        if (!testDatabaseConnection()) {
            showDatabaseError(primaryStage);
            return;
        }

        // Create main layout
        mainLayout = new BorderPane();
        mainLayout.setTop(createMenuBar());
        mainLayout.setCenter(createWelcomePane());
        mainLayout.setBottom(createStatusBar());

        Scene scene = new Scene(mainLayout, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        updateStatus("Application started successfully");
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        // Manage Menu
        Menu manageMenu = new Menu("Manage");
        MenuItem productsItem = new MenuItem("Products");
        productsItem.setOnAction(e -> loadView("products.fxml", "Product Management"));
        MenuItem categoriesItem = new MenuItem("Categories");
        categoriesItem.setOnAction(e -> loadView("categories.fxml", "Category Management"));
        MenuItem ordersItem = new MenuItem("Orders");
        ordersItem.setOnAction(e -> loadView("orders.fxml", "Order Management"));
        MenuItem usersItem = new MenuItem("Users");
        usersItem.setOnAction(e -> loadView("users.fxml", "User Management"));
        manageMenu.getItems().addAll(productsItem, categoriesItem, ordersItem, usersItem);

        // Performance Menu
        Menu perfMenu = new Menu("Performance");
        MenuItem adminItem = new MenuItem("Admin Panel");
        adminItem.setOnAction(e -> loadView("analitics.fxml", "Admin Panel"));
        MenuItem cacheItem = new MenuItem("Cache Demo");
        cacheItem.setOnAction(e -> runCacheDemo());
        MenuItem searchItem = new MenuItem("Search Demo");
        searchItem.setOnAction(e -> runSearchDemo());
        MenuItem sortItem = new MenuItem("Sort Demo");
        sortItem.setOnAction(e -> runSortDemo());
        perfMenu.getItems().addAll(adminItem, new SeparatorMenuItem(), cacheItem, searchItem, sortItem);

        // Reports Menu
        Menu reportsMenu = new Menu("Reports");
        MenuItem perfReportItem = new MenuItem("Performance Report");
        perfReportItem.setOnAction(e -> generatePerformanceReport());
        MenuItem queryStatsItem = new MenuItem("Query Statistics");
        queryStatsItem.setOnAction(e -> showQueryStats());
        reportsMenu.getItems().addAll(perfReportItem, queryStatsItem);

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAbout());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, manageMenu, perfMenu, reportsMenu, helpMenu);
        return menuBar;
    }

    private VBox createWelcomePane() {
        VBox welcomePane = new VBox(20);
        welcomePane.setAlignment(Pos.CENTER);
        welcomePane.setPadding(new Insets(50));

        Label titleLabel = new Label("Smart E-Commerce System");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Database Fundamentals Project");
        subtitleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666;");

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setAlignment(Pos.CENTER);

        Button btnProducts = createMenuButton("Manage Products", "Manage your product catalog");
        btnProducts.setOnAction(e -> loadView("products.fxml", "Product Management"));

        Button btnOrders = createMenuButton("Manage Orders", "View and manage customer orders");
        btnOrders.setOnAction(e -> loadView("orders.fxml", "Order Management"));

        Button btnAdmin = createMenuButton("Admin Panel", "Performance monitoring and demos");
        btnAdmin.setOnAction(e -> loadView("analitics.fxml", "Admin Panel"));

        Button btnReports = createMenuButton("Generate Report", "Create performance analysis report");
        btnReports.setOnAction(e -> generatePerformanceReport());

        buttonGrid.add(btnProducts, 0, 0);
        buttonGrid.add(btnOrders, 1, 0);
        buttonGrid.add(btnAdmin, 0, 1);
        buttonGrid.add(btnReports, 1, 1);

        Label infoLabel = new Label(
            "Features:\n" +
            "✓ CRUD Operations with JDBC\n" +
            "✓ HashMap-based Caching (O(1) lookup)\n" +
            "✓ QuickSort & MergeSort Algorithms\n" +
            "✓ Binary Search Implementation\n" +
            "✓ Database Indexing & Optimization\n" +
            "✓ Performance Metrics & Reports"
        );
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-background-color: #f0f0f0; -fx-padding: 20;");

        welcomePane.getChildren().addAll(titleLabel, subtitleLabel, buttonGrid, infoLabel);
        return welcomePane;
    }

    private Button createMenuButton(String title, String description) {
        Button button = new Button(title + "\n" + description);
        button.setPrefSize(250, 80);
        button.setStyle("-fx-font-size: 14px;");
        return button;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.setStyle("-fx-background-color: #e0e0e0;");

        statusLabel = new Label("Ready");
        statusBar.getChildren().add(statusLabel);

        return statusBar;
    }

    /**
     * Load FXML view and display in center pane
     */
    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/smartcommerce/ui/views/" + fxmlFile)
            );
            Parent view = loader.load();
            mainLayout.setCenter(view);
            updateStatus(title + " loaded");
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile);
            e.printStackTrace();
            showError("Error loading " + title + ": " + e.getMessage());
        }
    }


    private void runCacheDemo() {
        updateStatus("Running Cache Demo...");
        new Thread(() -> {
            CacheDemo.demonstrateCaching();
            javafx.application.Platform.runLater(() ->
                updateStatus("Cache Demo completed - check console"));
        }).start();
    }

    private void runSearchDemo() {
        updateStatus("Running Search Demo...");
        new Thread(() -> {
            SearchDemo.demonstrateSearch();
            javafx.application.Platform.runLater(() ->
                updateStatus("Search Demo completed - check console"));
        }).start();
    }

    private void runSortDemo() {
        updateStatus("Running Sort Demo...");
        new Thread(() -> {
            SortDemo.demonstrateSorting();
            javafx.application.Platform.runLater(() ->
                updateStatus("Sort Demo completed - check console"));
        }).start();
    }

    private void generatePerformanceReport() {
        updateStatus("Generating Performance Report...");
        new Thread(() -> {
            com.smartcommerce.service.ProductService ps = new com.smartcommerce.service.ProductService();
            PerformanceReport.generateReport(ps);
            PerformanceReport.printSummary();
            javafx.application.Platform.runLater(() -> {
                updateStatus("Performance Report generated");
                showInfo("Performance report has been generated.\nCheck 'performance_report.txt' in the project directory.");
            });
        }).start();
    }

    private void showQueryStats() {
        QueryTimer.printStats();
        updateStatus("Query statistics printed to console");
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Smart E-Commerce System");
        alert.setContentText(
            "Database Fundamentals Project\n\n" +
            "Features:\n" +
            "• Complete database design (3NF normalization)\n" +
            "• CRUD operations using JDBC\n" +
            "• Performance optimization with caching\n" +
            "• Sorting & searching algorithms\n" +
            "• Database indexing\n" +
            "• Performance metrics & analysis\n\n" +
            "Version: 1.0\n" +
            "Date: December 2024"
        );
        alert.showAndWait();
    }

    private boolean testDatabaseConnection() {
        try {
            return DatabaseConfig.testConnection();
        } catch (Exception e) {
            return false;
        }
    }

    private void showDatabaseError(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Connection Error");
        alert.setHeaderText("Cannot connect to database");
        alert.setContentText(
            "Please ensure:\n" +
            "1. MySQL server is running\n" +
            "2. Database 'smart_ecommerce' exists\n" +
            "3. Connection credentials are correct\n\n" +
            "Run the SQL scripts in this order:\n" +
            "1. Schema.sql\n" +
            "2. Indexes.sql\n" +
            "3. SampleData.sql"
        );
        alert.showAndWait();
        System.exit(1);
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
        updateStatus("Error: " + message);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }


    @Override
    public void stop() {
        System.out.println("Application closed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

