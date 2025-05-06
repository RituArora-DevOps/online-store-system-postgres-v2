package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // You can add custom queries here later if needed
}
