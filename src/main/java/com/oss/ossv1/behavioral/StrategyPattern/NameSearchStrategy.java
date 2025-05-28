package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search strategy to filter by product name (partial, case-insensitive).
 *
 * SOLID:
 * - SRP: Focuses only on name-based filtering.
 * - OCP: Doesn’t affect other strategies.
 */
public class NameSearchStrategy implements SearchStrategy {
    private final String keyword;

    public NameSearchStrategy(String keyword) {
        this.keyword = keyword.toLowerCase();
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }
}