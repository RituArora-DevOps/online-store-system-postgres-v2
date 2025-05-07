package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.Order;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(Long userId);

    // Custom query to fetch items eagerly
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.userId = :userId")
    List<Order> findWithItemsByUserId(@Param("userId") Long userId);

    // For Order Details page
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.orderId = :orderId")
    Optional<Order> findWithItemsById(@Param("orderId") Long orderId);
}
