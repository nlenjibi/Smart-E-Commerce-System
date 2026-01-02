# Troubleshooting Guide

## Table of Contents
1. [Installation Issues](#installation-issues)
2. [Database Connection Issues](#database-connection-issues)
3. [Build & Compile Errors](#build--compile-errors)
4. [Runtime Errors](#runtime-errors)
5. [UI & Display Issues](#ui--display-issues)
6. [Performance Issues](#performance-issues)
7. [Authentication Issues](#authentication-issues)
8. [Data Issues](#data-issues)

## Installation Issues

### Issue: Java Not Found

**Error Message:**
```
'java' is not recognized as an internal or external command
```

**Solution:**

1. **Verify Java installation:**
   ```bash
   java -version
   javac -version
   ```

2. **Install Java 17+ if not installed:**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

3. **Add Java to PATH:**

   **Windows:**
   ```powershell
   setx JAVA_HOME "C:\Program Files\Java\jdk-17"
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   ```

   **macOS/Linux:**
   ```bash
   echo 'export JAVA_HOME=/usr/lib/jvm/java-17' >> ~/.bashrc
   echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
   source ~/.bashrc
   ```

4. **Restart terminal/IDE** after setting PATH

---

### Issue: Maven Not Found

**Error Message:**
```
'mvn' is not recognized as an internal or external command
```

**Solution:**

1. **Verify Maven installation:**
   ```bash
   mvn -version
   ```

2. **Install Maven:**

   **Windows:**
   - Download from [Apache Maven](https://maven.apache.org/download.cgi)
   - Extract and add to PATH

   **macOS:**
   ```bash
   brew install maven
   ```

   **Linux:**
   ```bash
   sudo apt install maven
   ```

3. **Verify installation:**
   ```bash
   mvn -version
   ```

---

### Issue: Git Clone Fails

**Error Message:**
```
fatal: unable to access 'https://github.com/...': Could not resolve host
```

**Solution:**

1. **Check internet connection**

2. **Verify Git installation:**
   ```bash
   git --version
   ```

3. **Try SSH instead of HTTPS:**
   ```bash
   git clone git@github.com:username/repo.git
   ```

4. **Download as ZIP** if clone fails:
   - Go to GitHub repository
   - Click "Code" → "Download ZIP"
   - Extract and use

---

## Database Connection Issues

### Issue: Cannot Connect to Database

**Error Message:**
```
Connection failed! Check database credentials and ensure the database is running.
```

**Solution:**

1. **Check if database is running:**

   **PostgreSQL:**
   ```bash
   # Check status
   pg_isready
   
   # Start service (Windows)
   net start postgresql-x64-14
   
   # Start service (macOS)
   brew services start postgresql
   
   # Start service (Linux)
   sudo systemctl start postgresql
   ```

   **MySQL:**
   ```bash
   # Check status
   mysqladmin ping
   
   # Start service (Windows)
   net start MySQL80
   
   # Start service (macOS)
   brew services start mysql
   
   # Start service (Linux)
   sudo systemctl start mysql
   ```

2. **Verify database exists:**

   **PostgreSQL:**
   ```bash
   psql -U postgres
   \l
   # Look for smart_ecommerce
   ```

   **MySQL:**
   ```bash
   mysql -u root -p
   SHOW DATABASES;
   # Look for smart_ecommerce
   ```

3. **Check configuration:**
   
   Edit `config/app.properties`:
   ```properties
   database.type=postgresql
   database.host=localhost
   database.port=5432
   database.name=smart_ecommerce
   database.username=postgres
   database.password=your_password
   ```

4. **Test connection:**

   **PostgreSQL:**
   ```bash
   psql -h localhost -U postgres -d smart_ecommerce
   ```

   **MySQL:**
   ```bash
   mysql -h localhost -u root -p smart_ecommerce
   ```

5. **Check firewall:**
   - Ensure ports 5432 (PostgreSQL) or 3306 (MySQL) are open

---

### Issue: Database Driver Not Found

**Error Message:**
```
java.lang.ClassNotFoundException: org.postgresql.Driver
```

**Solution:**

1. **Clean and rebuild:**
   ```bash
   mvn clean install -U
   ```

2. **Verify dependencies in `pom.xml`:**
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <version>42.7.8</version>
   </dependency>
   ```

3. **Delete Maven cache and rebuild:**
   ```bash
   rm -rf ~/.m2/repository/org/postgresql
   mvn clean install
   ```

---

### Issue: Database Authentication Failed

**Error Message:**
```
FATAL: password authentication failed for user "postgres"
```

**Solution:**

1. **Verify username and password:**
   - Check `config/app.properties`
   - Test login manually

2. **Reset database password:**

   **PostgreSQL:**
   ```bash
   # Connect as superuser
   sudo -u postgres psql
   
   # Change password
   ALTER USER postgres WITH PASSWORD 'newpassword';
   ```

   **MySQL:**
   ```bash
   # Connect as root
   mysql -u root -p
   
   # Change password
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'newpassword';
   FLUSH PRIVILEGES;
   ```

3. **Update configuration:**
   ```properties
   database.password=newpassword
   ```

---

## Build & Compile Errors

### Issue: Maven Build Fails

**Error Message:**
```
[ERROR] Failed to execute goal on project...
```

**Solution:**

1. **Clean and rebuild:**
   ```bash
   mvn clean install
   ```

2. **Skip tests if they're failing:**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Update dependencies:**
   ```bash
   mvn clean install -U
   ```

4. **Check Java version:**
   ```bash
   mvn -version
   # Should show Java 17+
   ```

5. **Clear Maven cache:**
   ```bash
   rm -rf ~/.m2/repository
   mvn clean install
   ```

---

### Issue: Module System Errors

**Error Message:**
```
Error: Module jakarta.servlet not found
```

**Solution:**

1. **Check `module-info.java`:**
   ```java
   module com.ecommerce.smartecommercesystem {
       requires javafx.controls;
       requires javafx.fxml;
       requires java.sql;
       requires static jakarta.servlet;  // Note: static
       requires static gson;              // Note: static
       // ... other modules
   }
   ```

2. **Make optional dependencies static**

3. **Rebuild:**
   ```bash
   mvn clean compile
   ```

---

### Issue: JavaFX Runtime Missing

**Error Message:**
```
Error: JavaFX runtime components are missing
```

**Solution:**

1. **Run with Maven:**
   ```bash
   mvn javafx:run
   ```

2. **If running from IDE, add VM options:**
   ```
   --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```

3. **Verify JavaFX in `pom.xml`:**
   ```xml
   <dependency>
       <groupId>org.openjfx</groupId>
       <artifactId>javafx-controls</artifactId>
       <version>21.0.6</version>
   </dependency>
   ```

---

## Runtime Errors

### Issue: NullPointerException on Startup

**Error Message:**
```
java.lang.NullPointerException at com.smartecommerce...
```

**Solution:**

1. **Check logs for specific location**

2. **Common causes:**
   - Missing configuration file
   - FXML file not found
   - Database connection failed

3. **Enable debug logging:**
   ```properties
   logging.level=DEBUG
   ```

4. **Check FXML paths:**
   ```java
   // Correct
   FXMLLoader.load(getClass().getResource("/com/smartcommerce/ui/views/login.fxml"));
   
   // Wrong (missing leading slash)
   FXMLLoader.load(getClass().getResource("com/smartcommerce/ui/views/login.fxml"));
   ```

---

### Issue: FXML Load Exception

**Error Message:**
```
javafx.fxml.LoadException: Error loading FXML
```

**Solution:**

1. **Check FXML path is correct**

2. **Verify controller is specified:**
   ```xml
   <BorderPane xmlns:fx="http://javafx.com/fxml"
               fx:controller="com.smartecommerce.controllers.LoginController">
   ```

3. **Ensure @FXML annotations:**
   ```java
   @FXML
   private Button loginButton;
   
   @FXML
   private void handleLogin() {
       // ...
   }
   ```

4. **Check fx:id matches field name:**
   ```xml
   <Button fx:id="loginButton" text="Login"/>
   ```
   ```java
   @FXML
   private Button loginButton;  // Must match fx:id
   ```

---

### Issue: Scene Navigation Fails

**Error Message:**
```
IllegalArgumentException: Invalid FXML path
```

**Solution:**

1. **Use absolute paths:**
   ```java
   // Good
   "/com/smartcommerce/ui/views/dashboard.fxml"
   
   // Bad
   "com/smartcommerce/ui/views/dashboard.fxml"
   ```

2. **Check file exists in resources:**
   ```
   src/main/resources/com/smartcommerce/ui/views/dashboard.fxml
   ```

3. **Rebuild project:**
   ```bash
   mvn clean compile
   ```

---

## UI & Display Issues

### Issue: Window Appears Blank

**Solution:**

1. **Check CSS is loaded:**
   ```java
   scene.getStylesheets().add(
       getClass().getResource("/com/smartcommerce/ui/styles/common.css").toExternalForm()
   );
   ```

2. **Verify FXML has content**

3. **Check console for errors**

4. **Try without CSS:**
   ```java
   // Temporarily comment out CSS to isolate issue
   // scene.getStylesheets().add(...);
   ```

---

### Issue: Images Don't Display

**Solution:**

1. **Check image path:**
   ```java
   // For resources
   new Image(getClass().getResourceAsStream("/images/logo.png"));
   
   // For URLs
   new Image("https://example.com/image.jpg");
   
   // For local files
   new Image("file:///path/to/image.jpg");
   ```

2. **Verify image exists:**
   ```
   src/main/resources/images/logo.png
   ```

3. **Check image format:**
   - Supported: PNG, JPG, JPEG, GIF, BMP, WEBP

4. **Enable error handling:**
   ```java
   Image image = new Image(path);
   if (image.isError()) {
       System.err.println("Failed to load image: " + path);
   }
   ```

---

### Issue: Layout Broken/Overlapping

**Solution:**

1. **Check FXML layout constraints**

2. **Verify CSS doesn't have conflicts**

3. **Use Scene Builder** to visually debug layout

4. **Add borders temporarily to debug:**
   ```css
   * {
       -fx-border-color: red;
       -fx-border-width: 1px;
   }
   ```

---

### Issue: TableView Empty

**Solution:**

1. **Check data is loaded:**
   ```java
   System.out.println("Items: " + tableView.getItems().size());
   ```

2. **Verify cell value factories:**
   ```java
   nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
   ```

3. **Check property names match:**
   ```java
   // Property name must match getter
   private String name;
   public String getName() { return name; }
   ```

4. **Use debugging:**
   ```java
   tableView.getItems().addListener((ListChangeListener<Product>) c -> {
       System.out.println("Items changed: " + c.getList().size());
   });
   ```

---

## Performance Issues

### Issue: Application Slow to Start

**Solution:**

1. **Enable caching:**
   ```properties
   cache.fxml.enabled=true
   ```

2. **Reduce logging:**
   ```properties
   logging.level=WARN
   ```

3. **Increase JVM memory:**
   ```bash
   mvn javafx:run -Djavafx.args="-Xmx2g"
   ```

4. **Check for large datasets on startup**

---

### Issue: Slow Database Queries

**Solution:**

1. **Check indexes exist:**
   ```bash
   # Run indexes.sql
   psql -d smart_ecommerce -f database/indexes.sql
   ```

2. **Analyze query performance:**
   ```sql
   EXPLAIN ANALYZE SELECT * FROM products WHERE category_id = 1;
   ```

3. **Enable query logging:**
   ```properties
   performance.metrics.enabled=true
   ```

4. **Increase connection pool:**
   ```properties
   database.pool.maxPoolSize=50
   ```

See [Performance Guide](PERFORMANCE.md) for more optimization tips.

---

### Issue: High Memory Usage

**Solution:**

1. **Limit cache size:**
   ```properties
   cache.size.limit=500
   cache.product.size=200
   ```

2. **Check for memory leaks:**
   - Remove listeners when done
   - Clear collections
   - Close resources

3. **Monitor with JProfiler or VisualVM**

4. **Increase heap size if needed:**
   ```bash
   -Xms512m -Xmx2g
   ```

---

## Authentication Issues

### Issue: Cannot Login

**Solution:**

1. **Verify user exists:**
   ```sql
   SELECT * FROM users WHERE username = 'admin';
   ```

2. **Check password hash:**
   ```sql
   -- Password should be bcrypt hash starting with $2a$
   SELECT password FROM users WHERE username = 'admin';
   ```

3. **Reset password:**
   ```java
   // Generate hash
   String hash = SecurityUtils.hashPassword("admin123");
   System.out.println(hash);
   ```
   ```sql
   -- Update in database
   UPDATE users SET password = '$2a$10$...' WHERE username = 'admin';
   ```

4. **Check logs for specific error**

---

### Issue: Session Expires Too Quickly

**Solution:**

1. **Increase session timeout:**
   ```properties
   security.session.timeout=7200
   ```

2. **Enable remember me:**
   ```properties
   security.session.rememberMe=true
   ```

---

## Data Issues

### Issue: Products Not Showing

**Solution:**

1. **Check database has products:**
   ```sql
   SELECT COUNT(*) FROM products;
   ```

2. **Run sample data script:**
   ```bash
   psql -d smart_ecommerce -f database/SampleDataWithStockAndImages.sql
   ```

3. **Check filter/search criteria**

4. **Clear cache:**
   ```java
   productService.clearCache();
   ```

---

### Issue: Orders Not Saving

**Solution:**

1. **Check foreign key constraints:**
   ```sql
   -- Verify user exists
   SELECT * FROM users WHERE user_id = ?;
   
   -- Verify products exist
   SELECT * FROM products WHERE product_id = ?;
   ```

2. **Check database logs for errors**

3. **Verify order has items:**
   ```java
   if (order.getItems().isEmpty()) {
       throw new IllegalStateException("Order must have items");
   }
   ```

---

### Issue: Stock Not Updating

**Solution:**

1. **Check stock update query:**
   ```sql
   UPDATE products 
   SET stock = stock - ? 
   WHERE product_id = ? AND stock >= ?;
   ```

2. **Verify transaction commits**

3. **Check for concurrent updates:**
   - Use database transactions
   - Add optimistic locking

---

## Getting Help

If you're still experiencing issues:

1. **Enable debug logging:**
   ```properties
   logging.level=DEBUG
   app.debug=true
   ```

2. **Check logs in:**
   - Console output
   - `logs/application.log`

3. **Search existing issues:**
   - GitHub Issues
   - FAQ

4. **Submit detailed bug report:**
   - Error message
   - Stack trace
   - Steps to reproduce
   - Environment details (OS, Java version, database)

5. **Include relevant logs:**
   ```bash
   # Get last 100 lines of log
   tail -n 100 logs/application.log
   ```

---

## Common Error Patterns

### Pattern: Startup Failures

**Checklist:**
- [ ] Java 17+ installed
- [ ] Maven dependencies downloaded
- [ ] Database running
- [ ] Database exists
- [ ] Configuration correct
- [ ] Network accessible

### Pattern: UI Not Loading

**Checklist:**
- [ ] FXML path correct
- [ ] Controller specified
- [ ] CSS loaded
- [ ] Images exist
- [ ] No console errors

### Pattern: Data Not Saving

**Checklist:**
- [ ] Database connection works
- [ ] Tables exist
- [ ] Foreign keys valid
- [ ] No constraint violations
- [ ] Transaction commits

---

For more help, see:
- [FAQ](FAQ.md)
- [User Guide](USER_GUIDE.md)
- [Developer Guide](DEVELOPER_GUIDE.md)
- [Configuration](CONFIGURATION.md)

**Last Updated**: December 24, 2024

