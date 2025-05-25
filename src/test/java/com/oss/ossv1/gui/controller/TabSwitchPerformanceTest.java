package com.oss.ossv1.gui.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TabSwitchPerformanceTest {

    @BeforeAll
    public static void initJFX() {
        try {
            Platform.startup(() -> {}); // Starts JavaFX runtime
        } catch (IllegalStateException ignored) {
            // JavaFX already started
        }
    }
    @Test
    public void testPerformanceOfTabSwitch() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
        Parent root = loader.load(); // Injects FXML fields
        DashboardController controller = loader.getController();

        long start = System.nanoTime();
        controller.loadDashboard();
        long end = System.nanoTime();

        long durationMs = (end - start) / 1_000_000;
        System.out.println("⏱️ Tab switch took: " + durationMs + " ms");

        assertTrue(durationMs < 1000, "Tab switching is slow: " + durationMs + " ms");
    }

}
