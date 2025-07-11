package com.oss.ossv1.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "electronics")
@PrimaryKeyJoinColumn(name = "product_id") // maps inheritance to Product ID // // OCP: Inherits from Product without modification
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Electronics extends Product {

    @Column(name = "warranty_period")
    @Min(value = 0, message = "Warranty period must be a non-negative number")
    private int warrantyPeriod; // SRP: Only adds electronics-specific field

    /**
     * Displays information about the electronic product.
     * Includes inherited product details and warranty period.
     * OCP: Overriding behavior via polymorphism, without modifying base class.
     * LSP: Fully supports substitution — behaves like a Product without breaking logic.
     */
    
    @Override
    public String displayInfo() {
        return "Electronics Product: " + getName() + "\n" +
                "Price: $" + String.format("%.2f", getPrice()) + "\n" +
                "Warranty: " + warrantyPeriod + " months\n" +
                "Description: " + getDescription();
    }

}
