package com.oss.ossv1.dto;

import com.oss.ossv1.data.entity.OrderItem;
import com.oss.ossv1.data.entity.PaymentEntity;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {
    private Long userId;
    private List<OrderItem> orderItems;
    private PaymentEntity payment;
}
