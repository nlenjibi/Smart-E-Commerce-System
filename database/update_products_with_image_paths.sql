-- =====================================================
-- Update Products with Image Paths from IMAGE_PATHS_REFERENCE.md
-- Date: December 24, 2025
-- Purpose: Map product categories to local image paths
-- =====================================================

USE smart_ecommerce;

-- =====================================================
-- PART 1: View Current Products and Categories
-- =====================================================

-- View all categories
SELECT
    category_id,
    category_name,
    description
FROM Categories
ORDER BY category_id;

-- View all products with their current image URLs
SELECT
    p.product_id,
    p.product_name,
    p.description,
    p.price,
    c.category_name,
    p.stock_quantity,
    p.image_url as current_image_url
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
ORDER BY c.category_name, p.product_name;

-- =====================================================
-- PART 2: Category Mapping
-- =====================================================

/*
Database Categories → Image Folder Mapping:
- Electronics → Accessories/SmartWearables
- Clothing → Women, Men (image2/Men), Kids
- Books → (No direct mapping - use placeholder)
- Home & Kitchen → HomeNliving
- Sports & Outdoors → (Can use Kids sports items)
- Toys & Games → Kids
- Health & Beauty → Accessories (Skincare, HairCare, Fragrance)
- Automotive → (No direct mapping - use placeholder)
*/

-- =====================================================
-- PART 3: Update Products with Local Image Paths
-- =====================================================

-- Update Electronics Category Products
-- Using Accessories/SmartWearables images
UPDATE Products p
JOIN Categories c ON p.category_id = c.category_id
SET p.image_url = CASE p.product_name
    WHEN 'Laptop HP ProBook' THEN 'images/Accessories/SmartWearables/Aplle_Nike_Watch.jpg'
    WHEN 'Samsung Galaxy S23' THEN 'images/Accessories/SmartWearables/WirelessAirpods.jpg'
    WHEN 'Sony WH-1000XM5' THEN 'images/Accessories/SmartWearables/SennheiserBlackHD350.jpg'
    WHEN 'Apple iPad Air' THEN 'images/Accessories/SmartWearables/FrenchConnectionFitness.jpg'
    WHEN 'Dell Monitor 27 inch' THEN 'images/Accessories/SmartWearables/FitnessNeckBand.jpg'
    ELSE p.image_url
END
WHERE c.category_name = 'Electronics';

-- Update Clothing Category Products
-- Using Women, Men, and Kids clothing images
UPDATE Products p
JOIN Categories c ON p.category_id = c.category_id
SET p.image_url = CASE p.product_name
    WHEN 'Nike Running Shoes' THEN 'images/image2/Men/Shoes/NikeAirForce1.png'
    WHEN 'Levi''s Jeans 501' THEN 'images/image2/Men/Bottoms/Jeans.png'
    WHEN 'Adidas T-Shirt' THEN 'images/Women/Tees/OrangeTee.jpg'
    WHEN 'Winter Jacket' THEN 'images/Women/jackets/DarkBlue.png'
    WHEN 'Formal Shirt' THEN 'images/image2/Men/Tees/AwesomeSolidWhiteColor.jpg'
    ELSE p.image_url
END
WHERE c.category_name = 'Clothing';

-- Update Books Category Products
-- Using generic images from image2
UPDATE Products p
JOIN Categories c ON p.category_id = c.category_id
SET p.image_url = CASE p.product_name
    WHEN 'The Great Gatsby' THEN 'images/image2/5star.png'
    WHEN 'Java Programming' THEN 'images/image2/rating5.png'
    WHEN 'Atomic Habits' THEN 'images/image2/star5.png'
    WHEN '1984' THEN 'images/image2/rating4.png'
    WHEN 'Clean Code' THEN 'images/image2/star4.png'
    ELSE p.image_url
END
WHERE c.category_name = 'Books';

