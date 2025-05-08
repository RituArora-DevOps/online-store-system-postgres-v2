package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.ossv1.gui.model.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> idColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> categoryColumn;
    @FXML private TableColumn<Product, Void> actionColumn;

    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Button filterButton;
    @FXML private Button clearButton;
    @FXML private String lastSelectedCategory;
    @FXML private String lastMinPrice;
    @FXML private String lastMaxPrice;
    @FXML private TableColumn<Product, Double> originalPriceColumn;


    @FXML
    public void initialize() {
        // Reset filters every time this view is loaded fresh
        lastSelectedCategory = null;
        lastMinPrice = "";
        lastMaxPrice = "";

        originalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Table column bindings
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            double discounted = product.getDiscountedPrice(10); // 10% discount
            return new javafx.beans.property.SimpleDoubleProperty(discounted).asObject();
        });
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button addButton = new Button("Add to Cart");

            {
                addButton.setOnAction(e -> {
                    Product selectedProduct = getTableView().getItems().get(getIndex());
                    // Clone the product and apply 10% discount before adding to cart
                    Product discountedProduct = new Product(
                            selectedProduct.getId(),
                            selectedProduct.getName(),
                            selectedProduct.getDescription(),
                            selectedProduct.getDiscountedPrice(10), // 10% discount applied
                            selectedProduct.getCategory()
                    );

                    CartManager.getInstance().addToCart(discountedProduct);
                    System.out.println("Added to cart with discount: " + discountedProduct.getName() + " - $" + discountedProduct.getPrice());

                    System.out.println("Added to cart: " + selectedProduct.getName());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        });

        // Category dropdown options
        categoryCombo.getItems().addAll("clothing", "electronics", "grocery");

        // Load all products initially
        fetchProductsFromUrl("http://localhost:8080/products");
    }

    private void fetchProductsFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 5 second timeout

            try {
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
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Connection Error", 
                    "Could not read data from server. Make sure the Spring Boot application is running on port 8080.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", 
                "Could not connect to server. Make sure the Spring Boot application is running on port 8080.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onFilterClicked() {
        lastSelectedCategory = categoryCombo.getValue();
        lastMinPrice = minPriceField.getText();
        lastMaxPrice = maxPriceField.getText();

        String url;
        if (lastSelectedCategory != null && !lastSelectedCategory.isEmpty()) {
            url = "http://localhost:8080/products/category/" + lastSelectedCategory;
        } else if (!lastMinPrice.isEmpty() && !lastMaxPrice.isEmpty()) {
            url = "http://localhost:8080/products/price?min=" + lastMinPrice + "&max=" + lastMaxPrice;
        } else {
            url = "http://localhost:8080/products";
        }

        fetchProductsFromUrl(url);
    }


    @FXML
    public void onClearClicked() {
        categoryCombo.setValue(lastSelectedCategory);
        minPriceField.setText(lastMinPrice);
        maxPriceField.setText(lastMaxPrice);

        fetchProductsFromUrl("http://localhost:8080/products");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleViewCart() {
        try {
            // Get the dashboard controller from the scene
            Scene scene = productTable.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            dashboard.navigateToCart();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load Cart view.");
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Dashboard view.");
        }
    }

}
