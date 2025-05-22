package com.ecommerce.service;

import com.ecommerce.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAllCategories();
    Optional<Category> findCategoryById(Long id);
    Optional<Category> findCategoryByName(String name);
    Category saveCategory(Category category);
    void deleteCategory(Long id);
} 