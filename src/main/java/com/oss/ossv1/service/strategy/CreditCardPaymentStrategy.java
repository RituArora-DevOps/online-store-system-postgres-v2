package com.oss.ossv1.service.strategy;

import com.oss.ossv1.interfaces.PaymentStrategy;
import com.oss.ossv1.data.entity.CreditCardPayment;
import com.oss.ossv1.data.entity.PaymentEntity;
import com.oss.ossv1.gui.model.PaymentModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("credit_card")
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentEntity createPayment(PaymentModel model) {
        CreditCardPayment payment = new CreditCardPayment();
        payment.setAmount(model.getAmount());
        payment.setCardNumber(model.getCardNumber());
        payment.setExpirationDate(model.getExpirationDate()); 
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }
}
