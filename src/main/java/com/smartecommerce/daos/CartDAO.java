package com.smartecommerce.daos;

import com.smartcommerce.model.Cart;
import com.smartcommerce.model.CartItem;
import com.smartcommerce.model.Product;
import com.smartcommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartcommerce.utils.AppUtils.*;
import static com.smartcommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * CartDAO handles all database operations for Cart entity
 * Manages cart persistence and cart items
 */
public class CartDAO {

    /**
     * Create a new cart for a user
     */
    public boolean create(Cart cart) {
        String sql = "INSERT INTO Carts (user_id) VALUES (?)";
        QueryResult insertResult = executePreparedQuery(sql, cart.getUserId());

        if (insertResult.hasError()) {
            printE("Error creating cart: " + insertResult.getError());
            return false;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            cart.setCartId(generatedId.intValue());
            return true;
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Find cart by ID
     */
    public Cart findById(int cartId) {
        String sql = "SELECT * FROM Carts WHERE cart_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, cartId);

        if (queryResult.hasError()) {
            printE("Error finding cart: " + queryResult.getError());
            return null;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        Cart cart = mapToCart(row);
        // Load cart items
        cart.setItems(findCartItems(cartId));
        return cart;
    }

    /**
     * Find cart by user ID
     */
    public Cart findByUserId(int userId) {
        String sql = "SELECT * FROM Carts WHERE user_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, userId);

        if (queryResult.hasError()) {
            printE("Error finding cart by user: " + queryResult.getError());
            return null;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows.isEmpty()) {
            return null;
        }

        Map<String, Object> row = rows.get(0);
        Cart cart = mapToCart(row);
        // Load cart items
        cart.setItems(findCartItems(cart.getCartId()));
        return cart;
    }

    /**
     * Update cart (mainly timestamps)
     */
    public boolean update(Cart cart) {
        String sql = "UPDATE Carts SET updated_at = CURRENT_TIMESTAMP WHERE cart_id = ?";
        QueryResult updateResult = executePreparedQuery(sql, cart.getCartId());

        if (updateResult.hasError()) {
            printE("Error updating cart: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Delete cart and all its items
     */
    public boolean delete(int cartId) {
        // First delete cart items
        String deleteItemsSql = "DELETE FROM CartItems WHERE cart_id = ?";
        executePreparedQuery(deleteItemsSql, cartId);

        // Then delete cart
        String deleteCartSql = "DELETE FROM Carts WHERE cart_id = ?";
        QueryResult deleteResult = executePreparedQuery(deleteCartSql, cartId);

        if (deleteResult.hasError()) {
            printE("Error deleting cart: " + deleteResult.getError());
            return false;
        }

        Integer affectedRows = deleteResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Add item to cart
     */
    public boolean addCartItem(int cartId, CartItem item) {
        // Check if item already exists
        String checkSql = "SELECT quantity FROM CartItems WHERE cart_id = ? AND product_id = ?";
        QueryResult checkResult = executePreparedQuery(checkSql, cartId, item.getProduct().getProductId());

        if (checkResult.hasError()) {
            printE("Error checking cart item: " + checkResult.getError());
            return false;
        }

        if (!checkResult.getResultSet().isEmpty()) {
            // Update existing item
            int existingQuantity = asInt(checkResult.getResultSet().get(0).get("quantity"));
            int newQuantity = existingQuantity + item.getQuantity();
            String updateSql = "UPDATE CartItems SET quantity = ? WHERE cart_id = ? AND product_id = ?";
            QueryResult updateResult = executePreparedQuery(updateSql, newQuantity, cartId, item.getProduct().getProductId());

            if (updateResult.hasError()) {
                printE("Error updating cart item: " + updateResult.getError());
                return false;
            }
        } else {
            // Insert new item
            String insertSql = "INSERT INTO CartItems (cart_id, product_id, quantity) VALUES (?, ?, ?)";
            QueryResult insertResult = executePreparedQuery(insertSql, cartId, item.getProduct().getProductId(), item.getQuantity());

            if (insertResult.hasError()) {
                printE("Error inserting cart item: " + insertResult.getError());
                return false;
            }
        }

        // Update cart timestamp
        updateCartTimestamp(cartId);
        return true;
    }

    /**
     * Remove item from cart
     */
    public boolean removeCartItem(int cartId, int productId) {
        String sql = "DELETE FROM CartItems WHERE cart_id = ? AND product_id = ?";
        QueryResult deleteResult = executePreparedQuery(sql, cartId, productId);

        if (deleteResult.hasError()) {
            printE("Error removing cart item: " + deleteResult.getError());
            return false;
        }

        // Update cart timestamp
        updateCartTimestamp(cartId);
        return true;
    }

    /**
     * Update item quantity in cart
     */
    public boolean updateCartItemQuantity(int cartId, int productId, int quantity) {
        if (quantity <= 0) {
            return removeCartItem(cartId, productId);
        }

        String sql = "UPDATE CartItems SET quantity = ? WHERE cart_id = ? AND product_id = ?";
        QueryResult updateResult = executePreparedQuery(sql, quantity, cartId, productId);

        if (updateResult.hasError()) {
            printE("Error updating cart item quantity: " + updateResult.getError());
            return false;
        }

        // Update cart timestamp
        updateCartTimestamp(cartId);
        return true;
    }

    /**
     * Clear all items from cart
     */
    public boolean clearCart(int cartId) {
        String sql = "DELETE FROM CartItems WHERE cart_id = ?";
        QueryResult deleteResult = executePreparedQuery(sql, cartId);

        if (deleteResult.hasError()) {
            printE("Error clearing cart: " + deleteResult.getError());
            return false;
        }

        // Update cart timestamp
        updateCartTimestamp(cartId);
        return true;
    }

    /**
     * Get cart items for a cart
     */
    private List<CartItem> findCartItems(int cartId) {
        String sql = "SELECT ci.*, p.product_name, p.description, p.price, p.image_url, p.category_id " +
                "FROM CartItems ci " +
                "JOIN Products p ON ci.product_id = p.product_id " +
                "WHERE ci.cart_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, cartId);

        if (queryResult.hasError()) {
            printE("Error finding cart items: " + queryResult.getError());
            return new ArrayList<>();
        }

        List<CartItem> items = new ArrayList<>();
        for (Map<String, Object> row : queryResult.getResultSet()) {
            CartItem item = mapToCartItem(row);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Update cart timestamp
     */
    private void updateCartTimestamp(int cartId) {
        String sql = "UPDATE Carts SET updated_at = CURRENT_TIMESTAMP WHERE cart_id = ?";
        executePreparedQuery(sql, cartId);
    }

    /**
     * Map database row to Cart object
     */
    private Cart mapToCart(Map<String, Object> row) {
        Cart cart = new Cart();
        cart.setCartId(asInt(row.get("cart_id")));
        cart.setUserId(asInt(row.get("user_id")));
        cart.setCreatedAt(asLocalDateTime(row.get("created_at")));
        cart.setUpdatedAt(asLocalDateTime(row.get("updated_at")));
        return cart;
    }

    /**
     * Map database row to CartItem object
     */
    private CartItem mapToCartItem(Map<String, Object> row) {
        try {
            Product product = new Product();
            product.setProductId(asInt(row.get("product_id")));
            product.setProductName(asString(row.get("product_name")));
            product.setDescription(asString(row.get("description")));
            product.setPrice(asBigDecimal(row.get("price")));
            product.setImageUrl(asString(row.get("image_url")));
            product.setCategoryId(asInt(row.get("category_id")));

            int quantity = asInt(row.get("quantity"));
            return new CartItem(product, quantity);
        } catch (Exception e) {
            printE("Error mapping cart item: " + e.getMessage());
            return null;
        }
    }
}
