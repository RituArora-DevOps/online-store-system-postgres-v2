package com.oss.ossv1.data.entity;

import com.oss.ossv1.interfaces.Payment;
import jakarta.persistence.*;
import lombok.*;

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
    private String paypalEmail;

    @Override
    public boolean processPayment(double amount) {
        this.setAmount(amount);
        this.setPaymentDate(LocalDateTime.now());
        System.out.println("Processed PayPal payment of $" + amount + " from " + paypalEmail);
        return true;
    }
}
