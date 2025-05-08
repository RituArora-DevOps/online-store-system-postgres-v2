package com.oss.ossv1.data.entity;

import com.oss.ossv1.interfaces.Payment;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PayPalPayment")
@PrimaryKeyJoinColumn(name = "id") // FK to PaymentEntity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PayPalPayment extends PaymentEntity implements Payment {

    @Column(name = "paypal_email", nullable = false, length = 100)
    @NotBlank(message = "PayPal email is required")
    @Email(message = "Invalid PayPal email format")
    private String paypalEmail;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Override
    public boolean processPayment(double amount) {
        this.setAmount(amount);
        this.setPaymentDate(LocalDateTime.now());
        System.out.println("Processed PayPal payment of $" + amount + " from " + paypalEmail);
        return true;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
