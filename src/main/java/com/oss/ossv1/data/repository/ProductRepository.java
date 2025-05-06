package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface gives you methods like:
 * - save(product)
 * - findById(id)
 * - findAll()
 * - deleteById(id)
 *
 * You can use them directly in your ProductService or ProductController.
 */
@Repository  // This annotation is optional for Spring Data repositories but adds clarity
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory(String category);

    List<Product> findByPriceBetween(double min, double max); // <-- Add this for price filtering
}
