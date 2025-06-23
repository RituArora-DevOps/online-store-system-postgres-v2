package com.oss.ossv1.data.repository;

import com.oss.ossv1.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository — Data Access Layer
 *
 * Design Pattern: Repository Pattern
 * This interface abstracts database interactions, making code more modular and testable.
 *
 * The Repository Pattern:
 * Abstracts access to the database
 *** Allows you to change data sources (MySQL → MongoDB) with minimal impact
 * Encourages decoupling between business logic and data access
 *
 * SOLID:
 * S — Only handles data access.
 * D — Controller and Service depend on this abstraction.
 *
 * This interface gives you methods like:
 * - save(product)
 * - findById(id)
 * - findAll()
 * - deleteById(id)
 *
 * You can use them directly in your ProductService or ProductController.
 * Here, JpaRepository<T, ID> is a Spring Data JPA interface
 *** that provides built-in CRUD methods so you don’t have to write boilerplate SQL or JPQL.
 */
@Repository  // This annotation is optional for Spring Data repositories but adds clarity
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // These custom methods are redundant after introducing Search Strategy Pattern
//    List<Product> findByCategory(String category);
//
//    List<Product> findByPriceBetween(double min, double max); // <-- Add this for price filtering
}

