package com.smartecommerce.controllers;

import com.smartecommerce.optimization.CacheDemo;
import com.smartecommerce.optimization.SearchDemo;
import com.smartecommerce.optimization.SortDemo;
import com.smartecommerce.performance.PerformanceReport;
import com.smartecommerce.performance.QueryTimer;
import com.smartecommerce.service.ProductService;
import com.smartecommerce.service.ReportService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Map;

import static java.lang.IO.print;

/**
 * AnaliticsController manages analytics panel and performance monitoring
 * Optimized for comprehensive business intelligence and reporting
 */
public class AnaliticsController {

    @FXML private TextArea txtConsole;

    @FXML private Label lblStatus;

    private final ProductService productService;
    private final ReportService reportService;

    public AnaliticsController() {
        this.productService = new ProductService();
        this.reportService = new ReportService();
    }

    @FXML
    public void initialize() {
        appendToConsole("Analytics Panel Initialized\n");
        appendToConsole("=".repeat(60) + "\n");
        appendToConsole("Available Reports:\n");
        appendToConsole("  - Sales Summary Report\n");
        appendToConsole("  - Dashboard Analytics\n");
        appendToConsole("  - Inventory Report\n");
        appendToConsole("  - User Analytics\n");
        appendToConsole("  - Performance Metrics\n");
        appendToConsole("=".repeat(60) + "\n");
    }

    @FXML
    private void handleCacheDemo() {
        appendToConsole("\nRunning Cache Demonstration...\n");
        try {
          appendToConsole(CacheDemo.demonstrateCaching());
            appendToConsole("✓ Cache demo completed successfully.\n");
            showStatus("Cache demo completed");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Cache demo failed");
        }
    }

    @FXML
    private void handleSearchDemo() {
        appendToConsole("\nRunning Search Demonstration...\n");
        try {
            appendToConsole(SearchDemo.demonstrateSearch());
            appendToConsole("✓ Search demo completed successfully.\n");
            showStatus("Search demo completed");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Search demo failed");
        }
    }

    @FXML
    private void handleSortDemo() {
        appendToConsole("\nRunning Sort Demonstration...\n");
        try {
            appendToConsole(SortDemo.demonstrateSorting());
            appendToConsole("✓ Sort demo completed successfully.\n");
            showStatus("Sort demo completed");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Sort demo failed");
        }
    }

    @FXML
    private void handleGenerateReport() {
        appendToConsole("\nGenerating Performance Report...\n");
        appendToConsole("=".repeat(60) + "\n");
        try {
            PerformanceReport.generateReport(productService);
            PerformanceReport.printSummary();
            appendToConsole("✓ Performance report generated successfully!\n");
            appendToConsole("Check 'performance_report.txt' in the project directory.\n");
            showStatus("Report generated successfully");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Report generation failed");
        }
    }

    @FXML
    private void handleQueryStats() {
        appendToConsole("\n📊 Query Performance Statistics\n");
        appendToConsole("=".repeat(60) + "\n");
        Map<String, String> stats = QueryTimer.getAllStats();
        if (stats.isEmpty()) {
            appendToConsole("No query statistics available yet.\n");
            appendToConsole("Run some operations first to collect data.\n");
        } else {
            stats.forEach((query, stat) -> {
                appendToConsole("Query: " + query + "\n");
                appendToConsole("  Stats: " + stat + "\n\n");
            });
        }
        appendToConsole("=".repeat(60) + "\n");
        showStatus("Query statistics displayed");
    }

    @FXML
    private void handleClearCache() {
        appendToConsole("\n🗑️ Clearing all caches...\n");
        productService.clearCache();
        appendToConsole(productService.getCacheStats() + "\n");
        appendToConsole("✓ Cache cleared successfully.\n");
        showStatus("Cache cleared");
    }

