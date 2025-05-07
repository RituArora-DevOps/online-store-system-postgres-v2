package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.CartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CartController {

    @FXML
    private TableView<CartItem> cartTable;

    @FXML
    private TableColumn<CartItem, String> nameColumn;

    @FXML
    private TableColumn<CartItem, Double> priceColumn;

    @FXML
    private TableColumn<CartItem, Integer> quantityColumn;

    @FXML
    private TableColumn<CartItem, Double> totalColumn;

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
        nameColumn.setCellValueFactory(data -> data.getValue().getProduct().nameProperty());
        priceColumn.setCellValueFactory(data -> data.getValue().getProduct().priceProperty().asObject());
        quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        totalColumn.setCellValueFactory(data -> data.getValue().totalPriceProperty().asObject());

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PaymentView.fxml"));
            Scene scene = new Scene(loader.load());

            // Get controller and pass the amount
            PaymentController controller = loader.getController();
            double total = CartManager.getInstance().calculateTotal();
            controller.setAmount(total);

            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Payment");

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to load payment screen");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductView.fxml"));
            Scene productScene = new Scene(loader.load());
            Stage stage = (Stage) cartTable.getScene().getWindow();
            stage.setScene(productScene);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to return to the product listing.");
            alert.showAndWait();
        }
    }

}
