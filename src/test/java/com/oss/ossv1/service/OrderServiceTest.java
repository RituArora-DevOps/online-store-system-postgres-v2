package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.OrderRepository;
import com.oss.ossv1.data.repository.ProductRepository;
import com.oss.ossv1.dto.OrderItemDTO;
import com.oss.ossv1.dto.OrderRequestDTO;
import com.oss.ossv1.dto.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Represents the { class.
 */
class OrderServiceTest {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(orderRepository, productRepository);
    }

    @Test
    void testPlaceOrder_withMultipleProductTypes_success() {
        OrderItemDTO item1 = new OrderItemDTO();
        item1.setProductId(101L);
        item1.setQuantity(1);

        OrderItemDTO item2 = new OrderItemDTO();
        item2.setProductId(102L);
        item2.setQuantity(3);

        PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
        paymentDTO.setType("paypal");
        paymentDTO.setPaypalEmail("buyer@test.com");

        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(7L);
        request.setItems(List.of(item1, item2));
        request.setPayment(paymentDTO);

        Electronics electronics = new Electronics();
        electronics.setId(101);
        electronics.setName("Headphones");
        electronics.setPrice(59.99);
        electronics.setWarrantyPeriod(24);

        Grocery grocery = new Grocery();
        grocery.setId(102);
        grocery.setName("Milk");
        grocery.setPrice(2.99);
        grocery.setExpiryDate(LocalDate.now().plusDays(7));

        when(productRepository.findById(101)).thenReturn(Optional.of(electronics));
        when(productRepository.findById(102)).thenReturn(Optional.of(grocery));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order savedOrder = orderService.placeOrder(request);

        assertNotNull(savedOrder);
        assertEquals(7L, savedOrder.getUserId());
        assertEquals(2, savedOrder.getItems().size());
        assertTrue(savedOrder.getItems().stream().anyMatch(i -> i.getProduct() instanceof Electronics));
        assertTrue(savedOrder.getItems().stream().anyMatch(i -> i.getProduct() instanceof Grocery));
        assertTrue(savedOrder.getPayment() instanceof PayPalPayment);

        // Print statements
        System.out.println("Saved Order for userId=7:");
        savedOrder.getItems().forEach(item -> System.out.println(
                " - Product: " + item.getProduct().getName() +
                        ", Type: " + item.getProduct().getClass().getSimpleName() +
                        ", Quantity: " + item.getQuantity()
        ));
        System.out.println("Payment Type: " + savedOrder.getPayment().getClass().getSimpleName());
    }

    @Test
    void testPlaceOrder_productNotFound_throwsException() {
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId(999L);
        item.setQuantity(1);

        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(3L);
        request.setItems(List.of(item));
        request.setPayment(new PaymentRequestDTO());

        when(productRepository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));
        System.out.println("Expected exception: " + ex.getMessage());
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void testGetOrdersByUserId_returnsList() {
        Order order1 = new Order();
        order1.setOrderId(1L);
        order1.setUserId(1L);
        Order order2 = new Order();
        order2.setOrderId(2L);
        order2.setUserId(1L);

        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order1, order2));

        List<Order> orders = orderService.getOrdersByUserId(1L);

        assertEquals(2, orders.size());
        assertEquals(1L, orders.get(0).getUserId());

        // Print order IDs
        System.out.println("Fetched Orders for userId=1:");
        orders.forEach(o -> System.out.println(" - Order ID: " + o.getOrderId()));
    }
}
