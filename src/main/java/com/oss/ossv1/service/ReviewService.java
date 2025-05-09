package com.oss.ossv1.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.ReviewRepository;

/**
 * Service for managing product reviews in the online store
 */
@Service
public class ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * Gets all reviews from the database
     */
    public List<ProductReview> getAllReviews() {
        logger.info("Fetching all reviews");
        List<ProductReview> reviews = reviewRepository.findAll();
        logger.info("Found {} reviews", reviews.size());
        if (reviews.isEmpty()) {
            logger.warn("No reviews found in the database");
        } else {
            for (ProductReview review : reviews) {
                logger.info("Review: id={}, productId={}, userId={}, rating={}", 
                    review.getId(), 
                    review.getProduct() != null ? review.getProduct().getId() : "null",
                    review.getUser() != null ? review.getUser().getId() : "null",
                    review.getRating());
            }
        }
        return reviews;
    }
    
    /**
     * Gets a specific review by ID
     */
    public ProductReview getReview(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }
    
    /**
     * Creates a new review with the provided review object
     */
    public ProductReview createReview(ProductReview review) {
        // Set the current time if not already set
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDateTime.now());
        }
        return reviewRepository.save(review);
    }
    
    /**
     * Creates a new review with individual fields
     */
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
    
    /**
     * Updates an existing review
     */
    public ProductReview updateReview(ProductReview review) {
        return reviewRepository.save(review);
    }
    
    /**
     * Deletes a review by ID
     */
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
    
    /**
     * Gets all reviews for a specific product
     */
    public List<ProductReview> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }
    
    /**
     * Gets all reviews by a specific user
     */
    public List<ProductReview> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }
}
