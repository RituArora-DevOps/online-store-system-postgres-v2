package com.oss.ossv1.gui.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Grocery extends Product {

    private final SimpleObjectProperty<LocalDate> expiryDate = new SimpleObjectProperty<>(this, "expiryDate");

    public Grocery() {
        super();
    }

    public Grocery(int id, String name, String description, double price, LocalDate expiryDate) {
        super(id, name, description, price, "grocery");
        this.expiryDate.set(expiryDate);
    }

    public LocalDate getExpiryDate() {
        return expiryDate.get();
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate.set(expiryDate);
    }

    public ObjectProperty<LocalDate> expiryDateProperty() {
        return expiryDate;
    }

}
