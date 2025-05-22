package com.oss.ossv1.config;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class OrderDataInitializer {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CreditCardPaymentRepository creditCardPaymentRepository;

    @PostConstruct
    public void seedOrders() {
        if (orderRepository.count() > 0) return;

        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (users.size() < 3 || products.isEmpty()) {
            System.err.println("Insufficient users/products for seeding orders.");
            return;
        }

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            User user = users.get(random.nextInt(users.size()));

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());

            List<OrderItem> items = new ArrayList<>();
            for (int j = 0; j < random.nextInt(3) + 1; j++) {
                Product product = products.get(random.nextInt(products.size()));
                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(random.nextInt(3) + 1);
                item.setPriceAtOrder(product.getPrice());
                item.setOrder(order);
                items.add(item);
            }

            order.setItems(items);

            CreditCardPayment payment = new CreditCardPayment();
            payment.setAmount(order.getTotalAmount());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setCardNumber("1234567812345678");
            payment.setExpirationDate("12/25");
            payment.setCvv("123");

            order.setPayment(payment);
            payment.setAmount(order.getTotalAmount());

            orderRepository.save(order);
        }

        System.out.println(" Seeded 20 sample orders with items and payments.");
    }
}
