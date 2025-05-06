package com.oss.ossv1.gui.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.ossv1.gui.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> categoryColumn;

    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Button filterButton;
    @FXML private Button clearButton;

    @FXML
    public void initialize() {
        // Table column bindings
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Category dropdown options
        categoryCombo.getItems().addAll("clothing", "electronics", "grocery");

        // Load all products initially
        fetchProductsFromUrl("http://localhost:8080/products");

        // Button listeners
        filterButton.setOnAction(e -> onFilterClicked());
        clearButton.setOnAction(e -> onClearClicked());
    }

    private void fetchProductsFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder json = new StringBuilder();
            while (scanner.hasNext()) {
                json.append(scanner.nextLine());
            }
            scanner.close();

            ObjectMapper mapper = new ObjectMapper();
            List<Product> productList = mapper.readValue(json.toString(), new TypeReference<>() {});
            ObservableList<Product> observableList = FXCollections.observableArrayList(productList);
            productTable.setItems(observableList);
            if (observableList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No products found for the selected criteria.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onFilterClicked() {
        String category = categoryCombo.getValue();
        String minPriceText = minPriceField.getText();
        String maxPriceText = maxPriceField.getText();

        // Validate price fields if entered
        if (!minPriceText.isEmpty() && !maxPriceText.isEmpty()) {
            try {
                Double.parseDouble(minPriceText);
                Double.parseDouble(maxPriceText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Price", "Please enter valid numbers for price.");
                return;
            }
        }

        String url;
        if (category != null && !category.isEmpty()) {
            url = "http://localhost:8080/products/category/" + category;
        } else if (!minPriceText.isEmpty() && !maxPriceText.isEmpty()) {
            url = "http://localhost:8080/products/price?min=" + minPriceText + "&max=" + maxPriceText;
        } else {
            url = "http://localhost:8080/products";
        }

        fetchProductsFromUrl(url);
    }


    private void onClearClicked() {
        categoryCombo.setValue(null);
        minPriceField.clear();
        maxPriceField.clear();
        fetchProductsFromUrl("http://localhost:8080/products");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
