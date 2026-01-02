package com.smartecommerce.optimization;

import com.smartecommerce.models.Product;
import com.smartecommerce.service.ProductService;

import java.util.List;

import static com.smartecommerce.utils.AppUtils.printf;
import static com.smartecommerce.utils.AppUtils.println;

/**
 * CacheDemo demonstrates the benefits of in-memory caching
 * Shows performance improvements using HashMap-based cache
 */
public class CacheDemo {
    private CacheDemo() {}

    public static String demonstrateCaching() {
        final String NL = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append(NL).append("=".repeat(60)).append(NL)
          .append("CACHE DEMONSTRATION").append(NL)
          .append("=".repeat(60)).append(NL);

        ProductService productService = new ProductService();

        sb.append("Loading all products...").append(NL);
        List<Product> products = productService.getAllProducts();
        sb.append("Loaded ").append(products.size()).append(" products").append(NL);

        sb.append(NL).append("Test 1: Retrieving product ID 1 (5 times)").append(NL);
        for (int i = 1; i <= 5; i++) {
            long start = System.nanoTime();
            Product product = productService.getProductById(1);

            long duration = System.nanoTime() - start;
            sb.append(String.format("Attempt %d: %.3f ms - %s", i, duration / 1_000_000.0,
                    i == 1 ? "(from DB)" : "(from cache)"))
              .append(NL);
        }

        sb.append(NL).append("Test 2: Searching for 'laptop' (3 times)").append(NL);
        for (int i = 1; i <= 3; i++) {
            long start = System.nanoTime();
            List<Product> results = productService.searchProducts("laptop");
            long duration = System.nanoTime() - start;
            sb.append(String.format("Search %d: %.3f ms - %d results %s", i, duration / 1_000_000.0,
                    results.size(), i == 1 ? "(from DB)" : "(from cache)"))
              .append(NL);
        }

        sb.append(NL).append(productService.getCacheStats()).append(NL);

        sb.append(NL).append("Key Observations:").append(NL)
          .append("• First access fetches from database (slower)").append(NL)
          .append("• Subsequent accesses use cache (much faster)").append(NL)
          .append("• HashMap provides O(1) average lookup time").append(NL)
          .append("=".repeat(60)).append(NL);

        String output = sb.toString();
        System.out.print(output);
        return output;
    }
}
