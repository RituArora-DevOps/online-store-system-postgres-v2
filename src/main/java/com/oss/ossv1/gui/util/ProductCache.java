package com.oss.ossv1.gui.util;

import com.oss.ossv1.gui.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductCache {
    private static final ObservableList<Product> cachedProducts = FXCollections.observableArrayList();

    public static ObservableList<Product> getProducts() {
        return cachedProducts;
    }

    public static void setProducts(ObservableList<Product> products) {
        cachedProducts.setAll(products); // replaces current cache
    }

    public static boolean isEmpty() {
        return cachedProducts.isEmpty();
    }

    public static void clear() {
        cachedProducts.clear();
    }
}
