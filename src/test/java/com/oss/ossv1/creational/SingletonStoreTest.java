package com.oss.ossv1.creational;

import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for SingletonStore — verifies singleton behavior and in-memory caching.
 * It includes:
 * Singleton instance validation
 * Data consistency check
 * Cross-reference from multiple getInstance() calls
 */
class SingletonStoreTest {

    private List<Product> sampleProducts;

    @BeforeEach
    void setup() {
        sampleProducts = List.of(
                new Electronics(1, "iPhone", "Smartphone", 999.99, "electronics", 12),
                new Electronics(2, "Samsung TV", "Smart TV", 799.99, "electronics", 24)
        );
    }

    @Test
    void testSingleton_returnsSameInstance() {
        SingletonStore s1 = SingletonStore.getInstance();
        SingletonStore s2 = SingletonStore.getInstance();

        assertSame(s1, s2, "Should return the same SingletonStore instance");
    }

    @Test
    void testSingleton_storesAndRetrievesProductsCorrectly() {
        SingletonStore store = SingletonStore.getInstance();
        store.setProducts(sampleProducts);

        List<Product> retrieved = store.getProducts();

        assertEquals(sampleProducts, retrieved, "Retrieved product list should match what was stored");
    }

    @Test
    void testSingleton_doesNotCreateDuplicateInstances() {
        SingletonStore first = SingletonStore.getInstance();
        first.setProducts(List.of(new Electronics(99, "Test Product", "Desc", 100, "electronics", 1)));

        SingletonStore second = SingletonStore.getInstance();
        List<Product> fromSecond = second.getProducts();

        assertEquals(1, fromSecond.size());
        assertEquals("Test Product", fromSecond.get(0).getName());
    }
}
