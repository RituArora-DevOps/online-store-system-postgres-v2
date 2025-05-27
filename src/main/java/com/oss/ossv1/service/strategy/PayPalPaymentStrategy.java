package com.oss.ossv1.service.strategy;

import com.oss.ossv1.interfaces.PaymentStrategy;
import com.oss.ossv1.data.entity.PayPalPayment;
import com.oss.ossv1.data.entity.PaymentEntity;
import com.oss.ossv1.gui.model.PaymentModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("paypal")
public class PayPalPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentEntity createPayment(PaymentModel model) {
        PayPalPayment payment = new PayPalPayment();
        payment.setAmount(model.getAmount());
        payment.setPaypalEmail(model.getPaypalEmail());
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }
}
