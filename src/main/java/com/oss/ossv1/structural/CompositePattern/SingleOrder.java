package com.oss.ossv1.structural.CompositePattern;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.data.entity.OrderItem;

public class SingleOrder extends OrderComponent {
    private final Order order;

    public SingleOrder(Order order) {
        this.order = order;
    }

    @Override
    public void display() {
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("User: " + order.getUser().getUsername());
        System.out.println("Items:");
        for (OrderItem item : order.getItems()) {
            double price = item.getProduct().getPrice() * item.getQuantity();
            System.out.println("- " + item.getProduct().getName() +
                    " x" + item.getQuantity() +
                    " = $" + price);
        }
        System.out.println("Total: $" + order.getTotalAmount());
        System.out.println("-----------------------------");
    }

    public Order getOrder() {
        return order;
    }
}
