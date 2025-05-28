package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.oss.ossv1.LoginPage;
import com.oss.ossv1.behavioral.observer.ReviewObserver;
import com.oss.ossv1.service.OrderHistory;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.service.ProductService;
import com.oss.ossv1.service.ReviewService;
import com.oss.ossv1.session.UserSession;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controls the main dashboard view of the online store.
 * Manages the navigation bar and implements ReviewObserver for admin notifications.
 */
public class DashboardController implements ReviewObserver {
    // Main layout components
    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Label dashboardWelcomeLabel;
    @FXML private final Map<String, Parent> viewCache = new HashMap<>();
    @FXML private Button notificationsButton;
    @FXML private ListView<String> notificationListView;
    @FXML private VBox notificationPanel;
    
    private ObservableList<String> notifications;

    /**
     * Initializes the dashboard view.
     * Sets up the welcome message and notification system for admin users.
     */
    @FXML
    public void initialize() {
        rootPane.setUserData(this);
        updateWelcomeMessage();
        loadDashboard();
        
        // Initialize notifications if user is admin
        if (UserSession.getInstance().getUser() != null && UserSession.getInstance().getUser().isAdmin()) {
            initializeNotifications();
        } else {
            hideNotificationFeatures();
        }
    }
    
    /**
     * Initialize notification system for admin users
     */
    private void initializeNotifications() {
        notifications = FXCollections.observableArrayList();
        if (notificationListView != null) {
            notificationListView.setItems(notifications);
        }
        if (notificationsButton != null) {
            notificationsButton.setVisible(true);
        }
        if (notificationPanel != null) {
            notificationPanel.setVisible(true);
        }
        
        // Register this controller as an observer for review notifications
        try {
            com.oss.ossv1.service.NotificationService notificationService = 
                LoginPage.springContext.getBean(com.oss.ossv1.service.NotificationService.class);
            notificationService.registerObserver(this);
            
        } catch (Exception e) {
            System.err.println("Failed to register observer: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add a welcome notification
        addNotification("SYSTEM", "Admin notification system initialized");
    }
    
    /**
     * Hide notification features for non-admin users
     */
    private void hideNotificationFeatures() {
        if (notificationsButton != null) {
            notificationsButton.setVisible(false);
        }
        if (notificationPanel != null) {
            notificationPanel.setVisible(false);
        }
    }

    // Helper method to update welcome message
    private void updateWelcomeMessage() {
        if (UserSession.getInstance().isLoggedIn()) {
            String username = UserSession.getInstance().getUser().getUsername();
            String role = UserSession.getInstance().getUser().isAdmin() ? "administrator" : "user";
            dashboardWelcomeLabel.setText("Welcome, " + username + " (" + role + ")");
        } else {
            dashboardWelcomeLabel.setText("Welcome to Online Store System");
        }
    }

    /**
     * Loads the main dashboard grid with navigation tiles.
     * Creates clickable tiles for Products, Cart, Orders, and Profile.
     */
    @FXML
    public void loadDashboard() {
        try {
            GridPane dashboardGrid = new GridPane();
            dashboardGrid.setAlignment(javafx.geometry.Pos.CENTER);
            dashboardGrid.setHgap(20);
            dashboardGrid.setVgap(20);
            dashboardGrid.setPadding(new javafx.geometry.Insets(40));

            VBox productsTile = createDashboardTile("Products", "Browse and manage products");
            productsTile.setOnMouseClicked(e -> navigateToProducts());
            dashboardGrid.add(productsTile, 0, 0);

            VBox cartTile = createDashboardTile("Shopping Cart", "View and manage your cart");
            cartTile.setOnMouseClicked(e -> navigateToCart());
            dashboardGrid.add(cartTile, 1, 0);

            VBox ordersTile = createDashboardTile("Orders", "Track your orders");
            ordersTile.setOnMouseClicked(e -> navigateToOrders());
            dashboardGrid.add(ordersTile, 0, 1);

            VBox profileTile = createDashboardTile("Profile", "Manage your account");
            profileTile.setOnMouseClicked(e -> navigateToProfile());
            dashboardGrid.add(profileTile, 1, 1);

            // Add notification tile for admin users
            if (UserSession.getInstance().getUser() != null && UserSession.getInstance().getUser().isAdmin()) {
                VBox notificationsTile = createDashboardTile("Notifications", "View review notifications");
                notificationsTile.setOnMouseClicked(e -> showNotifications());
                dashboardGrid.add(notificationsTile, 0, 2);
                
                VBox testTile = createDashboardTile("Test Notifications", "Generate sample notifications");
                testTile.setOnMouseClicked(e -> testNotifications());
                dashboardGrid.add(testTile, 1, 2);
                
                VBox testObserverTile = createDashboardTile("Test Observer", "Test Observer Pattern connection");
                testObserverTile.setOnMouseClicked(e -> testObserverConnection());
                dashboardGrid.add(testObserverTile, 0, 3);
            }

            dashboardGrid.getStyleClass().add("dashboard-grid");

            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardGrid);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Dashboard Error", null, "Unable to load dashboard.");
        }
    }

    /**
     * Creates a styled tile for dashboard navigation.
     */
    private VBox createDashboardTile(String title, String description) {
        VBox tile = new VBox(10);
        tile.getStyleClass().add("dashboard-tile");
        tile.setPadding(new javafx.geometry.Insets(20));
        tile.setMinSize(200, 150);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("tile-title");

        Text descText = new Text(description);
        descText.getStyleClass().add("tile-description");

        tile.getChildren().addAll(titleLabel, descText);
        return tile;
    }

    // Navigation methods
    @FXML
    public void navigateToProducts() {
        loadView("/views/ProductView.fxml");
    }

    @FXML
    public void navigateToCart() {
        loadView("/views/CartView.fxml");
    }

    /**
     * Loads the order history view and injects necessary services.
     * Ensures order data is properly loaded before display.
     */
    @FXML
    public void navigateToOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderHistoryView.fxml"));
            Parent root = loader.load();

            // Inject OrderService to ensure order list is loaded
            OrderHistoryController controller = loader.getController();
            controller.setOrderService(LoginPage.springContext.getBean(OrderService.class));
            controller.setOrderHistory(LoginPage.springContext.getBean(OrderHistory.class)); // Composite integration

            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", null, "Unable to load Order History.");
        }
    }

    @FXML
    public void navigateToProfile() {
        showNotImplementedAlert("Profile functionality");
    }

    /**
     * Handles user logout.
     * Clears the session and returns to login screen.
     */
    @FXML
    public void handleLogout() {
        UserSession.getInstance().clear();

        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
            Scene loginScene = new Scene(loginRoot);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Logout Error", null, "Unable to return to login screen.");
        }
    }

    @FXML
    private void navigateToProductReviews() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductReviewView.fxml"));
            Parent reviewView = loader.load();

            // Get the controller and inject services
            ProductReviewController controller = loader.getController();
            controller.setReviewService(LoginPage.springContext.getBean(ReviewService.class));
            controller.setProductService(LoginPage.springContext.getBean(ProductService.class));
            controller.setOrderService(LoginPage.springContext.getBean(OrderService.class));

            contentArea.getChildren().clear();
            contentArea.getChildren().add(reviewView);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Could not load product reviews view", "Failed to load the product reviews screen.");
        }
    }

    // Added by RA- to cache views/controllers in DashboardController so they're only loaded once.
    public void loadView(String fxml) {
        try {
            Parent view;
            if (viewCache.containsKey(fxml)) {
                view = viewCache.get(fxml);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                view = loader.load();
                viewCache.put(fxml, view);

                if (fxml.contains("ProductView")) {
                    ProductController pc = loader.getController();
                    System.out.println("Loaded ProductController once: " + pc);
                }
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", null, "Unable to load view.");
        }
    }

    /**
     * Sets content directly in the dashboard's content area.
     * Used by child controllers to update the main view.
     */
    public void setContent(Parent view) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(view);
    }

    private void showNotImplementedAlert(String feature) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText(null);
        alert.setContentText(feature + " will be implemented in a future update.");
        alert.showAndWait();
    }

    /**
     * Shows an error message to the user
     */
    private void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Notification methods for admin users
    
    /**
     * Show notifications view for admin users
     */
    @FXML
    public void showNotifications() {
        if (UserSession.getInstance().getUser() != null && UserSession.getInstance().getUser().isAdmin()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NotificationsView.fxml"));
                Parent notificationsView = loader.load();
                
                NotificationsController controller = loader.getController();
                controller.setDashboardController(this);
                
                contentArea.getChildren().clear();
                contentArea.getChildren().add(notificationsView);
            } catch (IOException e) {
                e.printStackTrace();
                showError("Navigation Error", null, "Unable to load notifications view.");
            }
        }
    }
    
    /**
     * Get the notifications list for admin users
     */
    public ObservableList<String> getNotifications() {
        return notifications != null ? notifications : FXCollections.observableArrayList();
    }
    
    /**
     * Clear all notifications
     */
    public void clearNotifications() {
        if (notifications != null) {
            notifications.clear();
        }
    }
    
    /**
     * Add a notification manually
     */
    public void addNotification(String action, String details) {
        if (notifications != null) {
            Platform.runLater(() -> {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String notification = String.format("[%s] %s: %s", timestamp, action, details);
                notifications.add(0, notification); // Add to the top
            });
        }
    }

    /**
     * Implementation of ReviewObserver interface
     * Called when a review is updated (added, edited, or deleted)
     */
    @Override
    public void onReviewUpdate(String action, String reviewDetails) {
        if (UserSession.getInstance().getUser() != null && UserSession.getInstance().getUser().isAdmin()) {
            addNotification(action, reviewDetails);
        }
    }

    /**
     * Test method to simulate review notifications (for admin users)
     */
    @FXML
    public void testNotifications() {
        if (UserSession.getInstance().getUser() != null && UserSession.getInstance().getUser().isAdmin()) {
            try {
                // Test 1: Direct Observer Pattern notification
                com.oss.ossv1.service.NotificationService notificationService = 
                    LoginPage.springContext.getBean(com.oss.ossv1.service.NotificationService.class);
                
                notificationService.addReview("LAPTOP001", "bob", "Excellent laptop for programming!", 5);
                
                // Test 2: Through ReviewService (real integration)
                com.oss.ossv1.service.ReviewService reviewService = 
                    LoginPage.springContext.getBean(com.oss.ossv1.service.ReviewService.class);
                
                // Create a test review through the actual service
                Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000);
                        
                        // Get some test data
                        com.oss.ossv1.data.repository.ProductRepository productRepo = 
                            LoginPage.springContext.getBean(com.oss.ossv1.data.repository.ProductRepository.class);
                        com.oss.ossv1.data.repository.UserRepository userRepo = 
                            LoginPage.springContext.getBean(com.oss.ossv1.data.repository.UserRepository.class);
                        
                        var products = productRepo.findAll();
                        var users = userRepo.findAll();
                        
                        if (!products.isEmpty() && !users.isEmpty()) {
                            // Create a real review through ReviewService
                            reviewService.createReview(
                                products.get(0), 
                                users.stream().filter(u -> !u.isAdmin()).findFirst().orElse(users.get(0)),
                                4, 
                                "Test review created through ReviewService - this should trigger a notification!"
                            );
                        }
                    } catch (Exception e) {
                        System.err.println("Error creating test review: " + e.getMessage());
                    }
                });
                
            } catch (Exception e) {
                System.err.println("Failed to test notifications: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Test the Observer Pattern connection directly
     */
    @FXML
    public void testObserverConnection() {
        try {
            // Get the ReviewNotificationManager bean directly
            com.oss.ossv1.behavioral.observer.ReviewNotificationManager reviewNotificationManager = 
                LoginPage.springContext.getBean(com.oss.ossv1.behavioral.observer.ReviewNotificationManager.class);
            
            if (reviewNotificationManager != null) {
                // Test direct notification
                reviewNotificationManager.notifyObservers("TEST_NOTIFICATION", "This is a direct test of the Observer Pattern");
            }
            
        } catch (Exception e) {
            System.err.println("Error testing observer connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
