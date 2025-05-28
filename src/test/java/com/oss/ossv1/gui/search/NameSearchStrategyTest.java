package com.oss.ossv1.gui.search;

import com.oss.ossv1.behavioral.StrategyPattern.NameSearchStrategy;
import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NameSearchStrategyTest {

    private List<Product> sampleProducts;

    @BeforeEach
    void setup() {
        sampleProducts = List.of(
                new Electronics(1, "Apple iPhone", "Smartphone", 999.99, "electronics", 12),
                new Electronics(2, "Banana Smoothie Maker", "Appliance", 45.99, "electronics", 6),
                new Electronics(3, "Apple Watch", "Wearable tech", 299.99, "electronics", 12)
        );
    }

    @Test
    void testSearch_returnsMatchingProducts() {
        NameSearchStrategy strategy = new NameSearchStrategy("apple");

        List<Product> result = strategy.search(sampleProducts);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getName().toLowerCase().contains("apple")));
    }

    @Test
    void testSearch_noMatchReturnsEmptyList() {
        NameSearchStrategy strategy = new NameSearchStrategy("tofu");

        List<Product> result = strategy.search(sampleProducts);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_caseInsensitiveMatching() {
        NameSearchStrategy strategy = new NameSearchStrategy("BANANA");

        List<Product> result = strategy.search(sampleProducts);

        assertEquals(1, result.size());
        assertEquals("Banana Smoothie Maker", result.get(0).getName());
    }
}
