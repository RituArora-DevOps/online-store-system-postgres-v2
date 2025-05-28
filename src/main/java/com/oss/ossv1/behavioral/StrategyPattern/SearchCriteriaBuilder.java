package com.oss.ossv1.behavioral.StrategyPattern;


import com.oss.ossv1.gui.model.Product;
import java.time.LocalDate;

/**
 * Builder class to construct a CompositeSearchStrategy using optional user-provided filters.
 * This enhances modularity and clarity in controller code.
 *
 * SOLID Principles:
 * - SRP: This class solely builds a search strategy based on user input.
 * - OCP: Easy to add new `withX()` methods for more filter criteria.
 * - DIP: Depends only on abstractions (SearchStrategy).
 */
public class SearchCriteriaBuilder {

    private final CompositeSearchStrategy composite = new CompositeSearchStrategy();

    /**
     * Adds a category filter strategy.
     */
    public SearchCriteriaBuilder withCategory(String category) {
        if (category != null && !"all".equalsIgnoreCase(category)) {
            composite.addStrategy(new CategorySearchStrategy(category));
        }
        return this;
    }

    /**
     * Adds a price range filter strategy if both min and max are valid.
     */
    public SearchCriteriaBuilder withPriceRange(String minPriceStr, String maxPriceStr) {
        try {
            double min = Double.parseDouble(minPriceStr);
            double max = Double.parseDouble(maxPriceStr);
            composite.addStrategy(new PriceRangeSearchStrategy(min, max));
        } catch (NumberFormatException ignored) {}
        return this;
    }

    /**
     * Adds a name-based keyword filter strategy.
     */
    public SearchCriteriaBuilder withKeyword(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            composite.addStrategy(new NameSearchStrategy(keyword));
        }
        return this;
    }

    /**
     * Adds a clothing-size filter strategy.
     */
    public SearchCriteriaBuilder withSize(String size) {
        if (size != null && !size.isBlank()) {
            composite.addStrategy(new SizeSearchStrategy(size));
        }
        return this;
    }

    /**
     * Adds a clothing-color filter strategy.
     */
    public SearchCriteriaBuilder withColor(String color) {
        if (color != null && !color.isBlank()) {
            composite.addStrategy(new ColorSearchStrategy(color));
        }
        return this;
    }

    /**
     * Adds an electronics warranty filter strategy.
     */
    public SearchCriteriaBuilder withWarranty(String warrantyStr) {
        try {
            int warranty = Integer.parseInt(warrantyStr);
            composite.addStrategy(new WarrantySearchStrategy(warranty));
        } catch (NumberFormatException ignored) {}
        return this;
    }

    /**
     * Adds a grocery expiry date filter strategy.
     */
    public SearchCriteriaBuilder withExpiryDate(String expiryStr) {
        try {
            LocalDate expiry = LocalDate.parse(expiryStr);
            composite.addStrategy(new ExpiryDateSearchStrategy(expiry));
        } catch (Exception ignored) {}
        return this;
    }

    /**
     * Returns the composite search strategy containing all active filters.
     */
    public CompositeSearchStrategy build() {
        return composite;
    }
}
