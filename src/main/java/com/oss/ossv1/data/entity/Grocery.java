package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "grocery")
@PrimaryKeyJoinColumn(name = "product_id") // OCP: This class extends Product — no need to modify the Product class when adding a Grocery type.
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grocery extends Product {

    @Column(name = "expiration_date")
    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expiryDate; // SRP: Adds grocery-specific field only; class responsibility remains clear and focused.


    /**
     * Displays detailed information about the grocery product.
     * Includes inherited product details and the expiry date.
     * OCP: Custom display behavior implemented via polymorphism.
     * LSP: Grocery behaves like a Product — fully substitutable without surprises.
     */
    @Override
    public String displayInfo() {
        return "Grocery Product: " + getName() + "\n" +
                "Price: $" + String.format("%.2f", getPrice()) + "\n" +
                "Expiration Date: " + expiryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + "\n" +
                "Description: " + getDescription();
    }
}
