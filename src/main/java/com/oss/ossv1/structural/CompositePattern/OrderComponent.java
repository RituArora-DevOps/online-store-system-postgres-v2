package com.oss.ossv1.structural.CompositePattern;

public abstract class OrderComponent {

    public void add(OrderComponent component) {
        throw new UnsupportedOperationException("Add operation not supported.");
    }

    public void remove(OrderComponent component) {
        throw new UnsupportedOperationException("Remove operation not supported.");
    }

    public OrderComponent getChild(int index) {
        throw new UnsupportedOperationException("GetChild operation not supported.");
    }

    public abstract void display();
}
