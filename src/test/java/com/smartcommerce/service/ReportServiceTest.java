package com.smartcommerce.service;

import com.smartcommerce.dao.OrderDAO;
import com.smartcommerce.dao.ProductDAO;
import com.smartcommerce.dao.UserDAO;
import com.smartcommerce.model.Product;
import com.smartcommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ProductDAO productDAO;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private UserDAO userDAO;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(productDAO, orderDAO, userDAO);
    }

    @Test
    void testGenerateSalesSummary() {
        // Mock order DAO methods
        when(orderDAO.getTotalOrderCount()).thenReturn(100);
        when(orderDAO.getTotalRevenue()).thenReturn(BigDecimal.valueOf(5000.0));
        when(orderDAO.getAverageOrderValue()).thenReturn(BigDecimal.valueOf(50.0));

        Map<String, Integer> statusCounts = new HashMap<>();
        statusCounts.put("PENDING", 10);
        statusCounts.put("CONFIRMED", 80);
        statusCounts.put("DELIVERED", 10);
        when(orderDAO.getOrderCountByStatus()).thenReturn(statusCounts);

        Map<String, Integer> orderStats = new HashMap<>();
        orderStats.put("last24Hours", 5);
        orderStats.put("last7Days", 25);
        orderStats.put("last30Days", 75);
        when(orderDAO.getOrderStatsByPeriod()).thenReturn(orderStats);

        Map<String, BigDecimal> revenueStats = new HashMap<>();
        revenueStats.put("last24Hours", BigDecimal.valueOf(250.0));
        revenueStats.put("last7Days", BigDecimal.valueOf(1250.0));
        revenueStats.put("last30Days", BigDecimal.valueOf(3750.0));
        when(orderDAO.getRevenueStatsByPeriod()).thenReturn(revenueStats);

        Map<String, Object> summary = reportService.generateSalesSummary();

        assertEquals(100, summary.get("totalOrders"));
        assertEquals("$5,000.00", summary.get("totalRevenue"));
        assertEquals(BigDecimal.valueOf(5000.0), summary.get("totalRevenueRaw"));
        assertEquals("$50.00", summary.get("averageOrderValue"));
        assertEquals(BigDecimal.valueOf(50.0), summary.get("averageOrderValueRaw"));
        assertEquals(statusCounts, summary.get("ordersByStatus"));
        assertEquals(5, summary.get("ordersLast24h"));
        assertEquals(25, summary.get("ordersLast7d"));
        assertEquals(75, summary.get("ordersLast30d"));
        assertEquals("$250.00", summary.get("revenueLast24h"));
        assertEquals("$1,250.00", summary.get("revenueLast7d"));
        assertEquals("$3,750.00", summary.get("revenueLast30d"));
    }

    @Test
    void testGenerateInventoryReport() {
        when(productDAO.getTotalProductCount()).thenReturn(50);

        Map<String, Integer> productsByCategory = new HashMap<>();
        productsByCategory.put("Electronics", 20);
        productsByCategory.put("Clothing", 30);
        when(productDAO.getProductCountByCategory()).thenReturn(productsByCategory);

        when(productDAO.getTotalInventoryValue()).thenReturn(BigDecimal.valueOf(10000.0));
        when(productDAO.getAverageProductPrice()).thenReturn(BigDecimal.valueOf(200.0));

        List<Product> lowStockProducts = Arrays.asList(
            new Product(1, "Low Stock Product", "Desc", BigDecimal.valueOf(10.0), 1, 5)
        );
        when(productDAO.getLowStockProducts(10)).thenReturn(lowStockProducts);

        List<Product> outOfStockProducts = Arrays.asList(
            new Product(2, "Out of Stock Product", "Desc", BigDecimal.valueOf(20.0), 1, 0)
        );
        when(productDAO.getOutOfStockProducts()).thenReturn(outOfStockProducts);

        Map<String, Object> report = reportService.generateInventoryReport();

        assertEquals(50, report.get("totalProducts"));
        assertEquals(productsByCategory, report.get("productsByCategory"));
        assertEquals("$10,000.00", report.get("totalInventoryValue"));
        assertEquals(BigDecimal.valueOf(10000.0), report.get("totalInventoryValueRaw"));
        assertEquals("$200.00", report.get("averageProductPrice"));
        assertEquals(1, report.get("lowStockCount"));
        assertEquals(lowStockProducts, report.get("lowStockProducts"));
        assertEquals(1, report.get("outOfStockCount"));
        assertEquals(outOfStockProducts, report.get("outOfStockProducts"));
    }

    @Test
    void testGenerateUserReport() {
        when(userDAO.getUserCount()).thenReturn(200);

        Map<String, Integer> roleDistribution = new HashMap<>();
        roleDistribution.put("CUSTOMER", 180);
        roleDistribution.put("ADMIN", 20);
        when(userDAO.getUserRoleDistribution()).thenReturn(roleDistribution);

        when(userDAO.getActiveUsersCount()).thenReturn(150);

        Map<String, Integer> registrationStats = new HashMap<>();
        registrationStats.put("last24Hours", 5);
        registrationStats.put("last7Days", 25);
        registrationStats.put("last30Days", 75);
        when(userDAO.getUserRegistrationStats()).thenReturn(registrationStats);

        List<User> recentUsers = Arrays.asList(
            new User(1, "user1", "user1@example.com", "hash", "CUSTOMER")
        );
        when(userDAO.getRecentRegistrations(10)).thenReturn(recentUsers);

        Map<String, Object> report = reportService.generateUserReport();

        assertEquals(200, report.get("totalUsers"));
        assertEquals(roleDistribution, report.get("usersByRole"));
        assertEquals(150, report.get("activeUsers"));
        assertEquals(5, report.get("registrationsLast24h"));
        assertEquals(25, report.get("registrationsLast7d"));
        assertEquals(75, report.get("registrationsLast30d"));
        assertEquals(recentUsers, report.get("recentRegistrations"));
    }

    @Test
    void testGenerateDashboardAnalytics() {
        // Mock all required methods
        when(orderDAO.getTotalOrderCount()).thenReturn(100);
        when(orderDAO.getTotalRevenue()).thenReturn(BigDecimal.valueOf(5000.0));
        when(orderDAO.getAverageOrderValue()).thenReturn(BigDecimal.valueOf(50.0));
        when(orderDAO.getRecentOrders(5)).thenReturn(new ArrayList<>());

        when(productDAO.getTotalProductCount()).thenReturn(50);
        when(productDAO.getLowStockProducts(10)).thenReturn(new ArrayList<>());
        when(productDAO.getOutOfStockProducts()).thenReturn(new ArrayList<>());
        when(productDAO.getRecentProducts(5)).thenReturn(new ArrayList<>());

        when(userDAO.getUserCount()).thenReturn(200);
        when(userDAO.getActiveUsersCount()).thenReturn(150);
        when(userDAO.getRecentRegistrations(5)).thenReturn(new ArrayList<>());

        Map<String, Object> analytics = reportService.generateDashboardAnalytics();

        assertEquals(100, analytics.get("totalOrders"));
        assertEquals("$5,000.00", analytics.get("totalRevenue"));
        assertEquals("$50.00", analytics.get("averageOrderValue"));
        assertEquals(50, analytics.get("totalProducts"));
        assertEquals(0, analytics.get("lowStockCount"));
        assertEquals(0, analytics.get("outOfStockCount"));
        assertEquals(200, analytics.get("totalUsers"));
        assertEquals(150, analytics.get("activeUsers"));
    }

    @Test
    void testGetTopProducts() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(2, "Product2", "Desc", BigDecimal.valueOf(20.0), 1, 10),
            new Product(3, "Product3", "Desc", BigDecimal.valueOf(30.0), 1, 10)
        );
        when(productDAO.findAll()).thenReturn(products);

        List<Product> topProducts = reportService.getTopProducts(2);

        assertEquals(2, topProducts.size());
        assertEquals(products.get(0), topProducts.get(0));
        assertEquals(products.get(1), topProducts.get(1));
    }

    @Test
    void testGetTopProductsEmpty() {
        when(productDAO.findAll()).thenReturn(new ArrayList<>());

        List<Product> topProducts = reportService.getTopProducts(5);

        assertTrue(topProducts.isEmpty());
    }

    @Test
    void testGeneratePerformanceReport() {
        Map<String, Integer> orderStats = new HashMap<>();
        orderStats.put("last24Hours", 5);
        orderStats.put("last7Days", 25);
        orderStats.put("last30Days", 75);
        when(orderDAO.getOrderStatsByPeriod()).thenReturn(orderStats);

        Map<String, BigDecimal> revenueStats = new HashMap<>();
        revenueStats.put("last24Hours", BigDecimal.valueOf(250.0));
        revenueStats.put("last7Days", BigDecimal.valueOf(1250.0));
        revenueStats.put("last30Days", BigDecimal.valueOf(3750.0));
        when(orderDAO.getRevenueStatsByPeriod()).thenReturn(revenueStats);

        Map<String, Integer> registrationStats = new HashMap<>();
        registrationStats.put("last24Hours", 3);
        registrationStats.put("last7Days", 15);
        registrationStats.put("last30Days", 45);
        when(userDAO.getUserRegistrationStats()).thenReturn(registrationStats);

        Map<String, Object> report = reportService.generatePerformanceReport();

        assertEquals(5, report.get("orderGrowth24h"));
        assertEquals(25, report.get("orderGrowth7d"));
        assertEquals(75, report.get("orderGrowth30d"));
        assertEquals("$250.00", report.get("revenueGrowth24h"));
        assertEquals("$1,250.00", report.get("revenueGrowth7d"));
        assertEquals("$3,750.00", report.get("revenueGrowth30d"));
        assertEquals(3, report.get("userGrowth24h"));
        assertEquals(15, report.get("userGrowth7d"));
        assertEquals(45, report.get("userGrowth30d"));
    }

    @Test
    void testGenerateInventoryAlerts() {
        List<Product> criticalStock = Arrays.asList(
            new Product(1, "Critical Product", "Desc", BigDecimal.valueOf(10.0), 1, 2)
        );
        when(productDAO.getLowStockProducts(5)).thenReturn(criticalStock);

        List<Product> lowStock = Arrays.asList(
            new Product(2, "Low Product", "Desc", BigDecimal.valueOf(20.0), 1, 8)
        );
        when(productDAO.getLowStockProducts(20)).thenReturn(Arrays.asList(criticalStock.get(0), lowStock.get(0)));

        List<Product> outOfStock = Arrays.asList(
            new Product(3, "Out Product", "Desc", BigDecimal.valueOf(30.0), 1, 0)
        );
        when(productDAO.getOutOfStockProducts()).thenReturn(outOfStock);

        Map<String, Object> alerts = reportService.generateInventoryAlerts();

        assertEquals(criticalStock, alerts.get("critical"));
        assertEquals(2, ((List<?>) alerts.get("low")).size()); // critical + low
        assertEquals(outOfStock, alerts.get("outOfStock"));
        assertEquals(1, alerts.get("criticalCount"));
        assertEquals(2, alerts.get("lowCount"));
        assertEquals(1, alerts.get("outOfStockCount"));
    }

    @Test
    void testGenerateSalesSummaryWithException() {
        when(orderDAO.getTotalOrderCount()).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> summary = reportService.generateSalesSummary();

        assertTrue(summary.containsKey("error"));
        assertTrue(((String) summary.get("error")).contains("Failed to generate sales summary"));
    }

    @Test
    void testGenerateInventoryReportWithException() {
        when(productDAO.getTotalProductCount()).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> report = reportService.generateInventoryReport();

        assertTrue(report.containsKey("error"));
        assertTrue(((String) report.get("error")).contains("Failed to generate inventory report"));
    }

    @Test
    void testGenerateUserReportWithException() {
        when(userDAO.getUserCount()).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> report = reportService.generateUserReport();

        assertTrue(report.containsKey("error"));
        assertTrue(((String) report.get("error")).contains("Failed to generate user report"));
    }

    @Test
    void testGenerateDashboardAnalyticsWithException() {
        when(orderDAO.getTotalOrderCount()).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> analytics = reportService.generateDashboardAnalytics();

        assertTrue(analytics.containsKey("error"));
        assertTrue(((String) analytics.get("error")).contains("Failed to generate dashboard analytics"));
    }

    @Test
    void testGeneratePerformanceReportWithException() {
        when(orderDAO.getOrderStatsByPeriod()).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> report = reportService.generatePerformanceReport();

        assertTrue(report.containsKey("error"));
        assertTrue(((String) report.get("error")).contains("Failed to generate performance report"));
    }

    @Test
    void testGenerateInventoryAlertsWithException() {
        when(productDAO.getLowStockProducts(5)).thenThrow(new RuntimeException("Database error"));

        Map<String, Object> alerts = reportService.generateInventoryAlerts();

        assertTrue(alerts.containsKey("error"));
        assertTrue(((String) alerts.get("error")).contains("Failed to generate inventory alerts"));
    }
}
