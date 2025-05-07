package com.oss.ossv1.gui.controller;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

public class OrderHistoryController {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, String> colDate;
    @FXML private TableColumn<Order, Double> colAmount;
    @FXML private TableColumn<Order, Void> colAction;

    private OrderService orderService;

    public void setOrderService(OrderService service) {
        this.orderService = service;
        loadOrders(); // load after service is injected
    }

    @FXML
    public void initialize() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        // Do NOT call loadOrders() here
    }

    // 👇 Needs to be public so other controllers can call it
    public void loadOrders() {
        if (orderService == null) return;

        Long userId = UserSession.getInstance().getUser().getId().longValue();
        List<Order> orders = orderService.getOrdersByUserId(userId);
        orders.sort(Comparator.comparing(Order::getOrderDate).reversed());

        ObservableList<Order> observableOrders = FXCollections.observableArrayList(orders);
        orderTable.setItems(observableOrders);

        addViewDetailsButton();
    }

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
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void openOrderDetails(int orderId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderDetailsView.fxml"));
            Parent root = loader.load();

            OrderDetailsController controller = loader.getController();
            controller.setOrderService(orderService); // inject service
            controller.setOrderId(orderId);
            controller.loadOrderItems();

            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Order Details");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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
