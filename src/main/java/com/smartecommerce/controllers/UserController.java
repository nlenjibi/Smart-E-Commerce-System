package com.smartecommerce.controllers;

import com.smartecommerce.dao.UserDAO;
import com.smartecommerce.models.User;
import com.smartecommerce.utils.SecurityUtils;
import com.smartecommerce.utils.UIUtils;
import com.smartecommerce.utils.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * UserController manages the user management UI with full CRUD operations
 * Optimized with validation, duplicate checking, and better UX
 */
public class UserController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, LocalDateTime> colCreatedAt;

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cmbRole;
    @FXML private ComboBox<String> cmbRoleFilter;

    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    @FXML private Label lblStatus;

    private final UserDAO userDAO;
    private final ObservableList<User> userList;
    private User selectedUser;

    public UserController() {
        this.userDAO = new UserDAO();
        this.userList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        if (colId != null) {
            // Initialize table columns
            colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
            colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            // Initialize role ComboBox
            cmbRole.setItems(FXCollections.observableArrayList("CUSTOMER", "ADMIN"));

            // Initialize role filter
            cmbRoleFilter.setItems(FXCollections.observableArrayList("ALL", "CUSTOMER", "ADMIN"));
            cmbRoleFilter.setValue("ALL");
            cmbRoleFilter.setOnAction(e -> filterByRole());

            // Load users
            loadUsers();

            // Selection listener
            userTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            selectedUser = newSelection;
                            populateFields(newSelection);
                            btnUpdate.setDisable(false);
                            btnDelete.setDisable(false);
                        }
                    });

            // Double-click to edit
            userTable.setRowFactory(tv -> {
                TableRow<User> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !row.isEmpty()) {
                        handleUpdate();
                    }
                });
                return row;
            });

            // Initial button states
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            // Set placeholder
            userTable.setPlaceholder(new Label("No users found. Click 'Add User' to create one."));
        }
    }

    private void loadUsers() {
        try {
            userList.clear();
            List<User> users = userDAO.findAll();

            if (users != null) {
                userList.addAll(users);
                userTable.setItems(userList);
                showStatus("✅ Loaded " + users.size() + " users", "success");
            } else {
                showStatus("⚠️ No users found", "warning");
            }
        } catch (Exception e) {
            showError("❌ Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterByRole() {
        try {
            String role = cmbRoleFilter.getValue();
            if ("ALL".equals(role)) {
                loadUsers();
            } else {
                List<User> filtered = userDAO.findByRole(role);
                userList.clear();
                if (filtered != null) {
                    userList.addAll(filtered);
                }
                showStatus("🔍 Filtered: " + filtered.size() + " " + role + " users", "info");
            }
        } catch (Exception e) {
            showError("❌ Error filtering users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFields(User user) {
        txtUsername.setText(user.getUsername());
        txtEmail.setText(user.getEmail());
        txtPassword.setText(""); // Don't show password
        cmbRole.setValue(user.getRole());
    }

    @FXML
    private void handleAdd() {
        if (!validateInput()) {
            return;
        }

        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();

        // Check for duplicate username
        if (isDuplicateUsername(username, null)) {
            showError("❌ Username '" + username + "' already exists!");
            return;
        }

        // Check for duplicate email
        if (isDuplicateEmail(email, null)) {
            showError("❌ Email '" + email + "' is already registered!");
            return;
        }

        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(SecurityUtils.hashPassword(txtPassword.getText()));
            user.setRole(cmbRole.getValue());

            int userId = userDAO.create(user);
            if (userId > 0) {
                showStatus("✅ User '" + username + "' added successfully!", "success");
                clearFields();
                loadUsers();
            } else {
                showError("❌ Failed to add user. Please try again.");
            }
        } catch (Exception e) {
            showError("❌ Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        if (selectedUser == null) {
            showError("❌ Please select a user to update");
            return;
        }

        if (!validateInput()) {
            return;
        }

        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();

        // Check for duplicate username (excluding current user)
        if (isDuplicateUsername(username, selectedUser.getUserId())) {
            showError("❌ Username '" + username + "' already exists!");
            return;
        }

        // Check for duplicate email (excluding current user)
        if (isDuplicateEmail(email, selectedUser.getUserId())) {
            showError("❌ Email '" + email + "' is already registered!");
            return;
        }

        // Confirm update
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Update");
        confirmAlert.setHeaderText("Update User");
        confirmAlert.setContentText("Are you sure you want to update user '" + selectedUser.getUsername() + "'?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                selectedUser.setUsername(username);
                selectedUser.setEmail(email);

                // Only update password if a new one was provided
                if (!txtPassword.getText().isEmpty()) {
                    selectedUser.setPasswordHash(SecurityUtils.hashPassword(txtPassword.getText()));
                }

                selectedUser.setRole(cmbRole.getValue());

                if (userDAO.update(selectedUser)) {
                    showStatus("✅ User updated successfully!", "success");
                    clearFields();
                    loadUsers();
                } else {
                    showError("❌ Failed to update user. Please try again.");
                }
            } catch (Exception e) {
                showError("❌ Error updating user: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedUser == null) {
            showError("❌ Please select a user to delete");
            return;
        }

        // Enhanced confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("⚠️ Confirm Delete");
        alert.setHeaderText("Delete User: " + selectedUser.getUsername());
        alert.setContentText("Are you sure you want to delete this user?\n\n" +
                "⚠️ Warning: This action cannot be undone!\n" +
                "Email: " + selectedUser.getEmail() + "\n" +
                "Role: " + selectedUser.getRole());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (userDAO.delete(selectedUser.getUserId()) > 0) {
                    showStatus("✅ User '" + selectedUser.getUsername() + "' deleted successfully!", "success");
                    clearFields();
                    loadUsers();
                } else {
                    showError("❌ Failed to delete user. User may have associated data.");
                }
            } catch (Exception e) {
                showError("❌ Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh() {
        clearFields();
        cmbRoleFilter.setValue("ALL");
        loadUsers();
        showStatus("🔄 Data refreshed", "info");
    }

    private boolean validateInput() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        // Username validation using ValidationUtil
        if (!ValidationUtil.isNotEmpty(username)) {
            showError("❌ Username is required!");
            txtUsername.requestFocus();
            return false;
        }

        if (!ValidationUtil.isStringLengthValid(username, 3, 50)) {
            showError("❌ Username must be between 3 and 50 characters!");
            txtUsername.requestFocus();
            return false;
        }

        if (!ValidationUtil.isValidUsername(username)) {
            showError("❌ Username can only contain letters, numbers, dots, hyphens, and underscores!");
            txtUsername.requestFocus();
            return false;
        }

        // Email validation using ValidationUtil
        if (!ValidationUtil.isValidEmail(email)) {
            showError("❌ Invalid email format! Example: user@example.com");
            txtEmail.requestFocus();
            return false;
        }

        // Password validation (only for new users or when changing password)
        if (selectedUser == null && !ValidationUtil.isNotEmpty(password)) {
            showError("❌ Password is required for new users!");
            txtPassword.requestFocus();
            return false;
        }

        if (ValidationUtil.isNotEmpty(password) && !ValidationUtil.isStringLengthValid(password, 6, 100)) {
            showError("❌ Password must be at least 6 characters long!");
            txtPassword.requestFocus();
            return false;
        }

        // Role validation
        if (cmbRole.getValue() == null || UIUtils.isEmpty(cmbRole.getValue())) {
            showError("❌ Please select a role!");
            cmbRole.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Check if username already exists (case-insensitive)
     * @param username The username to check
     * @param excludeUserId User ID to exclude from check (for updates)
     * @return true if duplicate found, false otherwise
     */
    private boolean isDuplicateUsername(String username, Integer excludeUserId) {
        return userList.stream()
                .filter(user -> excludeUserId == null || user.getUserId() != excludeUserId)
                .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Check if email already exists (case-insensitive)
     * @param email The email to check
     * @param excludeUserId User ID to exclude from check (for updates)
     * @return true if duplicate found, false otherwise
     */
    private boolean isDuplicateEmail(String email, Integer excludeUserId) {
        return userList.stream()
                .filter(user -> excludeUserId == null || user.getUserId() != excludeUserId)
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private void clearFields() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        cmbRole.setValue(null);
        selectedUser = null;
        userTable.getSelectionModel().clearSelection();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        lblStatus.setText("");
    }


    /**
     * Show status message with color coding
     * @param message The message to display
     * @param type Message type: "success", "error", "warning", "info"
     */
    private void showStatus(String message, String type) {
        if (lblStatus != null) {
            switch (type.toLowerCase()) {
                case "success":
                    UIUtils.showSuccess(lblStatus, message);
                    break;
                case "error":
                    UIUtils.showError(lblStatus, message);
                    break;
                case "warning":
                    UIUtils.showWarning(lblStatus, message);
                    break;
                case "info":
                    UIUtils.showInfo(lblStatus, message);
                    break;
                default:
                    lblStatus.setText(message);
            }
        }
    }

    private void showError(String message) {
        showStatus(message, "error");
        UIUtils.showErrorAlert(message);
    }
}

