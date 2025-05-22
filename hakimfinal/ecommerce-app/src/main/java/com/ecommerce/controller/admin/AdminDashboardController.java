package com.ecommerce.controller.admin;

import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // Get counts for dashboard
        long productCount = productService.getProductCount();
        long orderCount = orderService.getOrderCount();
        long userCount = userService.getUserCount();
        
        // Get recent orders
        List<Order> recentOrders = orderService.findRecentOrders(5);
        
        // Calculate total revenue (simplified)
        BigDecimal totalRevenue = orderService.calculateTotalRevenue();
        
        // Get popular products
        List<Product> popularProducts = productService.findPopularProducts(5);
        
        // Add attributes to the model
        model.addAttribute("productCount", productCount);
        model.addAttribute("orderCount", orderCount);
        model.addAttribute("userCount", userCount);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("popularProducts", popularProducts);
        model.addAttribute("pageTitle", "Admin Dashboard");
        
        return "admin/dashboard";
    }
} 