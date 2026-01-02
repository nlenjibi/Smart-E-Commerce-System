-- ===================================================
-- Sample Data for Smart E-Commerce System
-- Purpose: Populate database with test data
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
-- Insert Products
-- ===================================================
INSERT INTO Products (product_name, description, price, category_id) VALUES
-- Electronics
('Laptop HP ProBook', '15.6 inch, Intel Core i5, 8GB RAM, 256GB SSD', 899.99, 1),
('Samsung Galaxy S23', '6.1 inch, 128GB, 5G Smartphone', 799.99, 1),
('Sony WH-1000XM5', 'Wireless Noise-Cancelling Headphones', 349.99, 1),
('Apple iPad Air', '10.9 inch, Wi-Fi, 64GB', 599.99, 1),
('Dell Monitor 27 inch', '4K UHD, IPS Panel, USB-C', 449.99, 1),

-- Clothing
('Nike Running Shoes', 'Men Air Zoom Pegasus, Size 10', 129.99, 2),
('Levi''s Jeans 501', 'Men Original Fit, Blue, W32 L34', 69.99, 2),
('Adidas T-Shirt', 'Women Performance Tee, Black, Medium', 29.99, 2),
('Winter Jacket', 'Waterproof Insulated Jacket, Large', 149.99, 2),
('Formal Shirt', 'Men Slim Fit, White, Size 16', 39.99, 2),

-- Books
('The Great Gatsby', 'F. Scott Fitzgerald - Classic Novel', 14.99, 3),
('Java Programming', 'Complete Reference, 12th Edition', 49.99, 3),
('Atomic Habits', 'James Clear - Self Help', 24.99, 3),
('1984', 'George Orwell - Dystopian Fiction', 16.99, 3),
('Clean Code', 'Robert C. Martin - Software Engineering', 44.99, 3),

-- Home & Kitchen
('Instant Pot Duo', '7-in-1 Electric Pressure Cooker, 6 Quart', 89.99, 4),
('Coffee Maker', 'Programmable Drip Coffee Machine', 79.99, 4),
('Blender', 'High Speed Professional Blender', 129.99, 4),
('Cookware Set', 'Non-stick 12-piece set', 199.99, 4),
('Vacuum Cleaner', 'Cordless Stick Vacuum', 249.99, 4),

-- Sports & Outdoors
('Yoga Mat', 'Premium 6mm Thick, Non-slip', 34.99, 5),
('Dumbbell Set', 'Adjustable 5-52.5 lbs', 299.99, 5),
('Camping Tent', '4-Person Waterproof Tent', 159.99, 5),
('Bicycle', 'Mountain Bike 27.5 inch', 499.99, 5),
('Basketball', 'Official Size Indoor/Outdoor', 29.99, 5);

-- ===================================================
-- Insert Inventory
-- ===================================================
INSERT INTO Inventory (product_id, quantity_available, reorder_level) VALUES
(1, 50, 10), (2, 75, 15), (3, 100, 20), (4, 60, 12), (5, 40, 8),
(6, 120, 25), (7, 150, 30), (8, 200, 40), (9, 80, 15), (10, 90, 18),
(11, 300, 50), (12, 200, 40), (13, 250, 45), (14, 180, 35), (15, 150, 30),
(16, 45, 10), (17, 55, 12), (18, 35, 8), (19, 25, 6), (20, 70, 15),
(21, 100, 20), (22, 30, 8), (23, 20, 5), (24, 15, 4), (25, 85, 18);

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
(3, 6, 2, 129.99),
(3, 8, 1, 29.99),
(3, 21, 2, 34.99),

-- Order 4
(4, 2, 2, 799.99),
(4, 5, 1, 449.99),

-- Order 5
(5, 11, 5, 14.99),
(5, 13, 3, 24.99),
(5, 21, 2, 34.99),

-- Order 6
(6, 4, 1, 599.99),

-- Order 7
(7, 6, 1, 129.99);

-- ===================================================
-- Insert Reviews
-- ===================================================
INSERT INTO Reviews (product_id, user_id, rating, comment, created_at) VALUES
(1, 2, 5, 'Excellent laptop! Fast and reliable.', '2024-11-20 14:00:00'),
(1, 3, 4, 'Good value for money, battery could be better.', '2024-11-22 10:30:00'),
(3, 2, 5, 'Best noise-cancelling headphones I''ve owned!', '2024-11-21 16:45:00'),
(6, 4, 5, 'Very comfortable running shoes.', '2024-12-05 09:20:00'),
(6, 5, 4, 'Great shoes but run a bit small.', '2024-12-08 11:15:00'),
(11, 6, 5, 'Classic book, must read!', '2024-11-28 13:40:00'),
(13, 2, 5, 'Life-changing book about habits.', '2024-12-14 15:25:00'),
(21, 4, 4, 'Good quality yoga mat.', '2024-12-06 08:50:00'),
(2, 5, 5, 'Amazing phone with great camera!', '2024-12-12 17:30:00'),
(16, 6, 4, 'Pressure cooker works well, a bit noisy.', '2024-11-30 12:10:00');

-- ===================================================
-- Verification Queries
-- ===================================================
SELECT 'Users Count:' as Info, COUNT(*) as Total FROM Users;
SELECT 'Categories Count:' as Info, COUNT(*) as Total FROM Categories;
SELECT 'Products Count:' as Info, COUNT(*) as Total FROM Products;
SELECT 'Inventory Count:' as Info, COUNT(*) as Total FROM Inventory;
SELECT 'Orders Count:' as Info, COUNT(*) as Total FROM Orders;
SELECT 'OrderItems Count:' as Info, COUNT(*) as Total FROM OrderItems;
SELECT 'Reviews Count:' as Info, COUNT(*) as Total FROM Reviews;

