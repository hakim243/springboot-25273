package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    @Override
    public Page<Product> findPaginated(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    @Override
    public Page<Product> findProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }
    
    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }
    
    @Override
    public List<Product> findFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }
    
    @Override
    public List<Product> findRecentProducts() {
        return productRepository.findTop8ByOrderByIdDesc();
    }
    
    @Override
    public long getProductCount() {
        return productRepository.count();
    }
    
    @Override
    public List<Product> findPopularProducts(int limit) {
        // This is a simplified implementation
        // In a real application, you would track product popularity through orders or views
        return productRepository.findTop8ByOrderByIdDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
} 