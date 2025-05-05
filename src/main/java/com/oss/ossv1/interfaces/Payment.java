package com.oss.ossv1.interfaces;

/**
 * Interface for processing a payment with a given amount.
 */
public interface Payment {
    boolean processPayment(double amount);
}
