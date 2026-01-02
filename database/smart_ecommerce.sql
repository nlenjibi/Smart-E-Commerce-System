-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 02, 2026 at 10:08 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smart_ecommerce`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `description`) VALUES
(1, 'Electronics', 'Electronic devices and accessories'),
(2, 'Clothing', 'Men and women apparel'),
(3, 'Books', 'Fiction, non-fiction, and educational books'),
(4, 'Home & Kitchen', 'Home appliances and kitchen essentials'),
(5, 'Sports & Outdoors', 'Sports equipment and outdoor gear'),
(6, 'Toys & Games', 'Children toys and board games'),
(7, 'Health & Beauty', 'Personal care and beauty products'),
(8, 'Automotive', 'Car accessories and parts');

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `inventory_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity_available` int(11) NOT NULL DEFAULT 0 CHECK (`quantity_available` >= 0),
  `reorder_level` int(11) NOT NULL DEFAULT 10
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `inventory`
--

INSERT INTO `inventory` (`inventory_id`, `product_id`, `quantity_available`, `reorder_level`) VALUES
(1, 1, 10, 10),
(2, 2, 75, 15),
(3, 3, 100, 20),
(4, 4, 60, 12),
(5, 5, 40, 8),
(6, 6, 120, 25),
(7, 7, 150, 30),
(8, 8, 200, 40),
(9, 9, 80, 15),
(10, 10, 90, 18),
(11, 11, 300, 50),
(12, 12, 200, 40),
(13, 13, 250, 45),
(14, 14, 180, 35),
(15, 15, 150, 30),
(16, 16, 45, 10),
(17, 17, 55, 12),
(18, 18, 35, 8),
(19, 19, 25, 6),
(20, 20, 70, 15),
(21, 21, 100, 20),
(22, 22, 30, 8),
(23, 23, 20, 5),
(24, 24, 15, 4),
(25, 25, 85, 18);

-- --------------------------------------------------------

--
-- Table structure for table `orderitems`
--

CREATE TABLE `orderitems` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL CHECK (`quantity` > 0),
  `unit_price` decimal(10,2) NOT NULL CHECK (`unit_price` >= 0),
  `subtotal` decimal(10,2) GENERATED ALWAYS AS (`quantity` * `unit_price`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderitems`
--

INSERT INTO `orderitems` (`order_item_id`, `order_id`, `product_id`, `quantity`, `unit_price`) VALUES
(1, 1, 1, 1, 899.99),
(2, 1, 3, 1, 349.99),
(3, 2, 1, 1, 899.99),
(4, 3, 6, 2, 129.99),
(5, 3, 8, 1, 29.99),
(6, 3, 21, 2, 34.99),
(7, 4, 2, 2, 799.99),
(8, 4, 5, 1, 449.99),
(9, 5, 11, 5, 14.99),
(10, 5, 13, 3, 24.99),
(11, 5, 21, 2, 34.99),
(12, 6, 4, 1, 599.99),
(13, 7, 6, 1, 129.99);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL CHECK (`total_amount` >= 0),
  `status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PENDING',
  `order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `total_amount`, `status`, `order_date`, `updated_at`) VALUES
