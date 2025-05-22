package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String login(Model model, String error, String logout, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        
        model.addAttribute("pageTitle", "Login");
        return "login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Register");
        return "register";
    }
    
    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("user") User user,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Register");
            return "register";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "An account already exists for this email");
            model.addAttribute("pageTitle", "Register");
            return "register";
        }
        
        userService.registerNewUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login");
        
        return "redirect:/login";
    }
    
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        User user = userService.findUserByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "My Profile");
        
        return "profile";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("pageTitle", "Access Denied");
        return "403";
    }
} 