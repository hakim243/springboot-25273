package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecommerce.service.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        User user = getUserFromUserDetails(currentUser);
        Cart cart = cartService.getCartForUser(user);
        
        model.addAttribute("cart", cart);
        model.addAttribute("pageTitle", "Shopping Cart");
        
        return "cart";
    }
    
    @PostMapping("/add")
    public String addToCart(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            RedirectAttributes redirectAttributes) {
        
        User user = getUserFromUserDetails(currentUser);
        Product product = productService.findProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        cartService.addProductToCart(user, product, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Product added to cart!");
        
        return "redirect:/product/" + productId;
    }
    
    @PostMapping("/update")
    public String updateCartItem(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam("cartItemId") Long cartItemId,
            @RequestParam("quantity") Integer quantity,
            RedirectAttributes redirectAttributes) {
        
        User user = getUserFromUserDetails(currentUser);
        cartService.updateCartItemQuantity(user, cartItemId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Cart updated!");
        
        return "redirect:/cart";
    }
    
    @PostMapping("/remove")
    public String removeCartItem(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam("cartItemId") Long cartItemId,
            RedirectAttributes redirectAttributes) {
        
        User user = getUserFromUserDetails(currentUser);
        cartService.removeProductFromCart(user, cartItemId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removed from cart!");
        
        return "redirect:/cart";
    }
    
    @PostMapping("/clear")
    public String clearCart(
            @AuthenticationPrincipal UserDetails currentUser,
            RedirectAttributes redirectAttributes) {
        
        User user = getUserFromUserDetails(currentUser);
        cartService.clearCart(user);
        redirectAttributes.addFlashAttribute("successMessage", "Cart cleared!");
        
        return "redirect:/cart";
    }
    
    private User getUserFromUserDetails(UserDetails userDetails) {
        return userService.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
} 