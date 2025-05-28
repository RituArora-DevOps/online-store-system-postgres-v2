package com.oss.ossv1.gui.search;

import com.oss.ossv1.behavioral.StrategyPattern.CompositeSearchStrategy;
import com.oss.ossv1.behavioral.StrategyPattern.NameSearchStrategy;
import com.oss.ossv1.behavioral.StrategyPattern.PriceRangeSearchStrategy;
import com.oss.ossv1.behavioral.StrategyPattern.WarrantySearchStrategy;
import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style test that verifies CompositeSearchStrategy
 * applies multiple search criteria in sequence.
 *  * A setup of Electronics products
 *  * A multi-criteria integration test
 *  * Edge cases (no matches, one filter only)
 */
class CompositeSearchStrategyTest {

    private List<Product> products;

    @BeforeEach
    void setup() {
        products = List.of(
                new Electronics(1, "Apple iPhone 13", "Smartphone", 999.99, "electronics", 12),
                new Electronics(2, "Samsung Galaxy S22", "Smartphone", 899.99, "electronics", 24),
                new Electronics(3, "Apple Watch", "Wearable", 399.99, "electronics", 12),
                new Electronics(4, "Sony TV", "Television", 1200.00, "electronics", 36)
        );
    }

    @Test
    void testCompositeSearch_appliesMultipleCriteria() {
        CompositeSearchStrategy strategy = new CompositeSearchStrategy();
        strategy.addStrategy(new NameSearchStrategy("apple"));
        strategy.addStrategy(new PriceRangeSearchStrategy(300, 1000));
        strategy.addStrategy(new WarrantySearchStrategy(12));

        List<Product> result = strategy.search(products);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getName().toLowerCase().contains("apple")));
        assertTrue(result.stream().allMatch(p -> p.getPrice() >= 300 && p.getPrice() <= 1000));
        assertTrue(result.stream().allMatch(p -> ((Electronics)p).getWarrantyPeriod() == 12));
    }

    @Test
    void testCompositeSearch_withNoMatches() {
        CompositeSearchStrategy strategy = new CompositeSearchStrategy();
        strategy.addStrategy(new NameSearchStrategy("nokia"));
        strategy.addStrategy(new WarrantySearchStrategy(60));

        List<Product> result = strategy.search(products);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCompositeSearch_withOneFilter() {
        CompositeSearchStrategy strategy = new CompositeSearchStrategy();
        strategy.addStrategy(new WarrantySearchStrategy(24));

        List<Product> result = strategy.search(products);
        assertEquals(1, result.size());
        assertEquals("Samsung Galaxy S22", result.get(0).getName());
    }
}
