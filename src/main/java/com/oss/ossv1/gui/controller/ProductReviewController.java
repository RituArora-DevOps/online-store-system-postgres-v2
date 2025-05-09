package com.oss.ossv1.gui.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.oss.ossv1.data.entity.Order;
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

    // Table to show all reviews
    @FXML private TableView<ProductReview> reviewsTable;
    @FXML private TableColumn<ProductReview, String> productColumn;
    @FXML private TableColumn<ProductReview, Integer> ratingColumn;
    @FXML private TableColumn<ProductReview, String> commentColumn;
    @FXML private TableColumn<ProductReview, String> dateColumn;
    @FXML private TableColumn<ProductReview, String> userColumn;
    
    // Form for writing new reviews
    @FXML private ComboBox<Product> productComboBox;        // Dropdown to select product
    @FXML private ComboBox<Integer> ratingComboBox;         // Dropdown for rating (1-5)
    @FXML private TextArea commentTextArea;                 // Text box for review comment
    @FXML private Button clearButton;                       // Clear form button
    @FXML private Button submitButton;                      // Submit review button

    // List to hold all reviews
    private ObservableList<ProductReview> allReviews;
    
    // Services we need
    private ReviewService reviewService;
    private ProductService productService;
    private OrderService orderService;

    /**
     * Sets up the table columns and form controls.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create a list to hold the reviews
        allReviews = FXCollections.observableArrayList();
        reviewsTable.setItems(allReviews);
        
        // Set up the table columns
        setupTable();
        
        // Set up the rating dropdown (1-5)
        setupRatingDropdown();
        
        // Set up button actions
        clearButton.setOnAction(e -> clearForm());
        submitButton.setOnAction(e -> submitNewReview());
    }

    /**
     * Sets up the table columns to show review information
     */
    private void setupTable() {
        // Show product name
        productColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getProduct().getName()));
        
        // Show rating number
        ratingColumn.setCellValueFactory(
            new PropertyValueFactory<>("rating"));
        
        // Show review comment
        commentColumn.setCellValueFactory(
            new PropertyValueFactory<>("comment"));
        
        // Show formatted date
        dateColumn.setCellValueFactory(cell -> {
            LocalDateTime date = cell.getValue().getReviewDate();
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return new SimpleStringProperty(formattedDate);
        });
        
        // Show username
        userColumn.setCellValueFactory(cell -> 
            new SimpleStringProperty(cell.getValue().getUser().getUsername()));
    }

    /**
     * Sets up the rating dropdown with numbers 1-5
     */
    private void setupRatingDropdown() {
        // Add numbers 1-5 to the rating dropdown
        ratingComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        ratingComboBox.setPromptText("Select 1-5");
    }

    /**
     * Loads the products that the current user has bought
     */
    private void loadUserProducts() {
        // Check if user is logged in
        if (!UserSession.getInstance().isLoggedIn()) {
            showMessage("Please log in first!", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Get the current user's ID
            Integer userId = UserSession.getInstance().getUser().getId();
            
            // Get all orders for this user
            List<Order> userOrders = orderService.getOrdersByUserId(userId.longValue());

            if (userOrders.isEmpty()) {
                showMessage("You haven't bought any products yet!", Alert.AlertType.INFORMATION);
                return;
            }

            // Get all products from the orders
            List<Product> boughtProducts = userOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(item -> item.getProduct())
                .distinct()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .toList();

            // Add products to the dropdown
            productComboBox.setItems(FXCollections.observableArrayList(boughtProducts));
            productComboBox.setPromptText("Choose a product");
            
            // Make products show their names in the dropdown
            productComboBox.setCellFactory(cb -> new ListCell<Product>() {
                @Override
                protected void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    setText(empty || product == null ? "" : product.getName());
                }
            });
            
        } catch (Exception e) {
            showError("Error loading your products", null, "Error: " + e.getMessage());
        }
    }

    /**
     * Clears all the form fields
     */
    private void clearForm() {
        productComboBox.setValue(null);
        ratingComboBox.setValue(null);
        commentTextArea.clear();
    }

    /**
     * Submits a new review
     */
    private void submitNewReview() {
        // Check if user is logged in
        if (!UserSession.getInstance().isLoggedIn()) {
            showMessage("Please log in first!", Alert.AlertType.WARNING);
            return;
        }

        // Get values from form
        Product selectedProduct = productComboBox.getValue();
        Integer rating = ratingComboBox.getValue();
        String comment = commentTextArea.getText().trim();

        // Basic validation
        if (selectedProduct == null || rating == null || comment.isEmpty()) {
            showError("Error submitting review", null, "Please fill in all fields!");
            return;
        }

        try {
            // Create new review
            ProductReview newReview = new ProductReview();
            newReview.setProduct(selectedProduct);
            newReview.setRating(rating);
            newReview.setComment(comment);
            newReview.setReviewDate(LocalDateTime.now());
            newReview.setUser(UserSession.getInstance().getUser());
            
            // Save review
            reviewService.createReview(newReview);
            
            // Clear form and reload reviews
            clearForm();
            loadAllReviews();
            showMessage("Review submitted successfully!", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showError("Error submitting review", null, "Error: " + e.getMessage());
        }
    }

    /**
     * Shows a message popup to the user
     */
    private void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.toString());
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an error message to the user
     * @param title The error window title
     * @param header The header text (can be null)
     * @param message The error message
     */
    private void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Loads all reviews into the table
     */
    private void loadAllReviews() {
        if (reviewService != null) {
            allReviews.clear();
            allReviews.addAll(reviewService.getAllReviews());
        }
    }

    // Methods to set up our services
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
        loadAllReviews();
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
        loadUserProducts();
    }
} 