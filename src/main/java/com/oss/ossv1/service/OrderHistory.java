package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.Order;
import com.oss.ossv1.data.repository.OrderRepository;
import com.oss.ossv1.structural.CompositePattern.OrderComponent;
import com.oss.ossv1.structural.CompositePattern.OrderGroup;
import com.oss.ossv1.structural.CompositePattern.SingleOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistory {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderHistory(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Builds a composite tree of a user's order history.
     */
    public OrderComponent getUserOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findWithItemsByUserId(userId);

        OrderGroup userGroup = new OrderGroup("User " + userId + " Order History");

        for (Order order : orders) {
            userGroup.add(new SingleOrder(order));
        }

        return userGroup;
    }

    /**
     * Prints a user's order history tree to the console.
     */
    public void displayUserOrderHistory(Long userId) {
        OrderComponent history = getUserOrderHistory(userId);
        history.display();
    }
}
