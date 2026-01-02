# Configuration Guide

## Table of Contents
1. [Overview](#overview)
2. [Configuration Files](#configuration-files)
3. [Database Configuration](#database-configuration)
4. [Application Settings](#application-settings)
5. [Environment Variables](#environment-variables)
6. [Advanced Configuration](#advanced-configuration)

## Overview

The Smart E-Commerce System uses multiple configuration methods:
- **config/app.properties** - Main configuration file
- **.env** - Environment-specific overrides
- **Environment Variables** - System-level configuration
- **ConfigManager** - Programmatic access

## Configuration Files

### app.properties

Located at `config/app.properties`, this is the main configuration file.

**Complete Configuration:**

```properties
# ============================================
# DATABASE CONFIGURATION
# ============================================

# Database Type (mysql or postgresql)
database.type=${DB_TYPE:postgresql}

# Connection Settings
database.host=${DB_HOST:localhost}
database.port=${DB_PORT:5432}
database.name=${DB_NAME:smart_ecommerce}
database.username=${DB_USERNAME:postgres}
database.password=${DB_PASSWORD:}

# Connection Pool Settings
database.pool.minIdle=5
database.pool.maxPoolSize=20
database.pool.connectionTimeout=30000
database.pool.idleTimeout=600000
database.pool.maxLifetime=1800000

# Additional JDBC Properties
database.jdbc.additionalProperties.useSSL=false
database.jdbc.additionalProperties.allowPublicKeyRetrieval=true
database.jdbc.additionalProperties.serverTimezone=UTC

# ============================================
# APPLICATION SETTINGS
# ============================================

# Application Information
app.name=Smart E-Commerce System
app.version=1.0.0
app.debug=true

# UI Settings
ui.theme=default
ui.language=en_US
ui.dateFormat=MM/dd/yyyy
ui.currency=USD
ui.currencySymbol=$

# ============================================
# CACHE SETTINGS
# ============================================

# Cache Configuration
cache.enabled=true
cache.size.limit=1000
cache.expiration.minutes=30

# Product Cache
cache.product.enabled=true
cache.product.size=500
cache.product.ttl=1800

# Category Cache
cache.category.enabled=true
cache.category.size=100
cache.category.ttl=3600

# FXML Cache
cache.fxml.enabled=true

# ============================================
# SECURITY SETTINGS
# ============================================

# Password Policy
security.password.minLength=8
security.password.requireUppercase=true
security.password.requireLowercase=true
security.password.requireDigit=true
security.password.requireSpecialChar=false

# Session Management
security.session.timeout=3600
security.session.rememberMe=true
security.session.maxConcurrent=1

# BCrypt Strength (4-31, higher = more secure but slower)
security.bcrypt.strength=10

# ============================================
# LOGGING SETTINGS
# ============================================

# Logging Configuration
logging.level=INFO
logging.file=logs/application.log
logging.console.enabled=true
logging.file.enabled=true
logging.file.maxSize=10MB
logging.file.maxHistory=30

# ============================================
# EMAIL CONFIGURATION (Optional)
# ============================================

# Email Settings
email.enabled=false
email.smtp.host=smtp.gmail.com
email.smtp.port=587
email.smtp.auth=true
email.smtp.starttls=true
email.username=
email.password=
email.from=noreply@smartcommerce.com

# ============================================
# PERFORMANCE SETTINGS
# ============================================

# Performance Tuning
performance.query.timeout=30
performance.async.enabled=true
performance.async.poolSize=10
performance.metrics.enabled=true

# Image Loading
performance.image.lazy=true
performance.image.cache=true
performance.image.maxWidth=1920
performance.image.maxHeight=1080

# ============================================
# BUSINESS RULES
# ============================================

# Order Settings
order.minAmount=0.01
order.maxAmount=50000.00
order.taxRate=0.08
order.shippingFee=9.99
order.freeShippingThreshold=100.00

# Cart Settings
cart.maxItems=50
cart.maxQuantityPerItem=99
cart.sessionDuration=3600

# Product Settings
product.maxImageSize=5242880
product.allowedImageTypes=jpg,jpeg,png,webp
product.lowStockThreshold=10

# ============================================
# FEATURE FLAGS
# ============================================

# Features (true/false)
feature.reviews.enabled=true
feature.wishlist.enabled=true
feature.recommendations.enabled=true
feature.guestCheckout.enabled=false
feature.socialLogin.enabled=false
```

### .env File

Create a `.env` file in the project root for environment-specific overrides:

```env
# Database Configuration
DB_TYPE=postgresql
DB_HOST=localhost
DB_PORT=5432
DB_NAME=smart_ecommerce
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# Application Settings
APP_DEBUG=false
APP_ENV=production

# Email Configuration
EMAIL_ENABLED=true
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# Security
SESSION_TIMEOUT=7200
BCRYPT_STRENGTH=12
```

**Note**: The `.env` file is ignored by Git for security. Never commit passwords!

## Database Configuration

### PostgreSQL Configuration

**For Development:**
```properties
database.type=postgresql
database.host=localhost
database.port=5432
database.name=smart_ecommerce
database.username=postgres
database.password=postgres
```

**For Production:**
```properties
database.type=postgresql
database.host=db.example.com
database.port=5432
database.name=smart_ecommerce_prod
database.username=ecommerce_user
database.password=${DB_PASSWORD}
database.jdbc.additionalProperties.ssl=true
database.jdbc.additionalProperties.sslmode=require
```

### MySQL Configuration

**For Development:**
```properties
database.type=mysql
database.host=localhost
database.port=3306
database.name=smart_ecommerce
database.username=root
database.password=
```

**For Production:**
```properties
database.type=mysql
database.host=db.example.com
database.port=3306
database.name=smart_ecommerce_prod
database.username=ecommerce_user
database.password=${DB_PASSWORD}
database.jdbc.additionalProperties.useSSL=true
database.jdbc.additionalProperties.requireSSL=true
```

### Connection Pool Settings

Adjust based on your needs:

**Low Traffic (Development):**
```properties
database.pool.minIdle=2
database.pool.maxPoolSize=5
database.pool.connectionTimeout=10000
```

**Medium Traffic (Production):**
```properties
database.pool.minIdle=10
database.pool.maxPoolSize=50
database.pool.connectionTimeout=30000
database.pool.idleTimeout=600000
```

**High Traffic (Enterprise):**
```properties
database.pool.minIdle=20
database.pool.maxPoolSize=100
database.pool.connectionTimeout=30000
database.pool.idleTimeout=300000
database.pool.maxLifetime=1800000
```

## Application Settings

### Debug Mode

**Development:**
```properties
app.debug=true
logging.level=DEBUG
performance.metrics.enabled=true
```

**Production:**
```properties
app.debug=false
logging.level=WARN
performance.metrics.enabled=false
```

### UI Configuration

**Theme Selection:**
```properties
ui.theme=default    # Options: default, dark, light
ui.language=en_US   # Options: en_US, es_ES, fr_FR
```

**Localization:**
```properties
ui.language=es_ES
ui.dateFormat=dd/MM/yyyy
ui.currency=EUR
ui.currencySymbol=€
```

### Cache Configuration

**Enable/Disable Caching:**
```properties
cache.enabled=true
cache.size.limit=1000
cache.expiration.minutes=30
```

**Per-Component Cache:**
```properties
# Product cache for frequently accessed products
cache.product.enabled=true
cache.product.size=500
cache.product.ttl=1800

# Category cache (changes rarely)
cache.category.enabled=true
cache.category.size=100
cache.category.ttl=3600

# FXML cache for faster view loading
cache.fxml.enabled=true
```

**Disable Caching for Development:**
```properties
cache.enabled=false
cache.product.enabled=false
cache.category.enabled=false
cache.fxml.enabled=false
```

## Environment Variables

### Setting Environment Variables

#### Windows (PowerShell)

**Temporary (current session):**
```powershell
$env:DB_PASSWORD="your_password"
$env:APP_ENV="production"
```

**Permanent (system-wide):**
```powershell
[System.Environment]::SetEnvironmentVariable("DB_PASSWORD", "your_password", "User")
```

#### macOS/Linux (Bash)

**Temporary (current session):**
```bash
export DB_PASSWORD="your_password"
export APP_ENV="production"
```

**Permanent (add to ~/.bashrc or ~/.zshrc):**
```bash
echo 'export DB_PASSWORD="your_password"' >> ~/.bashrc
source ~/.bashrc
```

### Supported Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_TYPE` | postgresql | Database type |
| `DB_HOST` | localhost | Database host |
| `DB_PORT` | 5432 | Database port |
| `DB_NAME` | smart_ecommerce | Database name |
| `DB_USERNAME` | postgres | Database user |
| `DB_PASSWORD` | (empty) | Database password |
| `APP_ENV` | development | Environment |
| `APP_DEBUG` | true | Debug mode |
| `CACHE_ENABLED` | true | Enable caching |
| `SESSION_TIMEOUT` | 3600 | Session timeout (seconds) |

## Advanced Configuration

### Programmatic Access

Access configuration in Java code:

```java
import com.smartcommerce.config.ConfigManager;

ConfigManager config = ConfigManager.getInstance();

// Get string property
String dbHost = config.getString("database.host", "localhost");

// Get integer property
int cacheSize = config.getInt("cache.size.limit", 1000);

// Get boolean property
boolean debugMode = config.getBoolean("app.debug", false);

// Build JDBC URL
String jdbcUrl = config.buildJdbcUrl();
```

### Custom Configuration

Add your own properties:

```properties
# Custom Business Rules
custom.promo.enabled=true
custom.promo.discountPercent=20
custom.promo.startDate=2024-12-01
custom.promo.endDate=2024-12-31

# Custom Features
custom.feature.chatbot=false
custom.feature.recommendations=true
```

Access in code:
```java
boolean promoEnabled = config.getBoolean("custom.promo.enabled", false);
int discountPercent = config.getInt("custom.promo.discountPercent", 0);
```

### Configuration Profiles

Create profile-specific configuration files:

**app-dev.properties** (Development):
```properties
app.debug=true
logging.level=DEBUG
database.host=localhost
cache.enabled=false
```

**app-prod.properties** (Production):
```properties
app.debug=false
logging.level=WARN
database.host=prod-db.example.com
cache.enabled=true
```

**app-test.properties** (Testing):
```properties
app.debug=true
logging.level=INFO
database.name=smart_ecommerce_test
cache.enabled=false
```

Load profile-specific config:
```java
String profile = System.getProperty("app.profile", "dev");
ConfigManager.loadProfile(profile);
```

### Security Best Practices

1. **Never commit passwords** to version control
2. **Use environment variables** for sensitive data
3. **Encrypt sensitive properties** in production
4. **Restrict file permissions** on config files:
   ```bash
   # Linux/macOS
   chmod 600 config/app.properties
   chmod 600 .env
   ```

5. **Use strong passwords** for database
6. **Enable SSL** for production databases
7. **Rotate credentials** regularly

### Configuration Validation

Validate configuration on startup:

```java
public class ConfigValidator {
    public static void validate() {
        ConfigManager config = ConfigManager.getInstance();
        
        // Check required properties
        requireProperty(config, "database.host");
        requireProperty(config, "database.name");
        requireProperty(config, "database.username");
        
        // Validate values
        int poolSize = config.getInt("database.pool.maxPoolSize", 20);
        if (poolSize < 1 || poolSize > 200) {
            throw new IllegalArgumentException("Invalid pool size");
        }
    }
    
    private static void requireProperty(ConfigManager config, String key) {
        if (config.getString(key, null) == null) {
            throw new IllegalStateException("Required property missing: " + key);
        }
    }
}
```

## Configuration Examples

### Example 1: High-Performance Setup

```properties
# Database
database.pool.maxPoolSize=100
database.pool.minIdle=20
database.jdbc.additionalProperties.prepStmtCacheSize=250
database.jdbc.additionalProperties.prepStmtCacheSqlLimit=2048

# Cache
cache.enabled=true
cache.product.size=1000
cache.category.size=200
cache.expiration.minutes=60

# Performance
performance.async.enabled=true
performance.async.poolSize=20
performance.image.lazy=true
```

### Example 2: Development Setup

```properties
# Database
database.type=postgresql
database.host=localhost
database.username=postgres
database.password=postgres

# Debug
app.debug=true
logging.level=DEBUG
logging.console.enabled=true

# Disable Cache
cache.enabled=false

# Fast Restarts
performance.async.enabled=false
```

### Example 3: Testing Setup

```properties
# Test Database
database.name=smart_ecommerce_test
database.pool.maxPoolSize=5

# Logging
logging.level=INFO
logging.file.enabled=false

# No Cache
cache.enabled=false

# Test Features
feature.guestCheckout.enabled=true
feature.socialLogin.enabled=false
```

## Troubleshooting

### Configuration Not Loading

**Check file location:**
```bash
ls -la config/app.properties
cat config/app.properties
```

**Check logs:**
```
Look for: "Loaded configuration from..."
```

### Environment Variables Not Working

**Verify variables are set:**
```bash
# Windows
echo $env:DB_PASSWORD

# macOS/Linux
echo $DB_PASSWORD
```

**Restart application** after setting variables

### Database Connection Issues

**Verify configuration:**
```properties
# Enable debug logging
logging.level=DEBUG

# Test connection
database.jdbc.additionalProperties.loginTimeout=10
```

**Check logs** for connection errors

---

For more information, see:
- [Installation Guide](INSTALLATION.md)
- [Database Design](DATABASE_DESIGN.md)
- [Troubleshooting](TROUBLESHOOTING.md)

**Configuration Version: 1.0**
**Last Updated: December 2024**

