package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public Cart getCartForUser(User user) {
        Optional<Cart> existingCart = cartRepository.findByUserId(user.getId());
        
        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
    }
    
    @Override
    @Transactional
    public Cart addProductToCart(User user, Product product, Integer quantity) {
        Cart cart = getCartForUser(user);
        
        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity if product already in cart
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new cart item
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.addItem(newItem);
        }
        
        return cartRepository.save(cart);
    }
    
    @Override
    @Transactional
    public Cart updateCartItemQuantity(User user, Long cartItemId, Integer quantity) {
        Cart cart = getCartForUser(user);
        
        cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        
        return cartRepository.save(cart);
    }
    
    @Override
    @Transactional
    public Cart removeProductFromCart(User user, Long cartItemId) {
        Cart cart = getCartForUser(user);
        
        cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(cart::removeItem);
        
        return cartRepository.save(cart);
    }
    
    @Override
    @Transactional
    public void clearCart(User user) {
        Cart cart = getCartForUser(user);
        cart.clear();
        cartRepository.save(cart);
    }
    
    @Override
    public Optional<CartItem> findCartItemById(Long id) {
        return Optional.ofNullable(entityManager.find(CartItem.class, id));
    }
} 