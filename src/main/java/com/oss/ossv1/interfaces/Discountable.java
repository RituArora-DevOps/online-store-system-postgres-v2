package com.oss.ossv1.interfaces;

/**
 * Interface for applying discounts to products.
 */
public interface Discountable {
    void applyDiscount(double percent);
    double getDiscountedPrice(double percent); // new
}
