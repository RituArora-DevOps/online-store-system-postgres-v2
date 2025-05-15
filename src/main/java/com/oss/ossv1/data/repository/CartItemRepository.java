package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.Cart;
import com.oss.ossv1.data.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(Cart cart);
}

