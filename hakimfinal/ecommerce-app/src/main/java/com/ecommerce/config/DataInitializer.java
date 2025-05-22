package com.ecommerce.config;

import com.ecommerce.model.*;
import com.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // List of sample image URLs from Unsplash
    private final List<String> sampleImageUrls = Arrays.asList(
        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?q=80&w=1064&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1572569511254-d8f925fe2cbb?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1585386959984-a4155224a1ad?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1560343090-f0409e92791a?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1583394838336-acd977736f90?q=80&w=1000&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1581235720704-06d3acfcb36f?q=80&w=1000&auto=format&fit=crop"
    );

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        createRoles();
        // Create sample categories
        createCategories();
        // Create sample products
        createProducts();
        // Update product images
        updateProductImages();
    }

    private void createRoles() {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
    }

    private void createCategories() {
        if (categoryRepository.count() == 0) {
            String[] categoryNames = {"Electronics", "Clothing", "Books", "Home & Kitchen", "Beauty & Personal Care"};
            
            for (String name : categoryNames) {
                Category category = new Category();
                category.setName(name);
                category.setDescription("Collection of " + name.toLowerCase() + " products");
                categoryRepository.save(category);
            }
        }
    }

    private void createProducts() {
        if (productRepository.count() == 0) {
            Category electronics = categoryRepository.findByName("Electronics").orElseThrow();
            Category clothing = categoryRepository.findByName("Clothing").orElseThrow();
            Category books = categoryRepository.findByName("Books").orElseThrow();
            
            // Electronics products
            createProduct("Smartphone X", "Latest smartphone with advanced features", new BigDecimal("699.99"), 50, electronics, true);
            createProduct("Laptop Pro", "High-performance laptop for professionals", new BigDecimal("1299.99"), 30, electronics, true);
            createProduct("Wireless Headphones", "Noise-cancelling wireless headphones", new BigDecimal("149.99"), 100, electronics, false);
            
            // Clothing products
            createProduct("Cotton T-Shirt", "Comfortable cotton t-shirt", new BigDecimal("19.99"), 200, clothing, false);
            createProduct("Jeans", "Classic blue jeans", new BigDecimal("49.99"), 150, clothing, true);
            createProduct("Winter Jacket", "Warm winter jacket", new BigDecimal("89.99"), 80, clothing, false);
            
            // Books products
            createProduct("Programming Guide", "Comprehensive programming guide", new BigDecimal("34.99"), 60, books, true);
            createProduct("Mystery Novel", "Bestselling mystery novel", new BigDecimal("14.99"), 120, books, false);
            createProduct("Cookbook", "Collection of delicious recipes", new BigDecimal("24.99"), 90, books, false);
        }
    }

    private void createProduct(String name, String description, BigDecimal price, Integer stock, Category category, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setCategory(category);
        product.setFeatured(featured);
        
        // Set a random image URL from our sample list
        int randomIndex = (int) (Math.random() * sampleImageUrls.size());
        product.setImageUrl(sampleImageUrls.get(randomIndex));
        
        productRepository.save(product);
    }

    private void updateProductImages() {
        // Update existing products with image URLs if they don't have one
        productRepository.findAll().forEach(product -> {
            if (product.getImageUrl() == null || product.getImageUrl().isEmpty()) {
                // Use product ID as index, but wrap around if it's larger than our list
                int imageIndex = (int) ((product.getId() - 1) % sampleImageUrls.size());
                product.setImageUrl(sampleImageUrls.get(imageIndex));
                productRepository.save(product);
            }
        });
    }
} 