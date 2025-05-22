package com.ecommerce.controller.admin;

import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public String listOrders(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "status", required = false) String statusStr,
            Model model) {
        
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("orderDate").descending());
        Page<Order> orderPage;
        
        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr.toUpperCase());
                model.addAttribute("orders", orderService.findOrdersByStatus(status));
                model.addAttribute("currentStatus", status);
            } catch (IllegalArgumentException e) {
                orderPage = orderService.findPaginated(pageable);
                model.addAttribute("orders", orderPage);
                model.addAttribute("currentPage", page);
                model.addAttribute("totalPages", orderPage.getTotalPages());
                model.addAttribute("totalItems", orderPage.getTotalElements());
            }
        } else {
            orderPage = orderService.findPaginated(pageable);
            model.addAttribute("orders", orderPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalItems", orderPage.getTotalElements());
        }
        
        model.addAttribute("statuses", Order.OrderStatus.values());
        model.addAttribute("pageTitle", "Manage Orders");
        
        return "admin/orders";
    }
    
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        model.addAttribute("order", order);
        model.addAttribute("statuses", Order.OrderStatus.values());
        model.addAttribute("pageTitle", "Order #" + order.getId());
        
        return "admin/order-detail";
    }
    
    @PostMapping("/update-status")
    public String updateOrderStatus(
            @RequestParam("orderId") Long orderId,
            @RequestParam("status") String statusStr,
            RedirectAttributes redirectAttributes) {
        
        try {
            Order.OrderStatus status = Order.OrderStatus.valueOf(statusStr.toUpperCase());
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Order status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating order status: " + e.getMessage());
        }
        
        return "redirect:/admin/orders/" + orderId;
    }
} 