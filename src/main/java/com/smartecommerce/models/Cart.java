package com.smartecommerce.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart entity representing a shopping cart
 * Contains cart items and cart-level information
 */
public class Cart {
    private int cartId;
    private int userId;
    private List<CartItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Cart() {
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cart(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items); // Defensive copy
    }

    public void setItems(List<CartItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business methods
    public void addItem(CartItem item) {
        if (item != null) {
            // Check if item already exists
            CartItem existing = items.stream()
                    .filter(i -> i.getProduct().getProductId() == item.getProduct().getProductId())
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
            } else {
                items.add(item);
            }
            this.updatedAt = LocalDateTime.now();
        }
    }

    public boolean removeItem(int productId) {
        boolean removed = items.removeIf(item -> item.getProduct().getProductId() == productId);
        if (removed) {
            this.updatedAt = LocalDateTime.now();
        }
        return removed;
    }

    public void updateItemQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }

        items.stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    this.updatedAt = LocalDateTime.now();
                });
    }

    public void clearCart() {
        items.clear();
        this.updatedAt = LocalDateTime.now();
    }

    // Computed properties
    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getUniqueProductCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public CartItem getItem(int productId) {
        return items.stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst()
                .orElse(null);
    }

    public boolean containsProduct(int productId) {
        return items.stream()
                .anyMatch(item -> item.getProduct().getProductId() == productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Cart cart = (Cart) obj;
        return cartId == cart.cartId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(cartId);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", items=" + items.size() +
                ", totalItems=" + getTotalItems() +
                ", totalPrice=" + getTotalPrice() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
