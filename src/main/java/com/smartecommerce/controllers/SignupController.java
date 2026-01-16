package com.smartecommerce.controllers;

import com.smartecommerce.dao.UserDAO;
import com.smartecommerce.models.User;
import com.smartecommerce.utils.PasswordValidator;
import com.smartecommerce.utils.SecurityUtils;
import com.smartecommerce.utils.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


/**
 * SignupController handles new user registration with validation and security
 */
public class SignupController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private CheckBox chkTerms;
    @FXML private Button btnSignup;
    
    @FXML private Label lblMessage;
    @FXML private Label lblUsernameError;
    @FXML private Label lblEmailError;
    @FXML private Label lblPasswordError;
    @FXML private Label lblConfirmPasswordError;

    private UserDAO userDAO;
    private PasswordValidator passwordValidator;


    public SignupController() {
        this.userDAO = new UserDAO();
        this.passwordValidator = new PasswordValidator();
    }

    @FXML
    public void initialize() {
        // Populate role ComboBox
        if (cmbRole != null) {
            cmbRole.getItems().addAll("CUSTOMER", "ADMIN");
            cmbRole.setValue("CUSTOMER");
        }

        // Bind managed property to visible property for error labels
        bindManagedToVisible(lblUsernameError);
        bindManagedToVisible(lblEmailError);
        bindManagedToVisible(lblPasswordError);
        bindManagedToVisible(lblConfirmPasswordError);
        bindManagedToVisible(lblMessage);

        // Add real-time validation listeners
        if (txtUsername != null) {
            txtUsername.textProperty().addListener((obs, old, newVal) -> validateUsernameRealtime());
        }
        if (txtEmail != null) {
            txtEmail.textProperty().addListener((obs, old, newVal) -> validateEmailRealtime());
        }
        if (txtPassword != null) {
            txtPassword.textProperty().addListener((obs, old, newVal) -> validatePasswordRealtime());
        }
        if (txtConfirmPassword != null) {
            txtConfirmPassword.textProperty().addListener((obs, old, newVal) -> validateConfirmPasswordRealtime());
        }
    }

    /**
     * Bind managed property to visible property for dynamic layout adjustment
     */
    private void bindManagedToVisible(Label label) {
        if (label != null) {
            label.managedProperty().bind(label.visibleProperty());
        }
    }

    @FXML
    private void handleSignup() {
        // Clear previous messages
        clearErrorMessages();

        // Validate all fields
        if (!validateAllFields()) {
            return;
        }

        // Check terms agreement
        if (!chkTerms.isSelected()) {
            showMessage("Please agree to the Terms and Conditions", "error");
            return;
        }

        // Create new user
        try {
            User user = new User();
            user.setUsername(txtUsername.getText().trim());
            user.setEmail(txtEmail.getText().trim().toLowerCase());

            // Hash password with SHA-256
            String hashedPassword = SecurityUtils.hashPassword(txtPassword.getText());
            user.setPasswordHash(hashedPassword);
            user.setRole(cmbRole.getValue());

            // Save to database
            if (userDAO.create(user) > 0) {
                showMessage("Account created successfully! Redirecting to login...", "success");

                // Delay and redirect to login
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(this::redirectToLogin);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showMessage("Registration failed. Username or email may already exist.", "error");
            }
        } catch (Exception e) {
            showMessage("Error: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() {
        redirectToLogin();
    }


    private void redirectToLogin() {
        try {
            Stage stage = (Stage) btnSignup.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/smartecommerce/ui/views/login.fxml")
            );
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error loading login page", "error");
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        // Validate username
        if (!validateUsername()) {
            isValid = false;
        }

        // Validate email
        if (!validateEmail()) {
            isValid = false;
        }

        // Validate password
        if (!validatePassword()) {
            isValid = false;
        }

        // Validate confirm password
        if (!validateConfirmPassword()) {
            isValid = false;
        }

        // Validate role selection
        if (cmbRole.getValue() == null) {
            showMessage("Please select an account type", "error");
            isValid = false;
        }

        return isValid;
    }

    private boolean validateUsername() {
        String username = txtUsername.getText().trim();

        if (!ValidationUtil.isNotEmpty(username)) {
            showFieldError(lblUsernameError, "Username is required");
            return false;
        }

        if (!ValidationUtil.isStringLengthValid(username, 3, 50)) {
            showFieldError(lblUsernameError, "Username must be between 3 and 50 characters");
            return false;
        }

        if (!ValidationUtil.isValidUsername(username)) {
            showFieldError(lblUsernameError, "Username can only contain letters, numbers, and underscores");
            return false;
        }

        // Check if username already exists
        if (userDAO.findByUsername(username) != null) {
            showFieldError(lblUsernameError, "Username already taken");
            return false;
        }

        hideFieldError(lblUsernameError);
        return true;
    }

    private boolean validateEmail() {
        String email = txtEmail.getText().trim();

        if (!ValidationUtil.isValidEmail(email)) {
            showFieldError(lblEmailError, "Invalid email format");
            return false;
        }

        // Check if email already exists
        if (userDAO.findByEmail(email) != null) {
            showFieldError(lblEmailError, "Email already registered");
            return false;
        }

        hideFieldError(lblEmailError);
        return true;
    }

    private boolean validatePassword() {
        String password = txtPassword.getText();

        if (password.isEmpty()) {
            showFieldError(lblPasswordError, "Password is required");
            return false;
        }

        // Use PasswordValidator for comprehensive validation
        PasswordValidator.ValidationResult result = passwordValidator.validate(password);

        if (!result.isValid()) {
            showFieldError(lblPasswordError, result.getErrorMessage());
            return false;
        }

        hideFieldError(lblPasswordError);
        return true;
    }

    private boolean validateConfirmPassword() {
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (confirmPassword.isEmpty()) {
            showFieldError(lblConfirmPasswordError, "Please confirm your password");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showFieldError(lblConfirmPasswordError, "Passwords do not match");
            return false;
        }

        hideFieldError(lblConfirmPasswordError);
        return true;
    }

    // Real-time validation methods
    private void validateUsernameRealtime() {
        String username = txtUsername.getText().trim();
        if (!username.isEmpty()) {
            validateUsername();
        } else {
            hideFieldError(lblUsernameError);
        }
    }

    private void validateEmailRealtime() {
        String email = txtEmail.getText().trim();
        if (!email.isEmpty()) {
            validateEmail();
        } else {
            hideFieldError(lblEmailError);
        }
    }

    private void validatePasswordRealtime() {
        String password = txtPassword.getText();
        if (!password.isEmpty()) {
            validatePassword();
        } else {
            hideFieldError(lblPasswordError);
        }
    }

    private void validateConfirmPasswordRealtime() {
        String confirmPassword = txtConfirmPassword.getText();
        if (!confirmPassword.isEmpty()) {
            validateConfirmPassword();
        } else {
            hideFieldError(lblConfirmPasswordError);
        }
    }

    private void showFieldError(Label errorLabel, String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideFieldError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }

    private void showMessage(String message, String type) {
        if (lblMessage != null) {
            lblMessage.setText(message);
            lblMessage.setVisible(true);

            if ("error".equals(type)) {
                lblMessage.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c;");
            } else if ("success".equals(type)) {
                lblMessage.setStyle("-fx-font-size: 12px; -fx-text-fill: #27ae60;");
            } else {
                lblMessage.setStyle("-fx-font-size: 12px; -fx-text-fill: #3498db;");
            }
        }
    }

    private void clearErrorMessages() {
        hideFieldError(lblUsernameError);
        hideFieldError(lblEmailError);
        hideFieldError(lblPasswordError);
        hideFieldError(lblConfirmPasswordError);
        if (lblMessage != null) {
            lblMessage.setVisible(false);
        }
    }
}

