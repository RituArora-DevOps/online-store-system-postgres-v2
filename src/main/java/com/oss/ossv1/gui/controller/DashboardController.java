package com.oss.ossv1.gui.controller;

import com.oss.ossv1.LoginPage;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.session.UserSession;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DashboardController {

    @FXML private BorderPane rootPane;
    @FXML private StackPane contentArea;
    @FXML private Label dashboardWelcomeLabel;

    private Map<String, Parent> cachedViews = new HashMap<>();
    private ProgressIndicator loadingIndicator;

    @FXML
    public void initialize() {
        try {
            if (UserSession.getInstance().isLoggedIn()) {
                String username = UserSession.getInstance().getUser().getUsername();
                dashboardWelcomeLabel.setText("Welcome, " + username);
            } else {
                dashboardWelcomeLabel.setText("Welcome to Online Store System");
            }

            // Initialize loading indicator
            loadingIndicator = new ProgressIndicator();
            loadingIndicator.setMaxSize(50, 50);
            loadingIndicator.setVisible(false);

            // Pre-load views in background
            preloadViews();
        } catch (Exception e) {
            e.printStackTrace();
            dashboardWelcomeLabel.setText("Welcome to Online Store System");
        }
    }

    private void preloadViews() {
        Task<Void> preloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Pre-load ProductView
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
                            Parent productView = productLoader.load();
                            cachedViews.put("product", productView);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    // Pre-load CartView
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader cartLoader = new FXMLLoader(getClass().getResource("/views/CartView.fxml"));
                            Parent cartView = cartLoader.load();
                            cachedViews.put("cart", cartView);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread preloadThread = new Thread(preloadTask);
        preloadThread.setDaemon(true);
        preloadThread.start();
    }

    @FXML
    public void loadDashboard() {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(createDefaultDashboard());
    }

    private Parent createDefaultDashboard() {
        try {
            Node content = contentArea.getChildren().get(0);
            return (Parent) content;
        } catch (Exception e) {
            e.printStackTrace();
            return new Label("Error loading dashboard content");
        }
    }

    @FXML
    public void navigateToProducts() {
        if (cachedViews.containsKey("product")) {
            // Use cached view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(cachedViews.get("product"));
        } else {
            // Load view if not cached
            showLoadingIndicator();
            Task<Parent> loadTask = new Task<>() {
                @Override
                protected Parent call() throws Exception {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
                    Parent content = loader.load();
                    cachedViews.put("product", content);
                    return content;
                }
            };

            loadTask.setOnSucceeded(event -> {
                hideLoadingIndicator();
                contentArea.getChildren().clear();
                contentArea.getChildren().add(loadTask.getValue());
            });

            loadTask.setOnFailed(event -> {
                hideLoadingIndicator();
                showError("Navigation Error", "Unable to load Products view.");
            });

            Thread loadThread = new Thread(loadTask);
            loadThread.setDaemon(true);
            loadThread.start();
        }
    }

    @FXML
    public void navigateToCart() {
        if (cachedViews.containsKey("cart")) {
            // Use cached view
            contentArea.getChildren().clear();
            contentArea.getChildren().add(cachedViews.get("cart"));
        } else {
            showLoadingIndicator();
            Task<Parent> loadTask = new Task<>() {
                @Override
                protected Parent call() throws Exception {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CartView.fxml"));
                    Parent content = loader.load();
                    cachedViews.put("cart", content);
                    return content;
                }
            };

            loadTask.setOnSucceeded(event -> {
                hideLoadingIndicator();
                contentArea.getChildren().clear();
                contentArea.getChildren().add(loadTask.getValue());
            });

            loadTask.setOnFailed(event -> {
                hideLoadingIndicator();
                showError("Navigation Error", "Unable to load Cart view.");
            });

            Thread loadThread = new Thread(loadTask);
            loadThread.setDaemon(true);
            loadThread.start();
        }
    }

    @FXML
    public void navigateToOrders() {
        showLoadingIndicator();
        Task<Parent> loadTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderHistoryView.fxml"));
                Parent content = loader.load();

                Platform.runLater(() -> {
                    OrderHistoryController controller = loader.getController();
                    OrderService orderService = LoginPage.springContext.getBean(OrderService.class);
                    controller.setOrderService(orderService);
                });

                return content;
            }
        };

        loadTask.setOnSucceeded(event -> {
            hideLoadingIndicator();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loadTask.getValue());
        });

        loadTask.setOnFailed(event -> {
            hideLoadingIndicator();
            showError("Navigation Error", "Unable to load Order History view.");
        });

        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void showLoadingIndicator() {
        Platform.runLater(() -> {
            if (!contentArea.getChildren().contains(loadingIndicator)) {
                contentArea.getChildren().add(loadingIndicator);
            }
            loadingIndicator.setVisible(true);
        });
    }

    private void hideLoadingIndicator() {
        Platform.runLater(() -> {
            loadingIndicator.setVisible(false);
            contentArea.getChildren().remove(loadingIndicator);
        });
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
            stage.setWidth(400);
            stage.setHeight(300);
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Logout Error", "Unable to return to login screen.");
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
