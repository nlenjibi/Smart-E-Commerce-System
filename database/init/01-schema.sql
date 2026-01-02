-- ===================================================
END $$;
    RAISE NOTICE 'Tables created: Users, Categories, Products, Inventory, Orders, OrderItems, Reviews';
    RAISE NOTICE 'Database schema created successfully!';
BEGIN
DO $$
-- ===================================================
-- Success message
-- ===================================================

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO CURRENT_USER;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO CURRENT_USER;
-- Grant all privileges to the application user
-- Note: The database user is created automatically by PostgreSQL container
-- ===================================================
-- Grant permissions
-- ===================================================

    EXECUTE FUNCTION update_updated_at_column();
    FOR EACH ROW
    BEFORE UPDATE ON Orders
CREATE TRIGGER update_orders_updated_at

    EXECUTE FUNCTION update_updated_at_column();
    FOR EACH ROW
    BEFORE UPDATE ON Products
CREATE TRIGGER update_products_updated_at

    EXECUTE FUNCTION update_updated_at_column();
    FOR EACH ROW
    BEFORE UPDATE ON Users
CREATE TRIGGER update_users_updated_at
-- ===================================================
-- Create triggers for updated_at
-- ===================================================

$$ LANGUAGE plpgsql;
END;
    RETURN NEW;
    NEW.updated_at = CURRENT_TIMESTAMP;
BEGIN
RETURNS TRIGGER AS $$
CREATE OR REPLACE FUNCTION update_updated_at_column()
-- ===================================================
-- Create function to update updated_at timestamp
-- ===================================================

CREATE INDEX idx_reviews_rating ON Reviews(rating);
CREATE INDEX idx_reviews_user ON Reviews(user_id);
CREATE INDEX idx_reviews_product ON Reviews(product_id);
-- Create indexes for Reviews

);
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment TEXT,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    review_id SERIAL PRIMARY KEY,
CREATE TABLE Reviews (
-- ===================================================
-- Purpose: Store product reviews
-- Table: Reviews
-- ===================================================

CREATE INDEX idx_orderitems_product ON OrderItems(product_id);
CREATE INDEX idx_orderitems_order ON OrderItems(order_id);
-- Create indexes for OrderItems

);
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE RESTRICT
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    quantity INT NOT NULL CHECK (quantity > 0),
    product_id INT NOT NULL,
    order_id INT NOT NULL,
    order_item_id SERIAL PRIMARY KEY,
CREATE TABLE OrderItems (
-- ===================================================
-- Purpose: Store individual items in an order
-- Table: OrderItems
-- ===================================================

CREATE INDEX idx_orders_date ON Orders(order_date);
CREATE INDEX idx_orders_status ON Orders(status);
CREATE INDEX idx_orders_user ON Orders(user_id);
-- Create indexes for Orders

);
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE RESTRICT
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status order_status DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount >= 0),
    user_id INT NOT NULL,
    order_id SERIAL PRIMARY KEY,
CREATE TABLE Orders (
-- ===================================================
-- Purpose: Store customer orders
-- Table: Orders
-- ===================================================

CREATE INDEX idx_inventory_reorder ON Inventory(reorder_level);
CREATE INDEX idx_inventory_quantity ON Inventory(quantity_available);
-- Create indexes for Inventory

);
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE
    reorder_level INT NOT NULL DEFAULT 10,
    quantity_available INT NOT NULL DEFAULT 0 CHECK (quantity_available >= 0),
    product_id INT NOT NULL UNIQUE,
    inventory_id SERIAL PRIMARY KEY,
CREATE TABLE Inventory (
-- ===================================================
-- Purpose: Advanced inventory tracking
-- Table: Inventory
-- ===================================================

CREATE INDEX idx_products_stock ON Products(stock_quantity);
CREATE INDEX idx_products_price ON Products(price);
CREATE INDEX idx_products_category ON Products(category_id);
CREATE INDEX idx_products_name ON Products(product_name);
-- Create indexes for Products

);
    FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE RESTRICT
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(500),
    stock_quantity INT NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    category_id INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    description TEXT,
    product_name VARCHAR(200) NOT NULL,
    product_id SERIAL PRIMARY KEY,
CREATE TABLE Products (
-- ===================================================
-- Purpose: Store product information
-- Table: Products
-- ===================================================

CREATE INDEX idx_categories_name ON Categories(category_name);
-- Create index for Categories

);
    description TEXT
    category_name VARCHAR(100) NOT NULL UNIQUE,
    category_id SERIAL PRIMARY KEY,
CREATE TABLE Categories (
-- ===================================================
-- Purpose: Product categorization
-- Table: Categories
-- ===================================================

CREATE INDEX idx_users_role ON Users(role);
CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_users_username ON Users(username);
-- Create indexes for Users

);
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role user_role DEFAULT 'CUSTOMER',
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    user_id SERIAL PRIMARY KEY,
CREATE TABLE Users (
-- ===================================================
-- Purpose: Store customer and admin information
-- Table: Users
-- ===================================================

END $$;
    WHEN duplicate_object THEN null;
EXCEPTION
    CREATE TYPE order_status AS ENUM ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED');
DO $$ BEGIN

END $$;
    WHEN duplicate_object THEN null;
EXCEPTION
    CREATE TYPE user_role AS ENUM ('CUSTOMER', 'ADMIN');
DO $$ BEGIN
-- ===================================================
-- Create ENUM types for PostgreSQL
-- ===================================================

DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Categories CASCADE;
DROP TABLE IF EXISTS Products CASCADE;
DROP TABLE IF EXISTS Inventory CASCADE;
DROP TABLE IF EXISTS Orders CASCADE;
DROP TABLE IF EXISTS OrderItems CASCADE;
DROP TABLE IF EXISTS Reviews CASCADE;
-- ===================================================
-- Drop tables if they exist (for clean setup)
-- ===================================================

SET client_encoding = 'UTF8';
-- Set client encoding

-- ===================================================
-- PostgreSQL container starts for the first time
-- This script will be automatically executed when the
-- ===================================================
-- Normalized to Third Normal Form (3NF)
-- Database Fundamentals Project
-- PostgreSQL Version
-- Smart E-Commerce System Database Schema

