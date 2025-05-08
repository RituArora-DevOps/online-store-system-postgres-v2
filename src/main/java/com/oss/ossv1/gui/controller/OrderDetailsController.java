package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.util.List;

import com.oss.ossv1.data.entity.OrderItem;
import com.oss.ossv1.service.OrderService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrderDetailsController {

    @FXML private Label orderTitle;
    @FXML private TableView<OrderItem> itemTable;
    @FXML private TableColumn<OrderItem, String> colItemName;
    @FXML private TableColumn<OrderItem, Integer> colQuantity;
    @FXML private TableColumn<OrderItem, Double> colPrice;
    @FXML private TableColumn<OrderItem, Double> colSubtotal;

    private OrderService orderService;
    private Long orderId;

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setOrderId(int orderId) {
        this.orderId = (long) orderId;
        orderTitle.setText("Order #" + orderId + " Details");
    }

    public void loadOrderItems() {
        if (orderService == null || orderId == null) {
            System.err.println("❌ OrderService or Order ID is null");
            return;
        }

        List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
        ObservableList<OrderItem> observableItems = FXCollections.observableArrayList(items);
        itemTable.setItems(observableItems);
    }

    @FXML
    public void initialize() {
        colItemName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("priceAtOrder"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderHistoryView.fxml"));
            Parent root = loader.load();

            OrderHistoryController controller = loader.getController();
            controller.setOrderService(orderService); // Reinject service

            // Get the dashboard controller and use its content area
            Scene scene = itemTable.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            if (dashboard != null) {
                dashboard.setContent(root);
            } else {
                throw new RuntimeException("Dashboard controller not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to order history.");
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
