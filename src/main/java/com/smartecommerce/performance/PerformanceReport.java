package com.smartecommerce.performance;

import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.smartcommerce.utils.AppUtils.println;

/**
 * PerformanceReport generates comprehensive performance analysis reports
 * Demonstrates the effectiveness of optimization techniques
 */
public class PerformanceReport {
    private static final String REPORT_FILE = "performance_report.txt";

    /**
     * Generate and save performance report
     */
    public static void generateReport(ProductService productService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
            writer.println("=".repeat(80));
            writer.println("SMART E-COMMERCE SYSTEM - PERFORMANCE ANALYSIS REPORT");
            writer.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("=".repeat(80));
            writer.println();

            // Test 1: Cache Performance
            writer.println("TEST 1: CACHE PERFORMANCE");
            writer.println("-".repeat(80));
            testCachePerformance(writer, productService);
            writer.println();

            // Test 2: Search Performance
            writer.println("TEST 2: SEARCH PERFORMANCE");
            writer.println("-".repeat(80));
            testSearchPerformance(writer, productService);
            writer.println();

            // Test 3: Sorting Performance
            writer.println("TEST 3: SORTING ALGORITHMS COMPARISON");
            writer.println("-".repeat(80));
            testSortingPerformance(writer, productService);
            writer.println();

            // Test 4: Query Statistics
            writer.println("TEST 4: DATABASE QUERY STATISTICS");
            writer.println("-".repeat(80));
            QueryTimer.getAllStats().forEach((query, stats) -> {
                writer.println(query + ": " + stats);
            });
            writer.println();

            // Cache Statistics
            writer.println("CACHE STATISTICS");
            writer.println("-".repeat(80));
            writer.println(productService.getCacheStats());
            writer.println();

            writer.println("=".repeat(80));
            writer.println("END OF REPORT");
            writer.println("=".repeat(80));

            System.out.println("Performance report generated: " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testCachePerformance(PrintWriter writer, ProductService productService) {
        writer.println("Testing product retrieval with and without cache...");

        // First access (database)
        long start1 = System.nanoTime();
        Product p1 = productService.getProductById(1);
        long time1 = System.nanoTime() - start1;

        // Second access (cache)
        long start2 = System.nanoTime();
        Product p2 = productService.getProductById(1);
        long time2 = System.nanoTime() - start2;

        double improvement = ((double) (time1 - time2) / time1) * 100;

        writer.println("First access (DB):    " + String.format("%.3f ms", time1 / 1_000_000.0));
        writer.println("Second access (Cache): " + String.format("%.3f ms", time2 / 1_000_000.0));
        writer.println("Performance improvement: " + String.format("%.2f%%", improvement));
    }

    private static void testSearchPerformance(PrintWriter writer, ProductService productService) {
        writer.println("Testing search performance...");

        String searchTerm = "laptop";

        // First search (database)
        long start1 = System.nanoTime();
        List<Product> results1 = productService.searchProducts(searchTerm);
        long time1 = System.nanoTime() - start1;

        // Second search (cache)
        long start2 = System.nanoTime();
        List<Product> results2 = productService.searchProducts(searchTerm);
        long time2 = System.nanoTime() - start2;

        writer.println("First search (DB):     " + String.format("%.3f ms", time1 / 1_000_000.0));
        writer.println("Second search (Cache): " + String.format("%.3f ms", time2 / 1_000_000.0));
        writer.println("Results found: " + results1.size());
    }

    private static void testSortingPerformance(PrintWriter writer, ProductService productService) {
        List<Product> products = productService.getAllProducts();
        writer.println("Testing sorting algorithms with " + products.size() + " products...");

        // QuickSort by name
        long start1 = System.nanoTime();
        productService.sortProductsByName(products, true);
        long time1 = System.nanoTime() - start1;

        // MergeSort by price
        long start2 = System.nanoTime();
        productService.sortProductsByPrice(products, true);
        long time2 = System.nanoTime() - start2;

        writer.println("QuickSort (by name):  " + String.format("%.3f ms", time1 / 1_000_000.0));
        writer.println("MergeSort (by price): " + String.format("%.3f ms", time2 / 1_000_000.0));
    }

    /**
     * Print summary to console
     */
    public static void printSummary() {
        println("\n" + "=".repeat(60));
        println("PERFORMANCE OPTIMIZATION SUMMARY");
        println("=".repeat(60));
        println("✓ HashMap-based caching implemented");
        println("✓ QuickSort algorithm for name sorting");
        println("✓ MergeSort algorithm for price sorting");
        println("✓ Binary search for ID lookup");
        println("✓ Database query indexing");
        println("✓ Parameterized queries for SQL injection prevention");
        println("=".repeat(60) + "\n");
    }
}

