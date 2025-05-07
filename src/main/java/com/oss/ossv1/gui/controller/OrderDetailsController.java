package com.oss.ossv1.gui.controller;

import com.oss.ossv1.data.entity.OrderItem;
import com.oss.ossv1.service.OrderService;
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
import java.util.List;

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
            controller.setOrderService(orderService); // Reinject
            Stage stage = (Stage) itemTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Your Order History");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
