package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Instead of calling the repository directly from the controller,
 * the controller calls the service,
 * and the service calls the repository.
 */
@Service // Tells Spring that this class is a service component,
    // so it will be auto-discovered and managed by Spring’s container.
public class ProductService {

    @Autowired // This injects (@Autowired) the ProductRepository (your data access layer)
        // so the service can use it to interact with the database.
    private ProductRepository productRepository;
    // DIP: Depends on abstraction (ProductRepository interface), not on a concrete implementation.
    // Spring injects the concrete proxy, which honors the Dependency Inversion Principle.

    // SRP: This method fetches all products — clearly within the business logic responsibility.
    public Iterable<Product> listProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Integer id) {
        return productRepository.findById(id).orElse(null); // safer than .get()
        // SRP: Retrieval logic with null safety — clear responsibility.
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
        // SRP: Responsible for saving product — not handling UI, request parsing, etc.
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
        // OCP: Extensible — if we add `findByBrand()` or `findByRating()`, no existing code breaks.
    }
    public List<Product> getProductsByPriceRange(double min, double max) {
        return productRepository.findByPriceBetween(min, max);
    }
}

// When you extend JpaRepository<Product, Integer>,
// you automatically inherit a huge set of CRUD methods
// like findAll(), save(), deleteById(), etc.
// without writing any SQL or JPQL yourself.
// public interface ProductRepository extends JpaRepository<Product, Integer> {
//}
