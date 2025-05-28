package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.data.entity.OrderItem;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.service.OrderHistory;
import com.oss.ossv1.session.UserSession;
import com.oss.ossv1.structural.CompositePattern.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import static com.oss.ossv1.gui.util.TableCellUtils.createCurrencyCell;

public class OrderHistoryController implements Initializable {

    // ===== TABLEVIEW (Flat View) =====
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, String> colDate;
    @FXML private TableColumn<Order, Double> colAmount;
    @FXML private TableColumn<Order, Void> colAction;

    // ===== TREEVIEW (Grouped View) =====
    @FXML private TreeView<String> orderTreeView;

    private OrderService orderService;
    private OrderHistory orderHistory;

    public void setOrderService(OrderService service) {
        this.orderService = service;
        loadOrders();
    }

    public void setOrderHistory(OrderHistory history) {
        this.orderHistory = history;
        loadGroupedOrderHistory(); // load tree when injected
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup flat view
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colAmount.setCellFactory(col -> createCurrencyCell());

        // Do not load data here; wait until service is injected
    }

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
            controller.setOrderService(orderService);
            controller.setOrderId(orderId);
            controller.loadOrderItems();

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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            URL fxmlUrl = getClass().getResource("/views/DashboardView.fxml");
            if (fxmlUrl == null) throw new IOException("❌ DashboardView.fxml not found");

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            URL cssUrl = getClass().getResource("/styles/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = (Stage) orderTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Online Store System - Dashboard");
            stage.setWidth(root.prefWidth(-1) + 16);
            stage.setHeight(root.prefHeight(-1) + 39);
            stage.centerOnScreen();

            System.out.println("✅ Successfully navigated to Dashboard.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== GROUPED VIEW SUPPORT ==========
    public void loadGroupedOrderHistory() {
        if (orderHistory == null) return;

        Long userId = UserSession.getInstance().getUser().getId().longValue();
        OrderComponent rootGroup = orderHistory.getUserOrderHistory(userId);

        TreeItem<String> rootItem = new TreeItem<>("Order History for User " + userId);
        orderTreeView.setRoot(rootItem);
        orderTreeView.setShowRoot(true);

        buildTree(rootGroup, rootItem);
    }

    private void buildTree(OrderComponent component, TreeItem<String> parent) {
        if (component instanceof OrderGroup group) {
            TreeItem<String> groupItem = new TreeItem<>("Group: " + group.getGroupName());
            parent.getChildren().add(groupItem);
            for (OrderComponent child : group.getChildren()) {
                buildTree(child, groupItem);
            }
        } else if (component instanceof SingleOrder single) {
            TreeItem<String> orderItem = new TreeItem<>("Order #" + single.getOrder().getOrderId());
            parent.getChildren().add(orderItem);
            for (OrderItem item : single.getOrder().getItems()) {
                String detail = "Item: " + item.getProduct().getName() +
                        " x" + item.getQuantity() +
                        " ($" + (item.getProduct().getPrice() * item.getQuantity()) + ")";
                orderItem.getChildren().add(new TreeItem<>(detail));
            }
        }
    }
}
