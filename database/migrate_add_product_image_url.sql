-- =====================================================
-- Product Image URL Feature - Database Migration
-- Date: December 23, 2025
-- =====================================================

-- This script adds the image_url column to the Products table
-- Compatible with both PostgreSQL and MySQL

-- =====================================================
-- FOR POSTGRESQL
-- =====================================================

-- Add image_url column to Products table
ALTER TABLE Products
ADD COLUMN IF NOT EXISTS image_url VARCHAR(500) NULL DEFAULT NULL;

-- Add comment to column (PostgreSQL only)
COMMENT ON COLUMN Products.image_url IS 'URL path to product image (optional)';

-- Optional: Set default placeholder for existing products
-- UPDATE Products
-- SET image_url = 'https://via.placeholder.com/300x300?text=Product+Image'
-- WHERE image_url IS NULL;

-- =====================================================
-- FOR MYSQL
-- =====================================================

-- Add image_url column to Products table (MySQL syntax)
-- Uncomment if using MySQL instead of PostgreSQL

-- ALTER TABLE Products
-- ADD COLUMN image_url VARCHAR(500) NULL
-- COMMENT 'URL path to product image (optional)';

-- Optional: Set default placeholder for existing products
-- UPDATE Products
-- SET image_url = 'https://via.placeholder.com/300x300?text=Product+Image'
-- WHERE image_url IS NULL;

-- =====================================================
-- Verification Queries
-- =====================================================

-- Check if column was added successfully
SELECT column_name, data_type, character_maximum_length, is_nullable
FROM information_schema.columns
WHERE table_name = 'products'
  AND column_name = 'image_url';

-- View sample products with image URLs
SELECT product_id, product_name, image_url
FROM Products
LIMIT 10;

-- Count products with/without image URLs
SELECT
    COUNT(*) as total_products,
    COUNT(image_url) as products_with_images,
    COUNT(*) - COUNT(image_url) as products_without_images
FROM Products;

-- =====================================================
-- Rollback Script (if needed)
-- =====================================================

-- WARNING: This will remove the image_url column and all data
-- Only run this if you need to rollback the migration

-- ALTER TABLE Products DROP COLUMN image_url;

-- =====================================================
-- Sample Data (for testing)
-- =====================================================

-- Insert sample products with image URLs
-- Uncomment to test the feature

/*
INSERT INTO Products (product_name, description, price, category_id, image_url)
VALUES
    ('Sample Product 1', 'Test product with image', 29.99, 1, 'https://via.placeholder.com/300'),
    ('Sample Product 2', 'Another test product', 49.99, 1, 'https://via.placeholder.com/300/0000FF'),
    ('Sample Product 3', 'Product without image', 19.99, 1, NULL);
*/

-- Update existing product with image URL
/*
UPDATE Products
SET image_url = 'https://example.com/images/product-001.jpg'
WHERE product_id = 1;
*/

-- =====================================================
-- Performance Notes
-- =====================================================

-- No index needed on image_url as it's not used in WHERE clauses
-- Column is nullable to allow products without images
-- VARCHAR(500) provides adequate length for most URLs

-- =====================================================
-- End of Migration Script
-- =====================================================

