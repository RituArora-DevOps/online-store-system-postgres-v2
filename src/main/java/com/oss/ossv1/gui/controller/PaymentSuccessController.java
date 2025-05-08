package com.oss.ossv1.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class PaymentSuccessController {
    
    @FXML
    private Button continueButton;

    @FXML
    public void goToProductView() {
        try {
            Scene scene = continueButton.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            if (dashboard != null) {
                dashboard.navigateToProducts();
            } else {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to find dashboard controller.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not return to products.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
