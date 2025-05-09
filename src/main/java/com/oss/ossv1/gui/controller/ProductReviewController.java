package com.oss.ossv1.gui.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.data.entity.OrderItem;
import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.service.ProductService;
import com.oss.ossv1.service.ReviewService;
import com.oss.ossv1.session.UserSession;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controls the product review functionality.
 * Allows users to view all reviews and submit new reviews for purchased products.
 */
public class ProductReviewController implements Initializable {

    // Table columns for displaying reviews
    @FXML private TableView<ProductReview> reviewsTable;
    @FXML private TableColumn<ProductReview, String> productColumn;
    @FXML private TableColumn<ProductReview, Integer> ratingColumn;
    @FXML private TableColumn<ProductReview, String> commentColumn;
    @FXML private TableColumn<ProductReview, String> dateColumn;
    @FXML private TableColumn<ProductReview, String> userColumn;
    
    // Form controls for submitting reviews
    @FXML private ComboBox<Product> productComboBox;
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextArea commentTextArea;
    @FXML private Button clearButton;
    @FXML private Button submitButton;

    // Services
    private ReviewService reviewService;
    private ProductService productService;
    private OrderService orderService;
    private ObservableList<ProductReview> reviews;

    /**
     * Sets up the table columns and form controls.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reviews = FXCollections.observableArrayList();
        reviewsTable.setItems(reviews);
        
        setupTableColumns();
        setupRatingComboBox();
        setupButtonHandlers();
    }

    /**
     * Sets up the table columns to display review information.
     */
    private void setupTableColumns() {
        productColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getProduct().getName()));
            
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
            
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
            
        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getReviewDate();
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return new SimpleStringProperty(formattedDate);
        });
        
        userColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
    }

    /**
     * Configures the rating dropdown with values 1-5.
     */
    private void setupRatingComboBox() {
        ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ratingComboBox.setItems(ratings);
        
        // Set cell factory to display rating values
        ratingComboBox.setCellFactory(lv -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                setText(empty || rating == null ? "" : rating.toString());
            }
        });

        ratingComboBox.setButtonCell(ratingComboBox.getCellFactory().call(null));

        ratingComboBox.setPromptText("1-5");
    }

    /**
     * Loads the products that the current user has purchased.
     * Users can only review products they have bought.
     */
    private void loadPurchasedProducts() {
        if (!UserSession.getInstance().isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "Please log in to write reviews.");
            return;
        }

        try {
            List<Order> userOrders = orderService.getOrdersByUserId(
                UserSession.getInstance().getUser().getId().longValue()
            );

            if (userOrders.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Purchases", 
                    "You haven't purchased any products yet. Make a purchase to write a review.");
                return;
            }

            List<Product> purchasedProducts = userOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(OrderItem::getProduct)
                .distinct()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .toList();

            if (purchasedProducts.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Products", 
                    "No products found in your order history.");
                return;
            }

            productComboBox.setItems(FXCollections.observableArrayList(purchasedProducts));
            
            productComboBox.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    setText(empty || product == null ? "" : product.getName());
                }
            });
            productComboBox.setButtonCell(productComboBox.getCellFactory().call(null));

            productComboBox.setPromptText("Select a purchased product");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", 
                "Failed to load purchased products: " + e.getMessage());
        }
    }

    private void setupButtonHandlers() {
        clearButton.setOnAction(e -> clearForm());
        submitButton.setOnAction(e -> submitReview());
    }

    private void loadReviews() {
        if (reviewService != null) {
            reviews.clear();
            reviews.addAll(reviewService.getAllReviews());
        }
    }

    private void clearForm() {
        productComboBox.setValue(null);
        ratingComboBox.setValue(null);
        commentTextArea.clear();
    }

    /**
     * Handles the review submission process.
     * Validates input and creates a new review in the database.
     */
    private void submitReview() {
        if (!UserSession.getInstance().isLoggedIn()) {
            showAlert(Alert.AlertType.WARNING, "Not Logged In", "Please log in to write reviews.");
            return;
        }

        Product product = productComboBox.getValue();
        Integer rating = ratingComboBox.getValue();
        String comment = commentTextArea.getText().trim();

        if (product == null || rating == null || comment.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields");
            return;
        }

        try {
            ProductReview review = new ProductReview();
            review.setProduct(product);
            review.setRating(rating);
            review.setComment(comment);
            review.setReviewDate(LocalDateTime.now());
            review.setUser(UserSession.getInstance().getUser());
            
            reviewService.createReview(review);
            
            clearForm();
            loadReviews();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Review submitted successfully");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit review: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Service setters
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
        loadReviews();
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
        loadPurchasedProducts();
    }
} 