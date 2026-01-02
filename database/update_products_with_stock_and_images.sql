-- =====================================================
-- Update Sample Products with Stock and Image URLs
-- Purpose: Add stock_quantity and image_url to existing products
-- =====================================================

USE smart_ecommerce;

-- =====================================================
-- Update Electronics Products
-- =====================================================
UPDATE Products SET stock_quantity = 25, image_url = 'https://i.ebayimg.com/images/g/aicAAeSwAMdoNGK5/s-l1600.webp' WHERE product_name = 'Laptop HP ProBook';
UPDATE Products SET stock_quantity = 40, image_url = 'https://i.ebayimg.com/images/g/Vq4AAOSwQDtlZPLe/s-l1600.webp' WHERE product_name = 'Samsung Galaxy S23';
UPDATE Products SET stock_quantity = 60, image_url = 'https://i.ebayimg.com/images/g/rLEAAOSw7XJlYmX~/s-l1600.webp' WHERE product_name = 'Sony WH-1000XM5';
UPDATE Products SET stock_quantity = 30, image_url = 'https://i.ebayimg.com/images/g/0nYAAOSwH5Rlhbpc/s-l1600.webp' WHERE product_name = 'Apple iPad Air';
UPDATE Products SET stock_quantity = 15, image_url = 'https://i.ebayimg.com/images/g/FwYAAOSw3ZNlm7QN/s-l1600.webp' WHERE product_name = 'Dell Monitor 27 inch';

-- =====================================================
-- Update Clothing Products
-- =====================================================
UPDATE Products SET stock_quantity = 100, image_url = 'https://i.ebayimg.com/images/g/mBUAAOSw7WFk9pKi/s-l1600.webp' WHERE product_name = 'Nike Running Shoes';
UPDATE Products SET stock_quantity = 75, image_url = 'https://i.ebayimg.com/images/g/FGQAAOSwD5Vlgjk~/s-l1600.webp' WHERE product_name LIKE 'Levi%s Jeans 501';
UPDATE Products SET stock_quantity = 120, image_url = 'https://i.ebayimg.com/images/g/2WQAAOSwPFRlm4ZC/s-l1600.webp' WHERE product_name = 'Adidas T-Shirt';
UPDATE Products SET stock_quantity = 45, image_url = 'https://i.ebayimg.com/images/g/U3AAAOSwNBNlmGkw/s-l1600.webp' WHERE product_name = 'Winter Jacket';
UPDATE Products SET stock_quantity = 80, image_url = 'https://i.ebayimg.com/images/g/KhYAAOSwBGRlnMF~/s-l1600.webp' WHERE product_name = 'Formal Shirt';

-- =====================================================
-- Update Books Products
-- =====================================================
UPDATE Products SET stock_quantity = 200, image_url = 'https://i.ebayimg.com/images/g/GnQAAOSwPFZlm8Hq/s-l1600.webp' WHERE product_name = 'The Great Gatsby';
UPDATE Products SET stock_quantity = 150, image_url = 'https://i.ebayimg.com/images/g/5KIAAOSwVRBlmz7~/s-l1600.webp' WHERE product_name = 'Java Programming';
UPDATE Products SET stock_quantity = 180, image_url = 'https://i.ebayimg.com/images/g/WnIAAOSwBNtlm6pY/s-l1600.webp' WHERE product_name = 'Atomic Habits';
UPDATE Products SET stock_quantity = 220, image_url = 'https://i.ebayimg.com/images/g/cJ8AAOSw1Sxlm3BF/s-l1600.webp' WHERE product_name = '1984';
UPDATE Products SET stock_quantity = 140, image_url = 'https://i.ebayimg.com/images/g/zYYAAOSwh5plm92K/s-l1600.webp' WHERE product_name = 'Clean Code';

-- =====================================================
-- Update Home & Kitchen Products
-- =====================================================
UPDATE Products SET stock_quantity = 35, image_url = 'https://i.ebayimg.com/images/g/7fEAAOSwbVRlm5gH/s-l1600.webp' WHERE product_name = 'Instant Pot Duo';
UPDATE Products SET stock_quantity = 50, image_url = 'https://i.ebayimg.com/images/g/0JQAAOSwPOZlm7vK/s-l1600.webp' WHERE product_name = 'Coffee Maker';
UPDATE Products SET stock_quantity = 40, image_url = 'https://i.ebayimg.com/images/g/R5MAAOSw3HBlm4TE/s-l1600.webp' WHERE product_name = 'Blender';
UPDATE Products SET stock_quantity = 28, image_url = 'https://i.ebayimg.com/images/g/~NoAAOSwqGRlm6Qw/s-l1600.webp' WHERE product_name = 'Cookware Set';
UPDATE Products SET stock_quantity = 22, image_url = 'https://i.ebayimg.com/images/g/HTYAAOSwMJJlm8pX/s-l1600.webp' WHERE product_name = 'Vacuum Cleaner';

-- =====================================================
-- Update Sports & Outdoors Products
-- =====================================================
UPDATE Products SET stock_quantity = 90, image_url = 'https://i.ebayimg.com/images/g/cw4AAOSwVBllm3sQ/s-l1600.webp' WHERE product_name = 'Yoga Mat';
UPDATE Products SET stock_quantity = 20, image_url = 'https://i.ebayimg.com/images/g/gw8AAOSw0Kxlm5ZT/s-l1600.webp' WHERE product_name = 'Dumbbell Set';
UPDATE Products SET stock_quantity = 18, image_url = 'https://i.ebayimg.com/images/g/fhcAAOSwtullm4bR/s-l1600.webp' WHERE product_name = 'Camping Tent';
UPDATE Products SET stock_quantity = 12, image_url = 'https://i.ebayimg.com/images/g/KSYAAOSwZAVlm7GN/s-l1600.webp' WHERE product_name = 'Bicycle';
UPDATE Products SET stock_quantity = 85, image_url = 'https://i.ebayimg.com/images/g/XGoAAOSwMNxlm6fC/s-l1600.webp' WHERE product_name = 'Basketball';

-- =====================================================
-- Add some out of stock and low stock products for testing
-- =====================================================

-- Set some products to low stock (1-10)
UPDATE Products SET stock_quantity = 5 WHERE product_name = 'Dell Monitor 27 inch';
UPDATE Products SET stock_quantity = 8 WHERE product_name = 'Winter Jacket';
UPDATE Products SET stock_quantity = 3 WHERE product_name = 'Vacuum Cleaner';

-- Set some products to out of stock (0)
UPDATE Products SET stock_quantity = 0 WHERE product_name = 'Bicycle';

-- =====================================================
-- Verification Queries
-- =====================================================

-- Check products with images and stock
SELECT
    product_id,
    product_name,
    stock_quantity,
    CASE
        WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW STOCK'
        ELSE 'IN STOCK'
    END as stock_status,
    CASE
        WHEN image_url IS NULL THEN 'No Image'
        ELSE 'Has Image'
    END as image_status
FROM Products
ORDER BY product_id;

-- Count products by stock status
SELECT
    CASE
        WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW STOCK'
        ELSE 'IN STOCK'
    END as stock_status,
    COUNT(*) as product_count
FROM Products
GROUP BY
    CASE
        WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
        WHEN stock_quantity <= 10 THEN 'LOW STOCK'
        ELSE 'IN STOCK'
    END;

-- Show sample products with all details
SELECT
    product_id,
    product_name,
    price,
    stock_quantity,
    LEFT(image_url, 50) as image_url_preview
FROM Products
LIMIT 10;

