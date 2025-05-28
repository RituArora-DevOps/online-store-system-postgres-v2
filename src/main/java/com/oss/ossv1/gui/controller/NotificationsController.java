package com.oss.ossv1.gui.controller;

import com.oss.ossv1.session.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Controller for the Notifications view.
 * Handles displaying live review notifications for admin users.
 */
public class NotificationsController {
    @FXML private ListView<String> notificationListView;
    @FXML private Label emptyStateLabel;
    
    private ObservableList<String> notifications;
    private DashboardController dashboardController;

    /**
     * Initialize the notifications view
     */
    @FXML
    public void initialize() {
        notifications = FXCollections.observableArrayList();
        notificationListView.setItems(notifications);
        
        // Set up empty state
        updateEmptyState();
        
        // Only load if user is admin
        if (UserSession.getInstance().getUser() != null && UserSession.getInstance().getUser().isAdmin()) {
            loadNotifications();
        }
    }

    /**
     * Set the dashboard controller reference and load existing notifications
     */
    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
        // Get notifications from dashboard controller
        if (dashboardController != null) {
            ObservableList<String> existingNotifications = dashboardController.getNotifications();
            if (existingNotifications != null) {
                notifications.setAll(existingNotifications);
                updateEmptyState();
            }
        }
    }

    /**
     * Handle clearing all notifications
     */
    @FXML
    private void handleClearNotifications() {
        notifications.clear();
        if (dashboardController != null) {
            dashboardController.clearNotifications();
        }
        updateEmptyState();
        System.out.println("All live notifications cleared by admin");
    }

    /**
     * Handle returning to dashboard
     */
    @FXML
    private void handleBack() {
        if (dashboardController != null) {
            dashboardController.loadDashboard();
        }
    }

    /**
     * Load existing notifications
     */
    private void loadNotifications() {
        // This will be populated when the dashboard controller is set
        updateEmptyState();
    }

    /**
     * Update the empty state visibility based on notification count
     */
    private void updateEmptyState() {
        boolean isEmpty = notifications.isEmpty();
        emptyStateLabel.setVisible(isEmpty);
        emptyStateLabel.setManaged(isEmpty);
        notificationListView.setVisible(!isEmpty);
    }

    /**
     * Add a notification to the list (called from dashboard controller)
     */
    public void addNotification(String notification) {
        notifications.add(0, notification); // Add to the top of the list
        updateEmptyState();
    }

    /**
     * Get the current notifications list
     */
    public ObservableList<String> getNotifications() {
        return notifications;
    }
} 