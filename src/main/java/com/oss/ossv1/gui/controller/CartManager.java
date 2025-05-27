// CartManager.java
package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.CartItem;
import com.oss.ossv1.gui.model.Product;
import com.oss.ossv1.gui.util.ProductRegistry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

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

    public synchronized void addToCart(Product product) {
        // Defensive copy to break shared reference
        Product copy = new Product(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory() );

        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == copy.getId())  {
                System.out.println("... comparing to: " + copy.getName() + " | ID: " + copy.getId());
                int qty = item.getQuantity();
                System.out.println("Before increment: Qty = " + qty);
                item.setQuantity(qty + 1);
                System.out.println("After increment: Qty = " + item.getQuantity());
                return;
            }
        }

        cartItems.add(new CartItem(copy, 1));
        System.out.println("NEW item added to cart: " + copy.getName());
    }


    private void printCartState() {
        System.out.println("🧾 === CURRENT CART STATE ===");
        for (CartItem item : cartItems) {
            System.out.println("🧺 " + item.getProduct().getName() +
                    " | Qty: " + item.getQuantity() +
                    " | hashCode: " + item.hashCode());
        }
        System.out.println("============================\n");
    }


    public void removeItem(CartItem itemToRemove) {
        System.out.println(">>> REMOVE called for: " + itemToRemove.getProduct().getName() + " x " + itemToRemove.getQuantity());
        cartItems.removeIf(ci -> Objects.equals(ci.getProduct().getId(), itemToRemove.getProduct().getId()));
        System.out.println("Cart size after removal: " + cartItems.size());

        System.out.println("CURRENT CART STATE:");
        cartItems.forEach(i -> System.out.println(i.getProduct().getName() + " x " + i.getQuantity()));
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double calculateTotal() {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public double calculateDiscountedTotal(){
        return cartItems.stream().mapToDouble(item -> {
                    double discountedPrice = item.getProduct().getDiscountedPrice(10); // 10% or use strategy
                    return discountedPrice * item.getQuantity();
                })
                .sum();
    }
}
