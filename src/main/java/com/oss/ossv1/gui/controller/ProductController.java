package com.oss.ossv1.gui.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.ossv1.gui.model.Clothing;
import com.oss.ossv1.gui.model.Electronics;
import com.oss.ossv1.gui.model.Grocery;
import com.oss.ossv1.gui.model.Product;
import com.oss.ossv1.creational.SingletonStore;
import com.oss.ossv1.gui.util.ProductRegistry;
import com.oss.ossv1.creational.ProductFactory; // Added: For creating correct product subclass
import com.oss.ossv1.creational.ProductFactoryProvider; // Added: Central registry for factories

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
        System.out.println(" ProductController initialized: " + this + " | Hash: " + this.hashCode());

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
                        System.out.println(" [CLICK] Add to Cart for: " + boundProduct.getName() + " | ID: " + boundProduct.getId());
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

        // If cache is empty, fetch from server once. Else use cached.
            if (SingletonStore.getInstance().getProducts().isEmpty()) {
                fetchProductsFromUrl("http://localhost:8080/products");
            } else {
                setProductsToTable(SingletonStore.getInstance().getProducts());
            }
    }

    private void setProductsToTable(List<Product> products) {
        ObservableList<Product> observable = FXCollections.observableArrayList();
        for (Product p : products) {
            ProductRegistry.register(p);
            observable.add(ProductRegistry.get(p.getId()));
        }
        productTable.setItems(observable);
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
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown or extra fields
                // We configure Jackson to ignore extra JSON fields so parsing doesn't break even if the backend sends more fields.
                List<Product> rawList = mapper.readValue(json.toString(), new TypeReference<>() {}); //  Base list
                // JSON response is parsed into a list of base Product objects, but these only contain common fields like name, price, id, etc.

                ObservableList<Product> enrichedList = FXCollections.observableArrayList();
                for (Product base : rawList) {
                    // Create the correct subclass instance using Factory pattern
                    ProductFactory factory = ProductFactoryProvider.getFactory(base.getCategory()); //  Get correct factory
                    Product full = factory.createProduct(); //  Create correct subclass (Clothing, Electronics, etc.)

                    // Copy the shared fields from the raw base product into the correct subclass instance.
                    full.setId(base.getId());
                    full.setName(base.getName());
                    full.setDescription(base.getDescription());
                    full.setPrice(base.getPrice());
                    full.setCategory(base.getCategory());

                    // Subclass-specific fields
                    if ("clothing".equalsIgnoreCase(base.getCategory()) && base instanceof Clothing source && full instanceof Clothing target) {
                        target.setSize(source.getSize());
                        target.setColor(source.getColor());
                    } else if ("electronics".equalsIgnoreCase(base.getCategory()) && base instanceof Electronics source && full instanceof Electronics target) {
                        target.setWarrantyPeriod(source.getWarrantyPeriod());
                    } else if ("grocery".equalsIgnoreCase(base.getCategory()) && base instanceof Grocery source && full instanceof Grocery target) {
                        target.setExpiryDate(source.getExpiryDate());
                    }

                    ProductRegistry.register(full); // Cache each object using a registry to avoid duplicates.
                    enrichedList.add(ProductRegistry.get(full.getId())); // Then add the correct subclass into the observable list.
                }

                SingletonStore.getInstance().setProducts(enrichedList); // Store it into a singleton in-memory cache for reuse.
                productTable.setItems(enrichedList); //  Populate table with subclass-rich Product objects.
                productTable.setItems(enrichedList); //  Display products
                if (!enrichedList.isEmpty()) {
                    updateDynamicColumns(enrichedList.get(0).getCategory()); // Adjust columns
                    // Dynamically switch UI columns based on the type of product
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

    // The controller now supports dynamic columns for Clothing, Electronics, and Grocery via the updateDynamicColumns() method
    private void updateDynamicColumns(String category) {
        // Remove previously added dynamic columns
        productTable.getColumns().removeIf(col -> "dynamic".equals(col.getUserData()));

        // Add Size and Color columns only for clothing using JavaFX Property.
        if ("clothing".equalsIgnoreCase(category)) {
            TableColumn<Product, String> sizeCol = new TableColumn<>("Size");
            sizeCol.setCellValueFactory(cellData -> {
                if (cellData.getValue() instanceof Clothing clothing) {
                    return clothing.sizeProperty();
                }
                return null;
            });
            sizeCol.setUserData("dynamic");

            TableColumn<Product, String> colorCol = new TableColumn<>("Color");
            colorCol.setCellValueFactory(cellData -> {
                if (cellData.getValue() instanceof Clothing clothing) {
                    return clothing.colorProperty();
                }
                return null;
            });
            colorCol.setUserData("dynamic");

            productTable.getColumns().addAll(sizeCol, colorCol);

        } else if ("electronics".equalsIgnoreCase(category)) { // Adds Warranty column only if the product is an electronic item.
            TableColumn<Product, Number> warrantyCol = new TableColumn<>("Warranty (months)");
            warrantyCol.setCellValueFactory(cellData -> {
                if (cellData.getValue() instanceof Electronics e) {
                    return e.warrantyPeriodProperty();
                }
                return null;
            });
            warrantyCol.setUserData("dynamic");
            productTable.getColumns().add(warrantyCol);

        } else if ("grocery".equalsIgnoreCase(category)) { // Adds Expiry Date column, converted to String for display.
            TableColumn<Product, String> expiryCol = new TableColumn<>("Expiry Date");
            expiryCol.setCellValueFactory(cellData -> {
                if (cellData.getValue() instanceof Grocery g) {
                    return new SimpleStringProperty(
                            g.getExpiryDate() != null ? g.getExpiryDate().toString() : "N/A" // safely handle null expiry date
                    );
                }
                return null;
            });
            expiryCol.setUserData("dynamic");
            productTable.getColumns().add(expiryCol);
        }
    }

}
