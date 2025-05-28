package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search strategy to filter products by price range.
 *
 * SOLID:
 * - SRP: Handles only price range logic.
 * - OCP: Can be extended independently.
 */
public class PriceRangeSearchStrategy implements SearchStrategy {
    private final double min;
    private final double max;

    public PriceRangeSearchStrategy(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
    }
}
