package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Validation Annotations - The validations only apply during request payload binding,
// such as when creating/updating via REST controller with @RequestBody.

@Entity
@Table(name = "Clothing")
@PrimaryKeyJoinColumn(name = "product_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Clothing extends Product {

    @Column(name = "size", length = 20)
    @NotBlank(message = "Size is required")
    @Size(max = 20, message = "Size must be less than or equal to 20 characters")
    private String size;

    @Column(name = "color", length = 50)
    @NotBlank(message = "Color is required")
    @Size(max = 50, message = "Color must be less than or equal to 50 characters")
    private String color;

    /**
     * Displays detailed information about the clothing product.
     * Includes inherited product details and clothing-specific attributes.
     */
    @Override
    public String displayInfo() {
        return "Clothing Product: " + getName() + "\n" +
                "Price: $" + String.format("%.2f", getPrice()) + "\n" +
                "Size: " + size + "\n" +
                "Color: " + color + "\n" +
                "Description: " + getDescription();
    }
}
