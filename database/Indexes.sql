-- ===================================================
-- Indexes for Smart E-Commerce System
-- Purpose: Optimize query performance
-- ===================================================

USE smart_ecommerce;

-- ===================================================
-- Users Table Indexes
-- ===================================================
-- Already created in schema: idx_username, idx_email, idx_role

-- Additional composite index for login queries
CREATE INDEX idx_user_login ON Users(username, password_hash);

-- ===================================================
-- Products Table Indexes
-- ===================================================
-- Already created: idx_product_name, idx_category_id, idx_price

-- Full-text search index for product search
ALTER TABLE Products ADD FULLTEXT INDEX ft_product_search(product_name, description);

-- Composite index for filtering by category and price
CREATE INDEX idx_category_price ON Products(category_id, price);

-- ===================================================
-- Orders Table Indexes
-- ===================================================
-- Already created: idx_user_id, idx_status, idx_order_date

-- Composite index for user order history queries
CREATE INDEX idx_user_date ON Orders(user_id, order_date DESC);

-- Composite index for status reporting
CREATE INDEX idx_status_date ON Orders(status, order_date DESC);

-- ===================================================
-- OrderItems Table Indexes
-- ===================================================
-- Already created: idx_order_id, idx_product_id

-- Composite index for product sales analysis
CREATE INDEX idx_product_order ON OrderItems(product_id, order_id);

-- ===================================================
-- Reviews Table Indexes
-- ===================================================
-- Already created: idx_product_id, idx_user_id, idx_rating, idx_created_at

-- Composite index for product review queries
CREATE INDEX idx_product_rating ON Reviews(product_id, rating DESC);

-- ===================================================
-- Inventory Table Indexes
-- ===================================================
-- Index for low stock alerts
CREATE INDEX idx_low_stock ON Inventory(quantity_available, reorder_level);

-- ===================================================
-- Show all indexes
-- ===================================================
SHOW INDEX FROM Users;
SHOW INDEX FROM Categories;
SHOW INDEX FROM Products;
SHOW INDEX FROM Inventory;
SHOW INDEX FROM Orders;
SHOW INDEX FROM OrderItems;
SHOW INDEX FROM Reviews;

-- ===================================================
-- Performance Analysis
-- ===================================================
-- Analyze table statistics for query optimization
ANALYZE TABLE Users;
ANALYZE TABLE Categories;
ANALYZE TABLE Products;
ANALYZE TABLE Inventory;
ANALYZE TABLE Orders;
ANALYZE TABLE OrderItems;
ANALYZE TABLE Reviews;

