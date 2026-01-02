package com.smartecommerce.service;

import com.smartecommerce.models.CartItem;
import com.smartecommerce.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    static {
        // Allow Byte Buddy to instrument on Java 25 for Mockito
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    private CartService cartService;

    @Mock
    private Consumer<List<CartItem>> observer;

    @BeforeEach
    void setUp() {
        CartService.resetInstance();
        cartService = CartService.getInstance();
    }

    @AfterEach
    void tearDown() {
        cartService.clearCart();
    }

    @Test
    void testSingletonInstance() {
        CartService instance1 = CartService.getInstance();
        CartService instance2 = CartService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testAddProduct() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);

        cartService.addProduct(product);

        assertEquals(1, cartService.getTotalItems());
        assertEquals(BigDecimal.valueOf(10.0), cartService.getTotalPrice());
        assertTrue(cartService.containsProduct(1));
    }

    @Test
    void testAddProductNull() {
        assertThrows(IllegalArgumentException.class, () -> cartService.addProduct(null));
    }

    @Test
    void testAddProductIncrementQuantity() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);

        cartService.addProduct(product);
        cartService.addProduct(product);

        assertEquals(2, cartService.getTotalItems());
        assertEquals(BigDecimal.valueOf(20.0), cartService.getTotalPrice());
        assertEquals(1, cartService.getUniqueProductCount());
    }

    @Test
    void testRemoveProduct() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        boolean removed = cartService.removeProduct(1);

        assertTrue(removed);
        assertEquals(0, cartService.getTotalItems());
        assertFalse(cartService.containsProduct(1));
    }

    @Test
    void testRemoveProductNotFound() {
        boolean removed = cartService.removeProduct(1);

        assertFalse(removed);
    }

    @Test
    void testUpdateQuantity() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        cartService.updateQuantity(1, 3);

        assertEquals(3, cartService.getTotalItems());
        assertEquals(BigDecimal.valueOf(30.0), cartService.getTotalPrice());
    }

    @Test
    void testUpdateQuantityZero() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        cartService.updateQuantity(1, 0);

        assertEquals(0, cartService.getTotalItems());
        assertFalse(cartService.containsProduct(1));
    }

    @Test
    void testUpdateQuantityNegative() {
        assertThrows(IllegalArgumentException.class, () -> cartService.updateQuantity(1, -1));
    }

    @Test
    void testGetCartItems() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        List<CartItem> items = cartService.getCartItems();

        assertEquals(1, items.size());
        assertEquals(product, items.get(0).getProduct());
        assertEquals(1, items.get(0).getQuantity());
    }

    @Test
    void testGetCartItemsDefensiveCopy() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        List<CartItem> items = cartService.getCartItems();
        items.clear(); // Modify the returned list

        // Original cart should not be affected
        assertEquals(1, cartService.getTotalItems());
    }

    @Test
    void testGetTotalItems() {
        Product product1 = new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10);
        Product product2 = new Product(2, "Product2", "Desc", BigDecimal.valueOf(20.0), 1, 10);
        cartService.addProduct(product1);
        cartService.addProduct(product2);
        cartService.addProduct(product2); // Add again to increment

        assertEquals(3, cartService.getTotalItems());
    }

    @Test
    void testGetTotalPrice() {
        Product product1 = new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10);
        Product product2 = new Product(2, "Product2", "Desc", BigDecimal.valueOf(20.0), 1, 10);
        cartService.addProduct(product1);
        cartService.addProduct(product2);
        cartService.addProduct(product2);

        assertEquals(BigDecimal.valueOf(50.0), cartService.getTotalPrice());
    }

    @Test
    void testClearCart() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        cartService.clearCart();

        assertEquals(0, cartService.getTotalItems());
        assertTrue(cartService.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(cartService.isEmpty());

        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        assertFalse(cartService.isEmpty());
    }

    @Test
    void testGetUniqueProductCount() {
        Product product1 = new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10);
        Product product2 = new Product(2, "Product2", "Desc", BigDecimal.valueOf(20.0), 1, 10);
        cartService.addProduct(product1);
        cartService.addProduct(product2);

        assertEquals(2, cartService.getUniqueProductCount());
    }

    @Test
    void testGetCartItem() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        Optional<CartItem> item = cartService.getCartItem(1);

        assertTrue(item.isPresent());
        assertEquals(product, item.get().getProduct());
        assertEquals(1, item.get().getQuantity());
    }

    @Test
    void testGetCartItemNotFound() {
        Optional<CartItem> item = cartService.getCartItem(1);

        assertFalse(item.isPresent());
    }

    @Test
    void testContainsProduct() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        assertTrue(cartService.containsProduct(1));
        assertFalse(cartService.containsProduct(2));
    }

    @Test
    void testObserverNotification() {
        cartService.addObserver(observer);

        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        verify(observer, times(1)).accept(anyList());
    }

    @Test
    void testRemoveObserver() {
        cartService.addObserver(observer);
        cartService.removeObserver(observer);

        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        cartService.addProduct(product);

        verify(observer, never()).accept(anyList());
    }
}
