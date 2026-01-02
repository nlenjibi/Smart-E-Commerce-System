package com.smartecommerce.controllers;

import com.smartcommerce.app.SessionManager;
import com.smartcommerce.dao.UserDAO;
import com.smartcommerce.model.Order;
import com.smartcommerce.model.User;
import com.smartcommerce.service.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CustomerDashboardController - Full-featured controller for customer's dashboard.
 * Features:
 * - View and edit profile information
 * - Change password securely
 * - View order history
 * - Navigate between sections
 */
public class CustomerDashboardController {

    private static final Logger LOGGER = Logger.getLogger(CustomerDashboardController.class.getName());

    // Navigation Buttons
    @FXML private Button btnHome;
    @FXML private Button btnProfile;
    @FXML private Button btnOrders;
    @FXML private Button btnSettings;
    @FXML private Button btnLogout;

    // Top Bar
    @FXML private Label lblPageTitle;
    @FXML private Label lblUserInfo;

    // Profile Section
    @FXML private VBox profileSection;
    @FXML private Label lblUsername;
    @FXML private TextField txtEmail;
    @FXML private TextField txtFullName;
    @FXML private TextField txtPhone;
    @FXML private TextArea txtAddress;
    @FXML private Label lblProfileMessage;

    // Password Section
    @FXML private VBox passwordSection;
    @FXML private PasswordField txtCurrentPassword;
    @FXML private PasswordField txtNewPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Label lblPasswordMessage;

    // Orders Section
    @FXML private VBox ordersSection;
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, LocalDateTime> colOrderDate;
    @FXML private TableColumn<Order, BigDecimal> colTotalAmount;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private Label lblOrdersMessage;

    // Services and DAOs
    private final UserDAO userDAO;
    private final OrderService orderService;
    private User currentUser;

    public CustomerDashboardController() {
        this.userDAO = new UserDAO();
        this.orderService = new OrderService();
    }

    @FXML
    private void initialize() {
        // Security check
        if (!checkIsCustomer()) {
            LOGGER.warning("Access denied: non-customer attempted to open customer dashboard");
            redirectToLogin();
            return;
        }

        // Load current user from session
        loadCurrentUser();

        // Setup table columns
        setupOrdersTable();

        // Load user data into form
        loadUserProfile();

        // Show profile section by default
        showProfileSection();
    }

    /**
     * Check if current user is a customer
     */
    private boolean checkIsCustomer() {
        User user = SessionManager.getCurrentUserStatic();
        return user != null && "CUSTOMER".equalsIgnoreCase(user.getRole());
    }

    /**
     * Load current user from session
     */
    private void loadCurrentUser() {
        currentUser = SessionManager.getCurrentUserStatic();
        if (currentUser != null) {
            lblUserInfo.setText(currentUser.getUsername() + " - Customer");
            LOGGER.info("Customer dashboard loaded for user: " + currentUser.getUsername());
        } else {
            LOGGER.warning("No user in session");
            redirectToLogin();
        }
    }

