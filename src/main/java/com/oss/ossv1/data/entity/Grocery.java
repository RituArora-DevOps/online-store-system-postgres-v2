package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Grocery")
@PrimaryKeyJoinColumn(name = "product_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Grocery extends Product {

    @Column(name = "expiration_date")
    private LocalDate expiryDate;

    /**
     * Displays detailed information about the grocery product.
     * Includes inherited product details and the expiry date.
     */
    @Override
    public String displayInfo() {
        return "Grocery Product: " + getName() + "\n" +
                "Price: $" + String.format("%.2f", getPrice()) + "\n" +
                "Expiration Date: " + expiryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + "\n" +
                "Description: " + getDescription();
    }
}
