package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartService cartService;
    
    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    @Override
    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }
    
    @Override
    public Page<Order> findOrdersByUserPaginated(User user, Pageable pageable) {
        return orderRepository.findByUserId(user.getId(), pageable);
    }
    
    @Override
    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Order createOrder(User user, String shippingAddress, String billingAddress, String paymentMethod) {
        // Get user's cart
        Cart cart = cartService.getCartForUser(user);
        
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order with empty cart");
        }
        
        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setPaymentMethod(paymentMethod);
        
        // Convert cart items to order items
        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.addOrderItem(orderItem);
        });
        
        // Calculate total
        order.calculateTotal();
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Clear the cart
        cartService.clearCart(user);
        
        return savedOrder;
    }
    
    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    @Override
    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    @Override
    public Page<Order> findPaginated(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    @Override
    public long getOrderCount() {
        return orderRepository.count();
    }
    
    @Override
    public BigDecimal calculateTotalRevenue() {
        List<Order> allOrders = orderRepository.findAll();
        return allOrders.stream()
                .filter(order -> order.getTotalAmount() != null)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public List<Order> findRecentOrders(int limit) {
        return orderRepository.findAllByOrderByOrderDateDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
} 