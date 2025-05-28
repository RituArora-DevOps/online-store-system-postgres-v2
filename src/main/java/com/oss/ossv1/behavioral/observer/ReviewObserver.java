package com.oss.ossv1.behavioral.observer;

/**
 * Observer interface for review notifications.
 * Implements the Observer pattern for real-time review updates.
 */
public interface ReviewObserver {
    /**
     * Called when a review is updated (added, edited, or deleted).
     * 
     * @param action The action performed (e.g., "Added", "Updated", "Deleted")
     * @param reviewDetails Details about the review and product
     */
    void onReviewUpdate(String action, String reviewDetails);
} 