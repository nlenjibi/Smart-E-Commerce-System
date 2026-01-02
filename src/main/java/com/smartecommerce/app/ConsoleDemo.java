package com.smartecommerce.app;

import com.smartecommerce.config.DatabaseConfig;
import com.smartecommerce.optimization.*;
import com.smartecommerce.performance.*;
import com.smartecommerce.service.ProductService;

import static com.smartecommerce.utils.AppUtils.printE;
import static com.smartecommerce.utils.AppUtils.println;

/**
 * ConsoleDemo - Command-line demonstration of the system
 * Can be run without JavaFX UI for testing
 */
public class ConsoleDemo {

    public static void main(String[] args) {
        println("=" .repeat(80));
        println("SMART E-COMMERCE SYSTEM - DATABASE FUNDAMENTALS PROJECT");
        println("Console Demonstration Mode");
        println("=".repeat(80));
        println("");

        // Test database connection
        if (!DatabaseConfig.testConnection()) {
            printE("❌ Database connection failed!");
            printE("Please ensure:");
            printE("1. MySQL server is running");
            printE("2. Database 'smart_ecommerce' exists");
            printE("3. SQL scripts have been executed (Schema.sql, Indexes.sql, SampleData.sql)");
            printE("4. Database credentials are correct in DatabaseConnection.java");
            return;
        }

        println("✅ Database connection successful!\n");

        // Run demonstrations
        try {
            println("Starting performance demonstrations...\n");

            // Demo 1: Caching
            println("\n" + "=".repeat(80));
            println("DEMO 1: CACHING PERFORMANCE");
            println("=".repeat(80));
            CacheDemo.demonstrateCaching();

            // Demo 2: Search
            println("\n" + "=".repeat(80));
            println("DEMO 2: SEARCH ALGORITHMS");
            println("=".repeat(80));
            SearchDemo.demonstrateSearch();

            // Demo 3: Sorting
            println("\n" + "=".repeat(80));
            println("DEMO 3: SORTING ALGORITHMS");
            println("=".repeat(80));
            SortDemo.demonstrateSorting();

            // Demo 4: Query Performance
            println("\n" + "=".repeat(80));
            println("DEMO 4: QUERY PERFORMANCE STATISTICS");
            println("=".repeat(80));
            QueryTimer.printStats();

            // Demo 5: Generate Report
            println("\n" + "=".repeat(80));
            println("DEMO 5: GENERATING PERFORMANCE REPORT");
            println("=".repeat(80));
            ProductService productService = new ProductService();
            PerformanceReport.generateReport(productService);
            PerformanceReport.printSummary();

            // Summary
            println("\n" + "=".repeat(80));
            println("DEMONSTRATION COMPLETE");
            println("=".repeat(80));
            println("✅ All demos executed successfully!");
            println("✅ Performance report generated: performance_report.txt");
            println("\nKey Achievements:");
            println("  • Database operations tested");
            println("  • Caching performance demonstrated");
            println("  • Sorting algorithms compared");
            println("  • Search optimization shown");
            println("  • Performance metrics collected");
            println("\nNext Steps:");
            println("  1. Review performance_report.txt");
            println("  2. Run JavaFX application: mvn javafx:run");
            println("  3. Explore product/order management features");
            println("=".repeat(80));

        } catch (Exception e) {
            printE("\n❌ Error during demonstration:");
            e.printStackTrace();
        } finally {
            // Cleanup
            println("\n✅ Demonstration complete.");
            println("Goodbye!");
        }
    }
}