    /**
     * Setup orders table columns
     */
    private void setupOrdersTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Format date column
        colOrderDate.setCellFactory(column -> new TableCell<Order, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString().substring(0, 16).replace("T", " "));
                }
            }
        });

        // Format amount column
        colTotalAmount.setCellFactory(column -> new TableCell<Order, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("$" + item.toString());
                }
            }
        });
    }

    /**
     * Load user profile data into form fields
     */
    private void loadUserProfile() {
        if (currentUser != null) {
            lblUsername.setText(currentUser.getUsername());
            txtEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "");

            // Load additional user details if available
            User fullUser = userDAO.findById(currentUser.getUserId());
            if (fullUser != null) {
                txtFullName.setText(fullUser.getUsername()); // Can be extended with full name field
                txtPhone.setText(""); // Add phone field to User model if needed
                txtAddress.setText(""); // Add address field to User model if needed
            }
        }
    }

    /**
     * Handle profile update
     */
    @FXML
    private void handleUpdateProfile() {
        lblProfileMessage.setText("");
        lblProfileMessage.setStyle("");

        if (currentUser == null) {
            showError(lblProfileMessage, "No user logged in");
            return;
        }

        // Validate email
        String email = txtEmail.getText().trim();
        if (email.isEmpty() || !email.contains("@")) {
            showError(lblProfileMessage, "Please enter a valid email address");
            return;
        }

        // Update user object
        currentUser.setEmail(email);

        // Update in database
        boolean success = userDAO.update(currentUser);

        if (success) {
            showSuccess(lblProfileMessage, "✓ Profile updated successfully!");
            LOGGER.info("Profile updated for user: " + currentUser.getUsername());
        } else {
            showError(lblProfileMessage, "✗ Failed to update profile. Please try again.");
        }
    }

    /**
     * Handle password change
     */
    @FXML
    private void handleChangePassword() {
        lblPasswordMessage.setText("");
        lblPasswordMessage.setStyle("");

        if (currentUser == null) {
            showError(lblPasswordMessage, "No user logged in");
            return;
        }

        String currentPassword = txtCurrentPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Validation
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError(lblPasswordMessage, "All password fields are required");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError(lblPasswordMessage, "New passwords do not match");
            return;
        }

        if (newPassword.length() < 6) {
            showError(lblPasswordMessage, "New password must be at least 6 characters");
            return;
        }

        // Verify current password
        User verifyUser = userDAO.authenticate(currentUser.getUsername(), currentPassword);
        if (verifyUser == null) {
            showError(lblPasswordMessage, "Current password is incorrect");
            return;
        }

        // Update password
        boolean success = userDAO.updatePassword(currentUser.getUserId(), newPassword);

        if (success) {
            showSuccess(lblPasswordMessage, "✓ Password changed successfully!");

            // Clear password fields
            txtCurrentPassword.clear();
            txtNewPassword.clear();
            txtConfirmPassword.clear();

            LOGGER.info("Password changed for user: " + currentUser.getUsername());
        } else {
            showError(lblPasswordMessage, "✗ Failed to change password. Please try again.");
        }
    }

    /**
     * Load user's orders
     */
    private void loadOrders() {
        if (currentUser == null) {
            return;
        }

        try {
            List<Order> orders = orderService.getOrdersByUser(currentUser.getUserId());
            ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
            ordersTable.setItems(orderList);

            lblOrdersMessage.setText("Total orders: " + orders.size());
            lblOrdersMessage.setStyle("-fx-text-fill: #6b7280;");

            LOGGER.info("Loaded " + orders.size() + " orders for user: " + currentUser.getUsername());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading orders", e);
            lblOrdersMessage.setText("✗ Failed to load orders");
            lblOrdersMessage.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    // Navigation Handlers

    @FXML
    private void handleHome() {
        try {
            // Navigate to landing page WITHOUT destroying session
            Stage stage = (Stage) btnHome.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/landing.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Smart E-Commerce System");
            LOGGER.info("Navigated to home page, session preserved for: " +
                       (currentUser != null ? currentUser.getUsername() : "unknown"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load home page", e);
        }
    }

    @FXML
    private void handleProfile() {
        showProfileSection();
    }

    @FXML
    private void handleOrders() {
        showOrdersSection();
        loadOrders();
    }

    @FXML
    private void handleSettings() {
        showPasswordSection();
    }

    @FXML
    private void handleLogout() {
        // Log the logout action
        String username = currentUser != null ? currentUser.getUsername() : "unknown";
        LOGGER.info("User logging out: " + username);

        // Destroy the session
        SessionManager.logout();

        // Redirect to landing page (not login)
        try {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/landing.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Smart E-Commerce System");
            LOGGER.info("User logged out successfully, redirected to landing page");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load landing page after logout", e);
        }
    }

    // Section Visibility Management

    private void showProfileSection() {
        lblPageTitle.setText("My Profile");
        profileSection.setVisible(true);
        profileSection.setManaged(true);
        passwordSection.setVisible(false);
        passwordSection.setManaged(false);
        ordersSection.setVisible(false);
        ordersSection.setManaged(false);
    }

    private void showPasswordSection() {
        lblPageTitle.setText("Change Password");
        profileSection.setVisible(false);
        profileSection.setManaged(false);
        passwordSection.setVisible(true);
        passwordSection.setManaged(true);
        ordersSection.setVisible(false);
        ordersSection.setManaged(false);
    }

    private void showOrdersSection() {
        lblPageTitle.setText("My Orders");
        profileSection.setVisible(false);
        profileSection.setManaged(false);
        passwordSection.setVisible(false);
        passwordSection.setManaged(false);
        ordersSection.setVisible(true);
        ordersSection.setManaged(true);
    }

    // Utility Methods

    private void showSuccess(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
    }

    private void showError(Label label, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
    }

    private void redirectToLogin() {
        try {
            Stage stage = (Stage) (btnLogout != null ? btnLogout.getScene().getWindow() : null);
            if (stage != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/smartcommerce/ui/views/login.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.setTitle("Login - Smart E-Commerce System");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to redirect to login", e);
        }
    }
}
