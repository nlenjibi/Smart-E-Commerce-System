package com.smartecommerce.performance;

import com.smartecommerce.models.Product;
import com.smartecommerce.service.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.smartecommerce.utils.AppUtils.println;

/**
 * PerformanceReport generates comprehensive performance analysis reports
 * Demonstrates the effectiveness of optimization techniques
 */
public class PerformanceReport {
    private static final String REPORT_FILE = "performance_report.txt";

    /**
     * Generate and save performance report, returning the report content.
     */
    public static String generateReport(ProductService productService) {
        final String NL = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append("=".repeat(80)).append(NL)
          .append("SMART E-COMMERCE SYSTEM - PERFORMANCE ANALYSIS REPORT").append(NL)
          .append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append(NL)
          .append("=".repeat(80)).append(NL).append(NL);

        sb.append("TEST 1: CACHE PERFORMANCE").append(NL)
          .append("-".repeat(80)).append(NL);
        testCachePerformance(sb, productService);
        sb.append(NL);

        sb.append("TEST 2: SEARCH PERFORMANCE").append(NL)
          .append("-".repeat(80)).append(NL);
        testSearchPerformance(sb, productService);
        sb.append(NL);

        sb.append("TEST 3: SORTING ALGORITHMS COMPARISON").append(NL)
          .append("-".repeat(80)).append(NL);
        testSortingPerformance(sb, productService);
        sb.append(NL);

        sb.append("TEST 4: DATABASE QUERY STATISTICS").append(NL)
          .append("-".repeat(80)).append(NL);
        QueryTimer.getAllStats().forEach((query, stats) -> sb.append(query).append(": ").append(stats).append(NL));
        sb.append(NL);

        sb.append("CACHE STATISTICS").append(NL)
          .append("-".repeat(80)).append(NL)
          .append(productService.getCacheStats()).append(NL).append(NL)
          .append("=".repeat(80)).append(NL)
          .append("END OF REPORT").append(NL)
          .append("=".repeat(80)).append(NL);

        String report = sb.toString();
        try {
            Files.writeString(Path.of(REPORT_FILE), report);
            System.out.println("Performance report generated: " + REPORT_FILE);
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
        return report;
    }

    private static void testCachePerformance(StringBuilder sb, ProductService productService) {
        sb.append("Testing product retrieval with and without cache...").append(System.lineSeparator());

        // First access (database)
        long start1 = System.nanoTime();
        Product p1 = productService.getProductById(1);
        long time1 = System.nanoTime() - start1;

        // Second access (cache)
        long start2 = System.nanoTime();
        Product p2 = productService.getProductById(1);
        long time2 = System.nanoTime() - start2;

        double improvement = ((double) (time1 - time2) / time1) * 100;

        sb.append("First access (DB):    ").append(String.format("%.3f ms", time1 / 1_000_000.0)).append(System.lineSeparator());
        sb.append("Second access (Cache): ").append(String.format("%.3f ms", time2 / 1_000_000.0)).append(System.lineSeparator());
        sb.append("Performance improvement: ").append(String.format("%.2f%%", improvement)).append(System.lineSeparator());
    }

    private static void testSearchPerformance(StringBuilder sb, ProductService productService) {
        sb.append("Testing search performance...").append(System.lineSeparator());

        String searchTerm = "laptop";

        // First search (database)
        long start1 = System.nanoTime();
        List<Product> results1 = productService.searchProducts(searchTerm);
        long time1 = System.nanoTime() - start1;

        // Second search (cache)
        long start2 = System.nanoTime();
        List<Product> results2 = productService.searchProducts(searchTerm);
        long time2 = System.nanoTime() - start2;

        sb.append("First search (DB):     ").append(String.format("%.3f ms", time1 / 1_000_000.0)).append(System.lineSeparator());
        sb.append("Second search (Cache): ").append(String.format("%.3f ms", time2 / 1_000_000.0)).append(System.lineSeparator());
        sb.append("Results found: ").append(results1.size()).append(System.lineSeparator());
    }

    private static void testSortingPerformance(StringBuilder sb, ProductService productService) {
        List<Product> products = productService.getAllProducts();
        sb.append("Testing sorting algorithms with " + products.size() + " products...")
          .append(System.lineSeparator());

        // QuickSort by name
        long start1 = System.nanoTime();
        productService.sortProductsByName(products, true);
        long time1 = System.nanoTime() - start1;

        // MergeSort by price
        long start2 = System.nanoTime();
        productService.sortProductsByPrice(products, true);
        long time2 = System.nanoTime() - start2;

        sb.append("QuickSort (by name):  ").append(String.format("%.3f ms", time1 / 1_000_000.0)).append(System.lineSeparator());
        sb.append("MergeSort (by price): ").append(String.format("%.3f ms", time2 / 1_000_000.0)).append(System.lineSeparator());
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

