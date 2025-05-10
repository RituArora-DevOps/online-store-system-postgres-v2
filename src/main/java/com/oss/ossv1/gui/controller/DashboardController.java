package com.oss.ossv1.gui.controller;

import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.service.ProductService;
import com.oss.ossv1.service.ReviewService;
import com.oss.ossv1.session.UserSession;
import com.oss.ossv1.LoginPage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Controls the main dashboard view of the online store.
 * Manages the navigation bar
 */
public class DashboardController {
    // Main layout components
    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Label dashboardWelcomeLabel;
    @FXML private final Map<String, Parent> viewCache = new HashMap<>();

    /**
     * Initializes the dashboard view.
     * Sets up the welcome message based on user login status.
     */
    @FXML
    public void initialize() {
        rootPane.setUserData(this);
        updateWelcomeMessage();
        loadDashboard();
    }

    // Helper method to update welcome message
    private void updateWelcomeMessage() {
        if (UserSession.getInstance().isLoggedIn()) {
            String username = UserSession.getInstance().getUser().getUsername();
            dashboardWelcomeLabel.setText("Welcome, " + username);
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

    /**
     * Loads a view into the dashboard's content area.

    public void loadView(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", null, "Unable to load view.");
        }
    }

    // updated to debug wierd quantity increment and decrement
    public void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();

            if (fxml.contains("ProductView")) {
                ProductController pc = loader.getController();
                System.out.println("Loaded ProductController once: " + pc);
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", null, "Unable to load view.");
        }
    }
     */

    // Added by RA- to cache views/controllers in DashboardController so they’re only loaded once.

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
}
