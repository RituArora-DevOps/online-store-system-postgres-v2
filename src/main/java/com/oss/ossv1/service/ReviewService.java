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
    
    // Database connection for reviews
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * Gets all reviews from the database
     * @return List of all reviews
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
     * Gets a specific review using its ID
     * @param id The ID of the review to find
     * @return The review if found, null if not found
     */
    public ProductReview getReview(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }
    
    /**
     * Creates a new review in the database
     * @param review The review to save
     * @return The saved review with its new ID
     */
    public ProductReview createReview(ProductReview review) {
        // Make sure we set the current time for new reviews
        if (review.getReviewDate() == null) {
            review.setReviewDate(LocalDateTime.now());
        }
        return reviewRepository.save(review);
    }
    
    /**
     * Creates a new review with individual fields
     * @param product The product being reviewed
     * @param user The user writing the review
     * @param rating The rating (1-5)
     * @param comment The review comment
     * @return The saved review
     */
    public ProductReview createReview(Product product, User user, int rating, String comment) {
        // Check that rating is between 1 and 5
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        // Create a new review
        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());
        
        // Save it to database
        return reviewRepository.save(review);
    }
    
    /**
     * Updates an existing review
     * @param review The review with updated information
     * @return The updated review
     */
    public ProductReview updateReview(ProductReview review) {
        return reviewRepository.save(review);
    }
    
    /**
     * Deletes a review from the database
     * @param id The ID of the review to delete
     */
    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }
    
    /**
     * Gets all reviews for a specific product
     * @param product The product to find reviews for
     * @return List of reviews for that product
     */
    public List<ProductReview> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }
    
    /**
     * Gets all reviews written by a specific user
     * @param user The user to find reviews for
     * @return List of reviews by that user
     */
    public List<ProductReview> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }
}
