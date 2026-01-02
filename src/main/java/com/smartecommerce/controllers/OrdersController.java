package com.smartecommerce.controllers;

import com.smartecommerce.models.Order;
import com.smartecommerce.service.OrderService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OrdersController - manages order viewing and status updates
 * NOTE: Order creation is NOT allowed - this is management only
 * OPTIMIZED: Uses AsyncTaskManager for non-blocking data loading
 */
public class OrdersController extends BaseController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(OrdersController.class.getName());

    // Order status constants
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_COMPLETED = "DELIVERED";
    private static final String STATUS_CANCELLED = "CANCELLED";

    @FXML private Label totalOrdersLabel;
    @FXML private Label pendingOrdersLabel;
    @FXML private Label completedOrdersLabel;
    @FXML private Label cancelledOrdersLabel;
    @FXML private VBox contentContainer;

    private final OrderService orderService;
    private final ObservableList<Order> ordersList = FXCollections.observableArrayList();

    public OrdersController() {
        this.orderService = new OrderService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initializing OrdersController with async loading...");

        // Initialize loading overlay if contentContainer is available
        if (contentContainer != null) {
            initializeLoadingOverlay(contentContainer, "Loading orders...");
        }

        loadOrderDataAsync();
        LOGGER.info("OrdersController initialized successfully");
    }

    /**
     * Load all orders from database asynchronously and populate UI
     * OPTIMIZED: Non-blocking async operation with loading feedback
     */
    private void loadOrderDataAsync() {
        loadDataAsync(
            () -> {
                // Background task: Fetch orders from database
                updateProgress("Fetching orders from database...");
                return orderService.getAllOrders();
            },
            orders -> {
                // UI update: Process and display orders
                ordersList.setAll(orders);
                updateStatistics(orders);
                LOGGER.info(String.format("Loaded %d orders asynchronously", orders.size()));
            },
            throwable -> {
                // Error handling
                LOGGER.log(Level.SEVERE, "Failed to load orders asynchronously", throwable);
                showError("Failed to load orders", throwable.getMessage());
            }
        );
    }

    /**
     * Reload order data (for refresh operations)
     */
    public void refreshOrderData() {
        LOGGER.info("Refreshing order data...");
        loadOrderDataAsync();
    }

    /**
     * Update order statistics in the UI
     */
    private void updateStatistics(List<Order> orders) {
        int total = orders.size();
        long pending = orders.stream()
                .filter(o -> STATUS_PENDING.equalsIgnoreCase(String.valueOf(o.getStatus())))
                .count();
        long completed = orders.stream()
                .filter(o -> STATUS_COMPLETED.equalsIgnoreCase(String.valueOf(o.getStatus())))
                .count();
        long cancelled = orders.stream()
                .filter(o -> STATUS_CANCELLED.equalsIgnoreCase(String.valueOf(o.getStatus())))
                .count();

        safeSetText(totalOrdersLabel, String.valueOf(total));
        safeSetText(pendingOrdersLabel, String.valueOf(pending));
        safeSetText(completedOrdersLabel, String.valueOf(completed));
        safeSetText(cancelledOrdersLabel, String.valueOf(cancelled));
    }

    /**
     * Handle View Orders button - displays full orders table
     */
    @FXML
    private void handleViewOrders() {
        LOGGER.info("View Orders button clicked");

        if (contentContainer == null) {
            LOGGER.warning("Content container is null");
            return;
        }

        // Clear existing content
        contentContainer.getChildren().clear();

        // Create table view
        TableView<Order> table = createOrdersTable();
        table.setItems(ordersList);

        VBox tableContainer = new VBox(15);
        tableContainer.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 12;");

        Label title = new Label("All Orders");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Filter buttons
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        Button btnAll = new Button("All");
        Button btnPending = new Button("Pending");
        Button btnCompleted = new Button("Completed");
        Button btnCancelled = new Button("Cancelled");

        btnAll.setOnAction(e -> table.setItems(FXCollections.observableArrayList(ordersList)));
        btnPending.setOnAction(e -> filterOrders(STATUS_PENDING, table));
        btnCompleted.setOnAction(e -> filterOrders(STATUS_COMPLETED, table));
        btnCancelled.setOnAction(e -> filterOrders(STATUS_CANCELLED, table));

        for (Button btn : new Button[]{btnAll, btnPending, btnCompleted, btnCancelled}) {
            btn.setStyle("-fx-background-color: #e5e7eb; -fx-padding: 8 16; -fx-cursor: hand;");
        }

        filterBar.getChildren().addAll(btnAll, btnPending, btnCompleted, btnCancelled);

        tableContainer.getChildren().addAll(title, filterBar, table);
        VBox.setMargin(table, new Insets(10, 0, 0, 0));

        contentContainer.getChildren().add(tableContainer);
    }

    /**
     * Filter orders by status
     */
    private void filterOrders(String status, TableView<Order> table) {
        List<Order> filtered = orderService.filterByStatus(status);
        table.setItems(FXCollections.observableArrayList(filtered));
    }

    /**
     * Create and configure the orders table
     */
    private TableView<Order> createOrdersTable() {
        TableView<Order> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<Order, Integer> colOrderId = new TableColumn<>("Order ID");
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderId.setPrefWidth(80);

        TableColumn<Order, String> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleStringProperty(order.getUserId() > 0 ? "User #" + order.getUserId() : "N/A");
        });
        colCustomer.setPrefWidth(120);

        TableColumn<Order, String> colDate = new TableColumn<>("Order Date");
        colDate.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            LocalDateTime date = order.getOrderDate();
            String formatted = date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A";
            return new SimpleStringProperty(formatted);
        });
        colDate.setPrefWidth(140);

        TableColumn<Order, String> colAmount = new TableColumn<>("Total Amount");
        colAmount.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            BigDecimal amount = order.getTotalAmount();
            String formatted = amount != null ? NumberFormat.getCurrencyInstance(Locale.US).format(amount) : "$0.00";
            return new SimpleStringProperty(formatted);
        });
        colAmount.setPrefWidth(120);

        TableColumn<Order, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            String status = order.getStatus() != null ? order.getStatus() : "UNKNOWN";
            return new SimpleStringProperty(status);
        });
        colStatus.setPrefWidth(100);

        TableColumn<Order, Void> colActions = new TableColumn<>("Actions");
        colActions.setPrefWidth(180);
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Update Status");
            private final Button btnCancel = new Button("Cancel");

            {
                btnUpdate.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 5 10; -fx-cursor: hand; -fx-font-size: 11px;");
                btnCancel.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 5 10; -fx-cursor: hand; -fx-font-size: 11px;");

                btnUpdate.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    handleUpdateStatus(order);
                });

                btnCancel.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    handleCancelOrder(order);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, btnUpdate, btnCancel);
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(colOrderId, colCustomer, colDate, colAmount, colStatus, colActions);

        return table;
    }

    /**
     * Handle Update Status button - shows status selection dialog
     */
    @FXML
    private void handleUpdateStatusAction() {
        LOGGER.info("Update Status / Cancel button clicked");

        // Show dialog to select an order
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Enter Order ID to update");
        dialog.setContentText("Order ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(orderId -> {
            try {
                int id = Integer.parseInt(orderId);
                Order order = orderService.getOrderById(id);
                if (order != null) {
                    handleUpdateStatus(order);
                } else {
                    showError("Order Not Found", "No order found with ID: " + id);
                }
            } catch (NumberFormatException e) {
                showError("Invalid Input", "Please enter a valid order ID number");
            }
        });
    }

    /**
     * Handle updating order status
     */
    private void handleUpdateStatus(Order order) {
        LOGGER.info("Updating status for order: " + order.getOrderId());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(STATUS_PENDING,
                STATUS_PENDING, "CONFIRMED", STATUS_COMPLETED, STATUS_CANCELLED);
        dialog.setTitle("Update Order Status");
        dialog.setHeaderText("Order #" + order.getOrderId());
        dialog.setContentText("Select new status:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(status -> {
            boolean success = orderService.updateOrderStatus(order.getOrderId(), status);
            if (success) {
                LOGGER.info("Order status updated successfully");
                showSuccess("Success", "Order status updated to: " + status);
                refreshOrderData(); // Refresh data asynchronously
            } else {
                showError("Update Failed", "Failed to update order status");
            }
        });
    }

    /**
     * Handle cancelling an order
     */
    private void handleCancelOrder(Order order) {
        LOGGER.info("Cancelling order: " + order.getOrderId());

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Cancel Order");
        confirmation.setHeaderText("Cancel Order #" + order.getOrderId());
        confirmation.setContentText("Are you sure you want to cancel this order?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = orderService.updateOrderStatus(order.getOrderId(), STATUS_CANCELLED);
            if (success) {
                LOGGER.info("Order cancelled successfully");
                showSuccess("Success", "Order has been cancelled");
                refreshOrderData(); // Refresh data asynchronously
            } else {
                showError("Cancel Failed", "Failed to cancel order");
            }
        }
    }

    // Helper methods
    private void safeSetText(Label label, String text) {
        if (label != null) {
            label.setText(text);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
