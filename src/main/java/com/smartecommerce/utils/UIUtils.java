package com.smartecommerce.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * UI Utility class for common JavaFX controller operations
 * Reduces code duplication across controllers
 */
public class UIUtils {
    private static final Logger logger = LoggerFactory.getLogger(UIUtils.class);
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    private UIUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Show success message in a label with green color
     */
    public static void showSuccess(Label label, String message) {
        if (label != null) {
            label.setText(message);
            label.setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
            logger.info("Success: {}", message);
        }
    }

    /**
     * Show error message in a label with red color
     */
    public static void showError(Label label, String message) {
        if (label != null) {
            label.setText(message);
            label.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            logger.error("Error: {}", message);
        }
    }

    /**
     * Show warning message in a label with orange color
     */
    public static void showWarning(Label label, String message) {
        if (label != null) {
            label.setText(message);
            label.setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold;");
            logger.warn("Warning: {}", message);
        }
    }

    /**
     * Show info message in a label with blue color
     */
    public static void showInfo(Label label, String message) {
        if (label != null) {
            label.setText(message);
            label.setStyle("-fx-text-fill: #3b82f6;");
            logger.info("Info: {}", message);
        }
    }

    /**
     * Show alert dialog
     */
    public static void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    /**
     * Show confirmation dialog and return result
     */
    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Show error alert
     */
    public static void showErrorAlert(String message) {
        showAlert(AlertType.ERROR, "Error", "Operation Failed", message);
    }

    /**
     * Show success alert
     */
    public static void showSuccessAlert(String message) {
        showAlert(AlertType.INFORMATION, "Success", "Operation Successful", message);
    }

    /**
     * Show warning alert
     */
    public static void showWarningAlert(String message) {
        showAlert(AlertType.WARNING, "Warning", "Warning", message);
    }

    /**
     * Format currency value
     */
    public static String formatCurrency(BigDecimal value) {
        if (value == null) {
            return CURRENCY_FORMAT.format(0);
        }
        return CURRENCY_FORMAT.format(value);
    }

    /**
     * Format currency value from double
     */
    public static String formatCurrency(double value) {
        return CURRENCY_FORMAT.format(value);
    }

