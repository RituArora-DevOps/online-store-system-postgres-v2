package com.oss.ossv1.controller;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The ProductController is a Spring REST controller that handles HTTP requests related to products.
 * It delegates all actual logic to the ProductService —
 * this is a common pattern in Spring Boot called the Service Layer Pattern,
 * which keeps your controller clean and focused only on request/response handling (or HTTP communication).
 *
 * Design Pattern:
 *1. Service Layer Pattern
 * Where: ProductController delegates to ProductService.
 * Why: Separates business logic from web/API logic.
 * Benefit: Keeps controller clean, improves testability and maintainability.
 *
 * 2. Repository Pattern (in the next layer)
 * Your ProductService likely uses a ProductRepository, which abstracts data access using Spring Data JPA.
 * This pattern decouples persistence logic from business logic.
 *
 *  * ProductController — adheres to SOLID Principles
 *  *
 *  * S - Single Responsibility: Only handles request/response mapping
 *  * O - Open/Closed: Can be extended with new endpoints (without modifying existing ones)
 *  * L - Liskov Substitution: Safely returns superclass (Product) that allows polymorphism
 *  * I - Interface Segregation: Delegates to ProductService which can be split as needed
 *  * D - Dependency Inversion: Depends on abstraction (ProductService), injected by Spring
 */
@RestController // Tells Spring this class handles HTTP REST endpoints and returns JSON.
@Validated // Enables validation for methods
@RequestMapping("/products") // All endpoints will be prefixed with /products.
public class ProductController {

    @Autowired // This injects (@Autowired) the ProductService bean so you can call its methods.
    private ProductService productService;

    // fetchProductsFromUrl("http://localhost:8080/products") call reaches this spring boot REST controller
    // This REST controller acts as the backend endpoint for the frontend (JavaFX)
    @GetMapping // Handles GET /products
    public List<Product> getAllProducts() {
        return (List<Product>) productService.listProducts(); // Calls productService.listProducts() which gets all products from the repository.
    }

    @GetMapping("/{id}") // Handles GET /products/{id} (e.g., /products/1)
    public Product getProduct(@PathVariable Integer id) { // @PathVariable binds the URL {id} to the method parameter.
        return productService.getProduct(id); // Calls service to fetch that product.
    }

    @PostMapping // Handles POST /products
    public Product createProduct(@RequestBody @Valid Product product) { // @RequestBody tells Spring to convert incoming JSON into a Product object.
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

    // Not used after introducing Strategy Pattern in JavaFX
//    @GetMapping("/category/{category}") // URL: GET /products/category/electronics
//    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) { // @PathVariable: Binds {category} from the URL to the method parameter.
//        return ResponseEntity.ok(productService.getProductsByCategory(category)); // Use case: Filters products where category matches exactly.
//    }
//    @GetMapping("/price") // URL: GET /products/price?min=50&max=200
//    public ResponseEntity<List<Product>> getByPriceRange(
//            @RequestParam double min, // @RequestParam: Grabs query parameters min and max from the URL.
//            @RequestParam double max
//    ) {
//        return ResponseEntity.ok(productService.getProductsByPriceRange(min, max)); // Use case: Filters products within a price range.
//    }

    // NOTE: When You Should Still Call /category or /price
    // When you want server-side filtering — especially for large datasets (e.g., thousands of products)
    // When user changes are expensive to compute client-side (e.g., complex queries, pagination, or security constraints)
    // When initial /products call is paginated or limited
    // You want to fetch pre-filtered data for performance
    // You build a REST-based web or mobile frontend in the future
}
