package com.smartecommerce.service;

import com.smartcommerce.model.Product;

import java.util.*;
import java.util.logging.Logger;

/**
 * ViewedProductsTracker - Tracks recently viewed products for users
 * Session-based tracking for logged-in users
 */
public class ViewedProductsTracker {

    private static final Logger LOGGER = Logger.getLogger(ViewedProductsTracker.class.getName());
    private static final int MAX_VIEWED_PRODUCTS = 20; // Keep last 20 viewed products

    // Thread-safe storage: userId -> LinkedHashSet of product IDs (maintains insertion order)
    private static final Map<Integer, LinkedHashSet<Integer>> userViewedProducts = new HashMap<>();

    /**
     * Track a product view for a user
     * @param userId The user ID
     * @param productId The product ID being viewed
     */
    public static synchronized void addViewedProduct(int userId, int productId) {
        // Get or create the user's viewed products set
        LinkedHashSet<Integer> viewedProducts = userViewedProducts.get(userId);

        if (viewedProducts == null) {
            viewedProducts = new LinkedHashSet<>();
            userViewedProducts.put(userId, viewedProducts);
        }

        // Remove if already exists (to move to end)
        viewedProducts.remove(productId);

        // Add to the end
        viewedProducts.add(productId);

        // Trim to max size if needed (remove oldest)
        if (viewedProducts.size() > MAX_VIEWED_PRODUCTS) {
            Iterator<Integer> iterator = viewedProducts.iterator();
            iterator.next();
            iterator.remove();
        }

        LOGGER.info("Added product " + productId + " to viewed history for user " + userId);
    }

    /**
     * Get recently viewed product IDs for a user (most recent first)
     * @param userId The user ID
     * @param limit Maximum number of products to return
     * @return List of product IDs in reverse order (most recent first)
     */
    public static synchronized List<Integer> getRecentlyViewedProductIds(int userId, int limit) {
        LinkedHashSet<Integer> viewedProducts = userViewedProducts.get(userId);

        if (viewedProducts == null || viewedProducts.isEmpty()) {
            return new ArrayList<>();
        }

        // Convert to list and reverse (most recent first)
        List<Integer> productIdList = new ArrayList<>(viewedProducts);
        Collections.reverse(productIdList);

        // Limit the results
        if (limit > 0 && productIdList.size() > limit) {
            productIdList = productIdList.subList(0, limit);
        }

        return productIdList;
    }

    /**
     * Get recently viewed products as Product objects
     * @param userId The user ID
     * @param limit Maximum number of products to return
     * @param productService ProductService to fetch product details
     * @return List of Product objects
     */
    public static List<Product> getRecentlyViewedProducts(int userId, int limit, ProductService productService) {
        List<Integer> productIds = getRecentlyViewedProductIds(userId, limit);

        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Fetch product details
        List<Product> products = new ArrayList<>();
        for (Integer productId : productIds) {
            try {
                Product product = productService.getProductById(productId);
                if (product != null) {
                    products.add(product);
                }
            } catch (Exception e) {
                LOGGER.warning("Failed to fetch product " + productId + ": " + e.getMessage());
            }
        }

        return products;
    }

    /**
     * Clear viewed products history for a user
     * @param userId The user ID
     */
    public static synchronized void clearHistory(int userId) {
        userViewedProducts.remove(userId);
        LOGGER.info("Cleared viewed products history for user " + userId);
    }

    /**
     * Clear all viewed products (for session cleanup)
     */
    public static synchronized void clearAllHistory() {
        userViewedProducts.clear();
        LOGGER.info("Cleared all viewed products history");
    }

    /**
     * Get count of viewed products for a user
     * @param userId The user ID
     * @return Number of viewed products
     */
    public static synchronized int getViewedCount(int userId) {
        LinkedHashSet<Integer> viewedProducts = userViewedProducts.get(userId);
        return viewedProducts != null ? viewedProducts.size() : 0;
    }
}

