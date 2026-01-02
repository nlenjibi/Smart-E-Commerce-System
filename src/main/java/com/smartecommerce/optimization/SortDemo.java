package com.smartecommerce.optimization;

import com.smartecommerce.models.Product;
import com.smartecommerce.service.ProductService;

import java.util.List;

import static com.smartecommerce.utils.AppUtils.printf;
import static com.smartecommerce.utils.AppUtils.println;
import static java.lang.IO.print;

/**
 * SortDemo demonstrates different sorting algorithms and their performance
 */
public class SortDemo {

    public static String demonstrateSorting() {
        final String NL = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        sb.append(NL).append("=".repeat(60)).append(NL)
          .append("SORTING ALGORITHMS DEMONSTRATION").append(NL)
          .append("=".repeat(60)).append(NL);

        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();

        sb.append("Dataset size: ").append(products.size()).append(" products").append(NL).append(NL);

        // QuickSort by Name
        sb.append("1. QuickSort (by Product Name):").append(NL);
        long start1 = System.nanoTime();
        List<Product> sortedByName = productService.sortProductsByName(products, true);
        long time1 = System.nanoTime() - start1;
        sb.append(String.format("Execution time: %.3f ms", time1 / 1_000_000.0)).append(NL);
        sb.append("First 3 products:").append(NL);
        for (int i = 0; i < Math.min(3, sortedByName.size()); i++) {
            sb.append("      ").append(i + 1).append(". ")
              .append(sortedByName.get(i).getProductName()).append(NL);
        }

        // MergeSort by Price
        sb.append(NL).append("2. MergeSort (by Price - Ascending):").append(NL);
        long start2 = System.nanoTime();
        List<Product> sortedByPrice = productService.sortProductsByPrice(products, true);
        long time2 = System.nanoTime() - start2;
        sb.append(String.format("   Execution time: %.3f ms", time2 / 1_000_000.0)).append(NL);
        sb.append("   First 3 products:").append(NL);
        for (int i = 0; i < Math.min(3, sortedByPrice.size()); i++) {
            Product p = sortedByPrice.get(i);
            sb.append(String.format("      %d. %s - $%.2f", i + 1, p.getProductName(), p.getPrice()))
              .append(NL);
        }

        // MergeSort by Price (Descending)
        sb.append(NL).append("3. MergeSort (by Price - Descending):").append(NL);
        long start3 = System.nanoTime();
        List<Product> sortedByPriceDesc = productService.sortProductsByPrice(products, false);
        long time3 = System.nanoTime() - start3;
        sb.append(String.format("   Execution time: %.3f ms", time3 / 1_000_000.0)).append(NL);
        sb.append("   First 3 products:").append(NL);
        for (int i = 0; i < Math.min(3, sortedByPriceDesc.size()); i++) {
            Product p = sortedByPriceDesc.get(i);
            sb.append(String.format("      %d. %s - $%.2f", i + 1, p.getProductName(), p.getPrice()))
              .append(NL);
        }

        sb.append(NL).append("Algorithm Analysis:").append(NL)
          .append("• QuickSort:  Average O(n log n), Worst O(n²)").append(NL)
          .append("• MergeSort:  Guaranteed O(n log n), Stable sort").append(NL)
          .append("• Use case:   QuickSort for general sorting, MergeSort when stability matters")
          .append(NL).append("=".repeat(60)).append(NL);

        String output = sb.toString();
        print(output);
        return output;
    }
}
