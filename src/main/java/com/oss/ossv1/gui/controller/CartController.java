package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.CartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

        cartItemList = FXCollections.observableArrayList(CartManager.getInstance().getCartItems());
        cartTable.setItems(cartItemList);

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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Payment Process");
        alert.setContentText("Checkout feature not implemented yet.");
        alert.showAndWait();
    }

    private void handleBack() {
        // Navigate back to Product view (implement via scene switch)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Back");
        alert.setHeaderText(null);
        alert.setContentText("Going back to product listing...");
        alert.showAndWait();
    }
}
