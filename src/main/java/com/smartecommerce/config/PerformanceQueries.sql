-- ===================================================
-- Performance Queries for Smart E-Commerce System
-- Purpose: Complex queries demonstrating optimization
-- ===================================================

USE smart_ecommerce;

-- ===================================================
-- Query 1: Product Sales Report with Category
-- Demonstrates: JOIN, GROUP BY, Aggregation
-- ===================================================
SELECT
    p.product_name,
    c.category_name,
    COUNT(oi.order_item_id) as times_ordered,
    SUM(oi.quantity) as total_quantity_sold,
    SUM(oi.subtotal) as total_revenue,
    AVG(oi.unit_price) as average_price
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
LEFT JOIN OrderItems oi ON p.product_id = oi.product_id
GROUP BY p.product_id, p.product_name, c.category_name
ORDER BY total_revenue DESC;

-- ===================================================
-- Query 2: Customer Order History with Details
-- Demonstrates: Multiple JOINs, Subqueries
-- ===================================================
SELECT
    u.username,
    u.email,
    o.order_id,
    o.order_date,
    o.status,
    o.total_amount,
    COUNT(oi.order_item_id) as item_count
FROM Users u
JOIN Orders o ON u.user_id = o.user_id
JOIN OrderItems oi ON o.order_id = oi.order_id
WHERE u.role = 'CUSTOMER'
GROUP BY u.username, u.email, o.order_id, o.order_date, o.status, o.total_amount
ORDER BY o.order_date DESC;

-- ===================================================
-- Query 3: Products with Average Rating
-- Demonstrates: JOIN, Aggregation, COALESCE
-- ===================================================
SELECT
    p.product_id,
    p.product_name,
    c.category_name,
    p.price,
    COALESCE(AVG(r.rating), 0) as avg_rating,
    COUNT(r.review_id) as review_count
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
LEFT JOIN Reviews r ON p.product_id = r.product_id
GROUP BY p.product_id, p.product_name, c.category_name, p.price
HAVING avg_rating >= 4 OR review_count = 0
ORDER BY avg_rating DESC, review_count DESC;

-- ===================================================
-- Query 4: Low Stock Alert
-- Demonstrates: JOIN, WHERE conditions, Filtering
-- ===================================================
SELECT
    p.product_id,
    p.product_name,
    c.category_name,
    i.quantity_available,
    i.reorder_level,
    (i.reorder_level - i.quantity_available) as units_needed
FROM Inventory i
JOIN Products p ON i.product_id = p.product_id
JOIN Categories c ON p.category_id = c.category_id
WHERE i.quantity_available <= i.reorder_level
ORDER BY units_needed DESC;

-- ===================================================
-- Query 5: Revenue by Category
-- Demonstrates: Multiple JOINs, Aggregation, GROUP BY
-- ===================================================
SELECT
    c.category_name,
    COUNT(DISTINCT p.product_id) as product_count,
    COUNT(DISTINCT oi.order_id) as order_count,
    SUM(oi.quantity) as total_units_sold,
    SUM(oi.subtotal) as category_revenue
FROM Categories c
LEFT JOIN Products p ON c.category_id = p.category_id
LEFT JOIN OrderItems oi ON p.product_id = oi.product_id
GROUP BY c.category_id, c.category_name
ORDER BY category_revenue DESC;

-- ===================================================
-- Query 6: Top Customers by Purchase Amount
-- Demonstrates: Aggregation, Ranking
-- ===================================================
SELECT
    u.user_id,
    u.username,
    u.email,
    COUNT(o.order_id) as total_orders,
    SUM(o.total_amount) as total_spent,
    AVG(o.total_amount) as avg_order_value
FROM Users u
JOIN Orders o ON u.user_id = o.user_id
WHERE u.role = 'CUSTOMER'
GROUP BY u.user_id, u.username, u.email
ORDER BY total_spent DESC
LIMIT 10;

-- ===================================================
-- Query 7: Order Status Summary
-- Demonstrates: GROUP BY, COUNT, Percentage calculation
-- ===================================================
SELECT
    status,
    COUNT(*) as order_count,
    SUM(total_amount) as total_value,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM Orders), 2) as percentage
FROM Orders
GROUP BY status
ORDER BY order_count DESC;

-- ===================================================
-- Query 8: Products Never Ordered
-- Demonstrates: LEFT JOIN with NULL check, Subquery alternative
-- ===================================================
SELECT
    p.product_id,
    p.product_name,
    c.category_name,
    p.price,
    i.quantity_available
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
JOIN Inventory i ON p.product_id = i.product_id
LEFT JOIN OrderItems oi ON p.product_id = oi.product_id
WHERE oi.product_id IS NULL
ORDER BY p.product_name;

-- ===================================================
-- Query 9: Monthly Sales Trend
-- Demonstrates: Date functions, Aggregation by time period
-- ===================================================
SELECT
    DATE_FORMAT(o.order_date, '%Y-%m') as month,
    COUNT(o.order_id) as total_orders,
    SUM(o.total_amount) as monthly_revenue,
    AVG(o.total_amount) as avg_order_value,
    COUNT(DISTINCT o.user_id) as unique_customers
FROM Orders o
GROUP BY DATE_FORMAT(o.order_date, '%Y-%m')
ORDER BY month DESC;

-- ===================================================
-- Query 10: Product Search with Full-Text (Optimized)
-- Demonstrates: Full-text search, LIKE comparison
-- ===================================================
-- Using LIKE (slower)
EXPLAIN SELECT
    p.product_id,
    p.product_name,
    c.category_name,
    p.price
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
WHERE p.product_name LIKE '%laptop%'
   OR p.description LIKE '%laptop%';

-- Using Full-Text Search (faster) - requires FULLTEXT index
-- EXPLAIN SELECT
--     p.product_id,
--     p.product_name,
--     c.category_name,
--     p.price,
--     MATCH(p.product_name, p.description) AGAINST('laptop' IN NATURAL LANGUAGE MODE) as relevance
-- FROM Products p
-- JOIN Categories c ON p.category_id = c.category_id
-- WHERE MATCH(p.product_name, p.description) AGAINST('laptop' IN NATURAL LANGUAGE MODE)
-- ORDER BY relevance DESC;

-- ===================================================
-- Query 11: Complex Order Details
-- Demonstrates: Multiple JOINs, Detailed information retrieval
-- ===================================================
SELECT
    o.order_id,
    u.username as customer,
    o.order_date,
    o.status,
    oi.order_item_id,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    oi.subtotal
FROM Orders o
JOIN Users u ON o.user_id = u.user_id
JOIN OrderItems oi ON o.order_id = oi.order_id
JOIN Products p ON oi.product_id = p.product_id
ORDER BY o.order_date DESC, o.order_id, oi.order_item_id;

-- ===================================================
-- Query 12: Product Performance Dashboard
-- Demonstrates: Complex aggregation, Window functions alternative
-- ===================================================
SELECT
    p.product_id,
    p.product_name,
    c.category_name,
    p.price,
    i.quantity_available as stock,
    COALESCE(SUM(oi.quantity), 0) as total_sold,
    COALESCE(AVG(r.rating), 0) as avg_rating,
    COUNT(DISTINCT r.review_id) as review_count,
    COALESCE(SUM(oi.subtotal), 0) as total_revenue
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
JOIN Inventory i ON p.product_id = i.product_id
LEFT JOIN OrderItems oi ON p.product_id = oi.product_id
LEFT JOIN Reviews r ON p.product_id = r.product_id
GROUP BY p.product_id, p.product_name, c.category_name, p.price, i.quantity_available
ORDER BY total_revenue DESC;

