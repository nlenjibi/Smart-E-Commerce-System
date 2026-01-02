# Developer Guide

## Table of Contents
1. [Development Environment Setup](#development-environment-setup)
2. [Project Architecture](#project-architecture)
3. [Code Structure](#code-structure)
4. [Development Workflow](#development-workflow)
5. [API Reference](#api-reference)
6. [Testing](#testing)
7. [Best Practices](#best-practices)
8. [Contributing](#contributing)

## Development Environment Setup

### IDE Setup

#### IntelliJ IDEA (Recommended)

1. **Install IntelliJ IDEA**
   - Download from [JetBrains](https://www.jetbrains.com/idea/)
   - Community Edition is sufficient

2. **Open Project**
   - File → Open → Select project directory
   - Wait for Maven import

3. **Configure JDK**
   - File → Project Structure → Project
   - Set SDK to Java 17+
   - Set language level to 17

4. **Install Plugins** (Optional but recommended)
   - JavaFX Support
   - Maven Helper
   - SonarLint (code quality)
   - GitToolBox

5. **Enable Annotation Processing**
   - Settings → Build → Compiler → Annotation Processors
   - Check "Enable annotation processing"

#### Eclipse Setup

1. **Install Eclipse IDE**
   - Download Eclipse IDE for Java Developers

2. **Import Project**
   - File → Import → Maven → Existing Maven Projects
   - Browse to project directory
   - Select pom.xml

3. **Configure Java Compiler**
   - Project → Properties → Java Compiler
   - Set to 17 or higher

4. **Install e(fx)clipse**
   - Help → Eclipse Marketplace
   - Search "e(fx)clipse"
   - Install

#### VS Code Setup

1. **Install VS Code**
   - Download from [code.visualstudio.com](https://code.visualstudio.com/)

2. **Install Extensions**
   - Extension Pack for Java
   - Maven for Java
   - JavaFX Support

3. **Configure Java**
   - Ctrl+Shift+P → "Java: Configure Java Runtime"
   - Set Java 17+

### Database Tools

1. **DBeaver** (Universal Database Tool)
   - Download from [dbeaver.io](https://dbeaver.io/)
   - Connect to PostgreSQL/MySQL
   - Browse tables, run queries

2. **pgAdmin** (PostgreSQL)
   - Included with PostgreSQL installation
   - Web-based interface

3. **MySQL Workbench** (MySQL)
   - Download from [MySQL](https://dev.mysql.com/downloads/workbench/)
   - Visual database design

### Version Control

1. **Git Configuration**
   ```bash
   git config --global user.name "Your Name"
   git config --global user.email "your.email@example.com"
   ```

2. **Clone Repository**
   ```bash
   git clone https://github.com/yourusername/smart-ecommerce-system.git
   cd smart-ecommerce-system
   ```

3. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Project Architecture

### Three-Tier Architecture

```
┌─────────────────────────────────────────────┐
│         PRESENTATION LAYER                  │
│    ┌──────────────────────────────────┐    │
│    │  JavaFX Views (FXML)             │    │
│    │  - login.fxml                    │    │
│    │  - dashboard.fxml                │    │
│    │  - products.fxml                 │    │
│    │  - ...                           │    │
│    └──────────────────────────────────┘    │
│    ┌──────────────────────────────────┐    │
│    │  Controllers                     │    │
│    │  - LoginController               │    │
│    │  - DashboardController           │    │
│    │  - ProductsController            │    │
│    └──────────────────────────────────┘    │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│         BUSINESS LOGIC LAYER                │
│    ┌──────────────────────────────────┐    │
│    │  Services                        │    │
│    │  - ProductService                │    │
│    │  - OrderService                  │    │
│    │  - CartService                   │    │
│    │  - ReportService                 │    │
│    └──────────────────────────────────┘    │
│    ┌──────────────────────────────────┐    │
│    │  Utilities                       │    │
│    │  - SecurityUtils                 │    │
│    │  - ValidationUtil                │    │
│    │  - UIUtils                       │    │
│    └──────────────────────────────────┘    │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│         DATA ACCESS LAYER                   │
│    ┌──────────────────────────────────┐    │
│    │  DAOs (Data Access Objects)      │    │
│    │  - ProductDAO                    │    │
│    │  - UserDAO                       │    │
│    │  - OrderDAO                      │    │
│    │  - CategoryDAO                   │    │
│    └──────────────────────────────────┘    │
│    ┌──────────────────────────────────┐    │
│    │  Database Configuration          │    │
│    │  - DatabaseConfig                │    │
│    │  - ConfigManager                 │    │
│    └──────────────────────────────────┘    │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│         DATABASE LAYER                      │
│         PostgreSQL / MySQL                  │
└─────────────────────────────────────────────┘
```

### Design Patterns Used

#### 1. Model-View-Controller (MVC)
- **Model**: `com.smartcommerce.model.*`
- **View**: FXML files in `resources/com/smartcommerce/ui/views/`
- **Controller**: `com.smartcommerce.controllers.*`

#### 2. Data Access Object (DAO)
```java
// Interface
public interface GenericDAO<T> {
    T findById(int id);
    List<T> findAll();
    void save(T entity);
    void update(T entity);
    void delete(int id);
}

// Implementation
public class ProductDAO implements GenericDAO<Product> {
    // CRUD implementations
}
```

#### 3. Singleton
```java
public class DatabaseConfig {
    private static DatabaseConfig instance;
    
    private DatabaseConfig() {}
    
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
}
```

#### 4. Service Layer Pattern
```java
public class ProductService {
    private final ProductDAO productDAO;
    private final Map<Integer, Product> cache;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
        this.cache = new HashMap<>();
    }
    
    public Product getProductById(int id) {
        // Check cache first
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        // Fetch from database
        Product product = productDAO.findById(id);
        cache.put(id, product);
        return product;
    }
}
```

#### 5. Observer Pattern (JavaFX Properties)
```java
public class CartItem {
    private IntegerProperty quantity = new SimpleIntegerProperty();
    
    public IntegerProperty quantityProperty() {
        return quantity;
    }
    
    // In controller
    cartItem.quantityProperty().addListener((obs, oldVal, newVal) -> {
        updateTotal();
    });
}
```

## Code Structure

### Package Organization

```
com.smartcommerce/
├── app/                    # Application entry points
│   ├── SmartEcommerceApp.java
│   └── ConsoleDemo.java
├── config/                 # Configuration classes
│   ├── DatabaseConfig.java
│   └── ConfigManager.java
├── controllers/            # JavaFX controllers
│   ├── BaseController.java
│   ├── LoginController.java
│   ├── DashboardController.java
│   ├── ProductsController.java
│   ├── OrdersController.java
│   ├── CartController.java
│   └── ...
├── dao/                    # Data Access Objects
│   ├── ProductDAO.java
│   ├── UserDAO.java
│   ├── OrderDAO.java
│   ├── CategoryDAO.java
│   ├── InventoryDAO.java
│   └── ReviewDAO.java
├── model/                  # Domain models
│   ├── Product.java
│   ├── User.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Category.java
│   ├── CartItem.java
│   ├── Inventory.java
│   └── Review.java
├── service/                # Business logic
│   ├── ProductService.java
│   ├── OrderService.java
│   ├── CartService.java
│   ├── ReportService.java
│   └── ViewedProductsTracker.java
├── utils/                  # Utility classes
│   ├── SecurityUtils.java
│   ├── ValidationUtil.java
│   ├── UIUtils.java
│   ├── JdbcUtils.java
│   ├── AppUtils.java
│   ├── FXMLCache.java
│   ├── AsyncTaskManager.java
│   └── PasswordValidator.java
├── exceptions/             # Custom exceptions
│   ├── ValidationException.java
│   └── DataAccessException.java
├── optimization/           # Algorithm demos
│   ├── QuickSortDemo.java
│   ├── MergeSortDemo.java
│   ├── BinarySearchDemo.java
│   ├── CacheDemo.java
│   ├── SearchDemo.java
│   └── SortDemo.java
└── performance/            # Performance tracking
    ├── QueryTimer.java
    └── PerformanceReport.java
```

### Model Classes

Example: **Product.java**
```java
package com.smartcommerce.model;

import java.time.LocalDateTime;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private String sku;
    private int categoryId;
    private String imageUrl;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public Product() {}
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    
    // ... other getters/setters
    
    // Business logic methods
    public boolean isInStock() {
        return stock > 0;
    }
    
    public void decreaseStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
        } else {
            throw new IllegalArgumentException("Insufficient stock");
        }
    }
    
    @Override
    public String toString() {
        return String.format("Product[id=%d, name=%s, price=%.2f]", 
            productId, name, price);
    }
}
```

### DAO Implementation

Example: **ProductDAO.java**
```java
package com.smartcommerce.dao;

import com.smartcommerce.model.Product;
import com.smartcommerce.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    public Product findById(int id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        
        try {
            var result = JdbcUtils.executePreparedQuery(sql, id);
            ResultSet rs = result.resultSet();
            
            if (rs != null && rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error finding product", e);
        }
        
        return null;
    }
    
    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY created_at DESC";
        List<Product> products = new ArrayList<>();
        
        try {
            var result = JdbcUtils.executePreparedQuery(sql);
            ResultSet rs = result.resultSet();
            
            while (rs != null && rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error fetching products", e);
        }
        
        return products;
    }
    
    public void save(Product product) {
        String sql = """
            INSERT INTO products (name, description, price, sku, 
                                 category_id, image_url, stock)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try {
            var result = JdbcUtils.executePreparedQuery(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSku(),
                product.getCategoryId(),
                product.getImageUrl(),
                product.getStock()
            );
            
            // Get generated ID
            if (result.generatedKeys() != null) {
                product.setProductId(result.generatedKeys().get(0));
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving product", e);
        }
    }
    
    public void update(Product product) {
        String sql = """
            UPDATE products 
            SET name = ?, description = ?, price = ?, sku = ?,
                category_id = ?, image_url = ?, stock = ?
            WHERE product_id = ?
        """;
        
        try {
            JdbcUtils.executePreparedQuery(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSku(),
                product.getCategoryId(),
                product.getImageUrl(),
                product.getStock(),
                product.getProductId()
            );
        } catch (Exception e) {
            throw new DataAccessException("Error updating product", e);
        }
    }
    
    public void delete(int id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        
        try {
            JdbcUtils.executePreparedQuery(sql, id);
        } catch (Exception e) {
            throw new DataAccessException("Error deleting product", e);
        }
    }
    
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setSku(rs.getString("sku"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setImageUrl(rs.getString("image_url"));
        product.setStock(rs.getInt("stock"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return product;
    }
}
```

### Service Layer

Example: **ProductService.java**
```java
package com.smartcommerce.service;

import com.smartcommerce.dao.ProductDAO;
import com.smartcommerce.model.Product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductDAO productDAO;
    private final Map<Integer, Product> cache;
    
    public ProductService() {
        this.productDAO = new ProductDAO();
        this.cache = new LinkedHashMap<>(100, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > 100; // LRU cache with max 100 items
            }
        };
    }
    
    public Product getProductById(int id) {
        // Check cache
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        
        // Fetch from database
        Product product = productDAO.findById(id);
        if (product != null) {
            cache.put(id, product);
        }
        
        return product;
    }
    
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }
    
    public List<Product> searchProducts(String keyword) {
        return productDAO.findAll().stream()
            .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase())
                      || p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public List<Product> filterByCategory(int categoryId) {
        return productDAO.findAll().stream()
            .filter(p -> p.getCategoryId() == categoryId)
            .collect(Collectors.toList());
    }
    
    public List<Product> filterByPriceRange(double minPrice, double maxPrice) {
        return productDAO.findAll().stream()
            .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }
    
    public void saveProduct(Product product) {
        productDAO.save(product);
        cache.put(product.getProductId(), product);
    }
    
    public void updateProduct(Product product) {
        productDAO.update(product);
        cache.put(product.getProductId(), product);
    }
    
    public void deleteProduct(int id) {
        productDAO.delete(id);
        cache.remove(id);
    }
    
    public void clearCache() {
        cache.clear();
    }
}
```

### Controller Implementation

Example: **ProductsController.java**
```java
package com.smartcommerce.controllers;

import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;
import com.smartcommerce.utils.UIUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductsController extends BaseController {
    
    @FXML private TableView<Product> productsTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> stockColumn;
    @FXML private TextField searchField;
    @FXML private Button addButton;
    
    private ProductService productService;
    
    @FXML
    public void initialize() {
        productService = new ProductService();
        setupTable();
        loadProducts();
        setupSearchListener();
    }
    
    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        // Format price column
        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        // Add action column
        addActionColumn();
    }
    
    private void addActionColumn() {
        TableColumn<Product, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            
            {
                editBtn.setOnAction(e -> handleEdit(getTableRow().getItem()));
                deleteBtn.setOnAction(e -> handleDelete(getTableRow().getItem()));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });
        productsTable.getColumns().add(actionColumn);
    }
    
    private void loadProducts() {
        var products = productService.getAllProducts();
        productsTable.setItems(FXCollections.observableArrayList(products));
    }
    
    private void setupSearchListener() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                loadProducts();
            } else {
                var filtered = productService.searchProducts(newVal);
                productsTable.setItems(FXCollections.observableArrayList(filtered));
            }
        });
    }
    
    @FXML
    private void handleAdd() {
        // Open add product dialog
        UIUtils.showDialog("Add Product", "add_product.fxml", result -> {
            if (result != null) {
                productService.saveProduct((Product) result);
                loadProducts();
                UIUtils.showSuccess("Product added successfully");
            }
        });
    }
    
    private void handleEdit(Product product) {
        UIUtils.showDialog("Edit Product", "edit_product.fxml", product, result -> {
            if (result != null) {
                productService.updateProduct((Product) result);
                loadProducts();
                UIUtils.showSuccess("Product updated successfully");
            }
        });
    }
    
    private void handleDelete(Product product) {
        boolean confirmed = UIUtils.showConfirmation(
            "Delete Product",
            "Are you sure you want to delete " + product.getName() + "?"
        );
        
        if (confirmed) {
            productService.deleteProduct(product.getProductId());
            loadProducts();
            UIUtils.showSuccess("Product deleted successfully");
        }
    }
}
```

## Development Workflow

### 1. Feature Development

1. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature
   ```

2. **Implement Feature**
   - Write code following best practices
   - Add comments and documentation
   - Follow naming conventions

3. **Write Tests**
   - Unit tests for business logic
   - Integration tests for database operations
   - UI tests for controllers

4. **Commit Changes**
   ```bash
   git add .
   git commit -m "feat: add product search functionality"
   ```

5. **Push to Remote**
   ```bash
   git push origin feature/your-feature
   ```

6. **Create Pull Request**
   - Describe changes
   - Link related issues
   - Request review

### 2. Code Review Process

**Reviewer Checklist**:
- [ ] Code follows style guide
- [ ] Tests are included and pass
- [ ] Documentation is updated
- [ ] No security vulnerabilities
- [ ] Performance is acceptable
- [ ] Edge cases are handled

### 3. Commit Messages

Follow **Conventional Commits**:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Build/tool changes

**Examples**:
```
feat(products): add product search functionality

fix(cart): resolve quantity update issue

docs(readme): update installation instructions

refactor(dao): optimize database queries
```

### 4. Testing Workflow

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=ProductServiceTest

# Run with coverage
mvn clean test jacoco:report

# View coverage
open target/site/jacoco/index.html
```

## Testing

### Unit Testing Example

```java
package com.ecommerce;

import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    
    private ProductService productService;
    
    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }
    
    @Test
    @DisplayName("Should get product by ID")
    void testGetProductById() {
        // Arrange
        int productId = 1;
        
        // Act
        Product product = productService.getProductById(productId);
        
        // Assert
        assertNotNull(product);
        assertEquals(productId, product.getProductId());
    }
    
    @Test
    @DisplayName("Should search products by keyword")
    void testSearchProducts() {
        // Arrange
        String keyword = "laptop";
        
        // Act
        var results = productService.searchProducts(keyword);
        
        // Assert
        assertFalse(results.isEmpty());
        assertTrue(results.stream()
            .allMatch(p -> p.getName().toLowerCase().contains(keyword.toLowerCase())));
    }
    
    @Test
    @DisplayName("Should throw exception for negative price")
    void testNegativePrice() {
        // Arrange
        Product product = new Product();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(-10.0);
        });
    }
}
```

## Best Practices

### Code Style

1. **Naming Conventions**
   - Classes: PascalCase (`ProductService`)
   - Methods: camelCase (`getProductById()`)
   - Constants: UPPER_SNAKE_CASE (`MAX_CACHE_SIZE`)
   - Packages: lowercase (`com.smartcommerce.service`)

2. **Formatting**
   - Indent: 4 spaces
   - Max line length: 120 characters
   - Braces on same line

3. **Comments**
   ```java
   /**
    * Retrieves a product by its ID.
    * 
    * @param id the product ID
    * @return the product, or null if not found
    * @throws DataAccessException if database error occurs
    */
   public Product getProductById(int id) {
       // Implementation
   }
   ```

### Database Access

1. **Use Prepared Statements** (prevent SQL injection)
   ```java
   // GOOD
   String sql = "SELECT * FROM products WHERE id = ?";
   var result = JdbcUtils.executePreparedQuery(sql, id);
   
   // BAD
   String sql = "SELECT * FROM products WHERE id = " + id;
   ```

2. **Close Resources** (use try-with-resources)
   ```java
   try (Connection conn = DatabaseConfig.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
       // Use resources
   } // Automatically closed
   ```

3. **Handle Exceptions**
   ```java
   try {
       // Database operation
   } catch (SQLException e) {
       logger.error("Database error", e);
       throw new DataAccessException("Error accessing data", e);
   }
   ```

### Performance

1. **Use Caching**
   ```java
   private Map<Integer, Product> cache = new HashMap<>();
   
   public Product getProduct(int id) {
       return cache.computeIfAbsent(id, productDAO::findById);
   }
   ```

2. **Lazy Loading**
   ```java
   private List<Product> products;
   
   public List<Product> getProducts() {
       if (products == null) {
           products = productDAO.findAll();
       }
       return products;
   }
   ```

3. **Use Indexes** (in database)
   ```sql
   CREATE INDEX idx_products_name ON products(name);
   CREATE INDEX idx_orders_user_id ON orders(user_id);
   ```

### Security

1. **Password Hashing**
   ```java
   String hashedPassword = SecurityUtils.hashPassword(plainPassword);
   boolean isValid = SecurityUtils.verifyPassword(plainPassword, hashedPassword);
   ```

2. **Input Validation**
   ```java
   if (!ValidationUtil.isValidEmail(email)) {
       throw new ValidationException("Invalid email format");
   }
   ```

3. **SQL Injection Prevention**
   - Always use prepared statements
   - Never concatenate user input into SQL

## Contributing

See [CONTRIBUTING.md](../CONTRIBUTING.md) for detailed guidelines.

### Quick Contribution Steps

1. Fork the repository
2. Create feature branch
3. Make changes
4. Write tests
5. Submit pull request

---

For more information, see:
- [API Documentation](API.md)
- [Testing Guide](TESTING.md)
- [Architecture](ARCHITECTURE.md)

**Happy Coding! 💻**

