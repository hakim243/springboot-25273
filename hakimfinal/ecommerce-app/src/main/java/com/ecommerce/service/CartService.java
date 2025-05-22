package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import java.util.Optional;

public interface CartService {
    Cart getCartForUser(User user);
    Cart addProductToCart(User user, Product product, Integer quantity);
    Cart updateCartItemQuantity(User user, Long cartItemId, Integer quantity);
    Cart removeProductFromCart(User user, Long cartItemId);
    void clearCart(User user);
    Optional<CartItem> findCartItemById(Long id);
} 