    /**
     * Parse text to BigDecimal with validation
     */
    public static Optional<BigDecimal> parseBigDecimal(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return Optional.empty();
            }
            BigDecimal value = new BigDecimal(text.trim());
            if (value.compareTo(BigDecimal.ZERO) < 0) {
                return Optional.empty();
            }
            return Optional.of(value);
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse BigDecimal from: {}", text);
            return Optional.empty();
        }
    }

    /**
     * Parse text to Integer with validation
     */
    public static Optional<Integer> parseInteger(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                return Optional.empty();
            }
            int value = Integer.parseInt(text.trim());
            if (value < 0) {
                return Optional.empty();
            }
            return Optional.of(value);
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse Integer from: {}", text);
            return Optional.empty();
        }
    }

    /**
     * Validate required field
     */
    public static boolean isValidRequired(String value, String fieldName, Consumer<String> errorHandler) {
        if (value == null || value.trim().isEmpty()) {
            errorHandler.accept(fieldName + " is required");
            return false;
        }
        return true;
    }

    /**
     * Validate numeric field
     */
    public static boolean isValidNumeric(String value, String fieldName, Consumer<String> errorHandler) {
        if (!isValidRequired(value, fieldName, errorHandler)) {
            return false;
        }
        try {
            double num = Double.parseDouble(value.trim());
            if (num <= 0) {
                errorHandler.accept(fieldName + " must be greater than 0");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            errorHandler.accept(fieldName + " must be a valid number");
            return false;
        }
    }

    /**
     * Validate price field
     */
    public static boolean isValidPrice(String value, Consumer<String> errorHandler) {
        return isValidNumeric(value, "Price", errorHandler);
    }

    /**
     * Clear label status
     */
    public static void clearStatus(Label label) {
        if (label != null) {
            label.setText("");
            label.setStyle("");
        }
    }

    /**
     * Safe text trimming
     */
    public static String safeTrim(String text) {
        return text == null ? "" : text.trim();
    }

    /**
     * Check if string is empty or null
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    // ==================== LOADING INDICATOR UTILITIES ====================

    /**
     * Create a loading overlay with progress indicator
     * Use this to show loading state while async operations are running
     *
     * @return StackPane overlay that can be added to a parent container
     */
    public static StackPane createLoadingOverlay() {
        return createLoadingOverlay("Loading...");
    }

    /**
     * Create a loading overlay with custom message
     *
     * @param message Loading message to display
     * @return StackPane overlay with progress indicator and message
     */
    public static StackPane createLoadingOverlay(String message) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setVisible(false);
        overlay.setManaged(false);

        // Create loading content
        VBox loadingBox = new VBox(15);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 10;");
        loadingBox.setMaxWidth(300);
        loadingBox.setMaxHeight(150);

        ProgressIndicator progress = new ProgressIndicator();
        progress.setPrefSize(50, 50);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4b5563;");

        loadingBox.getChildren().addAll(progress, messageLabel);
        overlay.getChildren().add(loadingBox);

        return overlay;
    }

    /**
     * Show loading overlay
     *
     * @param overlay The loading overlay to show
     */
    public static void showLoading(StackPane overlay) {
        if (overlay != null) {
            overlay.setVisible(true);
            overlay.setManaged(true);
            overlay.toFront();
            logger.debug("Loading overlay shown");
        }
    }

    /**
     * Hide loading overlay
     *
     * @param overlay The loading overlay to hide
     */
    public static void hideLoading(StackPane overlay) {
        if (overlay != null) {
            overlay.setVisible(false);
            overlay.setManaged(false);
            logger.debug("Loading overlay hidden");
        }
    }

    /**
     * Show loading state in a label
     *
     * @param label Status label to update
     */
    public static void showLoadingStatus(Label label) {
        showLoadingStatus(label, "Loading...");
    }

    /**
     * Show loading state with custom message
     *
     * @param label Status label to update
     * @param message Custom loading message
     */
    public static void showLoadingStatus(Label label, String message) {
        if (label != null) {
            label.setText("⏳ " + message);
            label.setStyle("-fx-text-fill: #3b82f6; -fx-font-size: 13px;");
            logger.debug("Loading status: {}", message);
        }
    }

    /**
     * Update progress message
     *
     * @param label Status label to update
     * @param message Progress message
     */
    public static void updateProgress(Label label, String message) {
        if (label != null) {
            label.setText("⚙️ " + message);
            label.setStyle("-fx-text-fill: #6366f1; -fx-font-size: 13px;");
            logger.debug("Progress: {}", message);
        }
    }

    /**
     * Show completion status
     *
     * @param label Status label to update
     */
    public static void showCompletionStatus(Label label) {
        showCompletionStatus(label, "Loaded successfully!");
    }

    /**
     * Show completion status with custom message
     *
     * @param label Status label to update
     * @param message Completion message
     */
    public static void showCompletionStatus(Label label, String message) {
        if (label != null) {
            label.setText("✓ " + message);
            label.setStyle("-fx-text-fill: #10b981; -fx-font-size: 13px;");
            logger.info("Completion: {}", message);
        }
    }

    /**
     * Disable UI controls during async operations
     *
     * @param controls Controls to disable
     */
    public static void disableControls(javafx.scene.Node... controls) {
        for (javafx.scene.Node control : controls) {
            if (control != null) {
                control.setDisable(true);
            }
        }
    }

    /**
     * Enable UI controls after async operations
     *
     * @param controls Controls to enable
     */
    public static void enableControls(javafx.scene.Node... controls) {
        for (javafx.scene.Node control : controls) {
            if (control != null) {
                control.setDisable(false);
            }
        }
    }
}
