package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.OrderRepository;
import com.oss.ossv1.structural.CompositePattern.OrderComponent;
import com.oss.ossv1.structural.CompositePattern.OrderGroup;
import com.oss.ossv1.structural.CompositePattern.SingleOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderHistoryTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderHistory orderHistory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup mock product
        Product product = new Electronics();
        product.setName("Mock Laptop");
        product.setPrice(1500.00);

        // Setup mock order item
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        // Setup mock PayPalPayment
        PayPalPayment payment = new PayPalPayment();
        payment.setPaypalEmail("paypal@example.com");
        payment.setAmount(3000.00); // match expected order value

        // Setup mock order
        Order order = new Order();
        order.setOrderId(123L);
        order.setOrderDate(LocalDateTime.now());
        order.setItems(List.of(item));
        order.setPayment(payment);  // inject payment

        // Setup mock user
        User user = new User();
        user.setId(1);
        user.setUsername("mockuser");
        order.setUser(user);

        // Mock repository return
        when(orderRepository.findWithItemsByUserId(1L)).thenReturn(List.of(order));
    }

    @Test
    public void testCompositeOrderHistoryStructure() {
        Long testUserId = 1L;
        OrderComponent result = orderHistory.getUserOrderHistory(testUserId);

        assertNotNull(result, "Composite tree should not be null");
        assertTrue(result instanceof OrderGroup, "Root should be OrderGroup");

        OrderGroup group = (OrderGroup) result;
        assertFalse(group.getChildren().isEmpty(), "OrderGroup should contain orders");

        SingleOrder single = (SingleOrder) group.getChildren().get(0);
        assertNotNull(single.getOrder(), "SingleOrder should wrap a valid Order");

        Order order = single.getOrder();
        assertEquals(123L, order.getOrderId());

        assertNotNull(order.getPayment());
        assertEquals(3000.00, order.getPayment().getAmount());  // Correct way to test amount

        assertNotNull(order.getItems());
        assertEquals(1, order.getItems().size());

        OrderItem item = order.getItems().get(0);
        assertEquals("Mock Laptop", item.getProduct().getName());
        assertEquals(2, item.getQuantity());

        System.out.println("Composite Order Tree test passed using mocks.");
    }
}
