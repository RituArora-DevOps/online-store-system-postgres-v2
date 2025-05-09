package com.oss.ossv1.interfaces;

/**
 * Interface for applying product-level discounts.
 *
 *  Purpose:
 * This interface is designed to support individual product discount logic,
 * such as applying a fixed percentage discount or calculating a discounted price.
 *
 *  Current Usage:
 * - This interface is currently **not active** in the main pricing logic.
 * - We are shifting to a **cart-level discount system** using the Adapter Pattern,
 *   which centrally applies discounts to the total cart value.

 *  Future Usage (Planned in Stage 2):
 * - Useful for UI/UX to display promotional slashed prices (e.g., "Was $50, now $40").
 * - Can be reused in admin tools to apply per-product offers.
 * - May be integrated back into pricing logic if product-level discounts are reintroduced.
 *
 * Avoid mixing `Discountable` logic with cart-level discount adapters unless
 * discount layering is clearly defined and tested (e.g., product discount first, then cart).
 */
public interface Discountable {
    void applyDiscount(double percent);
    double getDiscountedPrice(double percent); // new
}
