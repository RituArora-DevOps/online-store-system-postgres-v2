package com.oss.ossv1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.service.ProductService;
import com.oss.ossv1.service.ReviewService;

import jakarta.validation.Valid;

/**
 * Handle product review operations in the online store system.
 * Provides endpoints for creating, reading, updating, and deleting product reviews.
 */
@RestController
@Validated
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ProductService productService;

    /**
     * List all product reviews in the system.
     */
    @GetMapping
    public List<ProductReview> getAllReviews() {
        return reviewService.getAllReviews();
    }

    /**
     * Retrieves a specific review by its ID.
     */
    @GetMapping("/{id}")
    public ProductReview getReview(@PathVariable Integer id) {
        return reviewService.getReview(id);
    }

    /**
     * Creates a new product review.
     */
    @PostMapping
    public ProductReview createReview(@RequestBody @Valid ProductReview review) {
        return reviewService.createReview(review);
    }

    /**
     * Updates an existing review.
     */
    @PutMapping("/{id}")
    public ProductReview updateReview(@PathVariable Integer id, @RequestBody ProductReview review) {
        review.setId(id);
        return reviewService.updateReview(review);
    }

    /**
     * Deletes a review by its ID.
     */
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }

    /**
     * Retrieves all reviews for a specific product.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReview>> getByProduct(@PathVariable Integer productId) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.getReviewsByProduct(product));
    }
}
