package com.smartecommerce.service;

import com.smartecommerce.dao.ProductDAO;
import com.smartecommerce.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    static {
        System.setProperty("net.bytebuddy.experimental", "true");
    }

    @Mock
    private ProductDAO productDAO;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDAO);
    }

    @Test
    void testAddProduct() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        when(productDAO.create(product)).thenReturn(true);

        boolean result = productService.addProduct(product);

        assertTrue(result);
        verify(productDAO).create(product);
    }

    @Test
    void testAddProductFailure() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        when(productDAO.create(product)).thenReturn(false);

        boolean result = productService.addProduct(product);

        assertFalse(result);
        verify(productDAO).create(product);
    }

    @Test
    void testGetProductByIdCacheMiss() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        when(productDAO.findById(1)).thenReturn(product);

        Product result = productService.getProductById(1);

        assertEquals(product, result);
        verify(productDAO).findById(1);
    }

    @Test
    void testGetProductByIdCacheHit() {
        Product product = new Product(1, "Test Product", "Description", BigDecimal.valueOf(10.0), 1, 10);
        // First call to populate cache
        when(productDAO.findById(1)).thenReturn(product);
        productService.getProductById(1);

        // Second call should use cache
        Product result = productService.getProductById(1);

        assertEquals(product, result);
        verify(productDAO, times(1)).findById(1); // Only called once
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(2, "Product2", "Desc", BigDecimal.valueOf(20.0), 1, 10)
        );
        when(productDAO.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(products, result);
        verify(productDAO).findAll();
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(1, "Updated Product", "Description", BigDecimal.valueOf(15.0), 1, 10);
        when(productDAO.update(product)).thenReturn(true);

        boolean result = productService.updateProduct(product);

        assertTrue(result);
        verify(productDAO).update(product);
    }

    @Test
    void testDeleteProduct() {
        when(productDAO.delete(1)).thenReturn(true);

        boolean result = productService.deleteProduct(1);

        assertTrue(result);
        verify(productDAO).delete(1);
    }

    @Test
    void testSearchProducts() {
        List<Product> products = Arrays.asList(
            new Product(1, "Test Product", "Desc", BigDecimal.valueOf(10.0), 1, 10)
        );
        when(productDAO.searchByName("test")).thenReturn(products);

        List<Product> result = productService.searchProducts("test");

        assertEquals(products, result);
        verify(productDAO).searchByName("test");
    }

    @Test
    void testSearchProductsCacheHit() {
        List<Product> products = Arrays.asList(
            new Product(1, "Test Product", "Desc", BigDecimal.valueOf(10.0), 1, 10)
        );
        when(productDAO.searchByName("test")).thenReturn(products);

        // First search
        productService.searchProducts("test");
        // Second search should use cache
        List<Product> result = productService.searchProducts("test");

        assertEquals(products, result);
        verify(productDAO, times(1)).searchByName("test");
    }

    @Test
    void testSearchProductsEmptyTerm() {
        List<Product> result = productService.searchProducts("");

        assertTrue(result.isEmpty());
        verifyNoInteractions(productDAO);
    }

    @Test
    void testGetProductsByCategory() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10)
        );
        when(productDAO.findByCategory(1)).thenReturn(products);

        List<Product> result = productService.getProductsByCategory(1);

        assertEquals(products, result);
        verify(productDAO).findByCategory(1);
    }

    @Test
    void testGetTopPurchasedProducts() {
        List<Product> products = Arrays.asList(
            new Product(1, "Top Product", "Desc", BigDecimal.valueOf(10.0), 1, 10)
        );
        when(productDAO.getTopPurchasedProducts(5)).thenReturn(products);

        List<Product> result = productService.getTopPurchasedProducts(5);

        assertEquals(products, result);
        verify(productDAO).getTopPurchasedProducts(5);
    }

    @Test
    void testSortProductsByName() {
        List<Product> products = Arrays.asList(
            new Product(1, "Z Product", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(2, "A Product", "Desc", BigDecimal.valueOf(20.0), 1, 10)
        );

        List<Product> sorted = productService.sortProductsByName(products, true);

        assertEquals("A Product", sorted.get(0).getProductName());
        assertEquals("Z Product", sorted.get(1).getProductName());
    }

    @Test
    void testSortProductsByNameDescending() {
        List<Product> products = Arrays.asList(
            new Product(1, "A Product", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(2, "Z Product", "Desc", BigDecimal.valueOf(20.0), 1, 10)
        );

        List<Product> sorted = productService.sortProductsByName(products, false);

        assertEquals("Z Product", sorted.get(0).getProductName());
        assertEquals("A Product", sorted.get(1).getProductName());
    }

    @Test
    void testSortProductsByPrice() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(30.0), 1, 10),
            new Product(2, "Product2", "Desc", BigDecimal.valueOf(10.0), 1, 10)
        );

        List<Product> sorted = productService.sortProductsByPrice(products, true);

        assertEquals(BigDecimal.valueOf(10.0), sorted.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(30.0), sorted.get(1).getPrice());
    }

    @Test
    void testSortProductsByPriceDescending() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(2, "Product2", "Desc", BigDecimal.valueOf(30.0), 1, 10)
        );

        List<Product> sorted = productService.sortProductsByPrice(products, false);

        assertEquals(BigDecimal.valueOf(30.0), sorted.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(10.0), sorted.get(1).getPrice());
    }

    @Test
    void testBinarySearchById() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(2, "Product2", "Desc", BigDecimal.valueOf(20.0), 1, 10),
            new Product(3, "Product3", "Desc", BigDecimal.valueOf(30.0), 1, 10)
        );

        Product found = productService.binarySearchById(products, 2);

        assertNotNull(found);
        assertEquals(2, found.getProductId());
    }

    @Test
    void testBinarySearchByIdNotFound() {
        List<Product> products = Arrays.asList(
            new Product(1, "Product1", "Desc", BigDecimal.valueOf(10.0), 1, 10),
            new Product(3, "Product3", "Desc", BigDecimal.valueOf(30.0), 1, 10)
        );

        Product found = productService.binarySearchById(products, 2);

        assertNull(found);
    }

    @Test
    void testSortProductsByNameEmptyList() {
        List<Product> products = new ArrayList<>();

        List<Product> sorted = productService.sortProductsByName(products, true);

        assertTrue(sorted.isEmpty());
    }

    @Test
    void testSortProductsByNameNullList() {
        List<Product> sorted = productService.sortProductsByName(null, true);

        assertTrue(sorted.isEmpty());
    }

    @Test
    void testSortProductsByPriceEmptyList() {
        List<Product> products = new ArrayList<>();

        List<Product> sorted = productService.sortProductsByPrice(products, true);

        assertTrue(sorted.isEmpty());
    }

    @Test
    void testSortProductsByPriceNullList() {
        List<Product> sorted = productService.sortProductsByPrice(null, true);

        assertTrue(sorted.isEmpty());
    }

    @Test
    void testBinarySearchByIdEmptyList() {
        List<Product> products = new ArrayList<>();

        Product found = productService.binarySearchById(products, 1);

        assertNull(found);
    }

    @Test
    void testBinarySearchByIdNullList() {
        Product found = productService.binarySearchById(null, 1);

        assertNull(found);
    }

    @Test
    void testClearCache() {
        // Populate cache
        Product product = new Product(1, "Test", "Desc", BigDecimal.valueOf(10.0), 1, 10);
        when(productDAO.findById(1)).thenReturn(product);
        productService.getProductById(1);

        productService.clearCache();

        // Cache should be cleared, next call will hit DAO again
        productService.getProductById(1);
        verify(productDAO, times(2)).findById(1);
    }

    @Test
    void testGetCacheStats() {
        String stats = productService.getCacheStats();
        assertNotNull(stats);
        assertTrue(stats.contains("Product Cache"));
        assertTrue(stats.contains("Search Cache"));
    }
}
