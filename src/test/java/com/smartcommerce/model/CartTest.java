package com.smartcommerce.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testDefaultConstructor() {
        Cart cart = new Cart();

        assertEquals(0, cart.getCartId());
        assertEquals(0, cart.getUserId());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalItems());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    void testConstructorWithParameters() {
        Cart cart = new Cart(1, 100);

        assertEquals(1, cart.getCartId());
        assertEquals(100, cart.getUserId());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        Cart cart = new Cart();
        LocalDateTime now = LocalDateTime.now();

        cart.setCartId(5);
        cart.setUserId(200);
        cart.setCreatedAt(now);
        cart.setUpdatedAt(now);

        assertEquals(5, cart.getCartId());
        assertEquals(200, cart.getUserId());
        assertEquals(now, cart.getCreatedAt());
        assertEquals(now, cart.getUpdatedAt());
    }

    @Test
    void testAddItem() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        CartItem item = new CartItem(product, 2);

        cart.addItem(item);

        assertEquals(1, cart.getUniqueProductCount());
        assertEquals(2, cart.getTotalItems());
        assertEquals(BigDecimal.valueOf(20.0), cart.getTotalPrice());
        assertFalse(cart.isEmpty());
        assertTrue(cart.containsProduct(1));
    }

    @Test
    void testAddItemIncrementQuantity() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        CartItem item1 = new CartItem(product, 2);
        CartItem item2 = new CartItem(product, 3);

        cart.addItem(item1);
        cart.addItem(item2);

        assertEquals(1, cart.getUniqueProductCount());
        assertEquals(5, cart.getTotalItems());
        assertEquals(BigDecimal.valueOf(50.0), cart.getTotalPrice());
    }

    @Test
    void testAddItemNull() {
        Cart cart = new Cart();

        cart.addItem(null);

        assertTrue(cart.isEmpty());
    }

    @Test
    void testRemoveItem() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 2));

        boolean removed = cart.removeItem(1);

        assertTrue(removed);
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalItems());
    }

    @Test
    void testRemoveItemNotFound() {
        Cart cart = new Cart();

        boolean removed = cart.removeItem(1);

        assertFalse(removed);
    }

    @Test
    void testUpdateItemQuantity() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 2));

        cart.updateItemQuantity(1, 5);

        assertEquals(5, cart.getTotalItems());
        assertEquals(BigDecimal.valueOf(50.0), cart.getTotalPrice());
    }

    @Test
    void testUpdateItemQuantityZero() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 2));

        cart.updateItemQuantity(1, 0);

        assertTrue(cart.isEmpty());
        assertFalse(cart.containsProduct(1));
    }

    @Test
    void testUpdateItemQuantityNegative() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 2));

        cart.updateItemQuantity(1, -1);

        assertTrue(cart.isEmpty());
    }

    @Test
    void testClearCart() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 2));

        cart.clearCart();

        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalItems());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    void testGetItem() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        CartItem item = new CartItem(product, 2);
        cart.addItem(item);

        CartItem found = cart.getItem(1);

        assertNotNull(found);
        assertEquals(product, found.getProduct());
        assertEquals(2, found.getQuantity());
    }

    @Test
    void testGetItemNotFound() {
        Cart cart = new Cart();

        CartItem found = cart.getItem(1);

        assertNull(found);
    }

    @Test
    void testContainsProduct() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 1));

        assertTrue(cart.containsProduct(1));
        assertFalse(cart.containsProduct(2));
    }

    @Test
    void testGetItemsDefensiveCopy() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 1));

        List<CartItem> items = cart.getItems();
        items.clear(); // Modify the returned list

        // Original cart should not be affected
        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getUniqueProductCount());
    }

    @Test
    void testSetItems() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        List<CartItem> newItems = List.of(new CartItem(product, 3));

        cart.setItems(newItems);

        assertEquals(1, cart.getUniqueProductCount());
        assertEquals(3, cart.getTotalItems());
    }

    @Test
    void testSetItemsNull() {
        Cart cart = new Cart();
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 1));

        cart.setItems(null);

        assertTrue(cart.isEmpty());
    }

    @Test
    void testMultipleItems() {
        Cart cart = new Cart();
        Product product1 = new Product(1, "Product1", "Desc1", BigDecimal.valueOf(10.0), 1, 10);
        Product product2 = new Product(2, "Product2", "Desc2", BigDecimal.valueOf(20.0), 1, 10);

        cart.addItem(new CartItem(product1, 2));
        cart.addItem(new CartItem(product2, 1));

        assertEquals(2, cart.getUniqueProductCount());
        assertEquals(3, cart.getTotalItems());
        assertEquals(BigDecimal.valueOf(40.0), cart.getTotalPrice());
    }

    @Test
    void testEquals() {
        Cart cart1 = new Cart(1, 100);
        Cart cart2 = new Cart(1, 200);
        Cart cart3 = new Cart(2, 100);

        assertEquals(cart1, cart2); // Same ID
        assertNotEquals(cart1, cart3); // Different ID
        assertNotEquals(cart1, null);
        assertNotEquals(cart1, "not a cart");
    }

    @Test
    void testHashCode() {
        Cart cart1 = new Cart(1, 100);
        Cart cart2 = new Cart(1, 200);
        Cart cart3 = new Cart(2, 100);

        assertEquals(cart1.hashCode(), cart2.hashCode()); // Same ID
        assertNotEquals(cart1.hashCode(), cart3.hashCode()); // Different ID
    }

    @Test
    void testToString() {
        Cart cart = new Cart(1, 100);
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cart.addItem(new CartItem(product, 2));

        String toString = cart.toString();

        assertTrue(toString.contains("Cart{"));
        assertTrue(toString.contains("cartId=1"));
        assertTrue(toString.contains("userId=100"));
        assertTrue(toString.contains("items=1"));
        assertTrue(toString.contains("totalItems=2"));
        assertTrue(toString.contains("totalPrice=20.0"));
        assertTrue(toString.contains("createdAt="));
        assertTrue(toString.contains("updatedAt="));
        assertTrue(toString.contains("}"));
    }

    @Test
    void testToStringEmptyCart() {
        Cart cart = new Cart(1, 100);

        String toString = cart.toString();

        assertTrue(toString.contains("items=0"));
        assertTrue(toString.contains("totalItems=0"));
        assertTrue(toString.contains("totalPrice=0"));
    }
}
