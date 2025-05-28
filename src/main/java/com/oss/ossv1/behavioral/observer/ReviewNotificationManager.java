package com.oss.ossv1.behavioral.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * ReviewNotificationManager class that implements the Observer pattern as a Subject.
 * Manages review notifications and notifies observers about review changes.
 */
@Component
public class ReviewNotificationManager implements ReviewSubject {
    private final List<ReviewObserver> observers = new ArrayList<>();
    
    // Review data fields for tracking
    private String productId;
    private String userId;
    private String reviewText;
    private int rating;
    private LocalDateTime createdAt;
    
    /**
     * Default constructor for Spring
     */
    public ReviewNotificationManager() {
        this.createdAt = LocalDateTime.now();
    }
    
    /**
     * Constructor with review data
     */
    public ReviewNotificationManager(String productId, String userId, String reviewText, int rating) {
        this.productId = productId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }
    
    @Override
    public void registerObserver(ReviewObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer registered for review notifications");
        }
    }
    
    @Override
    public void removeObserver(ReviewObserver observer) {
        observers.remove(observer);
        System.out.println("Observer removed from review notifications");
    }
    
    @Override
    public void notifyObservers(String action, String reviewDetails) {
        System.out.println("=== OBSERVER PATTERN DEBUG ===");
        System.out.println("notifyObservers called with action: " + action);
        System.out.println("Number of registered observers: " + observers.size());
        System.out.println("Review details: " + reviewDetails);
        
        for (ReviewObserver observer : observers) {
            System.out.println("Notifying observer: " + observer.getClass().getSimpleName());
            observer.onReviewUpdate(action, reviewDetails);
        }
        System.out.println("=== END OBSERVER DEBUG ===");
    }
    
    /**
     * Add a new review and notify observers
     */
    public void addReview(String productId, String userId, String reviewText, int rating) {
        this.productId = productId;
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
        
        String details = String.format("Product: %s, User: %s, Rating: %d/5, Review: %s", 
            productId, userId, rating, reviewText);
        notifyObservers("REVIEW_ADDED", details);
    }
    
    /**
     * Update an existing review and notify observers
     */
    public void updateReview(String newReviewText, int newRating) {
        this.reviewText = newReviewText;
        this.rating = newRating;
        
        String details = String.format("Product: %s, User: %s, Updated Rating: %d/5, Updated Review: %s", 
            productId, userId, newRating, newReviewText);
        notifyObservers("REVIEW_UPDATED", details);
    }
    
    /**
     * Delete a review and notify observers
     */
    public void deleteReview() {
        String details = String.format("Product: %s, User: %s, Deleted Review (Rating was: %d/5)", 
            productId, userId, rating);
        notifyObservers("REVIEW_DELETED", details);
        
        // Clear the review data
        this.reviewText = null;
        this.rating = 0;
    }
    
    // Getters and setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("ReviewNotificationManager{productId='%s', userId='%s', rating=%d, createdAt=%s}", 
            productId, userId, rating, createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
} 