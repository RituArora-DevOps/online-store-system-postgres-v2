package com.oss.ossv1.creational;

import com.oss.ossv1.gui.model.Clothing;
import com.oss.ossv1.gui.model.Product;

public class ClothingFactory implements ProductFactory {

    @Override
    public Product createProduct() {
        return new Clothing();
    }
}