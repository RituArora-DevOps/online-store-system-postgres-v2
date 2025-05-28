package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Grocery;
import com.oss.ossv1.gui.model.Product;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to filter grocery items by expiry date.
 */
public class ExpiryDateSearchStrategy implements SearchStrategy {
    private final LocalDate expiryDate;

    public ExpiryDateSearchStrategy(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public List<Product> search(List<Product> products) {
        return products.stream()
                .filter(p -> p instanceof Grocery g && expiryDate.equals(g.getExpiryDate()))
                .collect(Collectors.toList());
    }
}
