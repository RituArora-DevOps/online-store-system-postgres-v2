package com.oss.ossv1.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oss.ossv1.data.entity.Cart;
import com.oss.ossv1.data.entity.CartItem;
import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.repository.CartRepository;
import com.oss.ossv1.data.repository.ProductRepository;

/**
 * Service for managing the shopping cart functionality
 */
@Service
public class CartService {

    // Database connections we need
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;

    /**
     * Gets a user's shopping cart. Creates a new one if it doesn't exist.
     * @param userId The ID of the user
     * @return The user's shopping cart
     */
    @Transactional
    public Cart getOrCreateCart(Integer userId) {
        // Try to find existing cart
        Cart cart = cartRepository.findByUserId(userId);
        
        // If no cart exists, create a new one
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }
        // Make sure cart has an items list
        else if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }
        
        return cart;
    }

    /**
     * Gets a user's shopping cart without creating a new one
     * @param userId The ID of the user
     * @return The user's cart, or null if none exists
     */
    public Cart getCart(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    /**
     * Adds a product to the cart
     * If the product already exists, increase the quantity
     */
    @Transactional
    public Cart addToCart(Integer userId, Integer productId, int quantity) {
        // Get the cart and product
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product is already in cart
        CartItem existingItem = findCartItem(cart, productId);

        // If found, update quantity
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        }
        // If not found, add new item
        else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    /**
     * Removes an item from the cart
     */
    @Transactional
    public Cart removeFromCart(Integer userId, Integer productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    /**
     * Updates the quantity of an item in the cart
     */
    @Transactional
    public Cart updateCartItemQuantity(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        
        // Find the item and update its quantity
        CartItem item = findCartItem(cart, productId);
        if (item != null) {
            item.setQuantity(quantity);
            return cartRepository.save(cart);
        }
        
        return cart;
    }

    /**
     * Clears all items from the cart
     */
    @Transactional
    public void clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    /**
     * Calculates the total price of all items in cart
     */
    public double getCartTotal(Integer userId) {
        Cart cart = getCart(userId);
        if (cart == null) return 0.0;
        
        double total = 0.0;
        for (CartItem item : cart.getItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    /**
     * Finds a cart item by product
     */
    private CartItem findCartItem(Cart cart, Integer productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}
