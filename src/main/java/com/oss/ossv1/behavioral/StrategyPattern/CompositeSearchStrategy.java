package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Strategy that applies multiple filters sequentially.
 */
public class CompositeSearchStrategy implements SearchStrategy {
    private final List<SearchStrategy> strategies = new ArrayList<>();

    public void addStrategy(SearchStrategy strategy) {
        strategies.add(strategy);
    }

    @Override
    public List<Product> search(List<Product> products) {
        List<Product> result = products;
        for (SearchStrategy strategy : strategies) {
            result = strategy.search(result);
        }
        return result;
    }
}

