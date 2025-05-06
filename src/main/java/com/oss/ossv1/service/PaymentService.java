package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.CreditCardPaymentRepository;
import com.oss.ossv1.data.repository.PayPalPaymentRepository;
import com.oss.ossv1.data.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Payments (CreditCardPayment, PayPalPayment) are handled polymorphically
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PayPalPaymentRepository payPalPaymentRepository;

    @Autowired
    private CreditCardPaymentRepository creditCardPaymentRepository;

    public PaymentEntity getPaymentById(Integer id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public PayPalPayment createPayPalPayment(PayPalPayment payment) {
        payment.processPayment(payment.getAmount());
        return payPalPaymentRepository.save(payment);
    }

    public CreditCardPayment createCreditCardPayment(CreditCardPayment payment) {
        payment.processPayment(payment.getAmount());
        return creditCardPaymentRepository.save(payment);
    }
}
