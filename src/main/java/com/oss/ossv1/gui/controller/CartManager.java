package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.CartItem;
import com.oss.ossv1.gui.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartManager {

    private static CartManager instance; // singleton instance
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    private CartManager() {
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public void addToCart(Product product) {
        Integer newProductId = product.getId();
        for (CartItem item : cartItems) {
            Integer existingProductId = item.getProduct().getId();
            if (newProductId != null && newProductId.equals(existingProductId)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }


    public void removeFromCart(CartItem item) {
        cartItems.remove(item);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double calculateTotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

}
