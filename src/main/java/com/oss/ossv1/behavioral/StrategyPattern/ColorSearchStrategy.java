package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Clothing;
import com.oss.ossv1.gui.model.Product;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to filter clothing by color.
 */
public class ColorSearchStrategy implements SearchStrategy {
    private final String color;

    public ColorSearchStrategy(String color) {
        this.color = color.toLowerCase();
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p instanceof Clothing c && c.getColor() != null && c.getColor().toLowerCase().equals(color))
                .collect(Collectors.toList());
    }
}
