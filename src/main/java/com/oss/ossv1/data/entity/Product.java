package com.oss.ossv1.data.entity;

import com.oss.ossv1.interfaces.Discountable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*; // server-side validation using Jakarta Bean Validation annotations Hibernate Validator

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

// SRP (Single Responsibility Principle)
// This class only represents the core attributes and shared behavior of a product.
// It does NOT handle persistence, UI, or business logic beyond basic discounting.
// Good separation of concerns.
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Clothing.class, name = "clothing"),
        @JsonSubTypes.Type(value = Electronics.class, name = "electronics"),
        @JsonSubTypes.Type(value = Grocery.class, name = "grocery")
})
@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product implements Serializable, Discountable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer id;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be at most 100 characters")
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Size(max = 255, message = "Description must be at most 255 characters")
    @Column(name = "description", length = 255)
    private String description;

    @Min(value = 0, message = "Price must be non-negative")
    @Column(name = "price", nullable = false)
    private double price;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must be at most 50 characters")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Override
    public void applyDiscount(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }
        this.price = this.price - (this.price * (percent / 100.0));
    }

    @Override
    public double getDiscountedPrice(double percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }
        double discounted = this.price - (this.price * (percent / 100.0));
        // Use BigDecimal to round to 2 decimal places
        BigDecimal bd = BigDecimal.valueOf(discounted);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public abstract String displayInfo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return 31 + (id != null ? id.hashCode() : 0);
    }
}
