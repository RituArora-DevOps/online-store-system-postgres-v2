package com.oss.ossv1.behavioral.StrategyPattern;

import com.oss.ossv1.gui.model.Product;
import java.util.List;

/**
 * Strategy interface to abstract search logic.
 *
 * SOLID:
 * - SRP: This interface has one reason to change — if search behavior abstraction needs change.
 * - OCP: New strategies can be added without modifying this interface.
 * - DIP: High-level ProductController depends on this abstraction.
 */
public interface SearchStrategy {
    List<Product> search(List<Product> products);
}
