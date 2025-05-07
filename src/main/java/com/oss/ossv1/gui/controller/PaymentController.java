package com.oss.ossv1.gui.controller;

import com.oss.ossv1.gui.model.PaymentModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class PaymentController {

    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField amountField;

    // Credit Card Fields
    @FXML private VBox creditCardBox;
    @FXML private TextField cardNumberField;
    @FXML private TextField expirationField;
    @FXML private TextField cvvField;

    // PayPal Fields
    @FXML private VBox paypalBox;
    @FXML private TextField paypalEmailField;

    @FXML private Button payButton;

    private final PaymentModel model = new PaymentModel();

    @FXML
    public void initialize() {
        paymentMethodCombo.getItems().addAll("CreditCard", "PayPal");

        // Bind visibility
        paymentMethodCombo.setOnAction(e -> {
            String selected = paymentMethodCombo.getValue();
            model.setPaymentMethod(selected);
            creditCardBox.setVisible("CreditCard".equals(selected));
            paypalBox.setVisible("PayPal".equals(selected));
        });

        payButton.setOnAction(e -> processPayment());
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

        if ("CreditCard".equals(method)) {
            model.setCardNumber(cardNumberField.getText());
            model.setExpirationDate(expirationField.getText());
            model.setCvv(cvvField.getText());

            if (model.getCardNumber().length() != 16 || model.getCvv().isEmpty() || model.getExpirationDate().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Credit Card", "Please fill all credit card fields correctly.");
                return;
            }

            // Simulated processing
            System.out.println("Processed Credit Card payment of $" + model.getAmount());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Credit Card payment processed!");

        } else if ("PayPal".equals(method)) {
            model.setPaypalEmail(paypalEmailField.getText());

            if (model.getPaypalEmail().isEmpty() || !model.getPaypalEmail().contains("@")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid PayPal email.");
                return;
            }

            // Simulated processing
            System.out.println("Processed PayPal payment of $" + model.getAmount());
            showAlert(Alert.AlertType.INFORMATION, "Success", "PayPal payment processed!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Select Payment Method", "Please choose a payment method.");
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
