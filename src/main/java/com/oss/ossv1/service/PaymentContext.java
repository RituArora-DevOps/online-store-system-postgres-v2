package com.oss.ossv1.service;

import com.oss.ossv1.interfaces.PaymentStrategy;
import com.oss.ossv1.gui.model.PaymentModel;
import com.oss.ossv1.data.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PaymentContext {

    private final Map<String, PaymentStrategy> strategyMap = new HashMap<>();

    @Autowired
    public PaymentContext(List<PaymentStrategy> strategies) {
        for (PaymentStrategy strategy : strategies) {
            String key = strategy.getClass()
                                 .getAnnotation(Component.class)
                                 .value()
                                 .toLowerCase();
            strategyMap.put(key, strategy);
        }
    }

    public PaymentEntity createPayment(PaymentModel model) {
        String type = model.getPaymentMethod().toLowerCase();
        PaymentStrategy strategy = strategyMap.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment type: " + type);
        }

        return strategy.createPayment(model);
    }
}
