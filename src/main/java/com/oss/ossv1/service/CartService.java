package com.oss.ossv1.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oss.ossv1.data.entity.Cart;
import com.oss.ossv1.data.entity.CartItem;
import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.repository.CartRepository;
import com.oss.ossv1.data.repository.ProductRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Service for managing the shopping cart functionality.
 * Handles operations like adding/removing items, updating quantities,
 * and calculating totals.
 */
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private ObservableList<CartItem> cartItems;

    /**
     * Initializes a new cart service with an empty observable list of items
     */
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItems = FXCollections.observableArrayList();
    }

    @Transactional
    public Cart getOrCreateCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        } else if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }
        return cart;
    }

    public Cart getCart(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    /**
     * Adds a product to the cart
     * If the product already exists, increase the quantity
     */
    @Transactional
    public Cart addToCart(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
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
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        return cartRepository.save(cart);
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
        return cart != null ? cart.getTotalPrice() : 0.0;
    }

    /**
     * Finds a cart item by product
     */
    private CartItem findCartItem(Product product) {
        return cartItems.stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }
}