-- Update Home & Kitchen Category Products
-- Using HomeNliving images
UPDATE Products p
JOIN Categories c ON p.category_id = c.category_id
SET p.image_url = CASE p.product_name
    WHEN 'Instant Pot Duo' THEN 'images/HomeNliving/Dining/8_Seater.png'
    WHEN 'Coffee Maker' THEN 'images/HomeNliving/Dining/Table_2_Wooden.png'
    WHEN 'Blender' THEN 'images/HomeNliving/Dining/marblewhite6seater.png'
    WHEN 'Cookware Set' THEN 'images/HomeNliving/Dining/Table_4_PeacheWhite.png'
    WHEN 'Vacuum Cleaner' THEN 'images/HomeNliving/office_Chair.png'
    ELSE p.image_url
END
WHERE c.category_name = 'Home & Kitchen';

-- Update Sports & Outdoors Category Products
-- Using Kids and general sports images
UPDATE Products p
JOIN Categories c ON p.category_id = c.category_id
SET p.image_url = CASE p.product_name
    WHEN 'Yoga Mat' THEN 'images/Kids/Tees/ColorBlock.jpg'
    WHEN 'Dumbbell Set' THEN 'images/image2/Men/Shoes/YeezyAdidas.png'
    WHEN 'Camping Tent' THEN 'images/HomeNliving/BeachChair.png'
    WHEN 'Bicycle' THEN 'images/Kids/Converse.jpg'
    WHEN 'Basketball' THEN 'images/Kids/Tees/GirlsAbstract.jpg'
    ELSE p.image_url
END
WHERE c.category_name = 'Sports & Outdoors';

-- =====================================================
-- PART 4: Add New Products with Proper Image Paths
-- =====================================================

-- Clear existing products first (optional - only if you want fresh start)
-- DELETE FROM OrderItems;
-- DELETE FROM Orders;
-- DELETE FROM Inventory;
-- DELETE FROM Products;

-- Add Women's Fashion Products
INSERT INTO Products (product_name, description, price, category_id, stock_quantity, image_url) VALUES
-- Women's Bags
('Black Shoulder Bag', 'Classic black shoulder bag', 89.99, 2, 45, 'images/Women/BlackBag1.png'),
('Metallic Shoulder Bag', 'Stylish metallic shoulder bag', 129.99, 2, 30, 'images/Women/Solid_MetallicShoulderBag.png'),
('Louis Vuitton White Bag', 'Solid white designer bag', 299.99, 2, 15, 'images/Women/SolidWhiteLouisVuitton.png'),
('Croc Style Sling Bag', 'Trendy croc style sling bag', 79.99, 2, 50, 'images/Women/irene_kredenets_CrocStyleSlingBag.png'),

-- Women's Bottoms
('Army Cargo Pants', 'Comfortable army cargo pants', 69.99, 2, 60, 'images/Women/bottom/ArmyCargoPants.png'),
('Black Joggers', 'Athletic black joggers', 49.99, 2, 80, 'images/Women/bottom/BlackJoggers.png'),
('Blue Denim Jeans', 'Classic blue denim jeans', 89.99, 2, 70, 'images/Women/bottom/Bluedenim.png'),
('Floral Bottoms', 'Stylish floral pattern bottoms', 59.99, 2, 55, 'images/Women/bottom/FloralBottoms.png'),

-- Women's Hoodies
('Adidas ColorBlock Hoodie', 'Sporty colorblock hoodie', 79.99, 2, 40, 'images/Women/Hoodies/AdidasColorBlock.jpg'),
('Beige Hoodie', 'Comfortable beige hoodie', 65.99, 2, 50, 'images/Women/Hoodies/Beige.png'),
('Dog Lover Hoodie', 'Cute dog lover graphic hoodie', 55.99, 2, 45, 'images/Women/Hoodies/DogLover.png'),
('Purple Haze Hoodie', 'Solid purple haze hoodie', 69.99, 2, 35, 'images/Women/Hoodies/SolidPurpleHaze.png'),

