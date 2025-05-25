package com.oss.ossv1.creational;

import com.oss.ossv1.gui.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Singleton design pattern for a central Store called SingletonStore.
 * This might help in improving tab switching performance.
 *
 * Caches the list of products in a singleton and reuse it.
 * Unlike ProductController that fetches from DB on every load, Controller will fetch from SingletonStore.getInstance().getProducts() instead.
 * Only one instance of product data is kept - no redundant reloads.
 *
 * Goal:
 * 1. Store the list of products in a singleton
 * 2. Avoid repeated load from DB on every tab switch in the GUI
 * 3. Reuse the cached data in GUI on every tab switch
 *
 */
public class SingletonStore {

    private static SingletonStore instance;
    private List<Product> products;

    private SingletonStore() {
        products = new ArrayList();
    }

    public static SingletonStore getInstance() {
        if (instance == null) {
            instance = new SingletonStore();
        }
        return instance;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void clearProducts() {
        products.clear();
    }
}
