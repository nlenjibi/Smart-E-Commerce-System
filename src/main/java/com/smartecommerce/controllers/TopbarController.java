package com.smartecommerce.controllers;

import com.smartecommerce.app.SessionManager;
import com.smartecommerce.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TopbarController - handles search, notifications, profile menu and theme toggle.
 * Displays admin name and role in the top bar.
 */
public class TopbarController {

    private static final Logger LOGGER = Logger.getLogger(TopbarController.class.getName());

    @FXML public Button btnToggleSidebar;

    @FXML public Label notifBadge;
    @FXML public ToggleButton themeToggle;
    @FXML public Label pageTitle;
    @FXML public Label adminNameLabel;
    @FXML public Label adminRoleLabel;


    private DashboardController dashboardController;

    @FXML
    public void initialize() {
        // Set notification badge
        if (notifBadge != null) {
            notifBadge.setText("3");
        }

        // Load admin info from session
        loadAdminInfo();
    }

    /**
     * Set reference to parent dashboard controller
     */
    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    /**
     * Load admin information from session
     */
    private void loadAdminInfo() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (adminNameLabel != null) {
                adminNameLabel.setText(currentUser.getUsername() != null ? currentUser.getUsername() : "Admin User");
            }
            if (adminRoleLabel != null) {
                String role = currentUser.getRole();
                adminRoleLabel.setText(role != null ? role : "Administrator");
            }
        } else {
            if (adminNameLabel != null) {
                adminNameLabel.setText("Admin User");
            }
            if (adminRoleLabel != null) {
                adminRoleLabel.setText("Administrator");
            }
        }
    }

    /**
     * Update page title dynamically
     */
    public void setPageTitle(String title) {
        if (pageTitle != null) {
            pageTitle.setText(title);
        }
    }


}

