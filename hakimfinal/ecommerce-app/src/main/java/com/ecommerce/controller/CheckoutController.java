package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String checkout(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = getUserFromUserDetails(currentUser);
        Cart cart = cartService.getCartForUser(user);
        
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Checkout");
        
        return "checkout";
    }
    
    @PostMapping("/place-order")
    public String placeOrder(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam("billingAddress") String billingAddress,
            @RequestParam("paymentMethod") String paymentMethod,
            RedirectAttributes redirectAttributes) {
        
        User user = getUserFromUserDetails(currentUser);
        Cart cart = cartService.getCartForUser(user);
        
        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty");
            return "redirect:/cart";
        }
        
        try {
            Order order = orderService.createOrder(user, shippingAddress, billingAddress, paymentMethod);
            redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
            return "redirect:/order/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error placing order: " + e.getMessage());
            return "redirect:/checkout";
        }
    }
    
    private User getUserFromUserDetails(UserDetails userDetails) {
        return userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 