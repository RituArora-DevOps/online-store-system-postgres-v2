package com.oss.ossv1.gui.search;

import com.oss.ossv1.behavioral.StrategyPattern.CompositeSearchStrategy;
import com.oss.ossv1.behavioral.StrategyPattern.SearchCriteriaBuilder;
import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for building and executing composite strategies
 * using the SearchCriteriaBuilder.
 * Proper setup with Electronics instances
 * Tests for valid filters, invalid input, empty input
 * End-to-end validation that SearchCriteriaBuilder assembles the strategy chain correctly
 */
class SearchCriteriaBuilderTest {

    private List<Product> products;

    @BeforeEach
    void init() {
        products = List.of(
                new Electronics(1, "iPhone 14 Pro", "Apple smartphone", 1099.99, "electronics", 12),
                new Electronics(2, "Samsung TV", "Smart TV", 799.99, "electronics", 24),
                new Electronics(3, "MacBook Pro", "Laptop", 1999.99, "electronics", 12),
                new Electronics(4, "Dell Monitor", "Display", 299.99, "electronics", 24)
        );
    }

    @Test
    void testBuilder_combinesMultipleFiltersCorrectly() {
        CompositeSearchStrategy strategy = new SearchCriteriaBuilder()
                .withKeyword("pro")
                .withPriceRange("500", "2000")
                .withWarranty("12")
                .build();

        List<Product> result = strategy.search(products);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().toLowerCase().contains("pro"));
        assertTrue(result.get(1).getName().toLowerCase().contains("pro"));
    }

    @Test
    void testBuilder_withInvalidPriceRange_doesNotBreak() {
        CompositeSearchStrategy strategy = new SearchCriteriaBuilder()
                .withPriceRange("abc", "xyz") // invalid inputs
                .build();

        List<Product> result = strategy.search(products);

        assertEquals(4, result.size()); // No filters applied, should return all
    }

    @Test
    void testBuilder_withNoFilters_returnsAll() {
        CompositeSearchStrategy strategy = new SearchCriteriaBuilder()
                .build();

        List<Product> result = strategy.search(products);
        assertEquals(products.size(), result.size());
    }

    @Test
    void testBuilder_handlesEmptyKeywordGracefully() {
        CompositeSearchStrategy strategy = new SearchCriteriaBuilder()
                .withKeyword("")
                .build();

        List<Product> result = strategy.search(products);
        assertEquals(products.size(), result.size());
    }
}
