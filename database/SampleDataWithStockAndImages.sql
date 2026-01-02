-- ===================================================
-- Sample Data with Stock and Images - Smart E-Commerce System
-- Purpose: Populate database with test data including stock and image URLs
-- ===================================================

USE smart_ecommerce;

-- ===================================================
-- Insert Users
-- ===================================================
INSERT INTO Users (username, email, password_hash, role) VALUES
('admin', 'admin@smartcommerce.com', SHA2('admin123', 256), 'ADMIN'),
('john_doe', 'john@example.com', SHA2('password123', 256), 'CUSTOMER'),
('jane_smith', 'jane@example.com', SHA2('password123', 256), 'CUSTOMER'),
('bob_wilson', 'bob@example.com', SHA2('password123', 256), 'CUSTOMER'),
('alice_jones', 'alice@example.com', SHA2('password123', 256), 'CUSTOMER'),
('charlie_brown', 'charlie@example.com', SHA2('password123', 256), 'CUSTOMER');

-- ===================================================
-- Insert Categories
-- ===================================================
INSERT INTO Categories (category_name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Clothing', 'Men and women apparel'),
('Books', 'Fiction, non-fiction, and educational books'),
('Home & Kitchen', 'Home appliances and kitchen essentials'),
('Sports & Outdoors', 'Sports equipment and outdoor gear'),
('Toys & Games', 'Children toys and board games'),
('Health & Beauty', 'Personal care and beauty products'),
('Automotive', 'Car accessories and parts');

-- ===================================================
-- Insert Products with Stock Quantity and Image URLs
-- ===================================================
INSERT INTO Products (product_name, description, price, category_id, stock_quantity, image_url) VALUES
-- Electronics
('Laptop HP ProBook', '15.6 inch, Intel Core i5, 8GB RAM, 256GB SSD', 899.99, 1, 25, 'https://i.ebayimg.com/images/g/aicAAeSwAMdoNGK5/s-l1600.webp'),
('Samsung Galaxy S23', '6.1 inch, 128GB, 5G Smartphone', 799.99, 1, 40, 'https://i.ebayimg.com/images/g/Vq4AAOSwQDtlZPLe/s-l1600.webp'),
('Sony WH-1000XM5', 'Wireless Noise-Cancelling Headphones', 349.99, 1, 60, 'https://i.ebayimg.com/images/g/rLEAAOSw7XJlYmX~/s-l1600.webp'),
('Apple iPad Air', '10.9 inch, Wi-Fi, 64GB', 599.99, 1, 30, 'https://i.ebayimg.com/images/g/0nYAAOSwH5Rlhbpc/s-l1600.webp'),
('Dell Monitor 27 inch', '4K UHD, IPS Panel, USB-C', 449.99, 1, 5, 'https://i.ebayimg.com/images/g/FwYAAOSw3ZNlm7QN/s-l1600.webp'),

-- Clothing
('Nike Running Shoes', 'Men Air Zoom Pegasus, Size 10', 129.99, 2, 100, 'https://i.ebayimg.com/images/g/mBUAAOSw7WFk9pKi/s-l1600.webp'),
('Levi''s Jeans 501', 'Men Original Fit, Blue, W32 L34', 69.99, 2, 75, 'https://i.ebayimg.com/images/g/FGQAAOSwD5Vlgjk~/s-l1600.webp'),
('Adidas T-Shirt', 'Women Performance Tee, Black, Medium', 29.99, 2, 120, 'https://i.ebayimg.com/images/g/2WQAAOSwPFRlm4ZC/s-l1600.webp'),
('Winter Jacket', 'Waterproof Insulated Jacket, Large', 149.99, 2, 8, 'https://i.ebayimg.com/images/g/U3AAAOSwNBNlmGkw/s-l1600.webp'),
('Formal Shirt', 'Men Slim Fit, White, Size 16', 39.99, 2, 80, 'https://i.ebayimg.com/images/g/KhYAAOSwBGRlnMF~/s-l1600.webp'),

-- Books
('The Great Gatsby', 'F. Scott Fitzgerald - Classic Novel', 14.99, 3, 200, 'https://i.ebayimg.com/images/g/GnQAAOSwPFZlm8Hq/s-l1600.webp'),
('Java Programming', 'Complete Reference, 12th Edition', 49.99, 3, 150, 'https://i.ebayimg.com/images/g/5KIAAOSwVRBlmz7~/s-l1600.webp'),
('Atomic Habits', 'James Clear - Self Help', 24.99, 3, 180, 'https://i.ebayimg.com/images/g/WnIAAOSwBNtlm6pY/s-l1600.webp'),
('1984', 'George Orwell - Dystopian Fiction', 16.99, 3, 220, 'https://i.ebayimg.com/images/g/cJ8AAOSw1Sxlm3BF/s-l1600.webp'),
('Clean Code', 'Robert C. Martin - Software Engineering', 44.99, 3, 140, 'https://i.ebayimg.com/images/g/zYYAAOSwh5plm92K/s-l1600.webp'),

-- Home & Kitchen
('Instant Pot Duo', '7-in-1 Electric Pressure Cooker, 6 Quart', 89.99, 4, 35, 'https://i.ebayimg.com/images/g/7fEAAOSwbVRlm5gH/s-l1600.webp'),
('Coffee Maker', 'Programmable Drip Coffee Machine', 79.99, 4, 50, 'https://i.ebayimg.com/images/g/0JQAAOSwPOZlm7vK/s-l1600.webp'),
('Blender', 'High Speed Professional Blender', 129.99, 4, 40, 'https://i.ebayimg.com/images/g/R5MAAOSw3HBlm4TE/s-l1600.webp'),
('Cookware Set', 'Non-stick 12-piece set', 199.99, 4, 28, 'https://i.ebayimg.com/images/g/~NoAAOSwqGRlm6Qw/s-l1600.webp'),
('Vacuum Cleaner', 'Cordless Stick Vacuum', 249.99, 4, 3, 'https://i.ebayimg.com/images/g/HTYAAOSwMJJlm8pX/s-l1600.webp'),

-- Sports & Outdoors
('Yoga Mat', 'Premium 6mm Thick, Non-slip', 34.99, 5, 90, 'https://i.ebayimg.com/images/g/cw4AAOSwVBllm3sQ/s-l1600.webp'),
('Dumbbell Set', 'Adjustable 5-52.5 lbs', 299.99, 5, 20, 'https://i.ebayimg.com/images/g/gw8AAOSw0Kxlm5ZT/s-l1600.webp'),
('Camping Tent', '4-Person Waterproof Tent', 159.99, 5, 18, 'https://i.ebayimg.com/images/g/fhcAAOSwtullm4bR/s-l1600.webp'),
('Bicycle', 'Mountain Bike 27.5 inch', 499.99, 5, 0, 'https://i.ebayimg.com/images/g/KSYAAOSwZAVlm7GN/s-l1600.webp'),
('Basketball', 'Official Size Indoor/Outdoor', 29.99, 5, 85, 'https://i.ebayimg.com/images/g/XGoAAOSwMNxlm6fC/s-l1600.webp');

-- Note:
-- Stock levels include:
-- - OUT OF STOCK (0): Bicycle
-- - LOW STOCK (1-10): Dell Monitor (5), Winter Jacket (8), Vacuum Cleaner (3)
-- - IN STOCK (>10): All other products

-- ===================================================
-- Insert Inventory (synced with stock_quantity)
-- ===================================================
INSERT INTO Inventory (product_id, quantity_available, reorder_level) VALUES
(1, 25, 10), (2, 40, 15), (3, 60, 20), (4, 30, 12), (5, 5, 8),
(6, 100, 25), (7, 75, 30), (8, 120, 40), (9, 8, 15), (10, 80, 18),
(11, 200, 50), (12, 150, 40), (13, 180, 45), (14, 220, 35), (15, 140, 30),
(16, 35, 10), (17, 50, 12), (18, 40, 8), (19, 28, 6), (20, 3, 15),
(21, 90, 20), (22, 20, 8), (23, 18, 5), (24, 0, 4), (25, 85, 18);

-- ===================================================
-- Insert Orders
-- ===================================================
INSERT INTO Orders (user_id, total_amount, status, order_date) VALUES
(2, 1249.98, 'DELIVERED', '2024-11-15 10:30:00'),
(3, 899.99, 'SHIPPED', '2024-11-20 14:45:00'),
(4, 359.97, 'CONFIRMED', '2024-12-01 09:15:00'),
(5, 1799.96, 'PENDING', '2024-12-10 16:20:00'),
(6, 199.98, 'DELIVERED', '2024-11-25 11:00:00'),
(2, 549.98, 'CONFIRMED', '2024-12-12 13:30:00'),
(3, 129.99, 'PENDING', '2024-12-15 10:00:00');

-- ===================================================
-- Insert OrderItems
-- ===================================================
INSERT INTO OrderItems (order_id, product_id, quantity, unit_price) VALUES
-- Order 1
(1, 1, 1, 899.99),
(1, 3, 1, 349.99),

-- Order 2
(2, 1, 1, 899.99),

-- Order 3
(3, 6, 1, 129.99),
(3, 8, 2, 29.99),
(3, 11, 5, 14.99),

-- Order 4
(4, 2, 1, 799.99),
(4, 4, 1, 599.99),
(4, 5, 1, 449.99),

-- Order 5
(5, 7, 2, 69.99),
(5, 10, 1, 39.99),

-- Order 6
(6, 13, 10, 24.99),
(6, 16, 2, 89.99),

-- Order 7
(7, 6, 1, 129.99);

-- ===================================================
-- Verification Queries
-- ===================================================

-- View all products with stock status
SELECT
    product_id,
    product_name,
    price,
    stock_quantity,
    CASE
        WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW STOCK'
        ELSE 'IN STOCK'
    END as stock_status,
    CASE
        WHEN image_url IS NOT NULL THEN 'Yes'
        ELSE 'No'
    END as has_image
FROM Products
ORDER BY product_id;

-- Count by stock status
SELECT
    CASE
        WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW STOCK'
        ELSE 'IN STOCK'
    END as status,
    COUNT(*) as count
FROM Products
GROUP BY
    CASE
        WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW STOCK'
        ELSE 'IN STOCK'
    END;

