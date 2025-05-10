package com.oss.ossv1.gui.model;

import javafx.beans.property.*;

import java.util.Objects;

public class CartItem {
    private final ObjectProperty<Product> product;
    private final IntegerProperty quantity;

    public CartItem(Product product, int quantity) {
        this.product = new SimpleObjectProperty<>(product);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public Product getProduct() {
        return product.get();
    }

    public void setProduct(Product product) {
        this.product.set(product);
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getTotalPrice() {
        return getProduct().getPrice() * getQuantity();
    }

    public ReadOnlyDoubleProperty totalPriceProperty() {
        return new SimpleDoubleProperty(getTotalPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem that = (CartItem) o;
        return Objects.equals(getProduct().getId(), that.getProduct().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct().getId());
    }

}
