package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Represents the Order class.
 */
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

//    @NotNull(message = "User ID cannot be null")
//    @Column(nullable = false)
//    private Long userId;
    @NotNull(message = "User cannot be null")  // A user must always be associated with an order
    @ManyToOne // Each order belongs to one user
    @JoinColumn(name = "user_id", nullable = false) // The column user_id in the orders table is the foreign key that points to the users table
    private User user;

    @NotNull(message = "Order date cannot be null")
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @NotEmpty(message = "Order must contain at least one item")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @NotNull(message = "Payment cannot be null")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    // Added method for JavaFX table binding
/**
 * getTotalAmount method.
 */
    public double getTotalAmount() {
        return items != null
                ? items.stream().mapToDouble(OrderItem::getSubtotal).sum()
                : 0.0;
    }
}
