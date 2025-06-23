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

/**
 Entity: Abstract base class for all product types.
 * - This is a shared template for Clothing, Electronics, Grocery
 * - Defines the common fields and behaviors (name, price, category, etc.)
 *
 *  Design Patterns:
 * -  Inheritance Pattern: Used via @Inheritance(strategy = JOINED)
 * -  Strategy Pattern: via Discountable interface (dynamic discount behavior)
 * -  Polymorphic Deserialization: @JsonTypeInfo and @JsonSubTypes for correct subclassing during deserialization
 *
 *  SOLID Principles:
 * -  SRP: Only responsible for Product data and shared logic (e.g., discount)
 * -  OCP: Open for extension via subclassing (Clothing, Electronics, Grocery)
 * -  LSP: Subtypes can substitute Product without breaking correctness
 * -  ISP: Only implements relevant interface (`Discountable`)
 * -  DIP: Higher-level modules depend on abstraction (Product, not subtype directly)
* Uses Jakarta Bean Validation (e.g., @NotBlank, @Min) for input integrity
 */
// SRP (Single Responsibility Principle)
// This class only represents the core attributes and shared behavior of a product.
// It does NOT handle persistence, UI, or business logic beyond basic discounting.
// Good separation of concerns.
    // @JsonTypeInfo + @JsonSubTypes -	Ensure correct subclass is created during deserialization
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
@Inheritance(strategy = InheritanceType.JOINED) // Ensures each subclass has its own table (Avoids null fields in shared table ); normalized schema
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Product implements Serializable, Discountable {

    // Unique identifier for each product
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

    // Redundant
//    @Override
//    public void applyDiscount(double percent) {
//        if (percent < 0 || percent > 100) {
//            throw new IllegalArgumentException("Discount must be between 0 and 100.");
//        }
//        this.price = this.price - (this.price * (percent / 100.0));
//    }

    /**
     * 🎯 Strategy Pattern: Implementing dynamic discount logic via Discountable interface
     */
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

    /**
     * Abstract method to be implemented by subclasses — allows customization
     */
    public abstract String displayInfo();

    /**
     * Proper equals() and hashCode() implementation for data consistency
     */
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
