package com.oss.ossv1.gui.model;

import javafx.beans.property.*;

public class Electronics extends Product {

    private IntegerProperty warrantyPeriod;

    public Electronics() {
        super();
        this.warrantyPeriod = new SimpleIntegerProperty();
    }

    public Electronics(int id, String name, String description, double price, String category, int warrantyPeriod) {
        super(id, name, description, price, "electronics"); // To prevent someone from doing "grocery"
        this.warrantyPeriod = new SimpleIntegerProperty(warrantyPeriod);
    }

    public int getWarrantyPeriod() { return this.warrantyPeriod.get(); }
    public IntegerProperty warrantyPeriodProperty() { return this.warrantyPeriod; }
    public void setWarrantyPeriod(int warrantyPeriod) { this.warrantyPeriod.set(warrantyPeriod); }

}
