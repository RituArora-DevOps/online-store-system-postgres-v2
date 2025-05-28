package com.oss.ossv1.gui.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.beans.property.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"  // <- this must match the JSON "type" field
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Clothing.class, name = "clothing"),
        @JsonSubTypes.Type(value = Electronics.class, name = "electronics"),
        @JsonSubTypes.Type(value = Grocery.class, name = "grocery")
})
public class Product {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty category = new SimpleStringProperty();

    public Product() {}

    public Product(int id, String name, String description, double price, String category) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.price.set(price);
        this.category.set(category);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }

    public double getDiscountedPrice(double percent) {
        double priceValue = this.price.get();
        if (percent < 0 || percent > 100) return priceValue;
        return priceValue - (priceValue * (percent / 100.0));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }
}
