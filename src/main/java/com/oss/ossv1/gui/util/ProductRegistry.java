// ProductRegistry.java
package com.oss.ossv1.gui.util;

import com.oss.ossv1.gui.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ProductRegistry {
    private static final ObservableMap<Integer, Product> productMap = FXCollections.observableHashMap();

    public static void register(Product product) {
        if (product != null && product.getId() != 0 && !productMap.containsKey(product.getId())) {
            productMap.put(product.getId(), product);
        }
    }

    public static Product get(int id) {
        return productMap.get(id);
    }

    public static boolean contains(int id) {
        return productMap.containsKey(id);
    }

    public static void clear() {
        productMap.clear();
    }

    public static ObservableMap<Integer, Product> getAll() {
        return productMap;
    }
}
