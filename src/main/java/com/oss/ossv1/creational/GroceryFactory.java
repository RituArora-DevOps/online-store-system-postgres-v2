package com.oss.ossv1.creational;

import com.oss.ossv1.gui.model.Grocery;
import com.oss.ossv1.gui.model.Product;

public class GroceryFactory implements ProductFactory {
    @Override
    public Product createProduct() {
        return new Grocery();
    }
}
