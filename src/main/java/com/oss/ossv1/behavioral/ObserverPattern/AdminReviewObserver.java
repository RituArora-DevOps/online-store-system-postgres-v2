package com.oss.ossv1.behavioral.ObserverPattern;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Concrete observer that handles admin notifications for new reviews
 */
public class AdminReviewObserver implements ReviewObserver {
    @Override
    public void update(ProductReview review) {
        // Show notification in JavaFX UI thread
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Review Notification");
            alert.setHeaderText("New Product Review");
            alert.setContentText(String.format(
                    "Product: %s\nUser: %s\nRating: %d/5\nComment: %s",
                    review.getProduct().getName(),
                    review.getUser().getUsername(),
                    review.getRating(),
                    review.getComment()
            ));
            alert.show();
        });
    }
}