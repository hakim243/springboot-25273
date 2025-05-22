package com.ecommerce.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();
    
    // Helper method to calculate total
    @Transient
    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Helper method to add item to cart
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }
    
    // Helper method to remove item from cart
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }
    
    // Helper method to clear cart
    public void clear() {
        items.clear();
    }
    
    public Cart() {
    }
    
    public Cart(Long id, User user, Set<CartItem> items) {
        this.id = id;
        this.user = user;
        this.items = items;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Set<CartItem> getItems() {
        return items;
    }
    
    public void setItems(Set<CartItem> items) {
        this.items = items;
    }
} 