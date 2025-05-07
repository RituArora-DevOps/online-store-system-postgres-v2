package com.oss.ossv1.gui.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class PaymentModel {

    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDateTime> paymentDate = new SimpleObjectProperty<>();
    private final StringProperty paymentMethod = new SimpleStringProperty(); // "CreditCard" or "PayPal"

    // Credit Card fields
    private final StringProperty cardNumber = new SimpleStringProperty();
    private final StringProperty expirationDate = new SimpleStringProperty();
    private final StringProperty cvv = new SimpleStringProperty();

    // PayPal fields
    private final StringProperty paypalEmail = new SimpleStringProperty();

    // --- Getters and Setters ---

    public double getAmount() { return amount.get(); }
    public void setAmount(double value) { amount.set(value); }
    public DoubleProperty amountProperty() { return amount; }

    public LocalDateTime getPaymentDate() { return paymentDate.get(); }
    public void setPaymentDate(LocalDateTime value) { paymentDate.set(value); }
    public ObjectProperty<LocalDateTime> paymentDateProperty() { return paymentDate; }

    public String getPaymentMethod() { return paymentMethod.get(); }
    public void setPaymentMethod(String value) { paymentMethod.set(value); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }

    public String getCardNumber() { return cardNumber.get(); }
    public void setCardNumber(String value) { cardNumber.set(value); }
    public StringProperty cardNumberProperty() { return cardNumber; }

    public String getExpirationDate() { return expirationDate.get(); }
    public void setExpirationDate(String value) { expirationDate.set(value); }
    public StringProperty expirationDateProperty() { return expirationDate; }

    public String getCvv() { return cvv.get(); }
    public void setCvv(String value) { cvv.set(value); }
    public StringProperty cvvProperty() { return cvv; }

    public String getPaypalEmail() { return paypalEmail.get(); }
    public void setPaypalEmail(String value) { paypalEmail.set(value); }
    public StringProperty paypalEmailProperty() { return paypalEmail; }
}
