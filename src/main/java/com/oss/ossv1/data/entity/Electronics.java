package com.oss.ossv1.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Electronics")
@PrimaryKeyJoinColumn(name = "product_id") // maps inheritance to Product ID
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Electronics extends Product {

    @Column(name = "warranty_period")
    private int warrantyPeriod; // e.g. 24 months

    /**
     * Displays information about the electronic product.
     * Includes inherited product details and warranty period.
     */
    @Override
    public String displayInfo() {
        return "Electronics Product: " + getName() + "\n" +
                "Price: $" + String.format("%.2f", getPrice()) + "\n" +
                "Warranty: " + warrantyPeriod + " months\n" +
                "Description: " + getDescription();
    }

}
