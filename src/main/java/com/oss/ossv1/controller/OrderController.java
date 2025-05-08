package com.oss.ossv1.controller;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.dto.OrderRequestDTO;
import com.oss.ossv1.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
/**
 * Represents the OrderController class.
 */
public class OrderController {

    private final OrderService orderService;

    @PostMapping
/**
 * placeOrder method.
 */
    public ResponseEntity<Order> placeOrder(@RequestBody @Valid OrderRequestDTO request) {
        Order savedOrder = orderService.placeOrder(request);
        return ResponseEntity.status(201).body(savedOrder);
    }

    @GetMapping("/history")
/**
 * getOrderHistory method.
 */
    public ResponseEntity<List<Order>> getOrderHistory(@RequestParam Long userId) {
        List<Order> history = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(history);
    }
}
