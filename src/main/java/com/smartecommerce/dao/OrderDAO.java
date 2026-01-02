package com.smartecommerce.dao;

import com.smartecommerce.models.Order;
import com.smartecommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartecommerce.utils.AppUtils.*;
import static com.smartecommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * OrderDAO handles all database operations for Order entity
 */
public class OrderDAO {

    public boolean create(Order order) {
        String sql = "INSERT INTO Orders (user_id, total_amount, status) VALUES (?, ?, ?)";
        QueryResult insertResult = executePreparedQuery(
                sql,
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus());

        if (insertResult.hasError()) {
            printE("Error creating order: " + insertResult.getError());
            return false;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            order.setOrderId(generatedId.intValue());
            return true;
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    public Order findById(int orderId) {
        String sql = "SELECT o.*, u.username as customer_name FROM Orders o " +
                "JOIN Users u ON o.user_id = u.user_id WHERE o.order_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, orderId);

        if (queryResult.hasError()) {
            printE("Error finding order: " + queryResult.getError());
            return null;
        }

        return mapSingleOrder(queryResult);
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as customer_name FROM Orders o " +
                "JOIN Users u ON o.user_id = u.user_id ORDER BY o.order_date DESC";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error retrieving orders: " + queryResult.getError());
            return orders;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return orders;
        }

        for (Map<String, Object> row : rows) {
            Order mappedOrder = mapRow(row);
            if (mappedOrder != null) {
                orders.add(mappedOrder);
            }
        }
        return orders;
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        QueryResult updateResult = executePreparedQuery(sql, status, orderId);

        if (updateResult.hasError()) {
            printE("Error updating order status: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    public List<Order> findByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as customer_name FROM Orders o " +
                "JOIN Users u ON o.user_id = u.user_id WHERE o.user_id = ? ORDER BY o.order_date DESC";
        QueryResult queryResult = executePreparedQuery(sql, userId);

        if (queryResult.hasError()) {
            printE("Error finding orders by user: " + queryResult.getError());
            return orders;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return orders;
        }

        for (Map<String, Object> row : rows) {
            Order mappedOrder = mapRow(row);
            if (mappedOrder != null) {
                orders.add(mappedOrder);
            }
        }
        return orders;
    }

    /**
     * Get total revenue from all orders
     * Analytics function for dashboard
     */
    public java.math.BigDecimal getTotalRevenue() {
        String sql = "SELECT ISNULL(SUM(total_amount), 0) as revenue FROM Orders";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error calculating total revenue: " + queryResult.getError());
            return java.math.BigDecimal.ZERO;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asBigDecimal(rows.get(0).get("revenue"));
        }
        return java.math.BigDecimal.ZERO;
    }

    /**
     * Get order count by status
     * Analytics function
     */
    public Map<String, Integer> getOrderCountByStatus() {
        Map<String, Integer> statusCounts = new java.util.HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM Orders GROUP BY status";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error getting order count by status: " + queryResult.getError());
            return statusCounts;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                String status = asString(row.get("status"));
                int count = asInt(row.get("count"));
                statusCounts.put(status, count);
            }
        }
        return statusCounts;
    }

    /**
     * Get revenue by date range
     * Analytics function
     */
    public java.math.BigDecimal getRevenueByDateRange(String startDate, String endDate) {
        String sql = "SELECT ISNULL(SUM(total_amount), 0) as revenue FROM Orders " +
                     "WHERE order_date BETWEEN ? AND ?";
        QueryResult queryResult = executePreparedQuery(sql, startDate, endDate);

        if (queryResult.hasError()) {
            printE("Error calculating revenue by date range: " + queryResult.getError());
            return java.math.BigDecimal.ZERO;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asBigDecimal(rows.get(0).get("revenue"));
        }
        return java.math.BigDecimal.ZERO;
    }

    /**
     * Get average order value
     * Analytics function
     */
    public java.math.BigDecimal getAverageOrderValue() {
        String sql = "SELECT ISNULL(AVG(total_amount), 0) as avg_value FROM Orders";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error calculating average order value: " + queryResult.getError());
            return java.math.BigDecimal.ZERO;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asBigDecimal(rows.get(0).get("avg_value"));
        }
        return java.math.BigDecimal.ZERO;
    }

    /**
     * Get orders statistics by time period
     * Analytics function
     */
    public Map<String, Integer> getOrderStatsByPeriod() {
        Map<String, Integer> stats = new java.util.HashMap<>();

        // Orders in last 24 hours
        String sql24h = "SELECT COUNT(*) as count FROM Orders WHERE order_date >= DATEADD(HOUR, -24, GETDATE())";
        QueryResult result24h = executePreparedQuery(sql24h);
        if (!result24h.hasError() && result24h.getResultSet() != null && !result24h.getResultSet().isEmpty()) {
            stats.put("last24Hours", asInt(result24h.getResultSet().get(0).get("count")));
        } else {
            stats.put("last24Hours", 0);
        }

        // Orders in last 7 days
        String sql7d = "SELECT COUNT(*) as count FROM Orders WHERE order_date >= DATEADD(DAY, -7, GETDATE())";
        QueryResult result7d = executePreparedQuery(sql7d);
        if (!result7d.hasError() && result7d.getResultSet() != null && !result7d.getResultSet().isEmpty()) {
            stats.put("last7Days", asInt(result7d.getResultSet().get(0).get("count")));
        } else {
            stats.put("last7Days", 0);
        }

        // Orders in last 30 days
        String sql30d = "SELECT COUNT(*) as count FROM Orders WHERE order_date >= DATEADD(DAY, -30, GETDATE())";
        QueryResult result30d = executePreparedQuery(sql30d);
        if (!result30d.hasError() && result30d.getResultSet() != null && !result30d.getResultSet().isEmpty()) {
            stats.put("last30Days", asInt(result30d.getResultSet().get(0).get("count")));
        } else {
            stats.put("last30Days", 0);
        }

        return stats;
    }

    /**
     * Get revenue statistics by time period
     * Analytics function
     */
    public Map<String, java.math.BigDecimal> getRevenueStatsByPeriod() {
        Map<String, java.math.BigDecimal> stats = new java.util.HashMap<>();

        // Revenue in last 24 hours
        String sql24h = "SELECT ISNULL(SUM(total_amount), 0) as revenue FROM Orders WHERE order_date >= DATEADD(HOUR, -24, GETDATE())";
        QueryResult result24h = executePreparedQuery(sql24h);
        if (!result24h.hasError() && result24h.getResultSet() != null && !result24h.getResultSet().isEmpty()) {
            stats.put("last24Hours", asBigDecimal(result24h.getResultSet().get(0).get("revenue")));
        } else {
            stats.put("last24Hours", java.math.BigDecimal.ZERO);
        }

        // Revenue in last 7 days
        String sql7d = "SELECT ISNULL(SUM(total_amount), 0) as revenue FROM Orders WHERE order_date >= DATEADD(DAY, -7, GETDATE())";
        QueryResult result7d = executePreparedQuery(sql7d);
        if (!result7d.hasError() && result7d.getResultSet() != null && !result7d.getResultSet().isEmpty()) {
            stats.put("last7Days", asBigDecimal(result7d.getResultSet().get(0).get("revenue")));
        } else {
            stats.put("last7Days", java.math.BigDecimal.ZERO);
        }

        // Revenue in last 30 days
        String sql30d = "SELECT ISNULL(SUM(total_amount), 0) as revenue FROM Orders WHERE order_date >= DATEADD(DAY, -30, GETDATE())";
        QueryResult result30d = executePreparedQuery(sql30d);
        if (!result30d.hasError() && result30d.getResultSet() != null && !result30d.getResultSet().isEmpty()) {
            stats.put("last30Days", asBigDecimal(result30d.getResultSet().get(0).get("revenue")));
        } else {
            stats.put("last30Days", java.math.BigDecimal.ZERO);
        }

        return stats;
    }

    /**
     * Get recent orders for activity feed
     * @param limit Maximum number of orders to return
     */
    public List<Order> getRecentOrders(int limit) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.username as customer_name FROM Orders o " +
                     "JOIN Users u ON o.user_id = u.user_id ORDER BY o.order_date DESC LIMIT ?";
        QueryResult queryResult = executePreparedQuery(sql, limit);

        if (queryResult.hasError()) {
            printE("Error getting recent orders: " + queryResult.getError());
            return orders;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return orders;
        }

        for (Map<String, Object> row : rows) {
            Order mappedOrder = mapRow(row);
            if (mappedOrder != null) {
                orders.add(mappedOrder);
            }
        }
        return orders;
    }

    /**
     * Get total order count
     * Analytics function
     */
    public int getTotalOrderCount() {
        String sql = "SELECT COUNT(*) as count FROM Orders";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error counting orders: " + queryResult.getError());
            return 0;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asInt(rows.get(0).get("count"));
        }
        return 0;
    }

    /**
     * Map the first row from a query result to an Order
     */
    private Order mapSingleOrder(QueryResult queryResult) {
        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        return mapRow(rows.get(0));
    }

    /**
     * Convert a row map into an Order instance
     */
    private Order mapRow(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setOrderId(asInt(row.get("order_id")));
        order.setUserId(asInt(row.get("user_id")));
        order.setCustomerName(asString(row.get("customer_name")));
        order.setTotalAmount(asBigDecimal(row.get("total_amount")));
        order.setStatus(asString(row.get("status")));
        order.setOrderDate(asLocalDateTime(row.get("order_date")));
        order.setUpdatedAt(asLocalDateTime(row.get("updated_at")));
        return order;
    }

   
}
