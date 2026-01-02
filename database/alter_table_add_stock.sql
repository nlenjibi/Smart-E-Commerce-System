-- ============================================
-- Add Stock Quantity Column to Products Table
-- Date: December 23, 2025
-- ============================================

-- Check if column exists (MySQL 5.7+)
-- If the column already exists, this will fail gracefully
-- You can run this to check first:
-- SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS
-- WHERE TABLE_SCHEMA = 'smartecommerce_db'
-- AND TABLE_NAME = 'Products'
-- AND COLUMN_NAME = 'stock_quantity';

-- ============================================
-- ALTER TABLE - Add stock_quantity column
-- ============================================

ALTER TABLE Products
ADD COLUMN stock_quantity INT NOT NULL DEFAULT 0
COMMENT 'Current stock quantity available';

-- ============================================
-- Optional: Add index for stock queries
-- ============================================

CREATE INDEX idx_stock_quantity ON Products(stock_quantity);

-- ============================================
-- Optional: Add check constraint (MySQL 8.0+)
-- Ensures stock quantity is never negative
-- ============================================

ALTER TABLE Products
ADD CONSTRAINT chk_stock_quantity_positive
CHECK (stock_quantity >= 0);

-- ============================================
-- Update existing products with default stock
-- ============================================

-- Set all existing products to have 10 units in stock
UPDATE Products
SET stock_quantity = 10
WHERE stock_quantity = 0;

-- ============================================
-- Verify the change
-- ============================================

-- Check table structure
DESCRIBE Products;

-- Check stock values
SELECT product_id, product_name, stock_quantity
FROM Products
ORDER BY stock_quantity DESC
LIMIT 10;

-- ============================================
-- Sample data for testing (optional)
-- ============================================

-- Update specific products with realistic stock levels
UPDATE Products SET stock_quantity = 50 WHERE product_name LIKE '%Laptop%';
UPDATE Products SET stock_quantity = 100 WHERE product_name LIKE '%Mouse%';
UPDATE Products SET stock_quantity = 25 WHERE product_name LIKE '%Keyboard%';
UPDATE Products SET stock_quantity = 15 WHERE product_name LIKE '%Monitor%';
UPDATE Products SET stock_quantity = 5 WHERE product_name LIKE '%Headphones%';
UPDATE Products SET stock_quantity = 0 WHERE product_name LIKE '%Out of Stock%';

-- ============================================
-- Rollback (if needed)
-- ============================================

-- To remove the column (use with caution!):
-- ALTER TABLE Products DROP COLUMN stock_quantity;

-- To remove the index:
-- DROP INDEX idx_stock_quantity ON Products;

-- To remove the constraint:
-- ALTER TABLE Products DROP CONSTRAINT chk_stock_quantity_positive;

-- ============================================
-- END OF SCRIPT
-- ============================================

