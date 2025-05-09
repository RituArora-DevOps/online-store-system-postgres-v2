package com.oss.ossv1.gui.util;

import javafx.scene.control.TableCell;

public class TableCellUtils {
    public static <T> TableCell<T, Double> createCurrencyCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        };
    }
}
