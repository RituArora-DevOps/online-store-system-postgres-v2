package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "OrderItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Represents the OrderItem class.
 */
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order cannot be null")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Product cannot be null")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private int quantity;

    @PositiveOrZero(message = "Price must be 0 or more")
    @Column(nullable = false)
    private double priceAtOrder;

    // --- GUI-specific getters ---

/**
 * getProductName method.
 */
    public String getProductName() {
        return product != null ? product.getName() : "N/A";
    }

/**
 * getSubtotal method.
 */
    public double getSubtotal() {
        return quantity * priceAtOrder;
    }

}
