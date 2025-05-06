package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public Iterable<Product> listProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Integer id) {
        return productRepository.findById(id).orElse(null); // safer than .get()
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
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
}

// When you extend JpaRepository<Product, Integer>,
// you automatically inherit a huge set of CRUD methods
// like findAll(), save(), deleteById(), etc.
// without writing any SQL or JPQL yourself.
// public interface ProductRepository extends JpaRepository<Product, Integer> {
//}