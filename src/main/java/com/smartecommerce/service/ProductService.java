package com.smartecommerce.service;

import com.smartcommerce.dao.ProductDAO;
import com.smartcommerce.model.Product;

import java.util.*;

import static com.smartcommerce.utils.AppUtils.*;

/**
 * ProductService provides business logic for product operations
 * Implements caching for performance optimization
 */
public class ProductService {
    private final ProductDAO productDAO;
    private final Map<Integer, Product> productCache; // In-memory cache using HashMap
    private final Map<String, List<Product>> searchCache; // Cache for search results

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.productCache = new HashMap<>();
        this.searchCache = new HashMap<>();
    }

    // Constructor for testing with mock DAO
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
        this.productCache = new HashMap<>();
        this.searchCache = new HashMap<>();
    }

    /**
     * Add product with cache update
     */
    public boolean addProduct(Product product) {
        boolean success = productDAO.create(product);
        if (success) {
            productCache.put(product.getProductId(), product);
            searchCache.clear(); // Invalidate search cache
        }
        return success;
    }

    /**
     * Get product by ID with caching
     */
    public Product getProductById(int productId) {
        // Check cache first
        if (productCache.containsKey(productId)) {
            println("Product retrieved from cache");
            return productCache.get(productId);
        }

        // If not in cache, fetch from database
        Product product = productDAO.findById(productId);
        if (product != null) {
            productCache.put(productId, product);
        }
        return product;
    }

    /**
     * Get all products with caching
     */
    public List<Product> getAllProducts() {
        List<Product> products = productDAO.findAll();
        if (products != null && !products.isEmpty()) {
            // Update cache
            products.forEach(p -> productCache.put(p.getProductId(), p));
        }
        return products != null ? products : new ArrayList<>();
    }

    /**
     * Update product with cache invalidation
     */
    public boolean updateProduct(Product product) {
        boolean success = productDAO.update(product);
        if (success) {
            productCache.put(product.getProductId(), product);
            searchCache.clear(); // Invalidate search cache
        }
        return success;
    }

    /**
     * Delete product with cache removal
     */
    public boolean deleteProduct(int productId) {
        boolean success = productDAO.delete(productId);
        if (success) {
            productCache.remove(productId);
            searchCache.clear(); // Invalidate search cache
        }
        return success;
    }

    /**
     * Search products with caching
     */
    public List<Product> searchProducts(String searchTerm) {
        // Sanitize search term
        String sanitizedTerm = sanitize(searchTerm);
        if (isEmpty(sanitizedTerm)) {
            return new ArrayList<>();
        }

        // Check search cache
        String cacheKey = sanitizedTerm.toLowerCase();
        if (searchCache.containsKey(cacheKey)) {
            println("Search results retrieved from cache");
            return searchCache.get(cacheKey);
        }

        // Fetch from database
        List<Product> results = productDAO.searchByName(sanitizedTerm);

        // Update cache
        searchCache.put(cacheKey, results);

        return results;
    }

    /**
     * Sort products by name using QuickSort algorithm
     */
    public List<Product> sortProductsByName(List<Product> products, boolean ascending) {
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        List<Product> sortedList = new ArrayList<>(products);
        quickSortByName(sortedList, 0, sortedList.size() - 1, ascending);
        return sortedList;
    }

    /**
     * QuickSort implementation for product names
     */
    private void quickSortByName(List<Product> products, int low, int high, boolean ascending) {
        if (low < high) {
            int partitionIndex = partition(products, low, high, ascending);
            quickSortByName(products, low, partitionIndex - 1, ascending);
            quickSortByName(products, partitionIndex + 1, high, ascending);
        }
    }

    private int partition(List<Product> products, int low, int high, boolean ascending) {
        Product pivot = products.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = ascending
                    ? products.get(j).getProductName().compareToIgnoreCase(pivot.getProductName()) <= 0
                    : products.get(j).getProductName().compareToIgnoreCase(pivot.getProductName()) >= 0;

            if (condition) {
                i++;
                Collections.swap(products, i, j);
            }
        }
        Collections.swap(products, i + 1, high);
        return i + 1;
    }

    /**
     * Sort products by price using MergeSort algorithm
     */
    public List<Product> sortProductsByPrice(List<Product> products, boolean ascending) {
        if (products == null || products.isEmpty()) {
            return new ArrayList<>();
        }
        List<Product> sortedList = new ArrayList<>(products);
        mergeSortByPrice(sortedList, ascending);
        return sortedList;
    }

    private void mergeSortByPrice(List<Product> products, boolean ascending) {
        if (products.size() < 2)
            return;

        int mid = products.size() / 2;
        List<Product> left = new ArrayList<>(products.subList(0, mid));
        List<Product> right = new ArrayList<>(products.subList(mid, products.size()));

        mergeSortByPrice(left, ascending);
        mergeSortByPrice(right, ascending);

        merge(products, left, right, ascending);
    }

    private void merge(List<Product> products, List<Product> left, List<Product> right, boolean ascending) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            boolean condition = ascending ? left.get(i).getPrice().compareTo(right.get(j).getPrice()) <= 0
                    : left.get(i).getPrice().compareTo(right.get(j).getPrice()) >= 0;

            if (condition) {
                products.set(k++, left.get(i++));
            } else {
                products.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            products.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            products.set(k++, right.get(j++));
        }
    }

    /**
     * Binary search for product by ID (requires sorted list)
     */
    public Product binarySearchById(List<Product> products, int productId) {
        if (products == null || products.isEmpty()) {
            return null;
        }

        // Sort by ID first
        products.sort(Comparator.comparingInt(Product::getProductId));

        int left = 0;
        int right = products.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Product midProduct = products.get(mid);

            if (midProduct.getProductId() == productId) {
                return midProduct;
            } else if (midProduct.getProductId() < productId) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.findByCategory(categoryId);
    }

    /**
     * Get top purchased products by order frequency
     * Landing page feature - shows most popular products
     */
    public List<Product> getTopPurchasedProducts(int limit) {
        return productDAO.getTopPurchasedProducts(limit);
    }

    /**
     * Clear all caches
     */
    public void clearCache() {
        productCache.clear();
        searchCache.clear();
        println("All caches cleared");
    }

    /**
     * Get cache statistics
     */
    public String getCacheStats() {
        return String.format("Product Cache: %d items, Search Cache: %d queries",
                productCache.size(), searchCache.size());
    }
}
