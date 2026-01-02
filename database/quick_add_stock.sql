-- Quick ALTER TABLE to add stock_quantity column
-- Run this in your MySQL database

USE smartecommerce_db;

-- Add stock_quantity column
ALTER TABLE Products
ADD COLUMN stock_quantity INT NOT NULL DEFAULT 0;

-- Set initial stock for existing products
UPDATE Products SET stock_quantity = 10;

-- Verify
SELECT product_id, product_name, stock_quantity FROM Products LIMIT 5;

