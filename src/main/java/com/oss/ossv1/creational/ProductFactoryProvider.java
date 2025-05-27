package com.oss.ossv1.creational;


import com.oss.ossv1.gui.model.Product;

// acts like a Factory Dispatcher - this selects the right factory for a given category
public class ProductFactoryProvider {
    public static ProductFactory getFactory(String category) {
        return switch (category.toLowerCase()) {
            case "electronics" -> new ElectronicsFactory();
            case "grocery" -> new GroceryFactory();
            case "clothing" -> new ClothingFactory();
            default -> () -> new Product(); // fallback
        };
    }
}
