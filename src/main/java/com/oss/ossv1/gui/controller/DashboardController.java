package com.oss.ossv1.gui.controller;

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
import com.oss.ossv1.session.UserSession;
import javafx.geometry.Insets;

import java.io.IOException;

public class DashboardController {
    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Label dashboardWelcomeLabel;

    @FXML
    public void initialize() {
        // Store this controller instance in the root's user data
        rootPane.setUserData(this);
        
        if (UserSession.getInstance().isLoggedIn()) {
            String username = UserSession.getInstance().getUser().getUsername();
            dashboardWelcomeLabel.setText("Welcome, " + username);
        } else {
            dashboardWelcomeLabel.setText("Welcome to Online Store System");
        }
        
        // Load dashboard content by default
        loadDashboard();
    }

    @FXML
    public void loadDashboard() {
        try {
            // Create default dashboard content
            GridPane dashboardGrid = new GridPane();
            dashboardGrid.setAlignment(javafx.geometry.Pos.CENTER);
            dashboardGrid.setHgap(20);
            dashboardGrid.setVgap(20);
            dashboardGrid.setPadding(new Insets(40));

            // Products Tile
            VBox productsTile = createDashboardTile("Products", "Browse and manage products");
            productsTile.setOnMouseClicked(e -> navigateToProducts());
            dashboardGrid.add(productsTile, 0, 0);

            // Cart Tile
            VBox cartTile = createDashboardTile("Shopping Cart", "View and manage your cart");
            cartTile.setOnMouseClicked(e -> navigateToCart());
            dashboardGrid.add(cartTile, 1, 0);

            // Orders Tile
            VBox ordersTile = createDashboardTile("Orders", "Track your orders");
            ordersTile.setOnMouseClicked(e -> navigateToOrders());
            dashboardGrid.add(ordersTile, 0, 1);

            // Profile Tile
            VBox profileTile = createDashboardTile("Profile", "Manage your account");
            profileTile.setOnMouseClicked(e -> navigateToProfile());
            dashboardGrid.add(profileTile, 1, 1);

            // Apply styles
            dashboardGrid.getStyleClass().add("dashboard-grid");
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(dashboardGrid);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Navigation Error", "Unable to load dashboard.");
        }
    }

    private VBox createDashboardTile(String title, String description) {
        VBox tile = new VBox(10);
        tile.getStyleClass().add("dashboard-tile");
        tile.setPadding(new Insets(20));
        tile.setMinSize(200, 150);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("tile-title");
        
        Text descText = new Text(description);
        descText.getStyleClass().add("tile-description");
        
        tile.getChildren().addAll(titleLabel, descText);
        return tile;
    }

    @FXML
    public void navigateToProducts() {
        loadView("/views/ProductView.fxml");
    }

    @FXML
    public void navigateToCart() {
        loadView("/views/CartView.fxml");
    }

    @FXML
    public void navigateToOrders() {
        loadView("/views/OrderHistoryView.fxml");
    }

    @FXML
    public void navigateToProfile() {
        showNotImplementedAlert("Profile functionality");
    }

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
            showError("Logout Error", "Unable to return to login screen.");
        }
    }

    public void loadView(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Unable to load view.");
        }
    }

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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
