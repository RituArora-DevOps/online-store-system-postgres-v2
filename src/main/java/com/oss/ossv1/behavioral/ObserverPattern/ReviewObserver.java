package com.oss.ossv1.behavioral.ObserverPattern;

/**
 * Observer interface for the Observer pattern
 * Defines the update method that gets called when a new review is added
 */
public interface ReviewObserver {
    void update(ProductReview review);
}