package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAllOrders();
    List<Order> findOrdersByUser(User user);
    Page<Order> findOrdersByUserPaginated(User user, Pageable pageable);
    Optional<Order> findOrderById(Long id);
    Order createOrder(User user, String shippingAddress, String billingAddress, String paymentMethod);
    Order updateOrderStatus(Long orderId, Order.OrderStatus status);
    List<Order> findOrdersByStatus(Order.OrderStatus status);
    Page<Order> findPaginated(Pageable pageable);
    
    // New methods for dashboard
    long getOrderCount();
    BigDecimal calculateTotalRevenue();
    List<Order> findRecentOrders(int limit);
} 