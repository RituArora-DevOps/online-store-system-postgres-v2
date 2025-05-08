package com.oss.ossv1.gui.controller;

import com.oss.ossv1.LoginPage;
import com.oss.ossv1.data.entity.*;
import com.oss.ossv1.gui.model.CartItem;
import com.oss.ossv1.gui.model.PaymentModel;
import com.oss.ossv1.service.OrderService;
import com.oss.ossv1.service.PaymentService;
import com.oss.ossv1.data.repository.ProductRepository;
import com.oss.ossv1.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentController {

    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField amountField;

    @FXML private VBox creditCardBox;
    @FXML private TextField cardNumberField;
    @FXML private TextField expirationField;
    @FXML private TextField cvvField;

    @FXML private VBox paypalBox;
    @FXML private TextField paypalEmailField;

    @FXML private Button payButton;

    private final PaymentModel model = new PaymentModel();

    private final PaymentService paymentService = LoginPage.springContext.getBean(PaymentService.class);
    private final OrderService orderService = LoginPage.springContext.getBean(OrderService.class);
    private final ProductRepository productRepository = LoginPage.springContext.getBean(ProductRepository.class);

    @FXML
    public void initialize() {
        paymentMethodCombo.getItems().addAll("CreditCard", "PayPal");

        paymentMethodCombo.setOnAction(e -> {
            String selected = paymentMethodCombo.getValue();
            model.setPaymentMethod(selected);
            creditCardBox.setVisible("CreditCard".equals(selected));
            paypalBox.setVisible("PayPal".equals(selected));
        });

        payButton.setOnAction(e -> processPayment());
    }

    @FXML
    private void handleBackToProducts() {
        try {
            Scene scene = payButton.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            if (dashboard != null) {
                dashboard.navigateToProducts();
            } else {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to find dashboard controller.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Product view.");
        }
    }

    @FXML
    private void handleBackToDashboard() {
        try {
            Scene scene = payButton.getScene();
            DashboardController dashboard = (DashboardController) scene.getRoot().getUserData();
            if (dashboard != null) {
                dashboard.loadDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to find dashboard controller.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load Dashboard view.");
        }
    }


    private void processPayment() {
        String method = model.getPaymentMethod();
        try {
            double amount = Double.parseDouble(amountField.getText());
            model.setAmount(amount);
            model.setPaymentDate(LocalDateTime.now());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a valid numeric amount.");
            return;
        }

        boolean success = false;

        // Build Order
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order();
        order.setUserId(Long.valueOf(UserSession.getInstance().getUser().getId()));
        order.setOrderDate(LocalDateTime.now());

        for (CartItem guiItem : cartItems) {
            Product entityProduct = productRepository.findById(guiItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + guiItem.getProduct().getId()));

            OrderItem item = new OrderItem();
            item.setProduct(entityProduct);
            item.setQuantity(guiItem.getQuantity());
            item.setPriceAtOrder(entityProduct.getPrice());
            item.setOrder(order);

            orderItems.add(item);
        }

        order.setItems(orderItems);

        // Handle payment type
        if ("CreditCard".equals(method)) {
            model.setCardNumber(cardNumberField.getText());
            model.setExpirationDate(expirationField.getText());
            model.setCvv(cvvField.getText());

            if (model.getCardNumber().length() != 16 || model.getCvv().isEmpty() || model.getExpirationDate().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Credit Card", "Please fill all credit card fields correctly.");
                return;
            }

            CreditCardPayment payment = new CreditCardPayment();
            payment.setCardNumber(model.getCardNumber());
            payment.setExpirationDate(model.getExpirationDate());
            payment.setCvv(model.getCvv());

            payment.setAmount(model.getAmount());
            payment.setPaymentDate(model.getPaymentDate());
            payment.setOrder(order);
            order.setPayment(payment);

            orderService.saveOrder(order);
            paymentService.createCreditCardPayment(payment);
            success = true;

        } else if ("PayPal".equals(method)) {
            model.setPaypalEmail(paypalEmailField.getText());

            if (model.getPaypalEmail().isEmpty() || !model.getPaypalEmail().contains("@")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid PayPal email.");
                return;
            }

            PayPalPayment payment = new PayPalPayment();
            payment.setPaypalEmail(model.getPaypalEmail());
            payment.setAmount(model.getAmount());
            payment.setPaymentDate(model.getPaymentDate());
            payment.setOrder(order);
            order.setPayment(payment);

            orderService.saveOrder(order);
            paymentService.createPayPalPayment(payment);
            success = true;
        }

        if (success) {
            CartManager.getInstance().clearCart();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Payment processed and order saved!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void setAmount(double amount) {
        amountField.setText(String.format("%.2f", amount));
    }
}
