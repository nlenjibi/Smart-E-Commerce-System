package com.smartecommerce.app;

import com.smartcommerce.config.DatabaseConfig;
import com.smartcommerce.optimization.*;
import com.smartcommerce.performance.*;
import com.smartcommerce.service.ProductService;

/**
 * ConsoleDemo - Command-line demonstration of the system
 * Can be run without JavaFX UI for testing
 */
public class ConsoleDemo {

    public static void main(String[] args) {
        System.out.println("=" .repeat(80));
        System.out.println("SMART E-COMMERCE SYSTEM - DATABASE FUNDAMENTALS PROJECT");
        System.out.println("Console Demonstration Mode");
        System.out.println("=".repeat(80));
        System.out.println();

        // Test database connection
        if (!DatabaseConfig.testConnection()) {
            System.err.println("❌ Database connection failed!");
            System.err.println("Please ensure:");
            System.err.println("1. MySQL server is running");
            System.err.println("2. Database 'smart_ecommerce' exists");
            System.err.println("3. SQL scripts have been executed (Schema.sql, Indexes.sql, SampleData.sql)");
            System.err.println("4. Database credentials are correct in DatabaseConnection.java");
            return;
        }

        System.out.println("✅ Database connection successful!\n");

        // Run demonstrations
        try {
            System.out.println("Starting performance demonstrations...\n");

            // Demo 1: Caching
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMO 1: CACHING PERFORMANCE");
            System.out.println("=".repeat(80));
            CacheDemo.demonstrateCaching();

            // Demo 2: Search
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMO 2: SEARCH ALGORITHMS");
            System.out.println("=".repeat(80));
            SearchDemo.demonstrateSearch();

            // Demo 3: Sorting
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMO 3: SORTING ALGORITHMS");
            System.out.println("=".repeat(80));
            SortDemo.demonstrateSorting();

            // Demo 4: Query Performance
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMO 4: QUERY PERFORMANCE STATISTICS");
            System.out.println("=".repeat(80));
            QueryTimer.printStats();

            // Demo 5: Generate Report
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMO 5: GENERATING PERFORMANCE REPORT");
            System.out.println("=".repeat(80));
            ProductService productService = new ProductService();
            PerformanceReport.generateReport(productService);
            PerformanceReport.printSummary();

            // Summary
            System.out.println("\n" + "=".repeat(80));
            System.out.println("DEMONSTRATION COMPLETE");
            System.out.println("=".repeat(80));
            System.out.println("✅ All demos executed successfully!");
            System.out.println("✅ Performance report generated: performance_report.txt");
            System.out.println("\nKey Achievements:");
            System.out.println("  • Database operations tested");
            System.out.println("  • Caching performance demonstrated");
            System.out.println("  • Sorting algorithms compared");
            System.out.println("  • Search optimization shown");
            System.out.println("  • Performance metrics collected");
            System.out.println("\nNext Steps:");
            System.out.println("  1. Review performance_report.txt");
            System.out.println("  2. Run JavaFX application: mvn javafx:run");
            System.out.println("  3. Explore product/order management features");
            System.out.println("=".repeat(80));

        } catch (Exception e) {
            System.err.println("\n❌ Error during demonstration:");
            e.printStackTrace();
        } finally {
            // Cleanup
            System.out.println("\n✅ Demonstration complete.");
            System.out.println("Goodbye!");
        }
    }
}