-- Women's Jackets
('Army Green Cargo Jacket', 'Trendy army green cargo jacket', 119.99, 2, 25, 'images/Women/jackets/ArmyGreenCargo.png'),
('Black Double Button Jacket', 'Elegant black double button jacket', 139.99, 2, 20, 'images/Women/jackets/BlackDoubleButton.png'),
('Checked Jacket', 'Classic checked pattern jacket', 99.99, 2, 30, 'images/Women/jackets/Checked_Jacket.png'),
('Red Leather Jacket', 'Bold red leather jacket', 179.99, 2, 15, 'images/Women/jackets/RedLeather.png'),

-- Women's Shoes
('Classic Black Converse', 'Timeless black Converse sneakers', 79.99, 2, 90, 'images/Women/Shoes/ClassicBlackConverse.jpg'),
('Floral Heels', 'Elegant floral pattern heels', 129.99, 2, 35, 'images/Women/Shoes/FloralHeels.png'),
('Nike ColorBlock Shoes', 'Athletic Nike colorblock design', 119.99, 2, 50, 'images/Women/Shoes/NikeColorBlock.png'),
('Jimmy Choo Sparkly Heels', 'Luxury sparkly heels', 399.99, 2, 10, 'images/Women/Shoes/SparklyHeels_JimmyCHOOLONDON.png'),

-- Women's Tees
('Broken Saints Tee', 'Graphic broken saints tee', 35.99, 2, 70, 'images/Women/Tees/BrokenSaints.jpg'),
('Dark Grey Tee', 'Basic dark grey tee', 29.99, 2, 100, 'images/Women/Tees/DarkGrey.jpg'),
('Floral Gucci Logo Tee', 'Designer floral Gucci logo tee', 159.99, 2, 20, 'images/Women/Tees/FloralGucciLogo.jpg'),
('Vogue White Tee', 'Stylish vogue white tee', 45.99, 2, 60, 'images/Women/Tees/VogueWhiteTEE.jpg'),

-- Women's Watches
('Rose Gold Watch', 'Elegant rose gold watch', 199.99, 1, 25, 'images/Women/Watches/RoseGold.png'),
('Rolex Watch', 'Luxury Rolex timepiece', 5999.99, 1, 5, 'images/Women/Watches/Rolex.png'),
('Wood Watch', 'Eco-friendly wooden watch', 149.99, 1, 30, 'images/Women/Watches/woodwatch.png'),
('Metallic Watch', 'Modern metallic watch', 179.99, 1, 20, 'images/Women/Watches/Metallic.png'),

-- Men's Clothing
('Grey Chinos', 'Comfortable grey chinos', 69.99, 2, 75, 'images/image2/Men/Bottoms/greyChinos.png'),
('Check Grey Chinos', 'Stylish check grey chinos', 79.99, 2, 60, 'images/image2/Men/Bottoms/CheckGreyChinos.png'),
('Navy Blue Formal Trousers', 'Professional navy blue trousers', 89.99, 2, 50, 'images/image2/Men/Bottoms/NavyBlueFormalTrousers.png'),
('Ripped Jeans Vans', 'Trendy ripped jeans', 99.99, 2, 45, 'images/image2/Men/Bottoms/rippedjeans_Vans.jpg'),

-- Men's Hoodies
('Classic Black ZARA Hoodie', 'Premium ZARA black hoodie', 89.99, 2, 55, 'images/image2/Men/Hoodies/ClassicBlack_ZARA.png'),
('Creme Color Hoodie', 'Comfortable creme hoodie', 75.99, 2, 40, 'images/image2/Men/Hoodies/CremeColor.png'),
('Light Orange OFFWHITE', 'Designer OFFWHITE orange hoodie', 199.99, 2, 20, 'images/image2/Men/Hoodies/LightOrange_By_OFFWHITE.png'),
('Vans Original Black', 'Classic Vans black hoodie', 69.99, 2, 65, 'images/image2/Men/Hoodies/VansOriginalBlack.png'),

-- Men's Jackets
('Black Leather Jacket', 'Classic black leather jacket', 249.99, 2, 30, 'images/image2/Men/Jackets/BlackLeatherJacket.png'),
('Grey Over Jacket', 'Casual grey over jacket', 119.99, 2, 40, 'images/image2/Men/Jackets/GreyOver.jpg'),
('Olive Green Jacket', 'Military-style olive green jacket', 139.99, 2, 35, 'images/image2/Men/Jackets/OliveGreen.png'),
('Turquoise Jacket', 'Bold turquoise jacket', 129.99, 2, 25, 'images/image2/Men/Jackets/Turquoise.jpg'),

