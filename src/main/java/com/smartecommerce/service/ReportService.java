package com.smartecommerce.service;

import com.smartecommerce.dao.OrderDAO;
import com.smartecommerce.dao.ProductDAO;
import com.smartecommerce.dao.UserDAO;
import com.smartecommerce.models.Product;
import com.smartecommerce.models.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.smartecommerce.utils.AppUtils.*;

/**
 * ReportService generates various reports and analytics
 * Optimized for dashboard and analytics views
 */
public class ReportService {
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;

    public ReportService() {
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.userDAO = new UserDAO();
    }

    // Constructor for testing with mock DAOs
    public ReportService(ProductDAO productDAO, OrderDAO orderDAO, UserDAO userDAO) {
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
    }

    /**
     * Generate comprehensive sales summary report
     */
    public Map<String, Object> generateSalesSummary() {
        Map<String, Object> summary = new HashMap<>();

        try {
            // Get total orders count
            int totalOrders = orderDAO.getTotalOrderCount();
            summary.put("totalOrders", totalOrders);

            // Get total revenue
            BigDecimal totalRevenue = orderDAO.getTotalRevenue();
            summary.put("totalRevenue", formatCurrencyString(totalRevenue));
            summary.put("totalRevenueRaw", totalRevenue);

            // Calculate average order value
            BigDecimal avgOrderValue = orderDAO.getAverageOrderValue();
            summary.put("averageOrderValue", formatCurrencyString(avgOrderValue));
            summary.put("averageOrderValueRaw", avgOrderValue);

            // Get order count by status
            Map<String, Integer> statusCounts = orderDAO.getOrderCountByStatus();
            summary.put("ordersByStatus", statusCounts);

            // Get order statistics by time period
            Map<String, Integer> orderStats = orderDAO.getOrderStatsByPeriod();
            summary.put("ordersLast24h", orderStats.getOrDefault("last24Hours", 0));
            summary.put("ordersLast7d", orderStats.getOrDefault("last7Days", 0));
            summary.put("ordersLast30d", orderStats.getOrDefault("last30Days", 0));

            // Get revenue statistics by time period
            Map<String, BigDecimal> revenueStats = orderDAO.getRevenueStatsByPeriod();
            summary.put("revenueLast24h", formatCurrencyString(revenueStats.getOrDefault("last24Hours", BigDecimal.ZERO)));
            summary.put("revenueLast7d", formatCurrencyString(revenueStats.getOrDefault("last7Days", BigDecimal.ZERO)));
            summary.put("revenueLast30d", formatCurrencyString(revenueStats.getOrDefault("last30Days", BigDecimal.ZERO)));

        } catch (Exception e) {
            summary.put("error", "Failed to generate sales summary: " + e.getMessage());
            printE("Error in generateSalesSummary: " + e.getMessage());
        }

        return summary;
    }

    /**
     * Generate detailed product inventory report
     */
    public Map<String, Object> generateInventoryReport() {
        Map<String, Object> report = new HashMap<>();

        try {
            // Total product count
            int totalProducts = productDAO.getTotalProductCount();
            report.put("totalProducts", totalProducts);

            // Products by category
            Map<String, Integer> productsByCategory = productDAO.getProductCountByCategory();
            report.put("productsByCategory", productsByCategory);

            // Total inventory value
            BigDecimal totalValue = productDAO.getTotalInventoryValue();
            report.put("totalInventoryValue", formatCurrencyString(totalValue));
            report.put("totalInventoryValueRaw", totalValue);

            // Average product price
            BigDecimal avgPrice = productDAO.getAverageProductPrice();
            report.put("averageProductPrice", formatCurrencyString(avgPrice));

            // Low stock products (threshold: 10)
            List<Product> lowStockProducts = productDAO.getLowStockProducts(10);
            report.put("lowStockCount", lowStockProducts.size());
            report.put("lowStockProducts", lowStockProducts);

            // Out of stock products
            List<Product> outOfStockProducts = productDAO.getOutOfStockProducts();
            report.put("outOfStockCount", outOfStockProducts.size());
            report.put("outOfStockProducts", outOfStockProducts);

        } catch (Exception e) {
            report.put("error", "Failed to generate inventory report: " + e.getMessage());
            printE("Error in generateInventoryReport: " + e.getMessage());
        }

        return report;
    }

