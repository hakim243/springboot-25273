package com.ecommerce.service;

import com.ecommerce.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUserById(Long id);
    Optional<User> findUserByEmail(String email);
    User saveUser(User user);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
    User registerNewUser(User user);
    
    // For admin dashboard
    long getUserCount();
} 