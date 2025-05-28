package com.oss.ossv1.structural.CompositePattern;

import java.util.ArrayList;
import java.util.List;

public class OrderGroup extends OrderComponent {

    private final String groupName;
    private final List<OrderComponent> children = new ArrayList<>();

    public OrderGroup(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public void add(OrderComponent component) {
        children.add(component);
    }

    @Override
    public void remove(OrderComponent component) {
        children.remove(component);
    }

    @Override
    public OrderComponent getChild(int index) {
        return children.get(index);
    }

    @Override
    public void display() {
        System.out.println(">> Order Group: " + groupName);
        for (OrderComponent child : children) {
            child.display();
        }
        System.out.println("<< End of Group: " + groupName);
        System.out.println();
    }

    public List<OrderComponent> getChildren() {
        return children;
    }

    public String getGroupName() {
        return groupName;
    }
}
