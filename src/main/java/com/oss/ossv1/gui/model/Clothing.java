package com.oss.ossv1.gui.model;

import javafx.beans.property.*;

public class Clothing extends Product {
    private final StringProperty size = new SimpleStringProperty();
    private final StringProperty color = new SimpleStringProperty();

    public Clothing() {}

    public Clothing(int id, String name, String description, double price, String size, String color) {
        super(id, name, description, price, "clothing");
        this.size.set(size);
        this.color.set(color);
    }

    public String getSize() { return size.get(); }
    public void setSize(String size) { this.size.set(size); }
    public StringProperty sizeProperty() { return size; }

    public String getColor() { return color.get(); }
    public void setColor(String color) { this.color.set(color); }
    public StringProperty colorProperty() { return color; }
}
