package com.smartecommerce.service;

import com.smartecommerce.dao.OrderDAO;
import com.smartecommerce.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.smartecommerce.utils.AppUtils.*;

/**
 * OrderService provides business logic for order operations
 */
public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    // Constructor for testing with mock DAO
    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    public boolean createOrder(Order order) {
        return orderDAO.create(order);
    }

    public Order getOrderById(int orderId) {
        return orderDAO.findById(orderId);
    }

    public List<Order> getAllOrders() {
        List<Order> orders = orderDAO.findAll();
        return orders != null ? orders : new ArrayList<>();
    }

    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = orderDAO.findByUserId(userId);
        return orders != null ? orders : new ArrayList<>();
    }

    /**
     * Filter orders by status
     */
    public List<Order> filterByStatus(String status) {
        if (isEmpty(status)) {
            return new ArrayList<>();
        }
        return getAllOrders().stream()
                .filter(order -> order != null && order.getStatus() != null)
                .filter(order -> order.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    /**
     * Sort orders by date
     */
    public List<Order> sortByDate(List<Order> orders, boolean ascending) {
        if (orders == null || orders.isEmpty()) {
            return new ArrayList<>();
        }
        List<Order> sorted = new ArrayList<>(orders);
        sorted.sort((o1, o2) -> {
            if (o1.getOrderDate() == null || o2.getOrderDate() == null)
                return 0;
            return ascending ? o1.getOrderDate().compareTo(o2.getOrderDate())
                    : o2.getOrderDate().compareTo(o1.getOrderDate());
        });
        return sorted;
    }

    /**
     * Sort orders by total amount
     */
    public List<Order> sortByAmount(List<Order> orders, boolean ascending) {
        if (orders == null || orders.isEmpty()) {
            return new ArrayList<>();
        }
        List<Order> sorted = new ArrayList<>(orders);
        sorted.sort((o1, o2) -> {
            if (o1.getTotalAmount() == null || o2.getTotalAmount() == null)
                return 0;
            return ascending ? o1.getTotalAmount().compareTo(o2.getTotalAmount())
                    : o2.getTotalAmount().compareTo(o1.getTotalAmount());
        });
        return sorted;
    }
//    public static void main(String[] args){
//        OrderService orderService = new OrderService();
//       println(  orderService.getAllOrders());
//    }
}
