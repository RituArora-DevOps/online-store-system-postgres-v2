package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.CartItem;
import com.oss.ossv1.gui.model.Product;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CartManager singleton class.
 *
 * Covers add, remove, quantity update, total, and discount calculations.
 */
public class CartManagerTest {

    private CartManager cartManager;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        cartManager = CartManager.getInstance();
        cartManager.clearCart(); // Reset state before each test

        sampleProduct = new Product(1, "Laptop", "Gaming laptop", 1200.0, "electronics") {
            @Override
            public double getDiscountedPrice(double discountPercent) {
                return getPrice() - (getPrice() * discountPercent / 100.0);
            }
        };
    }

    @Test
    void testAddToCart_newItem_addedSuccessfully() {
        cartManager.addToCart(sampleProduct);
        ObservableList<CartItem> items = cartManager.getCartItems();

        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getQuantity());
        assertEquals(sampleProduct.getId(), items.get(0).getProduct().getId());
    }

    @Test
    void testAddToCart_existingItem_quantityIncreased() {
        cartManager.addToCart(sampleProduct);
        cartManager.addToCart(sampleProduct);

        ObservableList<CartItem> items = cartManager.getCartItems();
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
    }

    @Test
    void testRemoveItem_removesCorrectItem() {
        cartManager.addToCart(sampleProduct);
        CartItem item = cartManager.getCartItems().get(0);
        cartManager.removeItem(item);

        assertTrue(cartManager.getCartItems().isEmpty());
    }

    @Test
    void testClearCart_emptiesAllItems() {
        cartManager.addToCart(sampleProduct);
        cartManager.addToCart(sampleProduct);
        cartManager.clearCart();

        assertTrue(cartManager.getCartItems().isEmpty());
    }

    @Test
    void testCalculateTotal_correctSum() {
        cartManager.addToCart(sampleProduct);
        cartManager.addToCart(sampleProduct); // Qty = 2

        double expected = sampleProduct.getPrice() * 2;
        assertEquals(expected, cartManager.calculateTotal(), 0.01);
    }

    @Test
    void testCalculateDiscountedTotal_10PercentApplied() {
        cartManager.addToCart(sampleProduct); // Qty = 1
        double expected = sampleProduct.getDiscountedPrice(10);
        assertEquals(expected, cartManager.calculateDiscountedTotal(), 0.01);
    }

    @Test
    void testProductAddedToCart_isDefensiveCopy() {
        cartManager.addToCart(sampleProduct);
        Product original = sampleProduct;
        CartItem added = cartManager.getCartItems().get(0);

        assertNotSame(original, added.getProduct());
        assertEquals(original.getId(), added.getProduct().getId());
    }
}
