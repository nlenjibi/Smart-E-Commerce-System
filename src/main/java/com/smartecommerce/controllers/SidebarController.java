package com.smartecommerce.controllers;

import com.smartecommerce.app.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SidebarController {

    private static final Logger LOGGER = Logger.getLogger(SidebarController.class.getName());

    @FXML public VBox navList;
    private final ToggleGroup navToggleGroup = new ToggleGroup();
    
    @FXML public ToggleButton navDashboard;
    @FXML public ToggleButton navHome;
    @FXML public ToggleButton navOrders;
    @FXML public ToggleButton navCategories;
    @FXML public ToggleButton navProducts;
    @FXML public ToggleButton navUsers;
    @FXML public ToggleButton navAnalytics;
    @FXML public Button btnLogout;

    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        // Initialize toggle group
        navDashboard.setToggleGroup(navToggleGroup);
        navHome.setToggleGroup(navToggleGroup);
        navOrders.setToggleGroup(navToggleGroup);
        navCategories.setToggleGroup(navToggleGroup);
        navProducts.setToggleGroup(navToggleGroup);
        navUsers.setToggleGroup(navToggleGroup);
        navAnalytics.setToggleGroup(navToggleGroup);

        // Set default selection
        navDashboard.setSelected(true);
    }

    /**
     * Set reference to parent dashboard controller for view switching
     */
    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML
    private void handleNavDashboard() {
        LOGGER.info("Navigating to Dashboard");
        try {
            Stage stage = (Stage) navDashboard.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/admin/dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Dashboard", e);
        }
    }

    @FXML
    private void handleNavHome() {
        LOGGER.info("Navigating to Landing Page - Session Preserved");
        try {
            // Navigate to landing WITHOUT destroying session
            Stage stage = (Stage) navHome.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/landing.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Home");
            stage.show();

            // Log session preservation
            var currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                LOGGER.info("User session preserved: " + currentUser.getUsername());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Landing Page", e);
        }
    }

    @FXML
    private void handleNavOrders() {
        LOGGER.info("Navigating to Orders");
        try {
            Stage stage = (Stage) navOrders.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/admin/orders.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Order Management");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Orders Page", e);
        }
    }

    @FXML
    private void handleNavCategories() {
        LOGGER.info("Navigating to Categories");
        try {
            Stage stage = (Stage) navCategories.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/admin/categories.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Category Management");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Categories Page", e);
        }
    }

    @FXML
    private void handleNavProducts() {
        LOGGER.info("Navigating to Products");
        try {
            Stage stage = (Stage) navProducts.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/admin/products.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Product Management");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Products Page", e);
        }
    }

    @FXML
    private void handleNavUsers() {
        LOGGER.info("Navigating to Users");
        try {
            Stage stage = (Stage) navUsers.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/admin/users.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - User Management");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Users Page", e);
        }
    }

    @FXML
    private void handleNavAnalytics() {
        LOGGER.info("Navigating to Analytics");
        try {
            Stage stage = (Stage) navAnalytics.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/admin/analitics.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System - Analytics & Performance");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load Analytics Page", e);
        }
    }

    /**
     * Auto-collapse sidebar on mobile/small screens after navigation
     * This improves UX by showing content immediately on mobile devices
     * NOTE: Not needed with full scene changes
     */
    private void collapseSidebarOnMobile() {
        // Method kept for compatibility but not used with full scene changes
        LOGGER.info("collapseSidebarOnMobile called (not needed with full scene changes)");
    }

    @FXML
    private void handleLogout() {
        LOGGER.info("User logging out");

        // Get username before destroying session
        var currentUser = SessionManager.getInstance().getCurrentUser();
        String username = currentUser != null ? currentUser.getUsername() : "unknown";

        try {
            // Destroy the session
            SessionManager.getInstance().destroySession();
            LOGGER.info("Session destroyed for user: " + username);

            // Redirect to landing page (not login)
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smartecommerce/ui/views/landing.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Smart E-Commerce System");
            stage.show();

            LOGGER.info("User logged out successfully, redirected to landing page");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load landing page after logout", e);
        }
    }
}

