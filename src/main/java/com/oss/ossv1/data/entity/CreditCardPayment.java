package com.oss.ossv1.data.entity;

import com.oss.ossv1.interfaces.Payment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit_card_payment")
@PrimaryKeyJoinColumn(name = "id") // FK to payment table
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditCardPayment extends PaymentEntity implements Payment {

    @Column(name = "card_number", nullable = false, length = 16)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false, length = 5) // MM/YY format
    private String expirationDate;

    @Column(name = "cvv", nullable = false, length = 4)
    private String cvv;

    @Override
    public boolean processPayment(double amount) {
        this.setAmount(amount);
        this.setPaymentDate(LocalDateTime.now());
        System.out.println("Processing credit card payment of $" + amount);
        return true; // simulate success
    }
}
