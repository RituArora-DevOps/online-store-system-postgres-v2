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

    public void addToCart(Product product) {
        System.out.println("🔁 addToCart called");
        Product canonical = ProductRegistry.get(product.getId());
        if (canonical == null) {
            System.out.println("[WARN] Product not in registry: " + product.getId());
            canonical = product;
            ProductRegistry.register(product);
        }

        System.out.println(">>> ADD TO CART: " + canonical.getName() + " | ID: " + canonical.getId());

        for (CartItem item : cartItems) {
            System.out.println("... comparing to: " + item.getProduct().getName() + " | ID: " + item.getProduct().getId());
            if (Objects.equals(item.getProduct().getId(), canonical.getId())) {
                System.out.println("Before increment: Qty = " + item.getQuantity());
                item.setQuantity(item.getQuantity() + 1);
                System.out.println("After increment: Qty = " + item.getQuantity());
                printCartState();
                return;
            }
        }

        CartItem newItem = new CartItem(canonical, 1);
        cartItems.add(newItem);
        System.out.println("NEW item added to cart: " + canonical.getName());
        printCartState();
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
}