    @FXML
    private void handleSalesReport() {
        appendToConsole("\n💰 Sales Summary Report\n");
        appendToConsole("=".repeat(60) + "\n");
        try {
            Map<String, Object> summary = reportService.generateSalesSummary();

            if (summary.containsKey("error")) {
                appendToConsole("✗ Error: " + summary.get("error") + "\n");
                showError("Sales report failed");
                return;
            }

            appendToConsole("📦 Total Orders: " + summary.get("totalOrders") + "\n");
            appendToConsole("💵 Total Revenue: " + summary.get("totalRevenue") + "\n");
            appendToConsole("📊 Average Order Value: " + summary.get("averageOrderValue") + "\n\n");

            appendToConsole("📈 Time-based Metrics:\n");
            appendToConsole("  Last 24 hours: " + summary.get("ordersLast24h") + " orders, " + summary.get("revenueLast24h") + "\n");
            appendToConsole("  Last 7 days: " + summary.get("ordersLast7d") + " orders, " + summary.get("revenueLast7d") + "\n");
            appendToConsole("  Last 30 days: " + summary.get("ordersLast30d") + " orders, " + summary.get("revenueLast30d") + "\n\n");

            appendToConsole("📋 Orders by Status:\n");
            @SuppressWarnings("unchecked")
            Map<String, Integer> statusCounts = (Map<String, Integer>) summary.get("ordersByStatus");
            if (statusCounts != null && !statusCounts.isEmpty()) {
                statusCounts.forEach((status, count) -> {
                    appendToConsole("  " + status + ": " + count + "\n");
                });
            } else {
                appendToConsole("  No order status data available\n");
            }

            appendToConsole("=".repeat(60) + "\n");
            showStatus("Sales report generated");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            e.printStackTrace();
            showError("Sales report failed");
        }
    }

    /**
     * Generate comprehensive dashboard analytics
     */
    @FXML
    public void handleDashboardAnalytics() {
        appendToConsole("\n📊 Dashboard Analytics\n");
        appendToConsole("=".repeat(60) + "\n");
        try {
            Map<String, Object> analytics = reportService.generateDashboardAnalytics();

            if (analytics.containsKey("error")) {
                appendToConsole("✗ Error: " + analytics.get("error") + "\n");
                showError("Dashboard analytics failed");
                return;
            }

            appendToConsole("🔢 Key Metrics:\n");
            appendToConsole("  Total Orders: " + analytics.get("totalOrders") + "\n");
            appendToConsole("  Total Revenue: " + analytics.get("totalRevenue") + "\n");
            appendToConsole("  Average Order: " + analytics.get("averageOrderValue") + "\n");
            appendToConsole("  Total Products: " + analytics.get("totalProducts") + "\n");
            appendToConsole("  Total Users: " + analytics.get("totalUsers") + "\n");
            appendToConsole("  Active Users: " + analytics.get("activeUsers") + "\n\n");

            appendToConsole("⚠️ Inventory Alerts:\n");
            appendToConsole("  Low Stock: " + analytics.get("lowStockCount") + " products\n");
            appendToConsole("  Out of Stock: " + analytics.get("outOfStockCount") + " products\n");

            appendToConsole("=".repeat(60) + "\n");
            showStatus("Dashboard analytics generated");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Dashboard analytics failed");
        }
    }

    /**
     * Generate inventory report with alerts
     */
    @FXML
    public void handleInventoryReport() {
        appendToConsole("\n📦 Inventory Report\n");
        appendToConsole("=".repeat(60) + "\n");
        try {
            Map<String, Object> report = reportService.generateInventoryReport();

            if (report.containsKey("error")) {
                appendToConsole("✗ Error: " + report.get("error") + "\n");
                showError("Inventory report failed");
                return;
            }

            appendToConsole("📊 Inventory Summary:\n");
            appendToConsole("  Total Products: " + report.get("totalProducts") + "\n");
            appendToConsole("  Total Value: " + report.get("totalInventoryValue") + "\n");
            appendToConsole("  Average Price: " + report.get("averageProductPrice") + "\n\n");

            appendToConsole("⚠️ Stock Alerts:\n");
            appendToConsole("  Low Stock: " + report.get("lowStockCount") + " products\n");
            appendToConsole("  Out of Stock: " + report.get("outOfStockCount") + " products\n\n");

            appendToConsole("📁 Products by Category:\n");
            @SuppressWarnings("unchecked")
            Map<String, Integer> categoryMap = (Map<String, Integer>) report.get("productsByCategory");
            if (categoryMap != null && !categoryMap.isEmpty()) {
                categoryMap.forEach((category, count) -> {
                    appendToConsole("  " + category + ": " + count + " products\n");
                });
            }

            appendToConsole("=".repeat(60) + "\n");
            showStatus("Inventory report generated");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Inventory report failed");
        }
    }

