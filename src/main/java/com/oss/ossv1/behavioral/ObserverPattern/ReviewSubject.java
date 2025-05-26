package com.oss.ossv1.behavioral.ObserverPattern;

/**
 * Subject interface for the Observer pattern
 * Defines methods for attaching, detaching, and notifying observers
 */
public interface ReviewSubject {
    void connect(ReviewObserver observer);
    void disconnect(ReviewObserver observer);
    void notifyObservers(ProductReview review);
}