package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AdminLoginController {
    
    @GetMapping("/admin-login")
    public String adminLogin(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        
        model.addAttribute("pageTitle", "Admin Login");
        return "admin-login";
    }
} 