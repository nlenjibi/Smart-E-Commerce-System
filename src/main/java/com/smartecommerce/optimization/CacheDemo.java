package com.smartecommerce.optimization;

import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;

import java.util.List;

/**
 * CacheDemo demonstrates the benefits of in-memory caching
 * Shows performance improvements using HashMap-based cache
 */
public class CacheDemo {

    public static void demonstrateCaching() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CACHE DEMONSTRATION");
        System.out.println("=".repeat(60));

        ProductService productService = new ProductService();

        // Load all products to populate cache
        System.out.println("Loading all products...");
        List<Product> products = productService.getAllProducts();
        System.out.println("Loaded " + products.size() + " products");

        // Test 1: Retrieve same product multiple times
        System.out.println("\nTest 1: Retrieving product ID 1 (5 times)");
        for (int i = 1; i <= 5; i++) {
            long start = System.nanoTime();
            Product product = productService.getProductById(1);
            long duration = System.nanoTime() - start;
            System.out.printf("Attempt %d: %.3f ms - %s\n",
                i, duration / 1_000_000.0,
                i == 1 ? "(from DB)" : "(from cache)");
        }

        // Test 2: Search caching
        System.out.println("\nTest 2: Searching for 'laptop' (3 times)");
        for (int i = 1; i <= 3; i++) {
            long start = System.nanoTime();
            List<Product> results = productService.searchProducts("laptop");
            long duration = System.nanoTime() - start;
            System.out.printf("Search %d: %.3f ms - %d results %s\n",
                i, duration / 1_000_000.0, results.size(),
                i == 1 ? "(from DB)" : "(from cache)");
        }

        // Display cache statistics
        System.out.println("\n" + productService.getCacheStats());

        System.out.println("\nKey Observations:");
        System.out.println("• First access fetches from database (slower)");
        System.out.println("• Subsequent accesses use cache (much faster)");
        System.out.println("• HashMap provides O(1) average lookup time");
        System.out.println("=".repeat(60) + "\n");
    }
}

