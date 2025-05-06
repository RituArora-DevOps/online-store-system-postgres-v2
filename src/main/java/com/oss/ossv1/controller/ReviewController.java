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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.service.ProductService;
import com.oss.ossv1.service.ReviewService;

import jakarta.validation.Valid;

/**
 * The ReviewController is a Spring REST controller that handles HTTP requests related to product reviews.
 * It follows the same pattern as ProductController, delegating all business logic to the ReviewService
 * following the Service Layer Pattern.
 */
@RestController
@Validated
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductReview> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ProductReview getReview(@PathVariable Integer id) {
        return reviewService.getReview(id);
    }

    @PostMapping
    public ProductReview createReview(@RequestBody @Valid ProductReview review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    public ProductReview updateReview(@PathVariable Integer id, @RequestBody ProductReview review) {
        review.setId(id);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReview>> getByProduct(@PathVariable Integer productId) {
        Product product = productService.getProduct(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.getReviewsByProduct(product));
    }

    @GetMapping("/rating")
    public ResponseEntity<List<ProductReview>> getByMinimumRating(
            @RequestParam int minRating
    ) {
        if (minRating < 1 || minRating > 5) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reviewService.getReviewsByMinRating(minRating));
    }
}
