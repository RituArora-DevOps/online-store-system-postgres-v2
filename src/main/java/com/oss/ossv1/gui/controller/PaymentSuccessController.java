package com.oss.ossv1.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PaymentSuccessController {

    @FXML
    private Button continueButton; // Add fx:id="continueButton" in FXML

    @FXML
    private void goToProductView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) continueButton.getScene().getWindow(); // Use reference node
            stage.setScene(scene);
            stage.setTitle("Online Store - Product Listing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
