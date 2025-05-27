package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.data.repository.*;
import com.oss.ossv1.dto.*;
import com.oss.ossv1.gui.model.PaymentModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private ProductRepository productRepository;
    @Mock private CreditCardPaymentRepository creditCardPaymentRepository;
    @Mock private PayPalPaymentRepository payPalPaymentRepository;
    @Mock private PaymentContext paymentContext;

    @InjectMocks private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder_withCreditCardPayment() {
        System.out.println("Starting test: CreditCardPayment");

        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(1L);

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(100L);
        itemDTO.setQuantity(2);
        request.setItems(List.of(itemDTO));

        PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
        paymentDTO.setType("creditcard");
        paymentDTO.setCardNumber("1234567890123456");
        paymentDTO.setExpirationDate("12/25");
        paymentDTO.setCvv("123");
        request.setPayment(paymentDTO);

        Product product = new Electronics();
        product.setId(100);
        product.setPrice(50.0);

        when(productRepository.findById(100)).thenReturn(Optional.of(product));

        CreditCardPayment mockPayment = new CreditCardPayment();
        mockPayment.setAmount(100.0);

        when(paymentContext.createPayment(any(PaymentModel.class))).thenReturn(mockPayment);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.placeOrder(request);

        System.out.println("Order processed. Verifying...");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(100.0, result.getItems().get(0).getPriceAtOrder() * result.getItems().get(0).getQuantity());

        verify(creditCardPaymentRepository).save(mockPayment);
        System.out.println("CreditCardPayment test passed.\n");
    }

    @Test
    public void testPlaceOrder_withPayPalPayment() {
        System.out.println("Starting test: PayPalPayment");

        OrderRequestDTO request = new OrderRequestDTO();
        request.setUserId(2L);

        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(200L);
        itemDTO.setQuantity(1);
        request.setItems(List.of(itemDTO));

        PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
        paymentDTO.setType("paypal");
        paymentDTO.setPaypalEmail("test@example.com");
        request.setPayment(paymentDTO);

        Product product = new Electronics();
        product.setId(200);
        product.setPrice(75.0);

        when(productRepository.findById(200)).thenReturn(Optional.of(product));

        PayPalPayment mockPayment = new PayPalPayment();
        mockPayment.setAmount(75.0);

        when(paymentContext.createPayment(any(PaymentModel.class))).thenReturn(mockPayment);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order result = orderService.placeOrder(request);

        System.out.println("Order processed. Verifying...");

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(75.0, result.getItems().get(0).getPriceAtOrder());

        verify(payPalPaymentRepository).save(mockPayment);
        System.out.println("PayPalPayment test passed.\n");
    }
}
