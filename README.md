# Smart E-Commerce System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A comprehensive, full-featured e-commerce system built with JavaFX, demonstrating database fundamentals, advanced SQL optimization, data structures, algorithms, and modern UI/UX design principles.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Documentation](#documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

The Smart E-Commerce System is an educational and production-ready application that demonstrates:

- **Database Design**: Third Normal Form (3NF) normalized schema with 7 core tables
- **SQL Optimization**: Strategic indexing, query optimization, and performance analysis
- **Data Structures**: HashMap-based caching, ArrayList/LinkedList comparisons
- **Algorithms**: QuickSort, MergeSort, Binary Search implementations
- **JavaFX UI**: Modern, responsive user interface with CSS styling
- **Design Patterns**: DAO, Service Layer, Singleton, Observer patterns
- **Testing**: JUnit 5 with Mockito for unit and integration tests

### Key Statistics

| Metric | Value |
|--------|-------|
| **Java Classes** | 50+ |
| **Lines of Code** | 5,000+ |
| **Database Tables** | 7 (3NF normalized) |
| **Indexes** | 15+ strategic indexes |
| **FXML Views** | 12 responsive layouts |
| **CSS Stylesheets** | 10 themed files |
| **Documentation** | 100+ markdown files |

## ✨ Features

### Admin Features
- 📊 **Dashboard**: Real-time analytics with charts and metrics
- 🛍️ **Product Management**: Full CRUD operations with image support
- 📦 **Category Management**: Hierarchical category system
- 👥 **User Management**: Customer and admin account management
- 📋 **Order Management**: View, process, and track orders
- 📈 **Analytics**: Sales reports, revenue tracking, and insights

### Customer Features
- 🏠 **Landing Page**: Browse products with filters and search
- 🛒 **Shopping Cart**: Add, remove, and update cart items
- 👤 **User Dashboard**: View orders, profile, and order history
- 🔍 **Product Search**: Advanced filtering by category, price, and availability
- 💳 **Checkout**: Secure order placement
- 🌓 **Session Management**: Persistent login across sessions

### Technical Features
- ⚡ **Performance Optimization**: Query caching, lazy loading, connection pooling
- 🔐 **Security**: Password hashing with BCrypt, SQL injection prevention
- 📱 **Responsive Design**: Adaptive layouts for different screen sizes
- 🎨 **Modern UI/UX**: Professional color scheme with accessibility features
- 🔄 **Real-time Updates**: Dynamic data loading and refresh
- 📊 **Performance Metrics**: Built-in query timing and performance reporting

## 🛠️ Technology Stack

### Backend
- **Java 17**: Core programming language
- **JDBC**: Database connectivity
- **Maven**: Dependency management and build tool
- **PostgreSQL/MySQL**: Relational database (configurable)
- **SLF4J**: Logging framework

### Frontend
- **JavaFX 21**: UI framework
- **FXML**: Declarative UI markup
- **CSS**: Styling and theming
- **ControlsFX**: Extended JavaFX controls

### Libraries & Tools
- **JUnit 5**: Unit testing
- **Mockito**: Mocking framework
- **Gson**: JSON processing
- **Apache Commons**: Utility libraries
- **BCrypt**: Password hashing

## 🏗️ Architecture

The application follows a clean, three-tier architecture:

```
┌─────────────────────────────────────────────┐
│         PRESENTATION LAYER                  │
│         (JavaFX + FXML + CSS)              │
│  • SmartEcommerceApp (Main Entry)         │
│  • Controllers (MVC Pattern)               │
│  • FXML Views & CSS Styles                 │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│         BUSINESS LOGIC LAYER                │
│         (Services + Utilities)              │
│  • ProductService (with caching)           │
│  • OrderService, CartService               │
│  • ReportService, ViewedProductsTracker    │
│  • Sorting & Searching Algorithms          │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│         DATA ACCESS LAYER                   │
│              (DAO Pattern)                  │
│  • ProductDAO, UserDAO, OrderDAO           │
│  • CategoryDAO, InventoryDAO, ReviewDAO    │
│  • JDBC Connection Management              │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│         DATABASE LAYER                      │
│      (PostgreSQL / MySQL)                   │
│  • Normalized Schema (3NF)                 │
│  • Indexes & Constraints                   │
│  • Stored Procedures & Views               │
└─────────────────────────────────────────────┘
```

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java JDK 17** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 12+** or **MySQL 8.0+** ([PostgreSQL](https://www.postgresql.org/download/) | [MySQL](https://dev.mysql.com/downloads/))
- **Git** ([Download](https://git-scm.com/downloads))
- **IDE**: IntelliJ IDEA (recommended), Eclipse, or VS Code

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/smart-ecommerce-system.git
   cd smart-ecommerce-system
   ```

2. **Set up the database**:
   
   **For PostgreSQL**:
   ```sql
   -- Create database
   CREATE DATABASE smart_ecommerce;
   
   -- Connect to database and run SQL scripts
   \c smart_ecommerce
   \i database/schema.sql
   \i database/indexes.sql
   \i database/sample_data.sql
   ```
   
   **For MySQL**:
   ```sql
   -- Create database
   CREATE DATABASE smart_ecommerce;
   
   -- Use database and run SQL scripts
   USE smart_ecommerce;
   SOURCE database/schema.sql;
   SOURCE database/indexes.sql;
   SOURCE database/sample_data.sql;
   ```

3. **Configure the application**:
   
   Edit `config/app.properties`:
   ```properties
   database.type=postgresql
   database.host=localhost
   database.port=5432
   database.name=smart_ecommerce
   database.username=your_username
   database.password=your_password
   ```
   
   Or use environment variables by creating a `.env` file:
   ```env
   DB_TYPE=postgresql
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=smart_ecommerce
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   ```

4. **Build the project**:
   ```bash
   mvn clean install
   ```

5. **Run the application**:
   
   **Option A**: Using Maven
   ```bash
   mvn javafx:run
   ```
   
   **Option B**: Using your IDE
   - Open the project in IntelliJ IDEA
   - Navigate to `src/main/java/com/smartcommerce/app/SmartEcommerceApp.java`
   - Right-click and select "Run 'SmartEcommerceApp.main()'"

6. **Login credentials**:
   
   **Admin Account**:
   - Username: `admin`
   - Password: `admin123`
   
   **Customer Account**:
   - Username: `john_doe`
   - Password: `password123`

### Quick Start Script

For Windows, use the provided batch file:
```bash
.\run.bat
```

For verification:
```powershell
.\verify-setup.ps1
```

## 📚 Documentation

Comprehensive documentation is available in the `/docs` directory:

### Core Documentation
- **[Installation Guide](docs/INSTALLATION.md)** - Detailed setup instructions
- **[User Guide](docs/USER_GUIDE.md)** - How to use the application
- **[Developer Guide](docs/DEVELOPER_GUIDE.md)** - Development guidelines
- **[API Documentation](docs/API.md)** - Class and method documentation
- **[Database Design](docs/DATABASE_DESIGN.md)** - Schema and ER diagrams
- **[Architecture](docs/ARCHITECTURE.md)** - System architecture details
- **[Configuration](docs/CONFIGURATION.md)** - Configuration options
- **[Testing Guide](docs/TESTING.md)** - Testing strategies and examples
- **[Deployment Guide](docs/DEPLOYMENT.md)** - Production deployment
- **[Troubleshooting](docs/TROUBLESHOOTING.md)** - Common issues and solutions

### Additional Resources
- **[Performance Optimization](docs/PERFORMANCE.md)** - Optimization techniques
- **[Security Best Practices](docs/SECURITY.md)** - Security guidelines
- **[Contributing Guidelines](CONTRIBUTING.md)** - How to contribute
- **[Changelog](CHANGELOG.md)** - Version history
- **[FAQ](docs/FAQ.md)** - Frequently asked questions

## 📁 Project Structure

```
Smart-E-Commerce-System/
├── config/                          # Configuration files
│   └── app.properties              # Application configuration
├── database/                        # Database scripts
│   ├── schema.sql                  # Database schema
│   ├── indexes.sql                 # Index definitions
│   └── sample_data.sql             # Sample data
├── docs/                           # Documentation
│   ├── INSTALLATION.md
│   ├── USER_GUIDE.md
│   ├── DEVELOPER_GUIDE.md
│   └── ...
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/smartcommerce/
│   │   │       ├── app/           # Application entry points
│   │   │       ├── config/        # Configuration classes
│   │   │       ├── controllers/   # JavaFX controllers
│   │   │       ├── dao/           # Data Access Objects
│   │   │       ├── exceptions/    # Custom exceptions
│   │   │       ├── model/         # Domain models
│   │   │       ├── service/       # Business logic
│   │   │       ├── utils/         # Utility classes
│   │   │       ├── optimization/  # Algorithm demos
│   │   │       └── performance/   # Performance tracking
│   │   └── resources/
│   │       ├── com/smartcommerce/
│   │       │   ├── ui/
│   │       │   │   ├── views/    # FXML files
│   │       │   │   └── styles/   # CSS files
│   │       │   └── images/       # Image resources
│   │       └── module-info.java
│   └── test/
│       └── java/                  # Unit tests
├── target/                        # Build output
├── .env                          # Environment variables
├── .gitignore                    # Git ignore rules
├── pom.xml                       # Maven configuration
├── README.md                     # This file
└── run.bat                       # Windows run script
```

## 🎨 Key Features Detailed

### Database Design
- **Normalization**: All tables in 3NF to eliminate redundancy
- **Referential Integrity**: Foreign key constraints
- **Strategic Indexing**: 15+ indexes on frequently queried columns
- **Performance**: Query optimization with EXPLAIN ANALYZE

### Caching Strategy
- **Product Cache**: HashMap-based LRU cache
- **Category Cache**: Hierarchical category tree caching
- **FXML Cache**: Pre-loaded views for faster navigation
- **Cache Invalidation**: Smart cache refresh on data changes

### Algorithms Implemented
- **Sorting**: QuickSort, MergeSort, BubbleSort with performance comparison
- **Searching**: Binary Search, Linear Search
- **Data Structures**: Custom implementations with benchmarking

### UI/UX Features
- **Responsive Layouts**: GridPane, FlowPane with dynamic sizing
- **Modern Design**: Professional color scheme (#1a1a2e, #16213e, #0f3460, #e94560)
- **Accessibility**: High contrast, keyboard navigation
- **Loading States**: Progress indicators for async operations
- **Error Handling**: User-friendly error messages

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=UserServiceTest
```

Run with coverage:
```bash
mvn clean test jacoco:report
```

View coverage report:
```bash
open target/site/jacoco/index.html
```

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Quick Contribution Steps:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Your Name** - *Initial work* - [GitHub](https://github.com/yourusername)

## 🙏 Acknowledgments

- JavaFX community for excellent documentation
- Database design principles from database fundamentals course
- Modern UI/UX inspiration from e-commerce leaders
- All contributors and testers

## 📧 Contact

- **Project Link**: [https://github.com/yourusername/smart-ecommerce-system](https://github.com/yourusername/smart-ecommerce-system)
- **Email**: your.email@example.com
- **Issues**: [GitHub Issues](https://github.com/yourusername/smart-ecommerce-system/issues)

## 🌟 Star History

If you find this project useful, please consider giving it a ⭐️!

---

**Made with ❤️ for Database Fundamentals Course**

