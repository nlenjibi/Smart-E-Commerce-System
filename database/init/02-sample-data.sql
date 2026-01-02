-- ===================================================
-- Sample Data for Smart E-Commerce System
-- PostgreSQL Version
-- ===================================================
-- This script inserts sample data for testing
-- Executed after schema creation (01-schema.sql)
-- ===================================================

-- ===================================================
-- Insert Categories
-- ===================================================
INSERT INTO Categories (category_name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Clothing', 'Apparel and fashion items'),
('Books', 'Physical and digital books'),
('Home & Garden', 'Home improvement and garden supplies'),
('Sports & Outdoors', 'Sporting goods and outdoor equipment'),
('Toys & Games', 'Toys and gaming products'),
('Health & Beauty', 'Health and beauty products'),
('Food & Beverages', 'Food items and beverages');

-- ===================================================
-- Insert Users (Customers and Admins)
-- Password hashes are SHA-256 of 'password123'
-- ===================================================
INSERT INTO Users (username, email, password_hash, role) VALUES
('admin', 'admin@smartcommerce.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'ADMIN'),
('john_doe', 'john.doe@email.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER'),
('jane_smith', 'jane.smith@email.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER'),
('bob_wilson', 'bob.wilson@email.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER'),
('alice_brown', 'alice.brown@email.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER');

-- ===================================================
-- Insert Products with proper stock and images
-- ===================================================
INSERT INTO Products (product_name, description, price, category_id, stock_quantity, image_url) VALUES
-- Electronics
('Laptop Pro 15"', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99, 1, 25, 'images/products/laptop.jpg'),
('Wireless Mouse', 'Ergonomic wireless mouse with 3 DPI settings', 29.99, 1, 150, 'images/products/mouse.jpg'),
('USB-C Hub', '7-in-1 USB-C hub with HDMI and SD card reader', 49.99, 1, 80, 'images/products/usb-hub.jpg'),
('Bluetooth Headphones', 'Noise-cancelling wireless headphones', 199.99, 1, 60, 'images/products/headphones.jpg'),
('4K Monitor 27"', 'Ultra HD 4K monitor with HDR support', 399.99, 1, 35, 'images/products/monitor.jpg'),

-- Clothing
('Cotton T-Shirt', 'Comfortable 100% cotton t-shirt', 19.99, 2, 200, 'images/products/tshirt.jpg'),
('Denim Jeans', 'Classic fit denim jeans', 59.99, 2, 120, 'images/products/jeans.jpg'),
('Winter Jacket', 'Warm winter jacket with hood', 129.99, 2, 45, 'images/products/jacket.jpg'),
('Running Shoes', 'Lightweight running shoes with cushioning', 89.99, 2, 90, 'images/products/shoes.jpg'),
('Baseball Cap', 'Adjustable baseball cap', 24.99, 2, 175, 'images/products/cap.jpg'),

-- Books
('The Art of Programming', 'Comprehensive programming guide', 49.99, 3, 55, 'images/products/programming-book.jpg'),
('Database Design Fundamentals', 'Learn database design principles', 39.99, 3, 40, 'images/products/database-book.jpg'),
('Fiction Bestseller', 'Award-winning fiction novel', 24.99, 3, 100, 'images/products/fiction.jpg'),
('Cookbook Collection', 'Delicious recipes from around the world', 34.99, 3, 65, 'images/products/cookbook.jpg'),
('Self-Help Guide', 'Personal development and motivation', 29.99, 3, 85, 'images/products/selfhelp.jpg'),

-- Home & Garden
('LED Desk Lamp', 'Adjustable LED desk lamp with USB port', 39.99, 4, 70, 'images/products/desk-lamp.jpg'),
('Garden Tool Set', '10-piece gardening tool set', 79.99, 4, 30, 'images/products/garden-tools.jpg'),
('Smart Thermostat', 'WiFi-enabled smart thermostat', 159.99, 4, 50, 'images/products/thermostat.jpg'),
('Bed Sheets Set', 'Soft microfiber bed sheets set', 44.99, 4, 95, 'images/products/bedsheets.jpg'),
('Wall Clock', 'Modern minimalist wall clock', 34.99, 4, 110, 'images/products/clock.jpg'),

-- Sports & Outdoors
('Yoga Mat', 'Non-slip exercise yoga mat', 29.99, 5, 140, 'images/products/yoga-mat.jpg'),
('Camping Tent', '4-person waterproof camping tent', 149.99, 5, 25, 'images/products/tent.jpg'),
('Dumbbell Set', 'Adjustable dumbbell set with rack', 199.99, 5, 35, 'images/products/dumbbells.jpg'),
('Bicycle Helmet', 'Safety-certified bicycle helmet', 49.99, 5, 75, 'images/products/helmet.jpg'),
('Water Bottle', 'Insulated stainless steel water bottle', 24.99, 5, 200, 'images/products/water-bottle.jpg');

-- ===================================================
-- Insert Inventory records for all products
-- ===================================================
INSERT INTO Inventory (product_id, quantity_available, reorder_level)
SELECT product_id, stock_quantity, 10
FROM Products;

-- ===================================================
-- Insert Sample Orders
-- ===================================================
INSERT INTO Orders (user_id, total_amount, status) VALUES
(2, 1349.98, 'DELIVERED'),
(3, 79.98, 'SHIPPED'),
(4, 249.97, 'CONFIRMED'),
(5, 159.99, 'PENDING'),
(2, 89.99, 'DELIVERED');

-- ===================================================
-- Insert Order Items
-- ===================================================
INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES
-- Order 1 (user 2)
(1, 1, 1, 1299.99),
(1, 2, 1, 29.99),
(1, 3, 1, 49.99),

-- Order 2 (user 3)
(2, 6, 2, 19.99),
(2, 10, 1, 24.99),

-- Order 3 (user 4)
(3, 4, 1, 199.99),
(3, 22, 1, 49.99),

-- Order 4 (user 5)
(4, 18, 1, 159.99),

-- Order 5 (user 2)
(5, 9, 1, 89.99);

-- ===================================================
-- Insert Sample Reviews
-- ===================================================
INSERT INTO Reviews (product_id, user_id, rating, comment) VALUES
(1, 2, 5, 'Excellent laptop! Very fast and great build quality.'),
(1, 3, 4, 'Good laptop but a bit pricey. Worth it for the performance.'),
(4, 2, 5, 'Best headphones I have ever owned. Noise cancellation is amazing!'),
(6, 3, 5, 'Very comfortable t-shirt. Will buy more colors.'),
(9, 4, 4, 'Great running shoes with good cushioning.'),
(11, 5, 5, 'Excellent book for learning programming. Highly recommend!'),
(22, 4, 5, 'Very comfortable and safe helmet. Fits perfectly.'),
(21, 2, 4, 'Good yoga mat but could be a bit thicker.'),
(18, 5, 5, 'Smart thermostat is easy to install and saves energy!'),
(25, 3, 5, 'Perfect water bottle for hiking. Keeps water cold all day.');

-- ===================================================
-- Success message
-- ===================================================
DO $$
BEGIN
    RAISE NOTICE 'Sample data inserted successfully!';
    RAISE NOTICE 'Users: 5 (1 admin, 4 customers)';
    RAISE NOTICE 'Categories: 8';
    RAISE NOTICE 'Products: 25';
    RAISE NOTICE 'Orders: 5';
    RAISE NOTICE 'Reviews: 10';
END $$;

