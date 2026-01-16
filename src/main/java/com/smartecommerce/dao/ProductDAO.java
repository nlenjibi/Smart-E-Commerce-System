package com.smartecommerce.dao;

import com.smartecommerce.models.Product;
import com.smartecommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartecommerce.utils.AppUtils.*;
import static com.smartecommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * ProductDAO handles all database operations for Product entity
 * Implements CRUD operations and search functionality
 */
public class ProductDAO {

    /**
     * Create a new product
     */
    public boolean create(Product product) {
        String sql = "INSERT INTO Products (product_name, description, price, category_id, stock_quantity, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        QueryResult insertResult = executePreparedQuery(
                sql,
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getStockQuantity(),
                product.getImageUrl()); // Can be null

        if (insertResult.hasError()) {
            printE("Error creating product: " + insertResult.getError());
            return false;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            product.setProductId(generatedId.intValue());
            return true;
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Read product by ID
     */
    public Product findById(int productId) {
        String sql = "SELECT p.*, c.category_name FROM Products p " +
                "JOIN Categories c ON p.category_id = c.category_id " +
                "WHERE p.product_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, productId);

        if (queryResult.hasError()) {
            printE("Error finding product: " + queryResult.getError());
            return null;
        }

        return mapSingleProduct(queryResult);
    }

    /**
     * Get all products
     */
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name FROM Products p " +
                "JOIN Categories c ON p.category_id = c.category_id " +
                "ORDER BY p.product_name";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error retrieving products: " + queryResult.getError());
            return products;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return products;
        }

