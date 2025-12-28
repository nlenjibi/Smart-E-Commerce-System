package com.smartecommerce.service;

import com.smartcommerce.model.CartItem;
import com.smartcommerce.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import static com.smartcommerce.utils.AppUtils.printE;
import static com.smartcommerce.utils.AppUtils.println;

/**
 * CartService manages shopping cart operations
 * Implements Singleton pattern for session-based cart
 *
 * OPTIMIZATIONS:
 * - Thread-safe operations with synchronized methods
 * - Observer pattern for cart change notifications
 * - Defensive copying to prevent external modifications
 * - Better null safety and validation
 */
public class CartService {

    private static volatile CartService instance;
    private final List<CartItem> cartItems;
    private final List<Consumer<List<CartItem>>> observers;

    private CartService() {
        this.cartItems = Collections.synchronizedList(new ArrayList<>());
        this.observers = new CopyOnWriteArrayList<>();
    }

    /**
     * Get singleton instance with double-checked locking
     */
    public static CartService getInstance() {
        if (instance == null) {
            synchronized (CartService.class) {
                if (instance == null) {
                    instance = new CartService();
                }
            }
        }
        return instance;
    }

    /**
     * Reset instance for testing
     */
    public static void resetInstance() {
        synchronized (CartService.class) {
            instance = null;
        }
    }

    /**
     * Register observer for cart changes
     */
    public void addObserver(Consumer<List<CartItem>> observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    /**
     * Remove observer
     */
    public void removeObserver(Consumer<List<CartItem>> observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers of cart changes
     */
    private void notifyObservers() {
        List<CartItem> snapshot = getCartItems();
        for (Consumer<List<CartItem>> observer : observers) {
            try {
                observer.accept(snapshot);
            } catch (Exception e) {
                printE("Error notifying observer: " + e.getMessage());
            }
        }
    }

    /**
     * Add product to cart
     * If product already exists, increment quantity
     */
    public synchronized void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        // Check if product already in cart
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getProductId() == product.getProductId())
                .findFirst();

        if (existingItem.isPresent()) {
            // Increment quantity
            existingItem.get().incrementQuantity();
            println("Incremented quantity for: " + product.getProductName());
        } else {
            // Add new item
            cartItems.add(new CartItem(product, 1));
            println("Added to cart: " + product.getProductName());
        }

        notifyObservers();
    }

    /**
     * Remove product from cart
     */
    public synchronized boolean removeProduct(int productId) {
        boolean removed = cartItems.removeIf(item -> item.getProduct().getProductId() == productId);
        if (removed) {
            notifyObservers();
        }
        return removed;
    }

    /**
     * Update quantity for a product
     */
    public synchronized void updateQuantity(int productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (quantity == 0) {
            removeProduct(productId);
            return;
        }

        cartItems.stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    notifyObservers();
                });
    }

    /**
     * Get all cart items (defensive copy)
     */
    public List<CartItem> getCartItems() {
        synchronized (cartItems) {
            return new ArrayList<>(cartItems);
        }
    }

    /**
     * Get total number of items in cart
     */
    public synchronized int getTotalItems() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Get cart total price
     */
    public synchronized BigDecimal getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Clear all items from cart
     */
    public synchronized void clearCart() {
        cartItems.clear();
        notifyObservers();
        println("Cart cleared");
    }

    /**
     * Check if cart is empty
     */
    public synchronized boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Get number of unique products in cart
     */
    public synchronized int getUniqueProductCount() {
        return cartItems.size();
    }

    /**
     * Get cart item by product ID
     */
    public synchronized Optional<CartItem> getCartItem(int productId) {
        return cartItems.stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst()
                .map(item -> new CartItem(item.getProduct(), item.getQuantity()));
    }

    /**
     * Check if product is in cart
     */
    public synchronized boolean containsProduct(int productId) {
        return cartItems.stream()
                .anyMatch(item -> item.getProduct().getProductId() == productId);
    }
}
