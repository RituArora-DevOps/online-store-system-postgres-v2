package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Clothing;
import com.oss.ossv1.gui.model.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to filter clothing by size.
 */
public class SizeSearchStrategy implements SearchStrategy {
    private final String size;

    public SizeSearchStrategy(String size) {
        this.size = size.toLowerCase();
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p instanceof Clothing c && c.getSize() != null && c.getSize().toLowerCase().equals(size))
                .collect(Collectors.toList());
    }
}

