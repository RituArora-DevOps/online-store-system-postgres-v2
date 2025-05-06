package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.OrderRepository;
import com.oss.ossv1.data.repository.ProductRepository;
import com.oss.ossv1.dto.OrderRequestDTO;
import com.oss.ossv1.dto.OrderItemDTO;
import com.oss.ossv1.dto.PaymentRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(Math.toIntExact(itemDTO.getProductId()))
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setPriceAtOrder(product.getPrice());
            item.setOrder(order);

            items.add(item);
        }

        order.setItems(items);

        // Build the payment object directly here
        PaymentEntity payment = buildPayment(dto.getPayment());
        order.setPayment(payment);

        return orderRepository.save(order);
    }

    private PaymentEntity buildPayment(PaymentRequestDTO dto) {
        if ("creditcard".equalsIgnoreCase(dto.getType())) {
            CreditCardPayment cc = new CreditCardPayment();
            cc.setCardNumber(dto.getCardNumber());
            cc.setExpirationDate(dto.getExpirationDate());
            cc.setCvv(dto.getCvv());
            cc.setPaymentDate(LocalDateTime.now());
            cc.setAmount(0);
            return cc;
        } else if ("paypal".equalsIgnoreCase(dto.getType())) {
            PayPalPayment pp = new PayPalPayment();
            pp.setPaypalEmail(dto.getPaypalEmail());
            pp.setPaymentDate(LocalDateTime.now());
            pp.setAmount(0);
            return pp;
        } else {
            throw new IllegalArgumentException("Invalid payment type: " + dto.getType());
        }
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

}

