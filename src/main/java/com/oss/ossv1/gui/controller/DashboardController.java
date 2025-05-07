package com.oss.ossv1.gui.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private BorderPane rootPane;

    @FXML
    public void navigateToProducts() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Products");
            stage.setScene(scene);
            System.out.println("Successfully navigated to Products view");
        } catch (Exception e) {
            System.err.println("Error loading ProductView.fxml: " + e.getMessage());
            e.printStackTrace();
            showError("Navigation Error", "Unable to load Products view. Error: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToCart() {
        loadView("/views/CartView.fxml", "Shopping Cart");
    }

    @FXML
    public void navigateToOrders() {
        // TODO: Implement Orders view
        showNotImplementedAlert("Orders functionality");
    }

    @FXML
    public void navigateToProfile() {
        // TODO: Implement Profile view
        showNotImplementedAlert("Profile functionality");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Unable to load " + title + " view.");
        }
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