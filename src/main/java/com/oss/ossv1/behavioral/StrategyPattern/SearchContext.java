package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Product;
import java.util.List;

/**
 * Context class to hold the current strategy and execute it.
 *
 * SOLID:
 * - SRP: Manages strategy invocation.
 * - OCP: New strategies work without changing context.
 * - DIP: Depends only on SearchStrategy abstraction.
 */
public class SearchContext {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Product> executeStrategy(List<Product> products) {
        if (strategy == null) return products;
        return strategy.search(products);
    }
}

