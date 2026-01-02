package com.smartecommerce.optimization;

import com.smartcommerce.model.Product;
import com.smartcommerce.service.ProductService;

import java.util.List;

import static com.smartcommerce.utils.AppUtils.printf;
import static com.smartcommerce.utils.AppUtils.println;

/**
 * SortDemo demonstrates different sorting algorithms and their performance
 */
public class SortDemo {

    public static void demonstrateSorting() {
        println("\n" + "=".repeat(60));
        println("SORTING ALGORITHMS DEMONSTRATION");
        println("=".repeat(60));

        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();

        println("Dataset size: " + products.size() + " products\n");

        // QuickSort by Name
        println("1. QuickSort (by Product Name):");
        long start1 = System.nanoTime();
        List<Product> sortedByName = productService.sortProductsByName(products, true);
        long time1 = System.nanoTime() - start1;
        printf("   Execution time: %.3f ms\n", time1 / 1_000_000.0);
        println("   First 3 products:");
        for (int i = 0; i < Math.min(3, sortedByName.size()); i++) {
           println("      " + (i + 1) + ". " + sortedByName.get(i).getProductName());
        }

        // MergeSort by Price
        println("\n2. MergeSort (by Price - Ascending):");
        long start2 = System.nanoTime();
        List<Product> sortedByPrice = productService.sortProductsByPrice(products, true);
        long time2 = System.nanoTime() - start2;
        printf("   Execution time: %.3f ms\n", time2 / 1_000_000.0);
        println("   First 3 products:");
        for (int i = 0; i < Math.min(3, sortedByPrice.size()); i++) {
            Product p = sortedByPrice.get(i);
            printf("      %d. %s - $%.2f\n",
                i + 1, p.getProductName(), p.getPrice());
        }

        // MergeSort by Price (Descending)
       println("\n3. MergeSort (by Price - Descending):");
        long start3 = System.nanoTime();
        List<Product> sortedByPriceDesc = productService.sortProductsByPrice(products, false);
        long time3 = System.nanoTime() - start3;
        printf("   Execution time: %.3f ms\n", time3 / 1_000_000.0);
        println("   First 3 products:");
        for (int i = 0; i < Math.min(3, sortedByPriceDesc.size()); i++) {
            Product p = sortedByPriceDesc.get(i);
            printf("      %d. %s - $%.2f\n",
                i + 1, p.getProductName(), p.getPrice());
        }

        println("\nAlgorithm Analysis:");
        println("• QuickSort:  Average O(n log n), Worst O(n²)");
        println("• MergeSort:  Guaranteed O(n log n), Stable sort");
        println("• Use case:   QuickSort for general sorting, MergeSort when stability matters");
        println("=".repeat(60) + "\n");
    }
}