-- Men's Shoes
('Nike Air Max Product Red', 'Limited edition Nike Air Max', 189.99, 2, 40, 'images/image2/Men/Shoes/NikeAirMaxProductRed.jpg'),
('Unisex Black Converse', 'Classic unisex Converse', 79.99, 2, 100, 'images/image2/Men/Shoes/UnisexBlackConverse.jpg'),
('Vans Sneakers', 'Casual Vans sneakers', 69.99, 2, 85, 'images/image2/Men/Shoes/Vans.png'),
('Yeezy Adidas', 'Designer Yeezy Adidas', 349.99, 2, 15, 'images/image2/Men/Shoes/YeezyAdidas.png'),

-- Men's Tees
('Audere Tee', 'Graphic audere tee', 39.99, 2, 70, 'images/image2/Men/Tees/audere.png'),
('Black Graphic Tee', 'Cool black graphic tee', 45.99, 2, 80, 'images/image2/Men/Tees/BlackGraphicTee.png'),
('Champion Enscription Tee', 'Champion brand tee', 49.99, 2, 60, 'images/image2/Men/Tees/EnscriptionbyChampion.png'),
('Solid Flame Orange Tee', 'Bold flame orange tee', 42.99, 2, 55, 'images/image2/Men/Tees/SolidFlameOrange.png'),

-- Men's Watches
('Apple Watch Series 6', 'Latest Apple Watch', 399.99, 1, 35, 'images/image2/Men/AppleWatchseries6.jpg'),
('Brown Tissot Watch', 'Elegant brown Tissot', 299.99, 1, 20, 'images/image2/Men/BrownTissot.jpg'),
('Fossil Classic Watch', 'Classic Fossil timepiece', 179.99, 1, 30, 'images/image2/Men/FossilClassic.jpg'),
('MVMT Rose Gold Dial', 'Stylish MVMT watch', 149.99, 1, 40, 'images/image2/Men/MVMTrosegolddial.jpg'),

-- Kids Clothing
('Brown Girl Boots', 'Cute brown boots for girls', 59.99, 6, 50, 'images/Kids/BrownGirlBoots.jpg'),
('Kids Converse Shoes', 'Classic kids Converse', 49.99, 6, 80, 'images/Kids/Converse.jpg'),
('Silver Shoes Kids', 'Shiny silver shoes', 45.99, 6, 60, 'images/Kids/Silvershoes.jpg'),

-- Kids Hoodies
('Creme Brown Hoodie', 'Cozy creme brown hoodie', 39.99, 6, 55, 'images/Kids/Hoodie/CremeBrown.jpg'),
('Grey Knit Hoodie', 'Warm grey knit hoodie', 44.99, 6, 45, 'images/Kids/Hoodie/GreyKnit.jpg'),
('Sky Blue Hoodie', 'Bright sky blue hoodie', 42.99, 6, 50, 'images/Kids/Hoodie/SkyBlue.jpg'),
('Yellow Wollen Hoodie', 'Cozy yellow woolen hoodie', 49.99, 6, 40, 'images/Kids/Hoodie/YelloWollen.jpg'),

-- Kids Jackets
('Boys Blue Denim Jacket', 'Classic boys denim jacket', 54.99, 6, 45, 'images/Kids/Jacket/BoyesBlueDenim.jpg'),
('Boys Black Denim Jacket', 'Stylish black denim', 59.99, 6, 40, 'images/Kids/Jacket/BoysBlackDenim.jpg'),
('Girls Overcoat', 'Elegant girls overcoat', 79.99, 6, 30, 'images/Kids/Jacket/GirlsOvercoat.jpg'),
('Girls Denim Jacket', 'Trendy girls denim', 54.99, 6, 45, 'images/Kids/Jacket/Girls_Denim.jpg'),

