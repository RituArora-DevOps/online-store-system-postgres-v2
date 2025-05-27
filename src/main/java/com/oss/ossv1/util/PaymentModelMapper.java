package com.oss.ossv1.util;

import com.oss.ossv1.gui.model.PaymentModel;
import com.oss.ossv1.dto.PaymentRequestDTO;

public class PaymentModelMapper {

    public static PaymentModel fromDTO(PaymentRequestDTO dto, double amount) {
        PaymentModel model = new PaymentModel();
        model.setPaymentMethod(dto.getType());
        model.setCardNumber(dto.getCardNumber());
        model.setExpirationDate(dto.getExpirationDate());
        model.setCvv(dto.getCvv());
        model.setPaypalEmail(dto.getPaypalEmail());
        model.setAmount(amount);
        return model;
    }
}
