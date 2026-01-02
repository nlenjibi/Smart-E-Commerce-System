# Frequently Asked Questions (FAQ)

## Table of Contents
1. [General Questions](#general-questions)
2. [Installation & Setup](#installation--setup)
3. [Database](#database)
4. [Features & Functionality](#features--functionality)
5. [Troubleshooting](#troubleshooting)
6. [Performance](#performance)
7. [Security](#security)
8. [Development](#development)

## General Questions

### What is the Smart E-Commerce System?

The Smart E-Commerce System is a full-featured e-commerce application built with JavaFX, demonstrating database fundamentals, SQL optimization, data structures, algorithms, and modern UI/UX design. It includes both admin and customer features.

### What technologies does it use?

- **Backend**: Java 17, JDBC, Maven
- **Frontend**: JavaFX 21, FXML, CSS
- **Database**: PostgreSQL or MySQL
- **Testing**: JUnit 5, Mockito
- **Libraries**: BCrypt, Gson, Apache Commons

### Is it production-ready?

Yes, the system is production-ready with:
- Secure password hashing
- SQL injection prevention
- Error handling
- Performance optimization
- Comprehensive testing

### Is it open source?

Yes, the project is open source under the MIT License.

### Can I use it for commercial purposes?

Yes, the MIT License allows commercial use. See the [LICENSE](../LICENSE) file for details.

## Installation & Setup

### What are the system requirements?

**Minimum Requirements:**
- Java JDK 17 or higher
- Maven 3.6+
- PostgreSQL 12+ or MySQL 8.0+
- 4GB RAM
- 500MB disk space

**Recommended:**
- Java JDK 21
- Maven 3.9+
- PostgreSQL 15+
- 8GB RAM
- 1GB disk space

### How do I install the system?

See the [Installation Guide](INSTALLATION.md) for step-by-step instructions.

**Quick Steps:**
1. Clone the repository
2. Set up database
3. Configure `config/app.properties`
4. Run `mvn clean install`
5. Run `mvn javafx:run`

### Can I use MySQL instead of PostgreSQL?

Yes! The system supports both PostgreSQL and MySQL. Configure in `config/app.properties`:

```properties
database.type=mysql
database.host=localhost
database.port=3306
```

### Do I need to install JavaFX separately?

No, JavaFX dependencies are managed by Maven and downloaded automatically during the build.

### Why does the build fail with module errors?

Make sure you have Java 17+ and Maven 3.6+. Run:
```bash
mvn clean install -U
```

If errors persist, see [Troubleshooting](TROUBLESHOOTING.md#build-errors).

## Database

### How do I create the database?

**PostgreSQL:**
```sql
CREATE DATABASE smart_ecommerce;
```

**MySQL:**
```sql
CREATE DATABASE smart_ecommerce;
```

Then run the SQL scripts in the `database/` directory.

### Where are the database scripts?

Database scripts are in the `/database` directory:
- `schema.sql` - Table creation
- `indexes.sql` - Index definitions
- `SampleDataWithStockAndImages.sql` - Sample data

### How do I reset the database?

```sql
-- PostgreSQL
DROP DATABASE smart_ecommerce;
CREATE DATABASE smart_ecommerce;
\i database/schema.sql

-- MySQL
DROP DATABASE smart_ecommerce;
CREATE DATABASE smart_ecommerce;
SOURCE database/schema.sql;
```

### Can I use a remote database?

Yes, configure the host in `config/app.properties`:

```properties
database.host=your-db-host.com
database.port=5432
database.username=your_user
database.password=your_password
```

### How do I backup the database?

**PostgreSQL:**
```bash
pg_dump smart_ecommerce > backup.sql
```

**MySQL:**
```bash
mysqldump -u root -p smart_ecommerce > backup.sql
```

### How do I restore from backup?

**PostgreSQL:**
```bash
psql smart_ecommerce < backup.sql
```

**MySQL:**
```bash
mysql -u root -p smart_ecommerce < backup.sql
```

## Features & Functionality

### What features are included?

**Admin Features:**
- Dashboard with analytics
- Product management (CRUD)
- Category management
- Order management
- User management
- Sales reports

**Customer Features:**
- Product browsing
- Shopping cart
- Order placement
- Order tracking
- Profile management

### How do I add products?

1. Login as admin
2. Navigate to Products
3. Click "Add Product"
4. Fill in details
5. Click "Save"

See [User Guide](USER_GUIDE.md#add-new-product) for details.

### How do I process orders?

1. Login as admin
2. Navigate to Orders
3. Select an order
4. Change status (Processing → Shipped → Delivered)
5. Click "Update"

### Can customers checkout without an account?

Currently, customers must create an account. Guest checkout can be enabled in `config/app.properties`:

```properties
feature.guestCheckout.enabled=true
```

### How do I add categories?

1. Login as admin
2. Navigate to Categories
3. Click "Add Category"
4. Enter name and description
5. Optionally select parent category
6. Click "Save"

### How do product images work?

Products support image URLs. You can use:
- Local file paths: `/images/products/laptop.jpg`
- Web URLs: `https://example.com/images/laptop.jpg`

Images are loaded asynchronously for better performance.

### How is inventory managed?

Stock levels are tracked in the `products` table. When orders are placed:
1. Stock is decreased automatically
2. Out of stock products can't be ordered
3. Low stock alerts shown in admin dashboard

## Troubleshooting

### The application won't start

**Check:**
1. Java 17+ is installed: `java -version`
2. Database is running
3. Database connection in `config/app.properties` is correct
4. Run `mvn clean install` to rebuild

See [Troubleshooting Guide](TROUBLESHOOTING.md).

### I can't connect to the database

**Check:**
1. Database server is running
2. Database exists
3. Credentials are correct in `config/app.properties`
4. Host and port are correct

**Test connection:**
```bash
# PostgreSQL
psql -h localhost -U postgres -d smart_ecommerce

# MySQL
mysql -h localhost -u root -p smart_ecommerce
```

### Products don't load on the landing page

**Possible causes:**
1. Database connection failed
2. No products in database
3. Cache issue

**Solutions:**
1. Check database connection
2. Run sample data script
3. Clear cache or disable in config:
   ```properties
   cache.enabled=false
   ```

### Images don't display

**Check:**
1. Image path/URL is correct
2. Image file exists
3. File permissions (for local files)
4. Network connection (for web URLs)

**Debug:**
Enable debug logging:
```properties
logging.level=DEBUG
```

### Login fails with correct credentials

**Check:**
1. User exists in database
2. Password was hashed correctly
3. Check console for error messages

**Reset password:**
```sql
UPDATE users 
SET password = '$2a$10$...' 
WHERE username = 'admin';
```

Use `SecurityUtils.hashPassword()` to generate hash.

### Application is slow

See [Performance Optimization](PERFORMANCE.md).

**Quick fixes:**
1. Enable caching
2. Increase connection pool size
3. Add database indexes
4. Enable lazy loading

## Performance

### How can I improve performance?

1. **Enable caching:**
   ```properties
   cache.enabled=true
   cache.product.size=1000
   ```

2. **Increase connection pool:**
   ```properties
   database.pool.maxPoolSize=50
   ```

3. **Use database indexes** (already included in `indexes.sql`)

4. **Enable async loading:**
   ```properties
   performance.async.enabled=true
   ```

See [Performance Guide](PERFORMANCE.md) for more.

### How does caching work?

The system uses in-memory LRU caching for:
- Products (frequently accessed)
- Categories (rarely change)
- FXML views (faster navigation)

Cache is automatically invalidated when data changes.

### Can I disable caching?

Yes, for development:
```properties
cache.enabled=false
```

### How many concurrent users can it handle?

With proper configuration, the system can handle:
- **100+ concurrent users** with default settings
- **500+ concurrent users** with optimized settings
- **1000+ concurrent users** with load balancing

Depends on hardware and database configuration.

## Security

### How are passwords secured?

Passwords are hashed using BCrypt with configurable strength:
```properties
security.bcrypt.strength=10
```

Plain text passwords are never stored.

### Is the system protected against SQL injection?

Yes, all database queries use prepared statements, which prevent SQL injection.

### How do I change password requirements?

Configure in `config/app.properties`:
```properties
security.password.minLength=8
security.password.requireUppercase=true
security.password.requireLowercase=true
security.password.requireDigit=true
security.password.requireSpecialChar=true
```

### How long do sessions last?

Default is 1 hour (3600 seconds). Configure:
```properties
security.session.timeout=7200
```

### Can I enable HTTPS?

For development, HTTP is sufficient. For production, use a reverse proxy (nginx, Apache) with SSL/TLS.

### How do I reset the admin password?

Connect to database and run:
```sql
UPDATE users 
SET password = '$2a$10$N9qo8uL6FOQH3l7YEZQWQu9w7K3FxXLWBV6.4FxTdpEYD4wEZS6eW'
WHERE username = 'admin';
```

This sets password to `admin123`.

## Development

### How do I set up the development environment?

See [Developer Guide](DEVELOPER_GUIDE.md#development-environment-setup).

**Quick steps:**
1. Install IntelliJ IDEA
2. Import Maven project
3. Configure JDK 17+
4. Run from IDE

### How do I run tests?

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=ProductServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### How do I add a new feature?

1. Create feature branch
2. Implement feature
3. Write tests
4. Update documentation
5. Submit pull request

See [Contributing Guidelines](CONTRIBUTING.md).

### Where should I add business logic?

Business logic goes in the **Service Layer** (`com.smartecommerce.service`), not in controllers or DAOs.

**Example:**
```java
// Good - in ProductService
public List<Product> getInStockProducts() {
    return getAllProducts().stream()
        .filter(Product::isInStock)
        .collect(Collectors.toList());
}

// Bad - in Controller
// (Controllers should be thin)
```

### How do I add a new database table?

1. Add table to `database/schema.sql`
2. Create model class in `com.smartecommerce.models`
3. Create DAO in `com.smartecommerce.dao`
4. Create service if needed
5. Update documentation

### How do I add a new view?

1. Create FXML file in `resources/com/smartcommerce/ui/views/`
2. Create CSS file in `resources/com/smartcommerce/ui/styles/`
3. Create controller in `com.smartecommerce.controllers`
4. Add navigation from existing views

### Can I contribute to the project?

Yes! Contributions are welcome. See [Contributing Guidelines](CONTRIBUTING.md).

### Where can I get help?

1. Read the [documentation](INDEX.md)
2. Check this FAQ
3. Review [Troubleshooting](TROUBLESHOOTING.md)
4. Submit an issue on GitHub
5. Join community discussions

---

## Still Have Questions?

If your question isn't answered here:

1. **Check** [Troubleshooting Guide](TROUBLESHOOTING.md)
2. **Read** [User Guide](USER_GUIDE.md) or [Developer Guide](DEVELOPER_GUIDE.md)
3. **Search** existing GitHub issues
4. **Submit** a new issue with details

---

**Last Updated**: December 24, 2024

