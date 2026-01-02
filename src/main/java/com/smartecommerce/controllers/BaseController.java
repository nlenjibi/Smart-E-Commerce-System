package com.smartecommerce.controllers;

import com.smartcommerce.utils.AsyncTaskManager;
import com.smartcommerce.utils.UIUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * BaseController - Abstract base class for all controllers
 * Provides reusable async data loading patterns with loading indicators
 * and consistent error handling
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // Loading overlay for visual feedback
    protected StackPane loadingOverlay;

    // Status label for progress messages
    protected Label statusLabel;

    /**
     * Initialize loading overlay
     * Call this in your controller's initialize method
     *
     * @param parentContainer The main container to add overlay to
     * @return The created loading overlay
     */
    protected StackPane initializeLoadingOverlay(javafx.scene.layout.Pane parentContainer) {
        return initializeLoadingOverlay(parentContainer, "Loading...");
    }

    /**
     * Initialize loading overlay with custom message
     *
     * @param parentContainer The main container to add overlay to
     * @param message Default loading message
     * @return The created loading overlay
     */
    protected StackPane initializeLoadingOverlay(javafx.scene.layout.Pane parentContainer, String message) {
        loadingOverlay = UIUtils.createLoadingOverlay(message);
        if (parentContainer != null) {
            parentContainer.getChildren().add(loadingOverlay);
        }
        return loadingOverlay;
    }

    /**
     * Load data asynchronously with loading indicator
     *
     * @param dataLoader The background task to load data
     * @param onSuccess Callback when data loads successfully
     * @param <T> The type of data being loaded
     * @return CompletableFuture for the async operation
     */
    protected <T> CompletableFuture<T> loadDataAsync(
            Callable<T> dataLoader,
            Consumer<T> onSuccess) {

        return loadDataAsync(dataLoader, onSuccess, null);
    }

    /**
     * Load data asynchronously with loading indicator and custom error handler
     *
     * @param dataLoader The background task to load data
     * @param onSuccess Callback when data loads successfully
     * @param onError Custom error handler (if null, uses default)
     * @param <T> The type of data being loaded
     * @return CompletableFuture for the async operation
     */
    protected <T> CompletableFuture<T> loadDataAsync(
            Callable<T> dataLoader,
            Consumer<T> onSuccess,
            Consumer<Throwable> onError) {

        showLoadingState();

        Consumer<Throwable> errorHandler = onError != null ? onError : this::handleAsyncError;

        return AsyncTaskManager.runAsync(
            dataLoader,
            result -> {
                hideLoadingState();
                if (onSuccess != null) {
                    onSuccess.accept(result);
                }
                showCompletionState();
            },
            throwable -> {
                hideLoadingState();
                errorHandler.accept(throwable);
            }
        );
    }

    /**
     * Load data asynchronously without return value
     *
     * @param dataLoader The background task to execute
     * @param onSuccess Callback when task completes successfully
     * @return CompletableFuture for the async operation
     */
    protected CompletableFuture<Void> loadDataAsync(
            Runnable dataLoader,
            Runnable onSuccess) {

        return loadDataAsync(dataLoader, onSuccess, null);
    }

    /**
     * Load data asynchronously without return value and custom error handler
     *
     * @param dataLoader The background task to execute
     * @param onSuccess Callback when task completes successfully
     * @param onError Custom error handler (if null, uses default)
     * @return CompletableFuture for the async operation
     */
    protected CompletableFuture<Void> loadDataAsync(
            Runnable dataLoader,
            Runnable onSuccess,
            Consumer<Throwable> onError) {

        showLoadingState();

        Consumer<Throwable> errorHandler = onError != null ? onError : this::handleAsyncError;

        return AsyncTaskManager.runAsync(
            dataLoader,
            () -> {
                hideLoadingState();
                if (onSuccess != null) {
                    onSuccess.run();
                }
                showCompletionState();
            },
            throwable -> {
                hideLoadingState();
                errorHandler.accept(throwable);
            }
        );
    }

    /**
     * Show loading state
     */
    protected void showLoadingState() {
        showLoadingState("Loading data...");
    }

    /**
     * Show loading state with custom message
     *
     * @param message Loading message
     */
    protected void showLoadingState(String message) {
        UIUtils.showLoading(loadingOverlay);
        UIUtils.showLoadingStatus(statusLabel, message);
        logger.debug("Loading state shown: {}", message);
    }

    /**
     * Hide loading state
     */
    protected void hideLoadingState() {
        UIUtils.hideLoading(loadingOverlay);
        logger.debug("Loading state hidden");
    }

    /**
     * Show completion state
     */
    protected void showCompletionState() {
        showCompletionState("Data loaded successfully");
    }

    /**
     * Show completion state with custom message
     *
     * @param message Completion message
     */
    protected void showCompletionState(String message) {
        UIUtils.showCompletionStatus(statusLabel, message);
        logger.info("Completion: {}", message);

        // Auto-clear after 3 seconds
        AsyncTaskManager.scheduleTask(
            () -> AsyncTaskManager.runOnUIThread(() -> UIUtils.clearStatus(statusLabel)),
            3,
            java.util.concurrent.TimeUnit.SECONDS
        );
    }

    /**
     * Default async error handler
     * Override this method for custom error handling
     *
     * @param throwable The error that occurred
     */
    protected void handleAsyncError(Throwable throwable) {
        logger.error("Async operation failed", throwable);

        String errorMessage = throwable.getMessage() != null
            ? throwable.getMessage()
            : "An unexpected error occurred";

        UIUtils.showError(statusLabel, errorMessage);
        UIUtils.showErrorAlert("Failed to load data: " + errorMessage);
    }

    /**
     * Update progress message
     *
     * @param message Progress message
     */
    protected void updateProgress(String message) {
        UIUtils.updateProgress(statusLabel, message);
        logger.debug("Progress: {}", message);
    }

    /**
     * Show error message
     *
     * @param message Error message
     */
    protected void showError(String message) {
        UIUtils.showError(statusLabel, message);
        logger.error("Error: {}", message);
    }

    /**
     * Show success message
     *
     * @param message Success message
     */
    protected void showSuccess(String message) {
        UIUtils.showSuccess(statusLabel, message);
        logger.info("Success: {}", message);
    }

    /**
     * Show warning message
     *
     * @param message Warning message
     */
    protected void showWarning(String message) {
        UIUtils.showWarning(statusLabel, message);
        logger.warn("Warning: {}", message);
    }
}

