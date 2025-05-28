package com.oss.ossv1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oss.ossv1.behavioral.observer.ReviewNotificationManager;
import com.oss.ossv1.behavioral.observer.ReviewObserver;

/**
 * Service for managing live review notifications using the Observer Pattern
 * Provides real-time notifications without database persistence
 */
@Service
public class NotificationService {
    
    @Autowired
    private ReviewNotificationManager reviewNotificationManager;
    
    /**
     * Register an observer to receive review notifications
     */
    public void registerObserver(ReviewObserver observer) {
        reviewNotificationManager.registerObserver(observer);
    }
    
    /**
     * Remove an observer from receiving review notifications
     */
    public void removeObserver(ReviewObserver observer) {
        reviewNotificationManager.removeObserver(observer);
    }
    
    /**
     * Add a new review and notify all observers (live notifications only)
     */
    public void addReview(String productId, String userId, String reviewText, int rating) {
        // Trigger live notifications through Observer Pattern
        reviewNotificationManager.addReview(productId, userId, reviewText, rating);
    }
    
    /**
     * Update an existing review and notify all observers (live notifications only)
     */
    public void updateReview(String newReviewText, int newRating) {
        reviewNotificationManager.updateReview(newReviewText, newRating);
    }
    
    /**
     * Delete a review and notify all observers (live notifications only)
     */
    public void deleteReview() {
        reviewNotificationManager.deleteReview();
    }
} 