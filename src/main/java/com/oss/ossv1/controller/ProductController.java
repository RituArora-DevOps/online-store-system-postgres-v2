package com.oss.ossv1.controller;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The ProductController is a Spring REST controller that handles HTTP requests related to products.
 * It delegates all actual logic to the ProductService —
 * this is a common pattern in Spring Boot called the Service Layer Pattern,
 * which keeps your controller clean and focused only on request/response handling.
 */
@RestController // Tells Spring this class handles HTTP REST endpoints and returns JSON.
@RequestMapping("/products") // All endpoints will be prefixed with /products.
public class ProductController {

    @Autowired // This injects (@Autowired) the ProductService bean so you can call its methods.
    private ProductService productService;

    @GetMapping // Handles GET /products
    public List<Product> getAllProducts() {
        return (List<Product>) productService.listProducts(); // Calls productService.listProducts() which gets all products from the repository.
    }

    @GetMapping("/{id}") // Handles GET /products/{id} (e.g., /products/1)
    public Product getProduct(@PathVariable Integer id) { // @PathVariable binds the URL {id} to the method parameter.
        return productService.getProduct(id); // Calls service to fetch that product.
    }

    @PostMapping // Handles POST /products
    public Product createProduct(@RequestBody Product product) { // @RequestBody tells Spring to convert incoming JSON into a Product object.
        return productService.createProduct(product); // Then it's passed to the service to be saved.
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    @DeleteMapping
    public void deleteAllProducts() {
        productService.deleteAllProducts();
    }
}
