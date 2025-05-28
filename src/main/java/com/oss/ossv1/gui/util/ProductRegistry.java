// ProductRegistry.java
package com.oss.ossv1.gui.util;

import com.oss.ossv1.gui.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * ProductRegistry is a utility class that stores and manages Product objects
 * mapped by their unique IDs. It uses an ObservableMap to enable real-time
 * JavaFX GUI updates if needed.
 *
 * This class supports quick access to product data (O(1) lookup)
 * and reduces backend calls by caching products in memory.
 *
 * SOLID Principles:
 * - Single Responsibility: This class only manages product lookup/cache logic.
 * - Open/Closed: We can add new product types without modifying this class.
 */
public class ProductRegistry {

    // Static ObservableMap to hold product ID and product object mappings.
    // ObservableMap is used instead of HashMap to support UI bindings if needed.
    private static final ObservableMap<Integer, Product> productMap = FXCollections.observableHashMap();

    /**
     * Registers a product in the registry if:
     * - It's not null
     * - It has a valid (non-zero) ID
     * - It's not already registered
     *
     * Prevents duplicate registration and null references.
     */
    public static void register(Product product) {
        if (product != null && product.getId() != 0 && !productMap.containsKey(product.getId())) {
            productMap.put(product.getId(), product);
        }
    }

    /**
     * Retrieves the product by its ID.
     * Enables constant-time (O(1)) lookup for performance.
     */
    public static Product get(int id) {
        return productMap.get(id);
    }

    /**
     * Checks if a product with the given ID is already in the registry.
     */
    public static boolean contains(int id) {
        return productMap.containsKey(id);
    }

    /**
     * Clears all products from the registry.
     * Useful for resetting state between sessions or test cases.
     */
    public static void clear() {
        productMap.clear();
    }

    /**
     * Returns all products currently stored in the registry.
     * Could be used for displaying or synchronizing with the UI.
     */
    public static ObservableMap<Integer, Product> getAll() {
        return productMap;
    }
}
