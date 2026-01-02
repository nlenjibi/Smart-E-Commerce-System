package com.smartcommerce.service;

import com.smartcommerce.dao.OrderDAO;
import com.smartcommerce.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderDAO);
    }

    @Test
    void testCreateOrder() {
        Order order = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        when(orderDAO.create(order)).thenReturn(true);

        boolean result = orderService.createOrder(order);

        assertTrue(result);
        verify(orderDAO).create(order);
    }

    @Test
    void testCreateOrderFailure() {
        Order order = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        when(orderDAO.create(order)).thenReturn(false);

        boolean result = orderService.createOrder(order);

        assertFalse(result);
        verify(orderDAO).create(order);
    }

    @Test
    void testGetOrderById() {
        Order order = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        when(orderDAO.findById(1)).thenReturn(order);

        Order result = orderService.getOrderById(1);

        assertEquals(order, result);
        verify(orderDAO).findById(1);
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderDAO.findById(1)).thenReturn(null);

        Order result = orderService.getOrderById(1);

        assertNull(result);
        verify(orderDAO).findById(1);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = Arrays.asList(
            new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING"),
            new Order(2, 2, BigDecimal.valueOf(200.0), "CONFIRMED")
        );
        when(orderDAO.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(orders, result);
        verify(orderDAO).findAll();
    }

    @Test
    void testGetAllOrdersNull() {
        when(orderDAO.findAll()).thenReturn(null);

        List<Order> result = orderService.getAllOrders();

        assertTrue(result.isEmpty());
        verify(orderDAO).findAll();
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderDAO.updateStatus(1, "CONFIRMED")).thenReturn(true);

        boolean result = orderService.updateOrderStatus(1, "CONFIRMED");

        assertTrue(result);
        verify(orderDAO).updateStatus(1, "CONFIRMED");
    }

    @Test
    void testUpdateOrderStatusFailure() {
        when(orderDAO.updateStatus(1, "CONFIRMED")).thenReturn(false);

        boolean result = orderService.updateOrderStatus(1, "CONFIRMED");

        assertFalse(result);
        verify(orderDAO).updateStatus(1, "CONFIRMED");
    }

    @Test
    void testGetOrdersByUser() {
        List<Order> orders = Arrays.asList(
            new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING")
        );
        when(orderDAO.findByUserId(1)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUser(1);

        assertEquals(orders, result);
        verify(orderDAO).findByUserId(1);
    }

    @Test
    void testGetOrdersByUserNull() {
        when(orderDAO.findByUserId(1)).thenReturn(null);

        List<Order> result = orderService.getOrdersByUser(1);

        assertTrue(result.isEmpty());
        verify(orderDAO).findByUserId(1);
    }

    @Test
    void testFilterByStatus() {
        List<Order> orders = Arrays.asList(
            new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING"),
            new Order(2, 2, BigDecimal.valueOf(200.0), "CONFIRMED"),
            new Order(3, 3, BigDecimal.valueOf(300.0), "PENDING")
        );
        when(orderDAO.findAll()).thenReturn(orders);

        List<Order> result = orderService.filterByStatus("PENDING");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(o -> "PENDING".equals(o.getStatus())));
        verify(orderDAO).findAll();
    }

    @Test
    void testFilterByStatusEmpty() {
        List<Order> result = orderService.filterByStatus("");

        assertTrue(result.isEmpty());
        verifyNoInteractions(orderDAO);
    }

    @Test
    void testFilterByStatusNull() {
        List<Order> result = orderService.filterByStatus(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(orderDAO);
    }

    @Test
    void testSortByDateAscending() {
        LocalDateTime now = LocalDateTime.now();
        Order order1 = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        order1.setOrderDate(now.minusDays(1));
        Order order2 = new Order(2, 2, BigDecimal.valueOf(200.0), "CONFIRMED");
        order2.setOrderDate(now);
        List<Order> orders = Arrays.asList(order2, order1);

        List<Order> result = orderService.sortByDate(orders, true);

        assertEquals(order1, result.get(0));
        assertEquals(order2, result.get(1));
    }

    @Test
    void testSortByDateDescending() {
        LocalDateTime now = LocalDateTime.now();
        Order order1 = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        order1.setOrderDate(now.minusDays(1));
        Order order2 = new Order(2, 2, BigDecimal.valueOf(200.0), "CONFIRMED");
        order2.setOrderDate(now);
        List<Order> orders = Arrays.asList(order1, order2);

        List<Order> result = orderService.sortByDate(orders, false);

        assertEquals(order2, result.get(0));
        assertEquals(order1, result.get(1));
    }

    @Test
    void testSortByDateNullList() {
        List<Order> result = orderService.sortByDate(null, true);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSortByDateEmptyList() {
        List<Order> orders = new ArrayList<>();

        List<Order> result = orderService.sortByDate(orders, true);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSortByAmountAscending() {
        Order order1 = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        Order order2 = new Order(2, 2, BigDecimal.valueOf(200.0), "CONFIRMED");
        List<Order> orders = Arrays.asList(order2, order1);

        List<Order> result = orderService.sortByAmount(orders, true);

        assertEquals(order1, result.get(0));
        assertEquals(order2, result.get(1));
    }

    @Test
    void testSortByAmountDescending() {
        Order order1 = new Order(1, 1, BigDecimal.valueOf(100.0), "PENDING");
        Order order2 = new Order(2, 2, BigDecimal.valueOf(200.0), "CONFIRMED");
        List<Order> orders = Arrays.asList(order1, order2);

        List<Order> result = orderService.sortByAmount(orders, false);

        assertEquals(order2, result.get(0));
        assertEquals(order1, result.get(1));
    }

    @Test
    void testSortByAmountNullList() {
        List<Order> result = orderService.sortByAmount(null, true);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSortByAmountEmptyList() {
        List<Order> orders = new ArrayList<>();

        List<Order> result = orderService.sortByAmount(orders, true);

        assertTrue(result.isEmpty());
    }
}
