package com.oss.ossv1.behavioral.ObserverPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oss.ossv1.data.entity.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Manages product reviews and observers using the Observer pattern
 */
public class ReviewManager implements ReviewSubject {
    private List<ReviewObserver> observers;
    private Map<Product, List<ProductReview>> productReviews;
    private ObservableList<ProductReview> recentReviews;

    public ReviewManager() {
        observers = new ArrayList<>();
        productReviews = new HashMap<>();
        recentReviews = FXCollections.observableArrayList();
    }

    @Override
    public void attach(ReviewObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(ReviewObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ProductReview review) {
        for (ReviewObserver observer : observers) {
            observer.update(review);
        }
    }

    public void addReview(ProductReview review) {
        Product product = review.getProduct();
        productReviews.computeIfAbsent(product, k -> new ArrayList<>()).add(review);
        recentReviews.add(0, review); // Add to the beginning of recent reviews
        if (recentReviews.size() > 10) {
            recentReviews.remove(10); // Keep only 10 most recent reviews
        }
        notifyObservers(review);
    }

    public List<ProductReview> getProductReviews(Product product) {
        return new ArrayList<>(productReviews.getOrDefault(product, new ArrayList<>()));
    }

    public ObservableList<ProductReview> getRecentReviews() {
        return recentReviews;
    }

    public double getAverageRating(Product product) {
        List<ProductReview> reviews = getProductReviews(product);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(ProductReview::getRating)
                .average()
                .orElse(0.0);
    }
}