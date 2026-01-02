package com.smartecommerce.controllers;

import com.smartecommerce.app.SessionManager;
import com.smartecommerce.dao.UserDAO;
import com.smartecommerce.models.User;
import com.smartecommerce.utils.SecurityUtils;
import com.smartecommerce.utils.UIUtils;
import com.smartecommerce.utils.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static com.smartecommerce.utils.AppUtils.println;

/**
 * LoginController handles user authentication with enhanced security
 */
public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox chkRememberMe;
    @FXML private Button btnLogin;
    @FXML private Hyperlink linkSignup;
    @FXML private Label lblError;

    private UserDAO userDAO;
    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 5;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    public void initialize() {
        // Setup enter key to login
        if (txtPassword != null) {
            txtPassword.setOnAction(e -> handleLogin());
        }
        if (txtUsername != null) {
            txtUsername.setOnAction(e -> handleLogin());
        }
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        // Input validation using ValidationUtil
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            showError("Please enter both username and password");
            return;
        }

        // Check login attempts (brute force protection)
        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            showError("Too many login attempts. Please try again later.");
            btnLogin.setDisable(true);
            return;
        }

        // Sanitize input to prevent SQL injection
        if (SecurityUtils.containsSQLInjection(username)) {
            showError("Invalid username format");
            loginAttempts++;
            return;
        }

        try {
            // Hash the password using SecurityUtils
            String hashedPassword = SecurityUtils.hashPassword(password);

            // Authenticate user
            User user = userDAO.authenticate(username, hashedPassword);

            if (user != null) {
                // Login successful
                hideError();
                loginAttempts = 0; // Reset attempts

                // Create session
                SessionManager.getInstance().setCurrentUser(user);
                SessionManager.getInstance().createSession();

                // Log successful login
                println("User logged in: " + user.getUsername() + " (Role: " + user.getRole() + ")");

                openMainApplication(user);
            } else {
                loginAttempts++;
                int attemptsLeft = MAX_LOGIN_ATTEMPTS - loginAttempts;
                showError("Invalid username or password. " + attemptsLeft + " attempts remaining.");
            }
        } catch (Exception e) {
            showError("Login error. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignup() {
        try {
            // Get stage from the signup link that was clicked
            Stage stage = (Stage) linkSignup.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/smartecommerce/ui/views/admin/signup.fxml")
            );
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Smart E-Commerce System - Sign Up");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading signup page: " + e.getMessage());
        }
    }


    private void openMainApplication(User user) {
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();

            // Redirect based on user role
            String viewPath;
            String viewTitle;

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                viewPath = "/smartecommerce/ui/views/admin/dashboard.fxml";
                viewTitle = "Smart E-Commerce System - Admin Panel";
            } else {
                // Non-admin users (customers) should be redirected to the public landing or customer dashboard
                // Use the customer dashboard for authenticated users; landing.fxml is still the public home page.
                viewPath = "/smartecommerce/ui/views/custormer/customer_dashboard.fxml";
                viewTitle = "Smart E-Commerce System - My Dashboard";
            }

            // Load the appropriate view (FXML must be present under resources)
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(viewTitle);
            stage.show();

            println("User " + user.getUsername() + " redirected to " +
                  (user.getRole().equalsIgnoreCase("ADMIN") ? "Admin Panel" : "Customer Dashboard"));

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening main application: " + e.getMessage());
        }
    }

    private void showError(String message) {
        if (lblError != null) {
            UIUtils.showError(lblError, message);
            lblError.setVisible(true);
        }
    }

    private void hideError() {
        if (lblError != null) {
            UIUtils.clearStatus(lblError);
            lblError.setVisible(false);
        }
    }
}
