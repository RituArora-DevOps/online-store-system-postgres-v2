package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.session.UserSession;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Represents the OrderHistoryController class.
 */
public class OrderHistoryController {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, String> colDate;
    @FXML private TableColumn<Order, Double> colAmount;
    @FXML private TableColumn<Order, Void> colAction;

    private OrderService orderService;

/**
 * setOrderService method.
 */
    public void setOrderService(OrderService service) {
        this.orderService = service;
        loadOrders(); // load after service is injected
    }

    @FXML
/**
 * initialize method.
 */
    public void initialize() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        // Do NOT call loadOrders() here
    }

    // 👇 Needs to be public so other controllers can call it
/**
 * loadOrders method.
 */
    public void loadOrders() {
        if (orderService == null) return;

        Long userId = UserSession.getInstance().getUser().getId().longValue();
        List<Order> orders = orderService.getOrdersByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());

        ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);
        orderTable.setItems(observableOrders);

        addViewDetailsButton();
    }

/**
 * addViewDetailsButton method.
 */
    private void addViewDetailsButton() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("View Details");

            {
                btn.setOnAction(e -> {
                    Order order = getTableView().getItems().get(getIndex());
                    openOrderDetails(order.getOrderId().intValue());
                });
            }

            @Override
/**
 * updateItem method.
 */
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

/**
 * openOrderDetails method.
 */
    private void openOrderDetails(int orderId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderDetailsView.fxml"));
            Parent root = loader.load();

            OrderDetailsController controller = loader.getController();
            controller.setOrderService(orderService); // inject service
            controller.setOrderId(orderId);
            controller.loadOrderItems();

            // Get the dashboard controller and use its content area
            Scene scene = orderTable.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            if (dashboard != null) {
                dashboard.setContent(root);
            } else {
                throw new RuntimeException("Dashboard controller not found");
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load order details.");
        }
    }

/**
 * showAlert method.
 */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
/**
 * handleBack method.
 */
    private void handleBack() {
        try {
            URL fxmlUrl = getClass().getResource("/views/DashboardView.fxml");
            if (fxmlUrl == null) {
                throw new IOException("❌ DashboardView.fxml not found at /views/");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            URL cssUrl = getClass().getResource("/styles/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("⚠️ style.css not found at /styles/");
            }

            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Online Store System - Dashboard");

            // Resize stage to match preferred layout dimensions
            stage.setWidth(root.prefWidth(-1) + 16);  // Add window border buffer
            stage.setHeight(root.prefHeight(-1) + 39); // Add title bar buffer
            stage.centerOnScreen(); //center after resizing

            System.out.println("✅ Successfully navigated to Dashboard.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
