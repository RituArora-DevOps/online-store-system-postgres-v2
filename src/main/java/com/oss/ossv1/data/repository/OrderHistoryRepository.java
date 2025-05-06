package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHistoryRepository extends JpaRepository<Order, Integer> {
}
