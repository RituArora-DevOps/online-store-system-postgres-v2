package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.CreditCardPaymentRepository;
import com.oss.ossv1.data.repository.OrderRepository;
import com.oss.ossv1.data.repository.PayPalPaymentRepository;
import com.oss.ossv1.data.repository.ProductRepository;
import com.oss.ossv1.dto.OrderRequestDTO;
import com.oss.ossv1.dto.OrderItemDTO;
import com.oss.ossv1.dto.PaymentRequestDTO;
import com.oss.ossv1.gui.model.PaymentModel;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oss.ossv1.service.PaymentContext;
import com.oss.ossv1.util.PaymentModelMapper;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
/**
 * Represents the OrderService class.
 */
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CreditCardPaymentRepository creditCardPaymentRepository;

    @Autowired
    private PayPalPaymentRepository payPalPaymentRepository;

    @Autowired
    private PaymentContext paymentContext;

    @Transactional
/**
 * placeOrder method.
 */
    public Order placeOrder(OrderRequestDTO dto) {
        Order order = new Order();
        User user = new User();
        user.setId(Math.toIntExact(dto.getUserId()));
        order.setUser(user);
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

        // Calculate total
        double totalAmount = items.stream()
                .mapToDouble(i -> i.getPriceAtOrder() * i.getQuantity())
                .sum();

        // Convert and use strategy pattern
        PaymentRequestDTO paymentRequest = dto.getPayment();
        PaymentModel model = PaymentModelMapper.fromDTO(paymentRequest, totalAmount);
        PaymentEntity payment = paymentContext.createPayment(model);

        if (payment instanceof CreditCardPayment) {
            creditCardPaymentRepository.save((CreditCardPayment) payment);
        } else if (payment instanceof PayPalPayment) {
            payPalPaymentRepository.save((PayPalPayment) payment);
        }

        order.setPayment(payment);
        return orderRepository.save(order);
    }
    @Transactional
/**
 * saveOrder method.
 */
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }


/**
 * buildPayment method. (Replaced by PaymentContext + PaymentModelMapper for Strategy Pattern)
 */
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

/**
 * getOrdersByUserId method.
 */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findWithItemsByUserId(userId);
    }

/**
 * getOrderItemsByOrderId method.
 */
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderRepository.findWithItemsById(orderId)
                .map(Order::getItems)
                .orElse(new ArrayList<>());
    }

}

