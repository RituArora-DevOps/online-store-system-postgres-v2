package com.oss.ossv1.behavioral.observer;

/**
 * Subject interface for review notifications.
 * Implements the Observer pattern for managing review observers.
 */
public interface ReviewSubject {
    /**
     * Register an observer to receive review notifications.
     * 
     * @param observer The observer to register
     */
    void registerObserver(ReviewObserver observer);
    
    /**
     * Remove an observer from receiving review notifications.
     * 
     * @param observer The observer to remove
     */
    void removeObserver(ReviewObserver observer);
    
    /**
     * Notify all registered observers about a review update.
     * 
     * @param action The action performed
     * @param reviewDetails Details about the review
     */
    void notifyObservers(String action, String reviewDetails);
} 