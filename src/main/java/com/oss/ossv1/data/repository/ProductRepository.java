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

/**
| Principle   | Status | Notes                                                                                                                                                   |
| ----------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **S (SRP)** | ✅      | This interface has only one responsibility: querying the database. It doesn't contain business logic or validation.                                     |
| **O (OCP)** | ✅      | You can extend it by adding more derived query methods (`findByName`, `findByRating`, etc.) without modifying existing methods.                         |
| **L (LSP)** | ✅      | It extends `JpaRepository<Product, Integer>`, and Spring can substitute it for any interface requiring `CrudRepository` or `JpaRepository` behavior.    |
| **I (ISP)** | ✅      | It only declares methods that are relevant to the persistence of `Product`. No extra methods are forced.                                                |
| **D (DIP)** | ✅      | `ProductService` depends on this interface, not a concrete implementation. Spring injects it via proxy. This is classic Dependency Inversion in action. |
*/
