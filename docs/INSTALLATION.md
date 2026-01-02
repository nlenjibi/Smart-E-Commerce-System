# Installation Guide

## Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17 or higher**
   - Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
   - Verify installation:
     ```bash
     java -version
     javac -version
     ```
   - Expected output: `java version "17.x.x"` or higher

2. **Apache Maven 3.6 or higher**
   - Download from [Maven Official Site](https://maven.apache.org/download.cgi)
   - Add to PATH
   - Verify installation:
     ```bash
     mvn -version
     ```

3. **Database Server**
   
   **Option A: PostgreSQL (Recommended)**
   - Download from [PostgreSQL Official Site](https://www.postgresql.org/download/)
   - Version 12 or higher
   - Default port: 5432
   
   **Option B: MySQL**
   - Download from [MySQL Official Site](https://dev.mysql.com/downloads/)
   - Version 8.0 or higher
   - Default port: 3306

4. **Git**
   - Download from [Git Official Site](https://git-scm.com/downloads)
   - Verify installation:
     ```bash
     git --version
     ```

5. **IDE (Recommended)**
   - IntelliJ IDEA Community Edition or Ultimate
   - Eclipse IDE for Java Developers
   - Visual Studio Code with Java extensions

## Step-by-Step Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/smart-ecommerce-system.git
cd smart-ecommerce-system
```

Or download as ZIP and extract.

### 2. Database Setup

#### PostgreSQL Setup

1. **Start PostgreSQL service**:
   ```bash
   # Windows
   net start postgresql-x64-14
   
   # macOS
   brew services start postgresql
   
   # Linux
   sudo systemctl start postgresql
   ```

2. **Create database**:
   ```bash
   # Connect to PostgreSQL
   psql -U postgres
   
   # Create database
   CREATE DATABASE smart_ecommerce;
   
   # Exit psql
   \q
   ```

3. **Run database scripts**:
   ```bash
   # Navigate to database directory
   cd database
   
   # Run schema creation
   psql -U postgres -d smart_ecommerce -f schema.sql
   
   # Add indexes
   psql -U postgres -d smart_ecommerce -f indexes.sql
   
   # Load sample data
   psql -U postgres -d smart_ecommerce -f SampleDataWithStockAndImages.sql
   ```

#### MySQL Setup

1. **Start MySQL service**:
   ```bash
   # Windows
   net start MySQL80
   
   # macOS
   brew services start mysql
   
   # Linux
   sudo systemctl start mysql
   ```

2. **Create database**:
   ```bash
   # Connect to MySQL
   mysql -u root -p
   
   # Create database
   CREATE DATABASE smart_ecommerce;
   
   # Exit MySQL
   exit;
   ```

3. **Run database scripts**:
   ```bash
   # Navigate to database directory
   cd database
   
   # Run schema creation
   mysql -u root -p smart_ecommerce < schema.sql
   
   # Add indexes
   mysql -u root -p smart_ecommerce < indexes.sql
   
   # Load sample data
   mysql -u root -p smart_ecommerce < SampleDataWithStockAndImages.sql
   ```

### 3. Configure Application

#### Option A: Using config/app.properties

1. **Navigate to config directory**:
   ```bash
   cd config
   ```

2. **Edit app.properties**:
   ```properties
   # For PostgreSQL
   database.type=postgresql
   database.host=localhost
   database.port=5432
   database.name=smart_ecommerce
   database.username=postgres
   database.password=your_password
   
   # For MySQL
   # database.type=mysql
   # database.host=localhost
   # database.port=3306
   # database.name=smart_ecommerce
   # database.username=root
   # database.password=your_password
   ```

#### Option B: Using .env file

1. **Create .env file in project root**:
   ```bash
   # Navigate to project root
   cd ..
   
   # Create .env file
   touch .env  # macOS/Linux
   # or
   type nul > .env  # Windows
   ```

2. **Edit .env file**:
   ```env
   # For PostgreSQL
   DB_TYPE=postgresql
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=smart_ecommerce
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   
   # For MySQL
   # DB_TYPE=mysql
   # DB_HOST=localhost
   # DB_PORT=3306
   # DB_NAME=smart_ecommerce
   # DB_USERNAME=root
   # DB_PASSWORD=your_password
   ```

### 4. Build the Project

```bash
# Clean and build
mvn clean install

# Skip tests if needed
mvn clean install -DskipTests
```

**Expected output**:
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

### 5. Run the Application

#### Option A: Using Maven

```bash
mvn javafx:run
```

#### Option B: Using IntelliJ IDEA

1. **Open project**:
   - File → Open → Select project directory
   - Wait for Maven import to complete

2. **Configure JDK**:
   - File → Project Structure → Project
   - Set Project SDK to Java 17 or higher
   - Click Apply and OK

3. **Run application**:
   - Navigate to `src/main/java/com/smartcommerce/app/SmartEcommerceApp.java`
   - Right-click → Run 'SmartEcommerceApp.main()'
   - Or use the green play button in the toolbar

#### Option C: Using Eclipse

1. **Import project**:
   - File → Import → Maven → Existing Maven Projects
   - Select project directory
   - Click Finish

2. **Configure JDK**:
   - Right-click project → Properties → Java Build Path
   - Add Library → JRE System Library → Select Java 17+

3. **Run application**:
   - Navigate to `SmartEcommerceApp.java`
   - Right-click → Run As → Java Application

#### Option D: Using Command Line (JAR)

```bash
# Create executable JAR
mvn clean package

# Run JAR
java -jar target/Smart-E-Commerce-System-1.0-SNAPSHOT.jar
```

### 6. Verify Installation

1. **Application should launch** with the landing page
2. **Test login**:
   - Admin: username `admin`, password `admin123`
   - Customer: username `john_doe`, password `password123`
3. **Check database connection**:
   - Products should load on landing page
   - Categories should appear in filters

## Quick Setup Script

### Windows (PowerShell)

```powershell
# Run verification script
.\verify-setup.ps1

# Run application
.\run.bat
```

### macOS/Linux (Bash)

```bash
# Make script executable
chmod +x scripts/setup.sh

# Run setup
./scripts/setup.sh
```

## Troubleshooting Installation

### Issue 1: Java Not Found

**Error**: `'java' is not recognized as an internal or external command`

**Solution**:
1. Install JDK 17+
2. Add Java to PATH:
   ```bash
   # Windows
   setx JAVA_HOME "C:\Program Files\Java\jdk-17"
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   
   # macOS/Linux
   export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
   export PATH=$JAVA_HOME/bin:$PATH
   ```

### Issue 2: Maven Not Found

**Error**: `'mvn' is not recognized as an internal or external command`

**Solution**:
1. Download and install Maven
2. Add Maven to PATH:
   ```bash
   # Windows
   setx M2_HOME "C:\Program Files\Apache Maven\apache-maven-3.9.5"
   setx PATH "%PATH%;%M2_HOME%\bin"
   
   # macOS
   brew install maven
   
   # Linux
   sudo apt install maven
   ```

### Issue 3: Database Connection Failed

**Error**: `Cannot connect to database`

**Solution**:
1. Verify database is running:
   ```bash
   # PostgreSQL
   pg_isready
   
   # MySQL
   mysqladmin ping
   ```

2. Check credentials in `config/app.properties`
3. Verify database exists:
   ```sql
   -- PostgreSQL
   \l
   
   -- MySQL
   SHOW DATABASES;
   ```

4. Test connection:
   ```bash
   # PostgreSQL
   psql -U postgres -d smart_ecommerce
   
   # MySQL
   mysql -u root -p smart_ecommerce
   ```

### Issue 4: JavaFX Module Errors

**Error**: `Error: JavaFX runtime components are missing`

**Solution**:
1. Ensure Maven dependencies are correct in `pom.xml`
2. Rebuild project:
   ```bash
   mvn clean install -U
   ```
3. Run with Maven:
   ```bash
   mvn javafx:run
   ```

### Issue 5: Port Already in Use

**Error**: `Address already in use: bind`

**Solution**:
1. Check if database is running on correct port:
   ```bash
   # Windows
   netstat -ano | findstr :5432
   netstat -ano | findstr :3306
   
   # macOS/Linux
   lsof -i :5432
   lsof -i :3306
   ```

2. Stop conflicting process or change port in `config/app.properties`

### Issue 6: Module System Errors

**Error**: `Module not found`

**Solution**:
1. Check `module-info.java` is present
2. Ensure all required modules are declared
3. Clean and rebuild:
   ```bash
   mvn clean compile
   ```

## Post-Installation Steps

### 1. Verify All Features

- ✅ Login as admin
- ✅ View dashboard with charts
- ✅ Navigate to Products page
- ✅ Add/Edit/Delete a product
- ✅ View Categories page
- ✅ Login as customer
- ✅ Browse products on landing page
- ✅ Add items to cart
- ✅ Place an order

### 2. Configure Email (Optional)

For email notifications, add to `config/app.properties`:
```properties
email.enabled=true
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.username=your-email@gmail.com
email.password=your-app-password
```

### 3. Enable Logging

Configure logging in `config/app.properties`:
```properties
logging.level=DEBUG
logging.file=logs/application.log
logging.console.enabled=true
```

### 4. Performance Tuning

Adjust cache settings:
```properties
cache.enabled=true
cache.size.limit=1000
cache.expiration.minutes=30
```

## Next Steps

- Read the [User Guide](USER_GUIDE.md) to learn how to use the application
- Check the [Developer Guide](DEVELOPER_GUIDE.md) for development setup
- Review [Configuration](CONFIGURATION.md) for advanced options
- See [Troubleshooting](TROUBLESHOOTING.md) for common issues

## Getting Help

- **Documentation**: Check the [docs](.) folder
- **FAQ**: See [FAQ.md](FAQ.md)
- **Issues**: Report bugs on [GitHub Issues](https://github.com/yourusername/smart-ecommerce-system/issues)
- **Community**: Join our discussions

---

**Installation complete! 🎉**

You're now ready to use the Smart E-Commerce System.

