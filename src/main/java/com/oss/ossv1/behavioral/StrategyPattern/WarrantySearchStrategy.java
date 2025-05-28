package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to filter electronics by warranty period.
 */
public class WarrantySearchStrategy implements SearchStrategy {
    private final int warranty;

    public WarrantySearchStrategy(int warranty) {
        this.warranty = warranty;
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p instanceof Electronics e && e.getWarrantyPeriod() == warranty)
                .collect(Collectors.toList());
    }
}