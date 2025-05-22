package com.ecommerce.service;

import com.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Page<Product> findPaginated(Pageable pageable);
    Optional<Product> findProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    Page<Product> findProductsByCategory(Long categoryId, Pageable pageable);
    Page<Product> searchProducts(String keyword, Pageable pageable);
    List<Product> findFeaturedProducts();
    List<Product> findRecentProducts();
    
    // New methods for dashboard
    long getProductCount();
    List<Product> findPopularProducts(int limit);
} 