-- Kids Jeans
('Cargo Denims', 'Comfortable cargo denims', 44.99, 6, 60, 'images/Kids/Jeans/CargoDenims.jpg'),
('Dark Blue Denims', 'Classic dark blue jeans', 39.99, 6, 70, 'images/Kids/Jeans/DarkBlueDenims.jpg'),
('Girls Basketball Jeans', 'Athletic girls jeans', 49.99, 6, 50, 'images/Kids/Jeans/GirlsBascketballJeans.jpg'),
('Girls Ripped Jeans', 'Trendy ripped jeans', 54.99, 6, 45, 'images/Kids/Jeans/GirlsRippedJeans.jpg'),

-- Kids Watches
('Blue Design Watch', 'Colorful blue design watch', 29.99, 6, 70, 'images/Kids/Watch/Bluedesign.jpg'),
('Blue Kids Watch', 'Simple blue kids watch', 24.99, 6, 80, 'images/Kids/Watch/Bluewatch.jpg'),
('Girls Pink Watch', 'Cute pink watch for girls', 27.99, 6, 75, 'images/Kids/Watch/Girlswatchpink.jpg'),
('Orange Kids Watch', 'Bright orange watch', 26.99, 6, 65, 'images/Kids/Watch/Orange.jpg'),

-- Home & Living Products
('Beach Chair', 'Comfortable beach chair', 89.99, 4, 40, 'images/HomeNliving/BeachChair.png'),
('Funky ColorBlock Chair', 'Modern colorblock chair', 159.99, 4, 25, 'images/HomeNliving/Funky_ColorBlock.png'),
('Grey Suede Chair', 'Elegant grey suede chair', 249.99, 4, 20, 'images/HomeNliving/GreaySuede.png'),
('Kitchen Bar Chair', 'Stylish bar chair', 119.99, 4, 35, 'images/HomeNliving/KitchenBar_Chair.png'),
('Leather Lounge Chair', 'Luxury leather lounge', 399.99, 4, 15, 'images/HomeNliving/Leather-LoungeChair.png'),
('Office Chair', 'Ergonomic office chair', 279.99, 4, 30, 'images/HomeNliving/office_Chair.png'),
('Wooden Chairs', 'Classic wooden chairs set', 349.99, 4, 20, 'images/HomeNliving/WoodenChairs.png'),

-- Carpets
('Abstract Carpet', 'Modern abstract pattern carpet', 189.99, 4, 25, 'images/HomeNliving/Carpet/Abstract.png'),
('Black Gold Stripes Carpet', 'Elegant striped carpet', 229.99, 4, 20, 'images/HomeNliving/Carpet/BlackGoldStripes.png'),
('Navy Blue Design Carpet', 'Classic navy design', 199.99, 4, 30, 'images/HomeNliving/Carpet/NavyBlueDesign.png'),
('Off White Fur Carpet', 'Soft fur carpet', 299.99, 4, 15, 'images/HomeNliving/Carpet/OffWhiteFur.png'),

-- Lamps
('Custom Neon Lights', 'Customizable neon lights', 149.99, 4, 35, 'images/HomeNliving/Lamp/CustomNeonLights.png'),
('Kitchen Lights', 'Modern kitchen lighting', 129.99, 4, 40, 'images/HomeNliving/Lamp/KitchenLights.png'),
('Lounge Lights', 'Ambient lounge lighting', 179.99, 4, 25, 'images/HomeNliving/Lamp/LoungeLights.png'),
('Modern Bedroom Lights', 'Contemporary bedroom lights', 159.99, 4, 30, 'images/HomeNliving/Lamp/ModernBedroomLights.png'),

-- Side Tables
('Circle Table', 'Modern circle side table', 119.99, 4, 30, 'images/HomeNliving/SideTable/CircleTable.png'),
('Multi Drawer Table', 'Functional drawer table', 189.99, 4, 20, 'images/HomeNliving/SideTable/LightBrown_MultiDrawertable.png'),
('Oak Wooden Tables', 'Classic oak tables', 249.99, 4, 15, 'images/HomeNliving/SideTable/OakWoodenTables.png'),
('Modern Side Table', 'Off-white modern table', 139.99, 4, 25, 'images/HomeNliving/SideTable/OffwhiteModernSideTable.png'),