(1, 2, 1249.98, 'DELIVERED', '2024-11-15 10:30:00', '2025-12-17 19:35:28'),
(2, 3, 899.99, 'SHIPPED', '2024-11-20 14:45:00', '2025-12-17 19:35:28'),
(3, 4, 359.97, 'PENDING', '2024-12-01 09:15:00', '2025-12-23 19:40:30'),
(4, 5, 1799.96, 'PENDING', '2024-12-10 16:20:00', '2025-12-23 19:39:41'),
(5, 6, 199.98, 'DELIVERED', '2024-11-25 11:00:00', '2025-12-17 19:35:28'),
(6, 2, 549.98, 'PENDING', '2024-12-12 13:30:00', '2025-12-23 19:39:10'),
(7, 3, 129.99, 'CANCELLED', '2024-12-15 10:00:00', '2025-12-23 19:38:50'),
(8, 1, 18.69, 'PENDING', '2025-12-24 08:34:09', '2025-12-24 08:34:09'),
(9, 7, 32.99, 'PENDING', '2025-12-24 08:41:03', '2025-12-24 08:41:03'),
(10, 1, 227.68, 'PENDING', '2025-12-24 08:52:52', '2025-12-24 08:52:52');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL CHECK (`price` >= 0),
  `category_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `stock_quantity` int(11) NOT NULL DEFAULT 0,
  `image_url` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `description`, `price`, `category_id`, `created_at`, `updated_at`, `stock_quantity`, `image_url`) VALUES
(1, 'Laptop HP ProBook', '15.6 inch, Intel Core i5, 8GB RAM, 256GB SSD', 899.99, 1, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Accessories/SmartWearables/Aplle_Nike_Watch.jpg'),
(2, 'Samsung Galaxy S23', '6.1 inch, 128GB, 5G Smartphone', 799.99, 1, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Accessories/SmartWearables/WirelessAirpods.jpg'),
(3, 'Sony WH-1000XM5', 'Wireless Noise-Cancelling Headphones', 349.99, 1, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Accessories/SmartWearables/SennheiserBlackHD350.jpg'),
(4, 'Apple iPad Air', '10.9 inch, Wi-Fi, 64GB', 599.99, 1, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Accessories/SmartWearables/FrenchConnectionFitness.jpg'),
(5, 'Dell Monitor 27 inch', '4K UHD, IPS Panel, USB-C', 449.99, 1, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Accessories/SmartWearables/FitnessNeckBand.jpg'),
(6, 'Nike Running Shoes', 'Men Air Zoom Pegasus, Size 10', 129.99, 2, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/Men/Shoes/NikeAirForce1.png'),
(7, 'Levi\'s Jeans 501', 'Men Original Fit, Blue, W32 L34', 69.99, 2, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/Men/Bottoms/Jeans.png'),
(8, 'Adidas T-Shirt', 'Women Performance Tee, Black, Medium', 29.99, 2, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Women/Tees/OrangeTee.jpg'),
(9, 'Winter Jacket', 'Waterproof Insulated Jacket, Large', 149.99, 2, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Women/jackets/DarkBlue.png'),
(10, 'Formal Shirt', 'Men Slim Fit, White, Size 16', 39.99, 2, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/Men/Tees/AwesomeSolidWhiteColor.jpg'),
(11, 'The Great Gatsby', 'F. Scott Fitzgerald - Classic Novel', 14.99, 3, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/5star.png'),
(12, 'Java Programming', 'Complete Reference, 12th Edition', 49.99, 3, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/rating5.png'),
(13, 'Atomic Habits', 'James Clear - Self Help', 24.99, 3, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/star5.png'),
(14, '1984', 'George Orwell - Dystopian Fiction', 16.99, 3, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 0, 'images/image2/rating4.png'),
(15, 'Clean Code', 'Robert C. Martin - Software Engineering', 44.99, 3, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/star4.png'),
(16, 'Instant Pot Duo', '7-in-1 Electric Pressure Cooker, 6 Quart', 89.99, 4, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/HomeNliving/Dining/8_Seater.png'),
(17, 'Coffee Maker', 'Programmable Drip Coffee Machine', 79.99, 4, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/HomeNliving/Dining/Table_2_Wooden.png'),
(18, 'Blender', 'High Speed Professional Blender', 129.99, 4, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/HomeNliving/Dining/marblewhite6seater.png'),
(19, 'Cookware Set', 'Non-stick 12-piece set', 199.99, 4, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/HomeNliving/Dining/Table_4_PeacheWhite.png'),
(20, 'Vacuum Cleaner', 'Cordless Stick Vacuum', 249.99, 4, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/HomeNliving/office_Chair.png'),
(21, 'Yoga Mat', 'Premium 6mm Thick, Non-slip', 34.99, 5, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Kids/Tees/ColorBlock.jpg'),
(22, 'Dumbbell Set', 'Adjustable 5-52.5 lbs', 299.99, 5, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/image2/Men/Shoes/YeezyAdidas.png'),
(23, 'Camping Tent', '4-Person Waterproof Tent', 159.99, 5, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/HomeNliving/BeachChair.png'),
(24, 'Bicycle', 'Mountain Bike 27.5 inch', 499.99, 5, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Kids/Converse.jpg'),
(25, 'Basketball', 'Official Size Indoor/Outdoor', 29.99, 5, '2025-12-17 19:35:28', '2025-12-24 00:24:58', 10, 'images/Kids/Tees/GirlsAbstract.jpg'),
(29, 'Air Jordan 4 Retro', 'Air Jordan 4 Retro OG Fire Red White Black DC7770-160 Men\'s Size 8-13 New', 200.00, 2, '2025-12-23 23:09:22', '2025-12-24 09:26:59', 12, 'images/HomeNliving/Sofas/BrownLeather.png'),
(30, 'Laptop HP ProBook', '15.6 inch, Intel Core i5, 8GB RAM, 256GB SSD', 899.99, 1, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 25, 'images/Accessories/SmartWearables/Aplle_Nike_Watch.jpg'),
(31, 'Samsung Galaxy S23', '6.1 inch, 128GB, 5G Smartphone', 799.99, 1, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 40, 'images/Accessories/SmartWearables/WirelessAirpods.jpg'),
(32, 'Sony WH-1000XM5', 'Wireless Noise-Cancelling Headphones', 349.99, 1, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 60, 'images/Accessories/SmartWearables/SennheiserBlackHD350.jpg'),
(33, 'Apple iPad Air', '10.9 inch, Wi-Fi, 64GB', 599.99, 1, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 30, 'images/Accessories/SmartWearables/FrenchConnectionFitness.jpg'),
(34, 'Dell Monitor 27 inch', '4K UHD, IPS Panel, USB-C', 449.99, 1, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 5, 'images/Accessories/SmartWearables/FitnessNeckBand.jpg'),
(35, 'Nike Running Shoes', 'Men Air Zoom Pegasus, Size 10', 129.99, 2, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 100, 'images/image2/Men/Shoes/NikeAirForce1.png'),
(36, 'Levi\'s Jeans 501', 'Men Original Fit, Blue, W32 L34', 69.99, 2, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 75, 'images/image2/Men/Bottoms/Jeans.png'),
(37, 'Adidas T-Shirt', 'Women Performance Tee, Black, Medium', 29.99, 2, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 120, 'images/Women/Tees/OrangeTee.jpg'),
(38, 'Winter Jacket', 'Waterproof Insulated Jacket, Large', 149.99, 2, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 8, 'images/Women/jackets/DarkBlue.png'),
(39, 'Formal Shirt', 'Men Slim Fit, White, Size 16', 39.99, 2, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 80, 'images/image2/Men/Tees/AwesomeSolidWhiteColor.jpg'),
(40, 'The Great Gatsby', 'F. Scott Fitzgerald - Classic Novel', 14.99, 3, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 200, 'images/image2/5star.png'),
(41, 'Java Programming', 'Complete Reference, 12th Edition', 49.99, 3, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 150, 'images/image2/rating5.png'),
(42, 'Atomic Habits', 'James Clear - Self Help', 24.99, 3, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 180, 'images/image2/star5.png'),
(43, '1984', 'George Orwell - Dystopian Fiction', 16.99, 3, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 220, 'images/image2/rating4.png'),
(44, 'Clean Code', 'Robert C. Martin - Software Engineering', 44.99, 3, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 140, 'images/image2/star4.png'),
(45, 'Instant Pot Duo', '7-in-1 Electric Pressure Cooker, 6 Quart', 89.99, 4, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 35, 'images/HomeNliving/Dining/8_Seater.png'),
(46, 'Coffee Maker', 'Programmable Drip Coffee Machine', 79.99, 4, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 50, 'images/HomeNliving/Dining/Table_2_Wooden.png'),
(47, 'Blender', 'High Speed Professional Blender', 129.99, 4, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 40, 'images/HomeNliving/Dining/marblewhite6seater.png'),
(48, 'Cookware Set', 'Non-stick 12-piece set', 199.99, 4, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 28, 'images/HomeNliving/Dining/Table_4_PeacheWhite.png'),
(49, 'Vacuum Cleaner', 'Cordless Stick Vacuum', 249.99, 4, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 3, 'images/HomeNliving/office_Chair.png'),
(50, 'Yoga Mat', 'Premium 6mm Thick, Non-slip', 34.99, 5, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 90, 'images/Kids/Tees/ColorBlock.jpg'),
(51, 'Dumbbell Set', 'Adjustable 5-52.5 lbs', 299.99, 5, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 20, 'images/image2/Men/Shoes/YeezyAdidas.png'),
(52, 'Camping Tent', '4-Person Waterproof Tent', 159.99, 5, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 18, 'images/HomeNliving/BeachChair.png'),
(53, 'Bicycle', 'Mountain Bike 27.5 inch', 499.99, 5, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 0, 'images/Kids/Converse.jpg'),
(54, 'Basketball', 'Official Size Indoor/Outdoor', 29.99, 5, '2025-12-23 23:32:03', '2025-12-24 00:24:58', 85, 'images/Kids/Tees/GirlsAbstract.jpg'),
(55, 'Black Shoulder Bag', 'Classic black shoulder bag', 89.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Women/BlackBag1.png'),
(56, 'Metallic Shoulder Bag', 'Stylish metallic shoulder bag', 129.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/Women/Solid_MetallicShoulderBag.png'),
(57, 'Louis Vuitton White Bag', 'Solid white designer bag', 299.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/Women/SolidWhiteLouisVuitton.png'),
(58, 'Croc Style Sling Bag', 'Trendy croc style sling bag', 79.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Women/irene_kredenets_CrocStyleSlingBag.png'),
(59, 'Army Cargo Pants', 'Comfortable army cargo pants', 69.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/Women/bottom/ArmyCargoPants.png'),
(60, 'Black Joggers', 'Athletic black joggers', 49.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 80, 'images/Women/bottom/BlackJoggers.png'),
(61, 'Blue Denim Jeans', 'Classic blue denim jeans', 89.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Women/bottom/Bluedenim.png'),
(62, 'Floral Bottoms', 'Stylish floral pattern bottoms', 59.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 55, 'images/Women/bottom/FloralBottoms.png'),
(63, 'Adidas ColorBlock Hoodie', 'Sporty colorblock hoodie', 79.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/Women/Hoodies/AdidasColorBlock.jpg'),
(64, 'Beige Hoodie', 'Comfortable beige hoodie', 65.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Women/Hoodies/Beige.png'),
(65, 'Dog Lover Hoodie', 'Cute dog lover graphic hoodie', 55.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Women/Hoodies/DogLover.png'),
(66, 'Purple Haze Hoodie', 'Solid purple haze hoodie', 69.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/Women/Hoodies/SolidPurpleHaze.png'),
(67, 'Army Green Cargo Jacket', 'Trendy army green cargo jacket', 119.99, 2, '2025-12-24 00:24:58', '2025-12-24 05:58:44', 25, 'images/Women/bottom/ArmyCargoPants.png'),
(68, 'Black Double Button Jacket', 'Elegant black double button jacket', 139.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/Women/jackets/BlackDoubleButton.png'),
(69, 'Checked Jacket', 'Classic checked pattern jacket', 99.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/Women/jackets/Checked_Jacket.png'),
(70, 'Red Leather Jacket', 'Bold red leather jacket', 179.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/Women/jackets/RedLeather.png'),
(71, 'Classic Black Converse', 'Timeless black Converse sneakers', 79.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 90, 'images/Women/Shoes/ClassicBlackConverse.jpg'),
(72, 'Floral Heels', 'Elegant floral pattern heels', 129.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/Women/Shoes/FloralHeels.png'),
(73, 'Nike ColorBlock Shoes', 'Athletic Nike colorblock design', 119.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Women/Shoes/NikeColorBlock.png'),
(74, 'Jimmy Choo Sparkly Heels', 'Luxury sparkly heels', 399.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 10, 'images/Women/Shoes/SparklyHeels_JimmyCHOOLONDON.png'),
(75, 'Broken Saints Tee', 'Graphic broken saints tee', 35.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Women/Tees/BrokenSaints.jpg'),
(76, 'Dark Grey Tee', 'Basic dark grey tee', 29.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 100, 'images/Women/Tees/DarkGrey.jpg'),
(77, 'Floral Gucci Logo Tee', 'Designer floral Gucci logo tee', 159.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/Women/Tees/FloralGucciLogo.jpg'),
(78, 'Vogue White Tee', 'Stylish vogue white tee', 45.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/Women/Tees/VogueWhiteTEE.jpg'),
(79, 'Rose Gold Watch', 'Elegant rose gold watch', 199.99, 1, '2025-12-24 00:24:58', '2025-12-24 05:58:44', 25, 'images/image2/Men/MVMTrosegolddial.jpg'),
(80, 'Rolex Watch', 'Luxury Rolex timepiece', 5999.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 5, 'images/Women/Watches/Rolex.png'),
(81, 'Wood Watch', 'Eco-friendly wooden watch', 149.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/Women/Watches/woodwatch.png'),
(82, 'Metallic Watch', 'Modern metallic watch', 179.99, 1, '2025-12-24 00:24:58', '2025-12-24 05:58:44', 20, 'images/Women/Metallic.png'),
(83, 'Grey Chinos', 'Comfortable grey chinos', 69.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 75, 'images/image2/Men/Bottoms/greyChinos.png'),
(84, 'Check Grey Chinos', 'Stylish check grey chinos', 79.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/image2/Men/Bottoms/CheckGreyChinos.png'),
(85, 'Navy Blue Formal Trousers', 'Professional navy blue trousers', 89.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/image2/Men/Bottoms/NavyBlueFormalTrousers.png'),
(86, 'Ripped Jeans Vans', 'Trendy ripped jeans', 99.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/image2/Men/Bottoms/rippedjeans_Vans.jpg'),
(87, 'Classic Black ZARA Hoodie', 'Premium ZARA black hoodie', 89.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 55, 'images/image2/Men/Hoodies/ClassicBlack_ZARA.png'),
(88, 'Creme Color Hoodie', 'Comfortable creme hoodie', 75.99, 2, '2025-12-24 00:24:58', '2025-12-24 05:58:44', 40, 'images/Kids/Hoodie/CremeBrown.jpg'),
(89, 'Light Orange OFFWHITE', 'Designer OFFWHITE orange hoodie', 199.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/image2/Men/Hoodies/LightOrange_By_OFFWHITE.png'),
(90, 'Vans Original Black', 'Classic Vans black hoodie', 69.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 65, 'images/image2/Men/Hoodies/VansOriginalBlack.png'),
(91, 'Black Leather Jacket', 'Classic black leather jacket', 249.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/image2/Men/Jackets/BlackLeatherJacket.png'),
(92, 'Grey Over Jacket', 'Casual grey over jacket', 119.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/image2/Men/Jackets/GreyOver.jpg'),
(93, 'Olive Green Jacket', 'Military-style olive green jacket', 139.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/image2/Men/Jackets/OliveGreen.png'),
(94, 'Turquoise Jacket', 'Bold turquoise jacket', 129.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/image2/Men/Jackets/Turquoise.jpg'),
(95, 'Nike Air Max Product Red', 'Limited edition Nike Air Max', 189.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/image2/Men/Shoes/NikeAirMaxProductRed.jpg'),
(96, 'Unisex Black Converse', 'Classic unisex Converse', 79.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 100, 'images/image2/Men/Shoes/UnisexBlackConverse.jpg'),
(97, 'Vans Sneakers', 'Casual Vans sneakers', 69.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 85, 'images/image2/Men/Shoes/Vans.png'),
(98, 'Yeezy Adidas', 'Designer Yeezy Adidas', 349.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/image2/Men/Shoes/YeezyAdidas.png'),
(99, 'Audere Tee', 'Graphic audere tee', 39.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/image2/Men/Tees/audere.png'),
(100, 'Black Graphic Tee', 'Cool black graphic tee', 45.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 80, 'images/image2/Men/Tees/BlackGraphicTee.png'),
(101, 'Champion Enscription Tee', 'Champion brand tee', 49.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/image2/Men/Tees/EnscriptionbyChampion.png'),
(102, 'Solid Flame Orange Tee', 'Bold flame orange tee', 42.99, 2, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 55, 'images/image2/Men/Tees/SolidFlameOrange.png'),
(103, 'Apple Watch Series 6', 'Latest Apple Watch', 399.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/image2/Men/AppleWatchseries6.jpg'),
(104, 'Brown Tissot Watch', 'Elegant brown Tissot', 299.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/image2/Men/BrownTissot.jpg'),
(105, 'Fossil Classic Watch', 'Classic Fossil timepiece', 179.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/image2/Men/FossilClassic.jpg'),
(106, 'MVMT Rose Gold Dial', 'Stylish MVMT watch', 149.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/image2/Men/MVMTrosegolddial.jpg'),
(107, 'Brown Girl Boots', 'Cute brown boots for girls', 59.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Kids/BrownGirlBoots.jpg'),
(108, 'Kids Converse Shoes', 'Classic kids Converse', 49.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 80, 'images/Kids/Converse.jpg'),
(109, 'Silver Shoes Kids', 'Shiny silver shoes', 45.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/Kids/Silvershoes.jpg'),
(110, 'Creme Brown Hoodie', 'Cozy creme brown hoodie', 39.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 55, 'images/Kids/Hoodie/CremeBrown.jpg'),
(111, 'Grey Knit Hoodie', 'Warm grey knit hoodie', 44.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Kids/Hoodie/GreyKnit.jpg'),
(112, 'Sky Blue Hoodie', 'Bright sky blue hoodie', 42.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Kids/Hoodie/SkyBlue.jpg'),
(113, 'Yellow Wollen Hoodie', 'Cozy yellow woolen hoodie', 49.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/Kids/Hoodie/YelloWollen.jpg'),
(114, 'Boys Blue Denim Jacket', 'Classic boys denim jacket', 54.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Kids/Jacket/BoyesBlueDenim.jpg'),
(115, 'Boys Black Denim Jacket', 'Stylish black denim', 59.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/Kids/Jacket/BoysBlackDenim.jpg'),
(116, 'Girls Overcoat', 'Elegant girls overcoat', 79.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/Kids/Jacket/GirlsOvercoat.jpg'),
(117, 'Girls Denim Jacket', 'Trendy girls denim', 54.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Kids/Jacket/Girls_Denim.jpg'),
(118, 'Cargo Denims', 'Comfortable cargo denims', 44.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/Kids/Jeans/CargoDenims.jpg'),
(119, 'Dark Blue Denims', 'Classic dark blue jeans', 39.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Kids/Jeans/DarkBlueDenims.jpg'),
(120, 'Girls Basketball Jeans', 'Athletic girls jeans', 49.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Kids/Jeans/GirlsBascketballJeans.jpg'),
(121, 'Girls Ripped Jeans', 'Trendy ripped jeans', 54.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Kids/Jeans/GirlsRippedJeans.jpg'),
(122, 'Blue Design Watch', 'Colorful blue design watch', 29.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Kids/Watch/Bluedesign.jpg'),
(123, 'Blue Kids Watch', 'Simple blue kids watch', 24.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 80, 'images/Kids/Watch/Bluewatch.jpg'),
(124, 'Girls Pink Watch', 'Cute pink watch for girls', 27.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 75, 'images/Kids/Watch/Girlswatchpink.jpg'),
(125, 'Orange Kids Watch', 'Bright orange watch', 26.99, 6, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 65, 'images/Kids/Watch/Orange.jpg'),
(126, 'Beach Chair', 'Comfortable beach chair', 89.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/HomeNliving/BeachChair.png'),
(127, 'Funky ColorBlock Chair', 'Modern colorblock chair', 159.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/HomeNliving/Funky_ColorBlock.png'),
(128, 'Grey Suede Chair', 'Elegant grey suede chair', 249.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/HomeNliving/GreaySuede.png'),
(129, 'Kitchen Bar Chair', 'Stylish bar chair', 119.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/HomeNliving/KitchenBar_Chair.png'),
(130, 'Leather Lounge Chair', 'Luxury leather lounge', 399.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/HomeNliving/Leather-LoungeChair.png'),
(131, 'Office Chair', 'Ergonomic office chair', 279.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/HomeNliving/office_Chair.png'),
(132, 'Wooden Chairs', 'Classic wooden chairs set', 349.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/HomeNliving/WoodenChairs.png'),
(133, 'Abstract Carpet', 'Modern abstract pattern carpet', 189.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/HomeNliving/Carpet/Abstract.png'),
(134, 'Black Gold Stripes Carpet', 'Elegant striped carpet', 229.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/HomeNliving/Carpet/BlackGoldStripes.png'),
(135, 'Navy Blue Design Carpet', 'Classic navy design', 199.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/HomeNliving/Carpet/NavyBlueDesign.png'),
(136, 'Off White Fur Carpet', 'Soft fur carpet', 299.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/HomeNliving/Carpet/OffWhiteFur.png'),
(137, 'Custom Neon Lights', 'Customizable neon lights', 149.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/HomeNliving/Lamp/CustomNeonLights.png'),
(138, 'Kitchen Lights', 'Modern kitchen lighting', 129.99, 4, '2025-12-24 00:24:58', '2025-12-24 05:59:04', 40, 'images/HomeNliving/Lamp/KitchenLights.png'),
(139, 'Lounge Lights', 'Ambient lounge lighting', 179.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/HomeNliving/Lamp/LoungeLights.png'),
(140, 'Modern Bedroom Lights', 'Contemporary bedroom lights', 159.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/HomeNliving/Lamp/ModernBedroomLights.png'),
(141, 'Circle Table', 'Modern circle side table', 119.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/HomeNliving/SideTable/CircleTable.png'),
(142, 'Multi Drawer Table', 'Functional drawer table', 189.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 20, 'images/HomeNliving/SideTable/LightBrown_MultiDrawertable.png'),
(143, 'Oak Wooden Tables', 'Classic oak tables', 249.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/HomeNliving/SideTable/OakWoodenTables.png'),
(144, 'Modern Side Table', 'Off-white modern table', 139.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/HomeNliving/SideTable/OffwhiteModernSideTable.png'),
(145, 'Black Leather L-shaped', 'Luxury L-shaped sofa', 1299.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 10, 'images/HomeNliving/Sofas/BlackLeatherLshaped.png'),
(146, 'Brown Leather Sofa', 'Classic brown leather', 999.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 15, 'images/HomeNliving/Sofas/BrownLeather.png'),
(147, 'Classic White 6 Seater', 'Elegant white sofa', 1499.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 8, 'images/HomeNliving/Sofas/ClassicWhite6Seater.png'),
(148, 'Grey Blue Sofa', 'Modern grey blue sofa', 899.99, 4, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 12, 'images/HomeNliving/Sofas/GreyBlue.png'),
(149, 'Blush Brush', 'Professional blush brush', 24.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 80, 'images/Accessories/BlushBrush2.jpg'),
(150, 'Concealer', 'Full coverage concealer', 19.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 100, 'images/Accessories/Concealer.jpg'),
(151, 'Lip Gloss', 'Shiny lip gloss', 14.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 120, 'images/Accessories/Lipgloss.jpg'),
(152, 'Lipstick', 'Long-lasting lipstick', 16.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 110, 'images/Accessories/lipstick.jpg'),
(153, 'Nail Polish Pack of 3', 'Nail polish set', 29.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Accessories/NailPolishPackof3.png'),
(154, 'Electric Hair Brush', 'Heated hair brush', 49.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Accessories/Appliances/ElectricHairBrush.jpg'),
(155, 'Epilator', 'Professional epilator', 79.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/Accessories/Appliances/epilator.jpg'),
(156, 'Hair Dryer', 'Professional hair dryer', 89.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 40, 'images/Accessories/Appliances/HairDryer.png'),
(157, 'Hair Trimmer', 'Cordless hair trimmer', 59.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Accessories/Appliances/HairTimmer.png'),
(158, 'Hair Straightener', 'Ceramic hair straightener', 69.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Accessories/Appliances/Hair_Straigthner.png'),
(159, 'Aqua Algoria Goddess', 'Fresh floral fragrance', 89.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/Accessories/Fragrance/AQUA_ALGORIA_GODESS.png'),
(160, 'Chanel Ether', 'Luxury Chanel perfume', 149.99, 7, '2025-12-24 00:24:58', '2025-12-24 05:58:44', 20, 'images/Women/Shoes/Peace_BlackChanel.jpg'),
(161, 'Daisy Marc Jacobs', 'Sweet daisy fragrance', 119.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/Accessories/Fragrance/DAISY_MARKJACOBS.png'),
(162, 'Dolce & Gabbana The One', 'Elegant D&G perfume', 139.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 22, 'images/Accessories/Fragrance/DOLCE_GABANA_THEONE.png'),
(163, 'Jeanne Lanvin The Sin', 'Sophisticated Lanvin scent', 129.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 18, 'images/Accessories/Fragrance/JEANE_LANVIN_THESIN.png'),
(164, 'Bath & Body Works', 'Hair care set', 34.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 60, 'images/Accessories/HairCare/Bath&BodyWORKS.jpg'),
(165, 'Hair Conditioner', 'Moisturizing conditioner', 24.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 80, 'images/Accessories/HairCare/HairConditioner.jpg'),
(166, 'Hair Cream', 'Styling hair cream', 19.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 90, 'images/Accessories/HairCare/HairCream.png'),
(167, 'Hair Serum', 'Shine boosting serum', 29.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Accessories/HairCare/HairSerum.jpg'),
(168, 'Hair Spray', 'Strong hold spray', 22.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 75, 'images/Accessories/HairCare/HairSpray.jpg'),
(169, 'Body Lotion', 'Moisturizing body lotion', 27.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 85, 'images/Accessories/Skincare/BodyLotion.jpg'),
(170, 'Brownie Lip Balm', 'Nourishing lip balm', 9.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 150, 'images/Accessories/Skincare/BrownieLipBalm.jpg'),
(171, 'Face Mask', 'Hydrating face mask', 34.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 65, 'images/Accessories/Skincare/FaceMask.jpg'),
(172, 'Face Moisturiser', 'Daily face moisturizer', 39.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 70, 'images/Accessories/Skincare/FaceMoisturisers.jpg'),
(173, 'Sunscreen', 'SPF 50 sunscreen', 29.99, 7, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 100, 'images/Accessories/Skincare/SunScreen.jpg'),
(174, 'Apple Nike Watch', 'Apple Watch Nike edition', 449.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 25, 'images/Accessories/SmartWearables/Aplle_Nike_Watch.jpg'),
(175, 'Fitness Neck Band', 'Wireless fitness band', 79.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 45, 'images/Accessories/SmartWearables/FitnessNeckBand.jpg'),
(176, 'French Connection Fitness', 'Premium fitness tracker', 129.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 35, 'images/Accessories/SmartWearables/FrenchConnectionFitness.jpg'),
(177, 'Sennheiser Black HD350', 'Professional headphones', 199.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 30, 'images/Accessories/SmartWearables/SennheiserBlackHD350.jpg'),
(178, 'Wireless Airpods', 'Premium wireless earbuds', 179.99, 1, '2025-12-24 00:24:58', '2025-12-24 00:24:58', 50, 'images/Accessories/SmartWearables/WirelessAirpods.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `review_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` between 1 and 5),
  `comment` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`review_id`, `product_id`, `user_id`, `rating`, `comment`, `created_at`) VALUES
(1, 1, 2, 5, 'Excellent laptop! Fast and reliable.', '2024-11-20 14:00:00'),
(2, 1, 3, 4, 'Good value for money, battery could be better.', '2024-11-22 10:30:00'),
(3, 3, 2, 5, 'Best noise-cancelling headphones I\'ve owned!', '2024-11-21 16:45:00'),
(4, 6, 4, 5, 'Very comfortable running shoes.', '2024-12-05 09:20:00'),
(5, 6, 5, 4, 'Great shoes but run a bit small.', '2024-12-08 11:15:00'),
(6, 11, 6, 5, 'Classic book, must read!', '2024-11-28 13:40:00'),
(7, 13, 2, 5, 'Life-changing book about habits.', '2024-12-14 15:25:00'),
(8, 21, 4, 4, 'Good quality yoga mat.', '2024-12-06 08:50:00'),
(9, 2, 5, 5, 'Amazing phone with great camera!', '2024-12-12 17:30:00'),
(10, 16, 6, 4, 'Pressure cooker works well, a bit noisy.', '2024-11-30 12:10:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('CUSTOMER','ADMIN') DEFAULT 'CUSTOMER',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password_hash`, `role`, `created_at`, `updated_at`) VALUES
(1, 'admin', 'admin@smartcommerce.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN', '2025-12-17 19:35:28', '2025-12-17 19:35:28'),
(2, 'john_doe', 'john@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER', '2025-12-17 19:35:28', '2025-12-17 19:35:28'),
(3, 'jane_smith', 'jane@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER', '2025-12-17 19:35:28', '2025-12-17 19:35:28'),
(4, 'bob_wilson', 'bob@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER', '2025-12-17 19:35:28', '2025-12-17 19:35:28'),
(5, 'alice_jones', 'alice@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER', '2025-12-17 19:35:28', '2025-12-17 19:35:28'),
(6, 'charlie_brown', 'charlie@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'CUSTOMER', '2025-12-17 19:35:28', '2025-12-17 19:35:28'),
(7, 'timo', 'timo@gmail.com', 'da9bc7989ed7320b46f9d8e84c8397eea883b9133423eac3e8ef61abdd6528d0', 'CUSTOMER', '2025-12-24 07:29:46', '2025-12-24 07:29:46');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`),
  ADD KEY `idx_category_name` (`category_name`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`inventory_id`),
  ADD UNIQUE KEY `product_id` (`product_id`),
  ADD KEY `idx_quantity` (`quantity_available`),
  ADD KEY `idx_reorder` (`reorder_level`),
  ADD KEY `idx_low_stock` (`quantity_available`,`reorder_level`);

--
-- Indexes for table `orderitems`
--
ALTER TABLE `orderitems`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `idx_order_id` (`order_id`),
  ADD KEY `idx_product_id` (`product_id`),
  ADD KEY `idx_product_order` (`product_id`,`order_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_status` (`status`),
  ADD KEY `idx_order_date` (`order_date`),
  ADD KEY `idx_user_date` (`user_id`,`order_date`),
  ADD KEY `idx_status_date` (`status`,`order_date`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `idx_product_name` (`product_name`),
  ADD KEY `idx_category_id` (`category_id`),
  ADD KEY `idx_price` (`price`),
  ADD KEY `idx_category_price` (`category_id`,`price`);
ALTER TABLE `products` ADD FULLTEXT KEY `ft_product_search` (`product_name`,`description`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `idx_product_id` (`product_id`),
  ADD KEY `idx_user_id` (`user_id`),
  ADD KEY `idx_rating` (`rating`),
  ADD KEY `idx_created_at` (`created_at`),
  ADD KEY `idx_product_rating` (`product_id`,`rating`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_username` (`username`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_role` (`role`),
  ADD KEY `idx_user_login` (`username`,`password_hash`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `inventory_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `orderitems`
--
ALTER TABLE `orderitems`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=179;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `inventory`
--
ALTER TABLE `inventory`
  ADD CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;

--
-- Constraints for table `orderitems`
--
ALTER TABLE `orderitems`
  ADD CONSTRAINT `orderitems_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `orderitems_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
