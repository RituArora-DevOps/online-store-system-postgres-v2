package com.oss.ossv1.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oss.ossv1.data.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Find cart by user ID
    Cart findByUserId(Integer userId);
}
