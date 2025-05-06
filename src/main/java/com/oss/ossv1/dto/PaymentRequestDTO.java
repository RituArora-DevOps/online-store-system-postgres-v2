package com.oss.ossv1.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {

    private String type; // "creditcard" or "paypal"

    // Credit card fields
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    // PayPal field
    private String paypalEmail;
}
