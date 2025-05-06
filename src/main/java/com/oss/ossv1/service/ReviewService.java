package com.oss.ossv1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.ReviewRepository;

// Service for managing product reviews - business logic layer
@Service
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    // Get all reviews
    public List<ProductReview> getAllReviews() {
        return reviewRepository.findAll();
    }
    
    // Get a review by ID
    public ProductReview getReview(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }
    
    // Create a new product review
    public ProductReview createReview(ProductReview review) {
        // Set the current time if not already set
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDateTime.now());
        }
        return reviewRepository.save(review);
    }
    
    // Create a review with individual fields
    public ProductReview createReview(Product product, User user, int rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());
        
        return reviewRepository.save(review);
    }
    
    // Update an existing review
    public ProductReview updateReview(ProductReview review) {
        return reviewRepository.save(review);
    }
    
    // Delete a review
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
    
    // Find reviews by product
    public List<ProductReview> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }
    
    // Find reviews by user
    public List<ProductReview> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }
    
    // Get reviews with minimum rating
    public List<ProductReview> getReviewsByMinRating(int rating) {
        return reviewRepository.findByRatingGreaterThanEqual(rating);
    }
}
