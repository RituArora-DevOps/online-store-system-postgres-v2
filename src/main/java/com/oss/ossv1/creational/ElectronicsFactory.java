package com.oss.ossv1.creational;

import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Product;

public class ElectronicsFactory implements ProductFactory {
    @Override
    public Product createProduct() {
        return new Electronics();
    }
}