-- Sofas
('Black Leather L-shaped', 'Luxury L-shaped sofa', 1299.99, 4, 10, 'images/HomeNliving/Sofas/BlackLeatherLshaped.png'),
('Brown Leather Sofa', 'Classic brown leather', 999.99, 4, 15, 'images/HomeNliving/Sofas/BrownLeather.png'),
('Classic White 6 Seater', 'Elegant white sofa', 1499.99, 4, 8, 'images/HomeNliving/Sofas/ClassicWhite6Seater.png'),
('Grey Blue Sofa', 'Modern grey blue sofa', 899.99, 4, 12, 'images/HomeNliving/Sofas/GreyBlue.png'),

-- Accessories - Beauty Products
('Blush Brush', 'Professional blush brush', 24.99, 7, 80, 'images/Accessories/BlushBrush2.jpg'),
('Concealer', 'Full coverage concealer', 19.99, 7, 100, 'images/Accessories/Concealer.jpg'),
('Lip Gloss', 'Shiny lip gloss', 14.99, 7, 120, 'images/Accessories/Lipgloss.jpg'),
('Lipstick', 'Long-lasting lipstick', 16.99, 7, 110, 'images/Accessories/lipstick.jpg'),
('Nail Polish Pack of 3', 'Nail polish set', 29.99, 7, 70, 'images/Accessories/NailPolishPackof3.png'),

-- Accessories - Appliances
('Electric Hair Brush', 'Heated hair brush', 49.99, 7, 45, 'images/Accessories/Appliances/ElectricHairBrush.jpg'),
('Epilator', 'Professional epilator', 79.99, 7, 35, 'images/Accessories/Appliances/epilator.jpg'),
('Hair Dryer', 'Professional hair dryer', 89.99, 7, 40, 'images/Accessories/Appliances/HairDryer.png'),
('Hair Trimmer', 'Cordless hair trimmer', 59.99, 7, 50, 'images/Accessories/Appliances/HairTimmer.png'),
('Hair Straightener', 'Ceramic hair straightener', 69.99, 7, 45, 'images/Accessories/Appliances/Hair_Straigthner.png'),

-- Accessories - Fragrances
('Aqua Algoria Goddess', 'Fresh floral fragrance', 89.99, 7, 30, 'images/Accessories/Fragrance/AQUA_ALGORIA_GODESS.png'),
('Chanel Ether', 'Luxury Chanel perfume', 149.99, 7, 20, 'images/Accessories/Fragrance/CHANEL_ETHER.png'),
('Daisy Marc Jacobs', 'Sweet daisy fragrance', 119.99, 7, 25, 'images/Accessories/Fragrance/DAISY_MARKJACOBS.png'),
('Dolce & Gabbana The One', 'Elegant D&G perfume', 139.99, 7, 22, 'images/Accessories/Fragrance/DOLCE_GABANA_THEONE.png'),
('Jeanne Lanvin The Sin', 'Sophisticated Lanvin scent', 129.99, 7, 18, 'images/Accessories/Fragrance/JEANE_LANVIN_THESIN.png'),

-- Accessories - Hair Care
('Bath & Body Works', 'Hair care set', 34.99, 7, 60, 'images/Accessories/HairCare/Bath&BodyWORKS.jpg'),
('Hair Conditioner', 'Moisturizing conditioner', 24.99, 7, 80, 'images/Accessories/HairCare/HairConditioner.jpg'),
('Hair Cream', 'Styling hair cream', 19.99, 7, 90, 'images/Accessories/HairCare/HairCream.png'),
('Hair Serum', 'Shine boosting serum', 29.99, 7, 70, 'images/Accessories/HairCare/HairSerum.jpg'),
('Hair Spray', 'Strong hold spray', 22.99, 7, 75, 'images/Accessories/HairCare/HairSpray.jpg'),

