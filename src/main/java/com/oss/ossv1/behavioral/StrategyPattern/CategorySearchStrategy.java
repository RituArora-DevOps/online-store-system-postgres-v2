package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to filter products by category.
 *
 * SOLID:
 * - SRP: Single responsibility — category filtering.
 * - OCP: Other criteria can be added independently.
 */
public class CategorySearchStrategy implements SearchStrategy {
    private final String category;

    public CategorySearchStrategy(String category) {
        this.category = category.toLowerCase();
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}

