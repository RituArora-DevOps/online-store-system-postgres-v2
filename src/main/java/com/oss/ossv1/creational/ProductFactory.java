package com.oss.ossv1.creational;

import com.oss.ossv1.gui.model.Product;

// Step 1. Define the interface for creating products
public interface ProductFactory {
    Product createProduct();
}
