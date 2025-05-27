package com.oss.ossv1.interfaces;
import com.oss.ossv1.data.entity.PaymentEntity;
import com.oss.ossv1.gui.model.PaymentModel;

public interface PaymentStrategy {
    PaymentEntity createPayment(PaymentModel paymentModel);
}
