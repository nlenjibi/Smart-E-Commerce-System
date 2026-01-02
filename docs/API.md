# API Documentation

## Table of Contents
1. [Overview](#overview)
2. [Data Access Layer (DAO)](#data-access-layer-dao)
3. [Service Layer](#service-layer)
4. [Model Classes](#model-classes)
5. [Utility Classes](#utility-classes)
6. [Configuration](#configuration)

## Overview

This document provides detailed API documentation for the Smart E-Commerce System backend components.

## Data Access Layer (DAO)

### ProductDAO

Manages database operations for products.

#### Methods

##### `Product findById(int id)`
Retrieves a product by its ID.

**Parameters:**
- `id` - Product ID

**Returns:** Product object or null if not found

**Throws:** `DataAccessException` if database error occurs

**Example:**
```java
ProductDAO dao = new ProductDAO();
Product product = dao.findById(1);
```

##### `List<Product> findAll()`
Retrieves all products.

**Returns:** List of all products

**Example:**
```java
List<Product> products = dao.findAll();
```

##### `List<Product> findByCategory(int categoryId)`
Retrieves products in a specific category.

**Parameters:**
- `categoryId` - Category ID

**Returns:** List of products in the category

**Example:**
```java
List<Product> laptops = dao.findByCategory(1);
```

##### `List<Product> searchByName(String keyword)`
Searches products by name.

**Parameters:**
- `keyword` - Search keyword

**Returns:** List of matching products

**Example:**
```java
List<Product> results = dao.searchByName("laptop");
```

##### `void save(Product product)`
Saves a new product.

**Parameters:**
- `product` - Product to save

**Throws:** `DataAccessException` if save fails

**Example:**
```java
Product product = new Product("Laptop", 999.99);
dao.save(product);
// product.getProductId() now contains generated ID
```

##### `void update(Product product)`
Updates an existing product.

**Parameters:**
- `product` - Product to update (must have valid ID)

**Throws:** `DataAccessException` if update fails

**Example:**
```java
product.setPrice(899.99);
dao.update(product);
```

##### `void delete(int id)`
Deletes a product by ID.

**Parameters:**
- `id` - Product ID to delete

**Throws:** `DataAccessException` if delete fails

**Example:**
```java
dao.delete(1);
```

##### `int getTotalCount()`
Gets total number of products.

**Returns:** Total product count

**Example:**
```java
int total = dao.getTotalCount();
```

##### `List<Product> findLowStock(int threshold)`
Finds products with stock below threshold.

**Parameters:**
- `threshold` - Stock threshold

**Returns:** List of low stock products

**Example:**
```java
List<Product> lowStock = dao.findLowStock(10);
```

---

### UserDAO

Manages database operations for users.

#### Methods

##### `User findById(int id)`
Retrieves a user by ID.

**Parameters:**
- `id` - User ID

**Returns:** User object or null

##### `User findByUsername(String username)`
Retrieves a user by username.

**Parameters:**
- `username` - Username to search

**Returns:** User object or null

**Example:**
```java
User user = dao.findByUsername("john_doe");
```

##### `User findByEmail(String email)`
Retrieves a user by email.

**Parameters:**
- `email` - Email to search

**Returns:** User object or null

##### `List<User> findAll()`
Retrieves all users.

**Returns:** List of all users

##### `List<User> findByRole(String role)`
Retrieves users by role.

**Parameters:**
- `role` - User role ("admin" or "customer")

**Returns:** List of users with specified role

##### `void save(User user)`
Saves a new user.

**Parameters:**
- `user` - User to save

**Note:** Password should be hashed before saving

**Example:**
```java
User user = new User();
user.setUsername("newuser");
user.setPassword(SecurityUtils.hashPassword("password"));
user.setEmail("user@example.com");
user.setRole("customer");
dao.save(user);
```

##### `void update(User user)`
Updates an existing user.

**Parameters:**
- `user` - User to update

##### `void delete(int id)`
Deletes a user by ID.

**Parameters:**
- `id` - User ID to delete

##### `boolean existsByUsername(String username)`
Checks if username exists.

**Parameters:**
- `username` - Username to check

**Returns:** true if exists, false otherwise

##### `boolean existsByEmail(String email)`
Checks if email exists.

**Parameters:**
- `email` - Email to check

**Returns:** true if exists, false otherwise

---

### OrderDAO

Manages database operations for orders.

#### Methods

##### `Order findById(int id)`
Retrieves an order by ID.

**Parameters:**
- `id` - Order ID

**Returns:** Order object with order items

##### `List<Order> findAll()`
Retrieves all orders.

**Returns:** List of all orders

##### `List<Order> findByUserId(int userId)`
Retrieves orders for a specific user.

**Parameters:**
- `userId` - User ID

**Returns:** List of user's orders

**Example:**
```java
List<Order> userOrders = dao.findByUserId(5);
```

##### `List<Order> findByStatus(String status)`
Retrieves orders by status.

**Parameters:**
- `status` - Order status ("pending", "processing", "shipped", "delivered", "cancelled")

**Returns:** List of orders with specified status

##### `void save(Order order)`
Saves a new order with items.

**Parameters:**
- `order` - Order to save (including order items)

**Example:**
```java
Order order = new Order();
order.setUserId(1);
order.setTotalAmount(299.99);
order.addItem(new OrderItem(productId, quantity, price));
dao.save(order);
```

##### `void update(Order order)`
Updates an existing order.

**Parameters:**
- `order` - Order to update

##### `void updateStatus(int orderId, String status)`
Updates order status.

**Parameters:**
- `orderId` - Order ID
- `status` - New status

**Example:**
```java
dao.updateStatus(1, "shipped");
```

##### `void delete(int id)`
Deletes an order by ID.

**Parameters:**
- `id` - Order ID to delete

##### `double getTotalRevenue()`
Gets total revenue from all orders.

**Returns:** Total revenue

##### `int getTotalOrders()`
Gets total number of orders.

**Returns:** Total order count

---

### CategoryDAO

Manages database operations for categories.

#### Methods

##### `Category findById(int id)`
Retrieves a category by ID.

**Parameters:**
- `id` - Category ID

**Returns:** Category object or null

##### `List<Category> findAll()`
Retrieves all categories.

**Returns:** List of all categories

##### `List<Category> findTopLevel()`
Retrieves top-level categories (no parent).

**Returns:** List of root categories

##### `List<Category> findByParentId(int parentId)`
Retrieves subcategories of a parent.

**Parameters:**
- `parentId` - Parent category ID

**Returns:** List of subcategories

##### `void save(Category category)`
Saves a new category.

**Parameters:**
- `category` - Category to save

##### `void update(Category category)`
Updates an existing category.

**Parameters:**
- `category` - Category to update

##### `void delete(int id)`
Deletes a category by ID.

**Parameters:**
- `id` - Category ID to delete

**Note:** Cannot delete category with products

---

## Service Layer

### ProductService

Business logic for product operations with caching.

#### Constructor

```java
public ProductService()
```
Initializes service with DAO and cache.

#### Methods

##### `Product getProductById(int id)`
Gets product by ID (cached).

**Parameters:**
- `id` - Product ID

**Returns:** Product object or null

**Example:**
```java
ProductService service = new ProductService();
Product product = service.getProductById(1);
```

##### `List<Product> getAllProducts()`
Gets all products.

**Returns:** List of all products

##### `List<Product> searchProducts(String keyword)`
Searches products by keyword.

**Parameters:**
- `keyword` - Search term

**Returns:** List of matching products

**Example:**
```java
List<Product> results = service.searchProducts("laptop");
```

##### `List<Product> filterByCategory(int categoryId)`
Filters products by category.

**Parameters:**
- `categoryId` - Category ID

**Returns:** List of products in category

##### `List<Product> filterByPriceRange(double minPrice, double maxPrice)`
Filters products by price range.

**Parameters:**
- `minPrice` - Minimum price
- `maxPrice` - Maximum price

**Returns:** List of products in price range

##### `List<Product> getInStockProducts()`
Gets products with stock > 0.

**Returns:** List of available products

##### `List<Product> sortByPrice(boolean ascending)`
Sorts products by price.

**Parameters:**
- `ascending` - true for low to high, false for high to low

**Returns:** Sorted list of products

##### `void saveProduct(Product product)`
Saves a new product and updates cache.

**Parameters:**
- `product` - Product to save

**Throws:** `ValidationException` if validation fails

**Example:**
```java
Product product = new Product();
product.setName("New Laptop");
product.setPrice(999.99);
service.saveProduct(product);
```

##### `void updateProduct(Product product)`
Updates product and refreshes cache.

**Parameters:**
- `product` - Product to update

##### `void deleteProduct(int id)`
Deletes product and removes from cache.

**Parameters:**
- `id` - Product ID to delete

##### `void clearCache()`
Clears product cache.

**Example:**
```java
service.clearCache(); // Force refresh from database
```

---

### OrderService

Business logic for order operations.

#### Methods

##### `Order createOrder(int userId, List<CartItem> cartItems)`
Creates a new order from cart items.

**Parameters:**
- `userId` - Customer ID
- `cartItems` - Items to order

**Returns:** Created order

**Throws:** `ValidationException` if validation fails

**Example:**
```java
OrderService service = new OrderService();
List<CartItem> items = cartService.getCartItems(userId);
Order order = service.createOrder(userId, items);
```

##### `Order getOrderById(int orderId)`
Gets order by ID.

**Parameters:**
- `orderId` - Order ID

**Returns:** Order with items

##### `List<Order> getUserOrders(int userId)`
Gets all orders for a user.

**Parameters:**
- `userId` - User ID

**Returns:** List of user's orders

##### `void updateOrderStatus(int orderId, String status)`
Updates order status.

**Parameters:**
- `orderId` - Order ID
- `status` - New status

**Example:**
```java
service.updateOrderStatus(1, "shipped");
```

##### `void cancelOrder(int orderId)`
Cancels an order.

**Parameters:**
- `orderId` - Order ID to cancel

**Throws:** `IllegalStateException` if order cannot be cancelled

##### `double calculateOrderTotal(Order order)`
Calculates order total including tax and shipping.

**Parameters:**
- `order` - Order to calculate

**Returns:** Total amount

##### `boolean canCancelOrder(Order order)`
Checks if order can be cancelled.

**Parameters:**
- `order` - Order to check

**Returns:** true if cancellable, false otherwise

---

### CartService

Manages shopping cart operations.

#### Methods

##### `void addToCart(int userId, int productId, int quantity)`
Adds product to cart.

**Parameters:**
- `userId` - User ID
- `productId` - Product ID
- `quantity` - Quantity to add

**Example:**
```java
CartService service = new CartService();
service.addToCart(1, 5, 2); // Add 2 units of product 5
```

##### `void updateQuantity(int userId, int productId, int quantity)`
Updates item quantity in cart.

**Parameters:**
- `userId` - User ID
- `productId` - Product ID
- `quantity` - New quantity

##### `void removeFromCart(int userId, int productId)`
Removes item from cart.

**Parameters:**
- `userId` - User ID
- `productId` - Product ID to remove

##### `List<CartItem> getCartItems(int userId)`
Gets all cart items for user.

**Parameters:**
- `userId` - User ID

**Returns:** List of cart items

##### `double getCartTotal(int userId)`
Calculates cart total.

**Parameters:**
- `userId` - User ID

**Returns:** Cart total amount

##### `void clearCart(int userId)`
Removes all items from cart.

**Parameters:**
- `userId` - User ID

##### `int getCartItemCount(int userId)`
Gets number of items in cart.

**Parameters:**
- `userId` - User ID

**Returns:** Item count

---

### ReportService

Generates reports and analytics.

#### Methods

##### `Map<String, Object> getDashboardStats()`
Gets dashboard statistics.

**Returns:** Map with keys:
- `totalRevenue` - Total revenue
- `totalOrders` - Order count
- `totalProducts` - Product count
- `totalUsers` - User count

**Example:**
```java
ReportService service = new ReportService();
Map<String, Object> stats = service.getDashboardStats();
double revenue = (double) stats.get("totalRevenue");
```

##### `List<Map<String, Object>> getSalesReport(LocalDate startDate, LocalDate endDate)`
Generates sales report for date range.

**Parameters:**
- `startDate` - Start date
- `endDate` - End date

**Returns:** List of sales data

##### `List<Product> getTopSellingProducts(int limit)`
Gets top selling products.

**Parameters:**
- `limit` - Number of products to return

**Returns:** List of top products

##### `List<Map<String, Object>> getCategorySales()`
Gets sales by category.

**Returns:** List with category sales data

##### `List<Map<String, Object>> getMonthlyRevenue(int year)`
Gets monthly revenue for a year.

**Parameters:**
- `year` - Year to analyze

**Returns:** List with monthly revenue data

---

## Model Classes

### Product

Represents a product in the catalog.

**Properties:**
```java
private int productId;           // Unique identifier
private String name;              // Product name
private String description;       // Detailed description
private double price;             // Price in USD
private String sku;               // Stock Keeping Unit
private int categoryId;           // Category ID
private String imageUrl;          // Image path/URL
private int stock;                // Available quantity
private String brand;             // Brand name
private double weight;            // Weight in kg
private String dimensions;        // L x W x H in cm
private LocalDateTime createdAt;  // Creation timestamp
private LocalDateTime updatedAt;  // Last update timestamp
```

**Key Methods:**
- `boolean isInStock()` - Returns true if stock > 0
- `void decreaseStock(int quantity)` - Decreases stock by quantity
- `void increaseStock(int quantity)` - Increases stock by quantity

---

### User

Represents a system user (customer or admin).

**Properties:**
```java
private int userId;
private String username;
private String email;
private String password;          // Hashed password
private String role;              // "admin" or "customer"
private String phone;
private String address;
private LocalDateTime createdAt;
```

**Key Methods:**
- `boolean isAdmin()` - Returns true if role is "admin"
- `boolean isCustomer()` - Returns true if role is "customer"

---

### Order

Represents a customer order.

**Properties:**
```java
private int orderId;
private int userId;
private double totalAmount;
private String status;            // pending/processing/shipped/delivered/cancelled
private String shippingAddress;
private String paymentMethod;
private LocalDateTime orderDate;
private List<OrderItem> items;
```

**Key Methods:**
- `void addItem(OrderItem item)` - Adds item to order
- `double calculateTotal()` - Calculates total from items
- `boolean canCancel()` - Checks if order can be cancelled

---

### OrderItem

Represents an item in an order.

**Properties:**
```java
private int orderItemId;
private int orderId;
private int productId;
private int quantity;
private double priceAtPurchase;   // Price when ordered
private double subtotal;
```

**Key Methods:**
- `double calculateSubtotal()` - Returns quantity * priceAtPurchase

---

### Category

Represents a product category.

**Properties:**
```java
private int categoryId;
private String name;
private String description;
private Integer parentCategoryId;  // Null for root categories
private String imageUrl;
private LocalDateTime createdAt;
```

**Key Methods:**
- `boolean isTopLevel()` - Returns true if parentCategoryId is null
- `boolean hasParent()` - Returns true if has parent category

---

### CartItem

Represents an item in shopping cart.

**Properties:**
```java
private int cartItemId;
private int userId;
private int productId;
private int quantity;
private Product product;          // Associated product
private LocalDateTime addedAt;
```

**Key Methods:**
- `double getSubtotal()` - Returns product.getPrice() * quantity

---

## Utility Classes

### SecurityUtils

Security-related utilities.

#### Methods

##### `String hashPassword(String plainPassword)`
Hashes a password using BCrypt.

**Parameters:**
- `plainPassword` - Plain text password

**Returns:** Hashed password

**Example:**
```java
String hashed = SecurityUtils.hashPassword("mypassword");
```

##### `boolean verifyPassword(String plainPassword, String hashedPassword)`
Verifies a password against hash.

**Parameters:**
- `plainPassword` - Plain text password
- `hashedPassword` - Hashed password to check against

**Returns:** true if password matches

**Example:**
```java
boolean isValid = SecurityUtils.verifyPassword("mypassword", hashed);
```

---

### ValidationUtil

Input validation utilities.

#### Methods

##### `boolean isValidEmail(String email)`
Validates email format.

**Parameters:**
- `email` - Email to validate

**Returns:** true if valid

##### `boolean isValidPhone(String phone)`
Validates phone number format.

**Parameters:**
- `phone` - Phone to validate

**Returns:** true if valid

##### `boolean isValidPrice(double price)`
Validates price is positive.

**Parameters:**
- `price` - Price to validate

**Returns:** true if price > 0

##### `void validateProduct(Product product)`
Validates product data.

**Parameters:**
- `product` - Product to validate

**Throws:** `ValidationException` if invalid

---

### UIUtils

UI helper methods for JavaFX.

#### Methods

##### `void showAlert(String title, String message, Alert.AlertType type)`
Shows an alert dialog.

**Parameters:**
- `title` - Dialog title
- `message` - Dialog message
- `type` - Alert type (INFORMATION, ERROR, WARNING, CONFIRMATION)

**Example:**
```java
UIUtils.showAlert("Success", "Product saved", Alert.AlertType.INFORMATION);
```

##### `boolean showConfirmation(String title, String message)`
Shows confirmation dialog.

**Parameters:**
- `title` - Dialog title
- `message` - Confirmation message

**Returns:** true if user clicked OK

##### `void loadScene(Stage stage, String fxmlPath)`
Loads and displays FXML scene.

**Parameters:**
- `stage` - Stage to load scene into
- `fxmlPath` - Path to FXML file

---

### JdbcUtils

JDBC helper methods.

#### Methods

##### `QueryResult executePreparedQuery(String query, Object... params)`
Executes a prepared statement.

**Parameters:**
- `query` - SQL query with ? placeholders
- `params` - Parameters to bind

**Returns:** QueryResult with ResultSet or affected rows

**Example:**
```java
String sql = "SELECT * FROM products WHERE category_id = ?";
QueryResult result = JdbcUtils.executePreparedQuery(sql, categoryId);
ResultSet rs = result.resultSet();
```

---

## Configuration

### ConfigManager

Manages application configuration.

#### Methods

##### `String getString(String key, String defaultValue)`
Gets string property.

**Parameters:**
- `key` - Property key
- `defaultValue` - Default if not found

**Returns:** Property value

**Example:**
```java
ConfigManager config = ConfigManager.getInstance();
String dbHost = config.getString("database.host", "localhost");
```

##### `int getInt(String key, int defaultValue)`
Gets integer property.

##### `boolean getBoolean(String key, boolean defaultValue)`
Gets boolean property.

---

For more examples and usage patterns, see:
- [Developer Guide](DEVELOPER_GUIDE.md)
- [Testing Guide](TESTING.md)
- [User Guide](USER_GUIDE.md)

---

**API Version: 1.0**
**Last Updated: December 2024**

