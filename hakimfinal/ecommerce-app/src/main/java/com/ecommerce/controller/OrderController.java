package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String orderList(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        
        User user = getUserFromUserDetails(currentUser);
        
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("orderDate").descending());
        Page<Order> orderPage = orderService.findOrdersByUserPaginated(user, pageable);
        
        model.addAttribute("orders", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("pageTitle", "My Orders");
        
        return "order-list";
    }
    
    // Add mapping for order history (redirects to main order page)
    @GetMapping("/history")
    public String orderHistory() {
        return "redirect:/order";
    }
    
    @GetMapping("/{id}")
    public String orderDetail(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable Long id,
            Model model) {
        
        User user = getUserFromUserDetails(currentUser);
        Order order = orderService.findOrderById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Security check: ensure user can only see their own orders
        if (!order.getUser().getId().equals(user.getId())) {
            return "redirect:/order";
        }
        
        model.addAttribute("order", order);
        model.addAttribute("pageTitle", "Order #" + order.getId());
        
        return "order-detail";
    }
    
    private User getUserFromUserDetails(UserDetails userDetails) {
        return userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 