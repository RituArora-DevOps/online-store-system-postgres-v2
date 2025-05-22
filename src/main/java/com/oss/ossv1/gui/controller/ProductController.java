package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.ossv1.gui.model.Product;
import com.oss.ossv1.gui.util.ProductRegistry;

import javafx.beans.property.SimpleDoubleProperty;
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

import static com.oss.ossv1.gui.util.TableCellUtils.createCurrencyCell;

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

    private boolean actionColumnInitialized = false;

    @FXML
    public void initialize() {
        System.out.println("🌀 ProductController initialized: " + this + " | Hash: " + this.hashCode());

        ProductRegistry.clear();
        lastSelectedCategory = null;
        lastMinPrice = "";
        lastMaxPrice = "";

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        originalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        priceColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            double discounted = product.getDiscountedPrice(10);
            return new SimpleDoubleProperty(discounted).asObject();
        });

        originalPriceColumn.setCellFactory(col -> createCurrencyCell());
        priceColumn.setCellFactory(col -> createCurrencyCell());
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button addButton = new Button("Add to Cart");
            private Product boundProduct;

            {
                addButton.getStyleClass().add("add-to-cart-button");

                // This handler never changes — just uses the latest bound product
                addButton.setOnAction(e -> {
                    if (boundProduct != null) {
                        System.out.println("🛒 [CLICK] Add to Cart for: " + boundProduct.getName() + " | ID: " + boundProduct.getId());
                        CartManager.getInstance().addToCart(boundProduct);
                        e.consume();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                int index = getIndex();
                if (empty || index < 0 || index >= getTableView().getItems().size()) {
                    boundProduct = null;
                    setGraphic(null);
                } else {
                    boundProduct = getTableView().getItems().get(index);
                    setGraphic(addButton);
                }
            }
        });

        categoryCombo.getItems().addAll("clothing", "electronics", "grocery");
        fetchProductsFromUrl("http://localhost:8080/products");
    }



    private void fetchProductsFromUrl(String urlString) {
        try {
            URL url = URI.create(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder json = new StringBuilder();
                while (scanner.hasNext()) {
                    json.append(scanner.nextLine());
                }

                ObjectMapper mapper = new ObjectMapper();
                List<Product> productList = mapper.readValue(json.toString(), new TypeReference<>() {});
                ObservableList<Product> observableList = FXCollections.observableArrayList();
                for (Product p : productList) {
                    ProductRegistry.register(p); // Store if not present
                    observableList.add(ProductRegistry.get(p.getId())); // Always use same instance
                }

                productTable.setItems(observableList);

                if (observableList.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "No Results", "No products found for the selected criteria.");
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not read or connect to the server.");
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
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Dashboard view.");
        }
    }
}
