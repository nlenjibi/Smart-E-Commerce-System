package com.smartecommerce.optimization;

import com.smartecommerce.models.Product;
import com.smartecommerce.service.ProductService;

import java.util.List;

import static com.smartecommerce.utils.AppUtils.printf;
import static com.smartecommerce.utils.AppUtils.println;
import static java.lang.IO.print;

/**
 * SearchDemo demonstrates different search approaches and their performance
 */
public class SearchDemo {

    public static String demonstrateSearch() {
        final String NL = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append(NL)
          .append("=".repeat(60)).append(NL)
          .append("SEARCH ALGORITHMS DEMONSTRATION").append(NL)
          .append("=".repeat(60)).append(NL);

        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();

        sb.append("Total products in database: ").append(products.size()).append(NL);

        sb.append(NL).append("1. Database Search (Linear - LIKE operator):").append(NL);
        String[] searchTerms = {"laptop", "phone", "book", "shoes"};

        for (String term : searchTerms) {
            long start = System.nanoTime();
            List<Product> results = productService.searchProducts(term);
            long durationNs = System.nanoTime() - start;
            sb.append(String.format("   '%s': %d results in %.3f ms", term, results.size(), durationNs / 1_000_000.0))
              .append(NL);
        }

        sb.append(NL).append("2. Binary Search (by Product ID):").append(NL);
        int[] searchIds = {1, 5, 10, 15, 25};

        for (int id : searchIds) {
            long start = System.nanoTime();
            Product result = productService.binarySearchById(products, id);
            long durationNs = System.nanoTime() - start;
            sb.append(String.format("   ID %d: %s in %.3f ms", id,
                result != null ? "Found" : "Not found",
                durationNs / 1_000_000.0)).append(NL);
        }

        sb.append(NL).append("Complexity Analysis:").append(NL)
          .append("• Linear Search (LIKE):  O(n) - checks every record").append(NL)
          .append("• Binary Search:         O(log n) - requires sorted data").append(NL)
          .append("• Hash-based (Cache):    O(1) - direct lookup").append(NL)
          .append(NL)
          .append("Optimization: Use database indexes for frequently searched columns").append(NL)
          .append("=".repeat(60)).append(NL).append(NL);

        String output = sb.toString();
        print(output);
        return output;
    }
}