    /**
     * Generate user analytics report
     */
    @FXML
    public void handleUserAnalytics() {
        appendToConsole("\n👥 User Analytics Report\n");
        appendToConsole("=".repeat(60) + "\n");
        try {
            Map<String, Object> report = reportService.generateUserReport();

            if (report.containsKey("error")) {
                appendToConsole("✗ Error: " + report.get("error") + "\n");
                showError("User analytics failed");
                return;
            }

            appendToConsole("📊 User Statistics:\n");
            appendToConsole("  Total Users: " + report.get("totalUsers") + "\n");
            appendToConsole("  Active Users: " + report.get("activeUsers") + "\n\n");

            appendToConsole("📈 Registration Trends:\n");
            appendToConsole("  Last 24 hours: " + report.get("registrationsLast24h") + " users\n");
            appendToConsole("  Last 7 days: " + report.get("registrationsLast7d") + " users\n");
            appendToConsole("  Last 30 days: " + report.get("registrationsLast30d") + " users\n\n");

            appendToConsole("👤 Users by Role:\n");
            @SuppressWarnings("unchecked")
            Map<String, Integer> roleMap = (Map<String, Integer>) report.get("usersByRole");
            if (roleMap != null && !roleMap.isEmpty()) {
                roleMap.forEach((role, count) -> {
                    appendToConsole("  " + role + ": " + count + " users\n");
                });
            }

            appendToConsole("=".repeat(60) + "\n");
            showStatus("User analytics generated");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("User analytics failed");
        }
    }

    /**
     * Generate performance metrics report
     */
    @FXML
    public void handlePerformanceMetrics() {
        appendToConsole("\n⚡ Performance Metrics Report\n");
        appendToConsole("=".repeat(60) + "\n");
        try {
            Map<String, Object> report = reportService.generatePerformanceReport();

            if (report.containsKey("error")) {
                appendToConsole("✗ Error: " + report.get("error") + "\n");
                showError("Performance metrics failed");
                return;
            }

            appendToConsole("📦 Order Growth:\n");
            appendToConsole("  24h: " + report.get("orderGrowth24h") + " orders\n");
            appendToConsole("  7d: " + report.get("orderGrowth7d") + " orders\n");
            appendToConsole("  30d: " + report.get("orderGrowth30d") + " orders\n\n");

            appendToConsole("💰 Revenue Growth:\n");
            appendToConsole("  24h: " + report.get("revenueGrowth24h") + "\n");
            appendToConsole("  7d: " + report.get("revenueGrowth7d") + "\n");
            appendToConsole("  30d: " + report.get("revenueGrowth30d") + "\n\n");

            appendToConsole("👥 User Growth:\n");
            appendToConsole("  24h: " + report.get("userGrowth24h") + " users\n");
            appendToConsole("  7d: " + report.get("userGrowth7d") + " users\n");
            appendToConsole("  30d: " + report.get("userGrowth30d") + " users\n");

            appendToConsole("=".repeat(60) + "\n");
            showStatus("Performance metrics generated");
        } catch (Exception e) {
            appendToConsole("✗ Error: " + e.getMessage() + "\n");
            showError("Performance metrics failed");
        }
    }

    private void appendToConsole(String text) {
        if (txtConsole != null) {
            txtConsole.appendText(text);
        }
        print(text);
    }

    private void showStatus(String message) {
        if (lblStatus != null) {
            lblStatus.setText("✓ " + message);
            lblStatus.setStyle("-fx-text-fill: green;");
        }
    }

    private void showError(String message) {
        if (lblStatus != null) {
            lblStatus.setText("✗ " + message);
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }
}

