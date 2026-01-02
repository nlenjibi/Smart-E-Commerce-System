# Database Design Documentation

## Table of Contents
1. [Overview](#overview)
2. [Database Schema](#database-schema)
3. [Entity Relationship Diagram](#entity-relationship-diagram)
4. [Table Descriptions](#table-descriptions)
5. [Normalization](#normalization)
6. [Indexing Strategy](#indexing-strategy)
7. [Sample Queries](#sample-queries)

## Overview

The Smart E-Commerce System uses a relational database design normalized to Third Normal Form (3NF). The schema supports:

- User management (customers and admins)
- Product catalog with categories
- Shopping cart functionality
- Order processing and tracking
- Product reviews and ratings
- Inventory management

### Supported Databases
- **PostgreSQL** (Recommended) - Version 12+
- **MySQL** - Version 8.0+

## Database Schema

### Complete Schema Diagram

```
┌──────────────────────┐
│       users          │
├──────────────────────┤
│ user_id (PK)         │
│ username (UNIQUE)    │
│ email (UNIQUE)       │
│ password             │
│ role                 │
│ phone                │
│ address              │
│ created_at           │
└──────┬───────────────┘
       │
       │ 1:N
       │
┌──────▼───────────────┐      ┌─────────────────────┐
│      orders          │      │    categories       │
├──────────────────────┤      ├─────────────────────┤
│ order_id (PK)        │      │ category_id (PK)    │
│ user_id (FK)         │      │ name                │
│ total_amount         │      │ description         │
│ status               │      │ parent_category_id  │
│ shipping_address     │      │ created_at          │
│ payment_method       │      └──────┬──────────────┘
│ order_date           │             │
└──────┬───────────────┘             │ 1:N
       │                             │
       │ 1:N                   ┌─────▼──────────────┐
       │                       │     products       │
┌──────▼───────────────┐      ├────────────────────┤
│   order_items        │      │ product_id (PK)    │
├──────────────────────┤      │ name               │
│ order_item_id (PK)   │      │ description        │
│ order_id (FK)        │◄─────┤ price              │
│ product_id (FK)      │  N:1 │ sku (UNIQUE)       │
│ quantity             │      │ category_id (FK)   │
│ price_at_purchase    │      │ image_url          │
│ subtotal             │      │ stock              │
└──────────────────────┘      │ brand              │
                              │ weight             │
                              │ dimensions         │
       ┌──────────────────────┤ created_at         │
       │                      │ updated_at         │
       │ N:1                  └──────┬─────────────┘
       │                             │
┌──────▼───────────────┐             │ 1:1
│   cart_items         │             │
├──────────────────────┤      ┌──────▼─────────────┐
│ cart_item_id (PK)    │      │    inventory       │
│ user_id (FK)         │      ├────────────────────┤
│ product_id (FK)      │      │ inventory_id (PK)  │
│ quantity             │      │ product_id (FK)    │
│ added_at             │      │ quantity           │
└──────────────────────┘      │ last_updated       │
                              └────────────────────┘
       ┌──────────────────────┐
       │                      │
       │ N:1                  │
       │                      │
┌──────▼───────────────┐      │
│      reviews         │      │
├──────────────────────┤      │
│ review_id (PK)       │      │
│ product_id (FK)      │──────┘
│ user_id (FK)         │
│ rating               │
│ comment              │
│ created_at           │
└──────────────────────┘
```

## Entity Relationship Diagram

### Relationships

| Parent Entity | Relationship | Child Entity | Type | Description |
|---------------|--------------|--------------|------|-------------|
| users | one-to-many | orders | 1:N | A user can have multiple orders |
| users | one-to-many | cart_items | 1:N | A user can have multiple cart items |
| users | one-to-many | reviews | 1:N | A user can write multiple reviews |
| categories | one-to-many | products | 1:N | A category contains multiple products |
| categories | self-reference | categories | 1:N | Categories can have subcategories |
| products | one-to-many | order_items | 1:N | A product can be in multiple orders |
| products | one-to-many | cart_items | 1:N | A product can be in multiple carts |
| products | one-to-many | reviews | 1:N | A product can have multiple reviews |
| products | one-to-one | inventory | 1:1 | Each product has inventory tracking |
| orders | one-to-many | order_items | 1:N | An order contains multiple items |

## Table Descriptions

### users

Stores customer and administrator accounts.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| username | VARCHAR(50) | UNIQUE, NOT NULL | Login username |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Email address |
| password | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| role | VARCHAR(20) | NOT NULL, DEFAULT 'customer' | User role (admin/customer) |
| phone | VARCHAR(20) | NULL | Phone number |
| address | TEXT | NULL | Mailing address |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Registration date |

**Indexes:**
- PRIMARY KEY on `user_id`
- UNIQUE INDEX on `username`
- UNIQUE INDEX on `email`
- INDEX on `role` for filtering users

**Sample Data:**
```sql
INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@smartcommerce.com', '$2a$10$...', 'admin'),
('john_doe', 'john@example.com', '$2a$10$...', 'customer');
```

---

### categories

Product categories with hierarchical support.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| category_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique category ID |
| name | VARCHAR(100) | NOT NULL | Category name |
| description | TEXT | NULL | Category description |
| parent_category_id | INT | FOREIGN KEY, NULL | Parent category (NULL for root) |
| image_url | VARCHAR(255) | NULL | Category image |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Creation date |

**Indexes:**
- PRIMARY KEY on `category_id`
- INDEX on `parent_category_id` for hierarchy queries
- INDEX on `name` for searching

**Foreign Keys:**
- `parent_category_id` REFERENCES `categories(category_id)` ON DELETE SET NULL

**Sample Data:**
```sql
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Clothing', 'Apparel and fashion');

INSERT INTO categories (name, parent_category_id) VALUES
('Laptops', 1),  -- Subcategory of Electronics
('Phones', 1);   -- Subcategory of Electronics
```

---

### products

Product catalog information.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| product_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique product ID |
| name | VARCHAR(255) | NOT NULL | Product name |
| description | TEXT | NULL | Product description |
| price | DECIMAL(10,2) | NOT NULL | Price in USD |
| sku | VARCHAR(50) | UNIQUE, NOT NULL | Stock Keeping Unit |
| category_id | INT | FOREIGN KEY, NOT NULL | Category ID |
| image_url | VARCHAR(500) | NULL | Product image URL |
| stock | INT | DEFAULT 0 | Available quantity |
| brand | VARCHAR(100) | NULL | Brand name |
| weight | DECIMAL(8,2) | NULL | Weight in kg |
| dimensions | VARCHAR(50) | NULL | L x W x H in cm |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Creation date |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update |

**Indexes:**
- PRIMARY KEY on `product_id`
- UNIQUE INDEX on `sku`
- INDEX on `category_id` for category filtering
- INDEX on `name` for searching
- INDEX on `price` for price range queries
- INDEX on `stock` for inventory management

**Foreign Keys:**
- `category_id` REFERENCES `categories(category_id)` ON DELETE RESTRICT

**Sample Data:**
```sql
INSERT INTO products (name, description, price, sku, category_id, stock) VALUES
('MacBook Pro 14"', 'Apple M2 Pro chip', 1999.99, 'MBP-14-M2', 1, 15),
('iPhone 15 Pro', 'A17 Pro chip', 999.99, 'IPH-15-PRO', 2, 50);
```

---

### orders

Customer order headers.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique order ID |
| user_id | INT | FOREIGN KEY, NOT NULL | Customer ID |
| total_amount | DECIMAL(10,2) | NOT NULL | Order total |
| status | VARCHAR(20) | DEFAULT 'pending' | Order status |
| shipping_address | TEXT | NOT NULL | Delivery address |
| payment_method | VARCHAR(50) | NULL | Payment method |
| order_date | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Order date |

**Valid Status Values:**
- `pending` - Order placed, awaiting processing
- `processing` - Order being prepared
- `shipped` - Order dispatched
- `delivered` - Order received by customer
- `cancelled` - Order cancelled

**Indexes:**
- PRIMARY KEY on `order_id`
- INDEX on `user_id` for user order history
- INDEX on `status` for order filtering
- INDEX on `order_date` for date range queries

**Foreign Keys:**
- `user_id` REFERENCES `users(user_id)` ON DELETE RESTRICT

**Sample Data:**
```sql
INSERT INTO orders (user_id, total_amount, status, shipping_address) VALUES
(2, 299.99, 'delivered', '123 Main St, New York, NY 10001');
```

---

### order_items

Line items for orders.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_item_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique item ID |
| order_id | INT | FOREIGN KEY, NOT NULL | Order ID |
| product_id | INT | FOREIGN KEY, NOT NULL | Product ID |
| quantity | INT | NOT NULL | Quantity ordered |
| price_at_purchase | DECIMAL(10,2) | NOT NULL | Price when ordered |
| subtotal | DECIMAL(10,2) | NOT NULL | quantity * price_at_purchase |

**Indexes:**
- PRIMARY KEY on `order_item_id`
- INDEX on `order_id` for order details
- INDEX on `product_id` for product sales analysis

**Foreign Keys:**
- `order_id` REFERENCES `orders(order_id)` ON DELETE CASCADE
- `product_id` REFERENCES `products(product_id)` ON DELETE RESTRICT

**Sample Data:**
```sql
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase, subtotal)
VALUES (1, 1, 1, 1999.99, 1999.99);
```

---

### cart_items

Shopping cart items (session-based).

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| cart_item_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique cart item ID |
| user_id | INT | FOREIGN KEY, NOT NULL | User ID |
| product_id | INT | FOREIGN KEY, NOT NULL | Product ID |
| quantity | INT | NOT NULL | Quantity in cart |
| added_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Date added |

**Indexes:**
- PRIMARY KEY on `cart_item_id`
- INDEX on `user_id` for user cart retrieval
- UNIQUE INDEX on (`user_id`, `product_id`) to prevent duplicates

**Foreign Keys:**
- `user_id` REFERENCES `users(user_id)` ON DELETE CASCADE
- `product_id` REFERENCES `products(product_id)` ON DELETE CASCADE

---

### inventory

Real-time inventory tracking.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| inventory_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique inventory ID |
| product_id | INT | UNIQUE, FOREIGN KEY, NOT NULL | Product ID |
| quantity | INT | DEFAULT 0 | Available quantity |
| last_updated | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last stock update |

**Indexes:**
- PRIMARY KEY on `inventory_id`
- UNIQUE INDEX on `product_id`
- INDEX on `quantity` for low stock alerts

**Foreign Keys:**
- `product_id` REFERENCES `products(product_id)` ON DELETE CASCADE

---

### reviews

Product reviews and ratings.

**Columns:**

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| review_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique review ID |
| product_id | INT | FOREIGN KEY, NOT NULL | Product ID |
| user_id | INT | FOREIGN KEY, NOT NULL | Reviewer ID |
| rating | INT | CHECK (1-5), NOT NULL | Star rating (1-5) |
| comment | TEXT | NULL | Review text |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Review date |

**Indexes:**
- PRIMARY KEY on `review_id`
- INDEX on `product_id` for product reviews
- INDEX on `user_id` for user reviews
- INDEX on `rating` for filtering

**Foreign Keys:**
- `product_id` REFERENCES `products(product_id)` ON DELETE CASCADE
- `user_id` REFERENCES `users(user_id)` ON DELETE CASCADE

---

## Normalization

### First Normal Form (1NF)
✅ All tables have:
- Atomic values (no repeating groups)
- Unique row identifiers (primary keys)
- No duplicate rows

### Second Normal Form (2NF)
✅ All tables are in 1NF and:
- All non-key attributes depend on the entire primary key
- No partial dependencies

**Example**: In `order_items`, both `order_id` and `product_id` were originally needed, but we use a surrogate key `order_item_id` to avoid issues.

### Third Normal Form (3NF)
✅ All tables are in 2NF and:
- No transitive dependencies
- Non-key attributes depend only on the primary key

**Example**: `categories` table stores hierarchy instead of repeating category information in products.

## Indexing Strategy

### Primary Keys
All tables have auto-incrementing primary keys for fast lookups.

### Unique Indexes
- `users.username` - Ensure unique usernames
- `users.email` - Ensure unique emails
- `products.sku` - Ensure unique product codes
- `inventory.product_id` - One-to-one relationship

### Foreign Key Indexes
Automatically created on all foreign key columns for:
- Join performance
- Referential integrity checks

### Search Indexes
- `products.name` - Product search
- `categories.name` - Category search
- `users.role` - User filtering

### Composite Indexes
- `(cart_items.user_id, cart_items.product_id)` - Cart operations
- `(orders.user_id, orders.status)` - Order filtering
- `(order_items.order_id, order_items.product_id)` - Order details

### Performance Indexes
- `products.price` - Price range queries
- `products.stock` - Inventory management
- `orders.order_date` - Date range reports
- `reviews.rating` - Rating filters

## Sample Queries

### Complex Queries

#### 1. Get Products with Category Name
```sql
SELECT p.*, c.name as category_name
FROM products p
JOIN categories c ON p.category_id = c.category_id
WHERE p.stock > 0
ORDER BY p.created_at DESC;
```

#### 2. Get User Orders with Items
```sql
SELECT 
    o.order_id,
    o.order_date,
    o.total_amount,
    o.status,
    oi.product_id,
    p.name as product_name,
    oi.quantity,
    oi.price_at_purchase
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE o.user_id = ?
ORDER BY o.order_date DESC;
```

#### 3. Top Selling Products
```sql
SELECT 
    p.product_id,
    p.name,
    SUM(oi.quantity) as total_sold,
    SUM(oi.subtotal) as total_revenue
FROM products p
JOIN order_items oi ON p.product_id = oi.product_id
JOIN orders o ON oi.order_id = o.order_id
WHERE o.status = 'delivered'
GROUP BY p.product_id, p.name
ORDER BY total_sold DESC
LIMIT 10;
```

#### 4. Category Sales Report
```sql
SELECT 
    c.name as category,
    COUNT(DISTINCT p.product_id) as product_count,
    SUM(oi.quantity) as items_sold,
    SUM(oi.subtotal) as revenue
FROM categories c
LEFT JOIN products p ON c.category_id = p.category_id
LEFT JOIN order_items oi ON p.product_id = oi.product_id
LEFT JOIN orders o ON oi.order_id = o.order_id
WHERE o.status = 'delivered' OR o.status IS NULL
GROUP BY c.category_id, c.name
ORDER BY revenue DESC;
```

#### 5. Low Stock Alert
```sql
SELECT 
    p.product_id,
    p.name,
    p.sku,
    p.stock,
    c.name as category
FROM products p
JOIN categories c ON p.category_id = c.category_id
WHERE p.stock < 10
ORDER BY p.stock ASC;
```

#### 6. Customer Lifetime Value
```sql
SELECT 
    u.user_id,
    u.username,
    u.email,
    COUNT(o.order_id) as total_orders,
    SUM(o.total_amount) as lifetime_value,
    AVG(o.total_amount) as avg_order_value,
    MAX(o.order_date) as last_order_date
FROM users u
LEFT JOIN orders o ON u.user_id = o.user_id
WHERE u.role = 'customer'
GROUP BY u.user_id, u.username, u.email
ORDER BY lifetime_value DESC;
```

#### 7. Product Reviews Summary
```sql
SELECT 
    p.product_id,
    p.name,
    COUNT(r.review_id) as review_count,
    AVG(r.rating) as avg_rating,
    SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END) as five_star,
    SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END) as four_star,
    SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END) as three_star,
    SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END) as two_star,
    SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END) as one_star
FROM products p
LEFT JOIN reviews r ON p.product_id = r.product_id
GROUP BY p.product_id, p.name
HAVING COUNT(r.review_id) > 0
ORDER BY avg_rating DESC, review_count DESC;
```

## Database Maintenance

### Backup Strategy
```bash
# PostgreSQL backup
pg_dump smart_ecommerce > backup_$(date +%Y%m%d).sql

# MySQL backup
mysqldump -u root -p smart_ecommerce > backup_$(date +%Y%m%d).sql
```

### Restore Database
```bash
# PostgreSQL restore
psql -U postgres smart_ecommerce < backup_20241224.sql

# MySQL restore
mysql -u root -p smart_ecommerce < backup_20241224.sql
```

### Index Maintenance
```sql
-- PostgreSQL
REINDEX DATABASE smart_ecommerce;

-- MySQL
OPTIMIZE TABLE products, orders, users;
```

### Analyze Tables (Update Statistics)
```sql
-- PostgreSQL
ANALYZE products;
ANALYZE orders;

-- MySQL
ANALYZE TABLE products, orders, users;
```

---

## Migration Scripts

Migration scripts are located in the `/database` directory:
- `schema.sql` - Complete schema creation
- `indexes.sql` - Index definitions
- `SampleDataWithStockAndImages.sql` - Sample data
- `migrate_*.sql` - Schema updates

---

For more information, see:
- [Installation Guide](INSTALLATION.md)
- [Configuration](CONFIGURATION.md)
- [API Documentation](API.md)

**Database Version: 1.0**
**Last Updated: December 2024**

