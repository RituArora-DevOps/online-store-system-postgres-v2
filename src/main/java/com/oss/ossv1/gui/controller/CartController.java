package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.CartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CartController {

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, String> categoryColumn;
    @FXML private TableColumn<CartItem, Double> originalPriceColumn;
    @FXML private TableColumn<CartItem, Double> discountedPriceColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;

    @FXML private TableColumn<CartItem, Void> removeColumn;

    @FXML
    private Label totalLabel;

    @FXML
    private Button removeButton;

    @FXML
    private Button checkoutButton;

    @FXML
    private Button backButton;

    private ObservableList<CartItem> cartItemList;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> data.getValue().getProduct().nameProperty());categoryColumn.setCellValueFactory(data -> data.getValue().getProduct().categoryProperty());
        originalPriceColumn.setCellValueFactory(data -> data.getValue().getProduct().priceProperty().asObject());
        discountedPriceColumn.setCellValueFactory(data -> {
            double discounted = data.getValue().getProduct().getDiscountedPrice(10); // Example: 10%
            return new javafx.beans.property.SimpleDoubleProperty(discounted).asObject();
        });
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        removeColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeBtn = new Button("X");
            {
                removeBtn.setOnAction(e -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    CartManager.getInstance().removeItem(item);
                    cartItemList.remove(item);
                    updateTotal();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });


        cartItemList = CartManager.getInstance().getCartItems(); // use the live observable list

        cartTable.setItems(cartItemList);
        System.out.println("Cart items: " + cartItemList.size());
        cartItemList.forEach(item -> System.out.println(item.getProduct().getName() + " x " + item.getQuantity()));

        updateTotal();

        removeButton.setOnAction(event -> removeSelectedItem());
        checkoutButton.setOnAction(event -> handleCheckout());
        backButton.setOnAction(event -> handleBack());
    }

    private void removeSelectedItem() {
        CartItem selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            CartManager.getInstance().removeItem(selected);
            cartItemList.remove(selected);
            updateTotal();
        }
    }

    private void updateTotal() {
        double total = cartItemList.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        totalLabel.setText(String.format("$%.2f", total));
    }

    private void handleCheckout() {
        try {
            // Get the dashboard controller
            Scene scene = cartTable.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            
            if (dashboard != null) {
                // Load the payment view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PaymentView.fxml"));
                Parent paymentView = loader.load(); // Load and store as Parent
                
                // Get controller and pass the amount
                PaymentController controller = loader.getController();
                double total = CartManager.getInstance().calculateTotal();
                controller.setAmount(total);
                
                // Add the view to the dashboard
                dashboard.setContent(paymentView); // Use setContent instead of loadView
            } else {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to find dashboard controller.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load payment screen");
        }
    }

    private void handleBack() {
        try {
            // Get the dashboard controller from the scene
            Scene scene = cartTable.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            dashboard.navigateToProducts();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to return to the product listing.");
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) cartTable.getScene().getWindow();
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            stage.setTitle("Dashboard");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
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