-- Accessories - Skincare
('Body Lotion', 'Moisturizing body lotion', 27.99, 7, 85, 'images/Accessories/Skincare/BodyLotion.jpg'),
('Brownie Lip Balm', 'Nourishing lip balm', 9.99, 7, 150, 'images/Accessories/Skincare/BrownieLipBalm.jpg'),
('Face Mask', 'Hydrating face mask', 34.99, 7, 65, 'images/Accessories/Skincare/FaceMask.jpg'),
('Face Moisturiser', 'Daily face moisturizer', 39.99, 7, 70, 'images/Accessories/Skincare/FaceMoisturisers.jpg'),
('Sunscreen', 'SPF 50 sunscreen', 29.99, 7, 100, 'images/Accessories/Skincare/SunScreen.jpg'),

-- Accessories - Smart Wearables
('Apple Nike Watch', 'Apple Watch Nike edition', 449.99, 1, 25, 'images/Accessories/SmartWearables/Aplle_Nike_Watch.jpg'),
('Fitness Neck Band', 'Wireless fitness band', 79.99, 1, 45, 'images/Accessories/SmartWearables/FitnessNeckBand.jpg'),
('French Connection Fitness', 'Premium fitness tracker', 129.99, 1, 35, 'images/Accessories/SmartWearables/FrenchConnectionFitness.jpg'),
('Sennheiser Black HD350', 'Professional headphones', 199.99, 1, 30, 'images/Accessories/SmartWearables/SennheiserBlackHD350.jpg'),
('Wireless Airpods', 'Premium wireless earbuds', 179.99, 1, 50, 'images/Accessories/SmartWearables/WirelessAirpods.jpg');

-- =====================================================
-- PART 5: Verification Queries
-- =====================================================

-- Count products by category with image status
SELECT
    c.category_name,
    COUNT(p.product_id) as total_products,
    COUNT(p.image_url) as products_with_images,
    COUNT(*) - COUNT(p.image_url) as products_without_images
FROM Categories c
LEFT JOIN Products p ON c.category_id = p.category_id
GROUP BY c.category_name
ORDER BY c.category_name;

-- View all products with their image paths
SELECT
    p.product_id,
    p.product_name,
    c.category_name,
    p.price,
    p.stock_quantity,
    p.image_url,
    CASE
        WHEN p.image_url IS NULL THEN '❌ No Image'
        WHEN p.image_url LIKE 'images/%' THEN '✅ Local Path'
        WHEN p.image_url LIKE 'http%' THEN '🌐 Web URL'
        ELSE '⚠️ Unknown'
    END as image_type
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
ORDER BY c.category_name, p.product_name;

-- Products grouped by image folder
SELECT
    SUBSTRING_INDEX(SUBSTRING_INDEX(image_url, '/', 2), '/', -1) as main_folder,
    COUNT(*) as product_count
FROM Products
WHERE image_url LIKE 'images/%'
GROUP BY main_folder
ORDER BY product_count DESC;

-- Stock summary
SELECT
    COUNT(*) as total_products,
    SUM(stock_quantity) as total_stock,
    AVG(stock_quantity) as avg_stock,
    MIN(stock_quantity) as min_stock,
    MAX(stock_quantity) as max_stock
FROM Products;

-- Price range by category
SELECT
    c.category_name,
    COUNT(p.product_id) as products,
    MIN(p.price) as min_price,
    MAX(p.price) as max_price,
    AVG(p.price) as avg_price
FROM Categories c
LEFT JOIN Products p ON c.category_id = p.category_id
GROUP BY c.category_name
ORDER BY avg_price DESC;

-- =====================================================
-- PART 6: Export Products for Reference
-- =====================================================

-- Full product list with all details
SELECT
    p.product_id,
    p.product_name,
    p.description,
    p.price,
    c.category_name,
    p.stock_quantity,
    p.image_url,
    p.created_at,
    p.updated_at
FROM Products p
JOIN Categories c ON p.category_id = c.category_id
ORDER BY c.category_name, p.product_name;

-- =====================================================
-- END OF SCRIPT
-- =====================================================

