package com.oss.ossv1.data.entity;

import com.oss.ossv1.interfaces.Payment;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

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
    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false, length = 5)
    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp = "(0[1-9]|1[0-2])\\/\\d{2}", message = "Expiration date must be in MM/YY format")
    private String expirationDate;

    @Column(name = "cvv", nullable = false, length = 4)
    @NotBlank(message = "CVV is required")
    @Pattern(regexp = "\\d{3,4}", message = "CVV must be 3 or 4 digits")
    private String cvv;

    // check PayPalPayment class for explanation
    //    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;

    @Override
    public boolean processPayment(double amount) {
        this.setAmount(amount);
        this.setPaymentDate(LocalDateTime.now());
        System.out.println("Processing credit card payment of $" + amount);
        return true; // simulate success
    }

    // check PayPalPayment class for explanation
//    public void setOrder(Order order) {
//        this.order = order;
//    }
//
//    public Order getOrder() {
//        return order;
//    }
}