        for (Map<String, Object> row : rows) {
            Product mappedProduct = mapRow(row);
            if (mappedProduct != null) {
                products.add(mappedProduct);
            }
        }
        return products;
    }

    /**
     * Update product
     */
    public boolean update(Product product) {
        String sql = "UPDATE Products SET product_name = ?, description = ?, price = ?, category_id = ?, stock_quantity = ?, image_url = ? " +
                "WHERE product_id = ?";
        QueryResult updateResult = executePreparedQuery(
                sql,
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getStockQuantity(),
                product.getImageUrl(), // Can be null
                product.getProductId());

        if (updateResult.hasError()) {
            printE("Error updating product: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Delete product
     */
    public boolean delete(int productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        QueryResult deleteResult = executePreparedQuery(sql, productId);

        if (deleteResult.hasError()) {
            printE("Error deleting product: " + deleteResult.getError());
            return false;
        }

        Integer affectedRows = deleteResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Search products by name (case-insensitive)
     */
    public List<Product> searchByName(String searchTerm) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name FROM Products p " +
                "JOIN Categories c ON p.category_id = c.category_id " +
                "WHERE LOWER(p.product_name) LIKE LOWER(?) " +
                "OR LOWER(p.description) LIKE LOWER(?) " +
                "ORDER BY p.product_name";
        String likePattern = "%" + searchTerm + "%";
        QueryResult queryResult = executePreparedQuery(sql, likePattern, likePattern);

        if (queryResult.hasError()) {
            printE("Error searching products: " + queryResult.getError());
            return products;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return products;
        }

        for (Map<String, Object> row : rows) {
            Product mappedProduct = mapRow(row);
            if (mappedProduct != null) {
                products.add(mappedProduct);
            }
        }
        return products;
    }

    /**
     * Find products by category
     */
    public List<Product> findByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name FROM Products p " +
                "JOIN Categories c ON p.category_id = c.category_id " +
                "WHERE p.category_id = ? " +
                "ORDER BY p.product_name";
        QueryResult queryResult = executePreparedQuery(sql, categoryId);

        if (queryResult.hasError()) {
            printE("Error finding products by category: " + queryResult.getError());
            return products;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return products;
        }

        for (Map<String, Object> row : rows) {
            Product mappedProduct = mapRow(row);
            if (mappedProduct != null) {
                products.add(mappedProduct);
            }
        }
        return products;
    }

    /**
     * Get total product count
     * Analytics function
     */
    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) as count FROM Products";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error counting products: " + queryResult.getError());
            return 0;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asInt(rows.get(0).get("count"));
        }
        return 0;
    }

    /**
     * Get product count by category
     * Analytics function
     */
    public Map<String, Integer> getProductCountByCategory() {
        Map<String, Integer> categoryCounts = new java.util.HashMap<>();
        String sql = "SELECT c.category_name, COUNT(*) as count FROM Products p " +
                     "JOIN Categories c ON p.category_id = c.category_id " +
                     "GROUP BY c.category_name";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error getting product count by category: " + queryResult.getError());
            return categoryCounts;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null) {
            for (Map<String, Object> row : rows) {
                String categoryName = asString(row.get("category_name"));
                int count = asInt(row.get("count"));
                categoryCounts.put(categoryName, count);
            }
        }
        return categoryCounts;
    }

    /**
     * Get low stock products (below threshold)
     * Analytics function
     */
    public List<Product> getLowStockProducts(int threshold) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name FROM Products p " +
                     "JOIN Categories c ON p.category_id = c.category_id " +
                     "WHERE p.stock_quantity <= ? ORDER BY p.stock_quantity ASC";
        QueryResult queryResult = executePreparedQuery(sql, threshold);

        if (queryResult.hasError()) {
            printE("Error finding low stock products: " + queryResult.getError());
            return products;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return products;
        }

        for (Map<String, Object> row : rows) {
            Product mappedProduct = mapRow(row);
            if (mappedProduct != null) {
                products.add(mappedProduct);
            }
        }
        return products;
    }

    /**
     * Get out of stock products
     * Analytics function
     */
    public List<Product> getOutOfStockProducts() {
        return getLowStockProducts(0);
    }

    /**
     * Get total inventory value
     * Analytics function - calculates total value of all products in stock
     */
    public java.math.BigDecimal getTotalInventoryValue() {
        String sql = "SELECT ISNULL(SUM(price * stock_quantity), 0) as total_value FROM Products";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error calculating total inventory value: " + queryResult.getError());
            return java.math.BigDecimal.ZERO;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asBigDecimal(rows.get(0).get("total_value"));
        }
        return java.math.BigDecimal.ZERO;
    }

    /**
     * Get average product price
     * Analytics function
     */
    public java.math.BigDecimal getAverageProductPrice() {
        String sql = "SELECT ISNULL(AVG(price), 0) as avg_price FROM Products";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error calculating average product price: " + queryResult.getError());
            return java.math.BigDecimal.ZERO;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows != null && !rows.isEmpty()) {
            return asBigDecimal(rows.get(0).get("avg_price"));
        }
        return java.math.BigDecimal.ZERO;
    }



    /**
     * Get recently added products
     * Analytics function
     */
    public List<Product> getRecentProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name FROM Products p " +
                     "JOIN Categories c ON p.category_id = c.category_id " +
                     "ORDER BY p.created_at DESC LIMIT ?";
        QueryResult queryResult = executePreparedQuery(sql, limit);

        if (queryResult.hasError()) {
            printE("Error getting recent products: " + queryResult.getError());
            return products;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return products;
        }

        for (Map<String, Object> row : rows) {
            Product mappedProduct = mapRow(row);
            if (mappedProduct != null) {
                products.add(mappedProduct);
            }
        }
        return products;
    }

    /**
     * Get top purchased products by order frequency
     * Landing page function - shows most popular products
     */
    public List<Product> getTopPurchasedProducts(int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name, COUNT(DISTINCT oi.order_id) as purchase_count " +
                     "FROM Products p " +
                     "JOIN Categories c ON p.category_id = c.category_id " +
                     "JOIN OrderItems oi ON p.product_id = oi.product_id " +
                     "GROUP BY p.product_id, p.product_name, p.description, p.price, " +
                     "p.category_id, p.stock_quantity, p.image_url, p.created_at, p.updated_at, c.category_name " +
                     "ORDER BY purchase_count DESC LIMIT ?";
        QueryResult queryResult = executePreparedQuery(sql, limit);

        if (queryResult.hasError()) {
            printE("Error getting top purchased products: " + queryResult.getError());
            return products;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return products;
        }

        for (Map<String, Object> row : rows) {
            Product mappedProduct = mapRow(row);
            if (mappedProduct != null) {
                products.add(mappedProduct);
            }
        }
        return products;
    }

    /**
     * Map the first row from a query result to a Product
     */
    private Product mapSingleProduct(QueryResult queryResult) {
        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        return mapRow(rows.get(0));
    }

    /**
     * Convert a row map into a Product instance
     */
    private Product mapRow(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        Product product = new Product();
        product.setProductId(asInt(row.get("product_id")));
        product.setProductName(asString(row.get("product_name")));
        product.setDescription(asString(row.get("description")));
        product.setPrice(asBigDecimal(row.get("price")));
        product.setCategoryId(asInt(row.get("category_id")));
        product.setCategoryName(asString(row.get("category_name")));
        product.setStockQuantity(asInt(row.get("stock_quantity")));
        product.setImageUrl(asString(row.get("image_url"))); // Can be null
        product.setCreatedAt(asLocalDateTime(row.get("created_at")));
        product.setUpdatedAt(asLocalDateTime(row.get("updated_at")));
        return product;
    }

}