    /**
     * Generate user analytics report
     */
    public Map<String, Object> generateUserReport() {
        Map<String, Object> report = new HashMap<>();

        try {
            // Total user count
            int totalUsers = userDAO.getUserCount();
            report.put("totalUsers", totalUsers);

            // User role distribution
            Map<String, Integer> roleDistribution = userDAO.getUserRoleDistribution();
            report.put("usersByRole", roleDistribution);

            // Active users count (users who placed orders)
            int activeUsers = userDAO.getActiveUsersCount();
            report.put("activeUsers", activeUsers);

            // Registration statistics
            Map<String, Integer> registrationStats = userDAO.getUserRegistrationStats();
            report.put("registrationsLast24h", registrationStats.getOrDefault("last24Hours", 0));
            report.put("registrationsLast7d", registrationStats.getOrDefault("last7Days", 0));
            report.put("registrationsLast30d", registrationStats.getOrDefault("last30Days", 0));

            // Recent registrations
            List<User> recentUsers = userDAO.getRecentRegistrations(10);
            report.put("recentRegistrations", recentUsers);

        } catch (Exception e) {
            report.put("error", "Failed to generate user report: " + e.getMessage());
            printE("Error in generateUserReport: " + e.getMessage());
        }

        return report;
    }

    /**
     * Generate comprehensive dashboard analytics
     * This is the main method for dashboard statistics
     */
    public Map<String, Object> generateDashboardAnalytics() {
        Map<String, Object> analytics = new HashMap<>();

        try {
            // Sales metrics
            analytics.put("totalOrders", orderDAO.getTotalOrderCount());
            analytics.put("totalRevenue", formatCurrencyString(orderDAO.getTotalRevenue()));
            analytics.put("averageOrderValue", formatCurrencyString(orderDAO.getAverageOrderValue()));

            // Product metrics
            analytics.put("totalProducts", productDAO.getTotalProductCount());
            analytics.put("lowStockCount", productDAO.getLowStockProducts(10).size());
            analytics.put("outOfStockCount", productDAO.getOutOfStockProducts().size());

            // User metrics
            analytics.put("totalUsers", userDAO.getUserCount());
            analytics.put("activeUsers", userDAO.getActiveUsersCount());

            // Recent activity
            analytics.put("recentOrders", orderDAO.getRecentOrders(5));
            analytics.put("recentProducts", productDAO.getRecentProducts(5));
            analytics.put("recentUsers", userDAO.getRecentRegistrations(5));

        } catch (Exception e) {
            analytics.put("error", "Failed to generate dashboard analytics: " + e.getMessage());
            printE("Error in generateDashboardAnalytics: " + e.getMessage());
        }

        return analytics;
    }

    /**
     * Get top selling products (mock implementation - would need OrderItems data)
     */
    public List<Product> getTopProducts(int limit) {
        List<Product> products = productDAO.findAll();
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        // In real implementation, would join with OrderItems and aggregate
        int actualLimit = Math.max(0, Math.min(limit, products.size()));
        return products.subList(0, actualLimit);
    }

    /**
     * Generate performance metrics report
     */
    public Map<String, Object> generatePerformanceReport() {
        Map<String, Object> report = new HashMap<>();

        try {
            // Order performance
            Map<String, Integer> orderStats = orderDAO.getOrderStatsByPeriod();
            report.put("orderGrowth24h", orderStats.getOrDefault("last24Hours", 0));
            report.put("orderGrowth7d", orderStats.getOrDefault("last7Days", 0));
            report.put("orderGrowth30d", orderStats.getOrDefault("last30Days", 0));

            // Revenue performance
            Map<String, BigDecimal> revenueStats = orderDAO.getRevenueStatsByPeriod();
            report.put("revenueGrowth24h", formatCurrencyString(revenueStats.getOrDefault("last24Hours", BigDecimal.ZERO)));
            report.put("revenueGrowth7d", formatCurrencyString(revenueStats.getOrDefault("last7Days", BigDecimal.ZERO)));
            report.put("revenueGrowth30d", formatCurrencyString(revenueStats.getOrDefault("last30Days", BigDecimal.ZERO)));

            // User growth
            Map<String, Integer> registrationStats = userDAO.getUserRegistrationStats();
            report.put("userGrowth24h", registrationStats.getOrDefault("last24Hours", 0));
            report.put("userGrowth7d", registrationStats.getOrDefault("last7Days", 0));
            report.put("userGrowth30d", registrationStats.getOrDefault("last30Days", 0));

        } catch (Exception e) {
            report.put("error", "Failed to generate performance report: " + e.getMessage());
            printE("Error in generatePerformanceReport: " + e.getMessage());
        }

        return report;
    }





}
