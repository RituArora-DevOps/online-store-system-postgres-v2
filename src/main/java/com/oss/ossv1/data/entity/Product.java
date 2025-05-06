package com.oss.ossv1.data.entity;

import com.oss.ossv1.interfaces.Discountable;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*; // server-side validation using Jakarta Bean Validation annotations Hibernate Validator

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

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
@Table(name = "Products")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
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

    public abstract String displayInfo();
}
