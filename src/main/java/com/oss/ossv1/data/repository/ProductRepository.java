package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface gives you methods like:
 * save(product)
 * findById(id)
 * findAll()
 * deleteById(id)
 *
 * You can use them directly in your ProductService or ProductController.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // You don't need to add anything here
}
