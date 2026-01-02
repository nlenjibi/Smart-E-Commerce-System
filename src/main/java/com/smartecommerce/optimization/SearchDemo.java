package com.smartecommerce.optimization;

import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;

import java.util.List;

/**
 * SearchDemo demonstrates different search approaches and their performance
 */
public class SearchDemo {

    public static void demonstrateSearch() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SEARCH ALGORITHMS DEMONSTRATION");
        System.out.println("=".repeat(60));

        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();

        System.out.println("Total products in database: " + products.size());

        // Linear Search (database LIKE query)
        System.out.println("\n1. Database Search (Linear - LIKE operator):");
        String[] searchTerms = {"laptop", "phone", "book", "shoes"};

        for (String term : searchTerms) {
            long start = System.nanoTime();
            List<Product> results = productService.searchProducts(term);
            long duration = System.nanoTime() - start;
            System.out.printf("   '%s': %d results in %.3f ms\n",
                term, results.size(), duration / 1_000_000.0);
        }

        // Binary Search (requires sorted list)
        System.out.println("\n2. Binary Search (by Product ID):");
        int[] searchIds = {1, 5, 10, 15, 25};

        for (int id : searchIds) {
            long start = System.nanoTime();
            Product result = productService.binarySearchById(products, id);
            long duration = System.nanoTime() - start;
            System.out.printf("   ID %d: %s in %.3f ms\n",
                id,
                result != null ? "Found" : "Not found",
                duration / 1_000_000.0);
        }

        System.out.println("\nComplexity Analysis:");
        System.out.println("• Linear Search (LIKE):  O(n) - checks every record");
        System.out.println("• Binary Search:         O(log n) - requires sorted data");
        System.out.println("• Hash-based (Cache):    O(1) - direct lookup");
        System.out.println("\nOptimization: Use database indexes for frequently searched columns");
        System.out.println("=".repeat(60) + "\n");
    }
}

