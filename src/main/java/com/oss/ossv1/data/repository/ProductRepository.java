package com.oss.ossv1.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oss.ossv1.data.entity.Product;

/**
 * This interface gives you methods like:
 * save(product)
 * findById(id)
 * findAll()
 * deleteById(id)
 *
 * You can use them directly in your ProductService or ProductController.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    // You don't need to add anything here
}
