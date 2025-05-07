package com.oss.ossv1.gui.controller;

import com.oss.ossv1.LoginPage;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private BorderPane rootPane;
    @FXML private Label dashboardWelcomeLabel;

    @FXML
    public void initialize() {
        try {
            if (UserSession.getInstance().isLoggedIn()) {
                String username = UserSession.getInstance().getUser().getUsername();
                dashboardWelcomeLabel.setText("Welcome to your dashboard, " + username + ".");
            } else {
                dashboardWelcomeLabel.setText("Welcome to your dashboard.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            dashboardWelcomeLabel.setText("Welcome to your dashboard.");
        }
    }


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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderHistoryView.fxml"));
            Parent root = loader.load();

            // Inject Spring-managed OrderService
            OrderHistoryController controller = loader.getController();
            OrderService orderService = LoginPage.springContext.getBean(OrderService.class);
            controller.setOrderService(orderService);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Your Orders");
            stage.setScene(scene);
            System.out.println("Successfully navigated to Order History view");
        } catch (Exception e) {
            System.err.println("Error loading OrderHistoryView.fxml: " + e.getMessage());
            e.printStackTrace();
            showError("Navigation Error", "Unable to load Order History view. Error: " + e.getMessage());
        }
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
            stage.setTitle("Login");
            stage.setScene(loginScene);

            // Force resize to match login view's preferred size
            stage.setWidth(400);
            stage.setHeight(300);
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
