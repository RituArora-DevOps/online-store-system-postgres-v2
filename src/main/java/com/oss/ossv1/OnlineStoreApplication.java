// NOTE: This class is only for local desktop use with JavaFX.
// It should NOT be used in Docker, which runs only the backend (OssV1Application).

package com.oss.ossv1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the OnlineStoreApplication class.
 * Loads the main dashboard view after login.
 *
 * Design Pattern: None
 * SOLID: SRP - launches GUI dashboard only
 */
public class OnlineStoreApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DashboardView.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        primaryStage.setTitle("Online Store System - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        // NEXT FLOW: Loads DashboardController (handles dashboard navigation)
    }

    public static void main(String[] args) {
        launch(args);
    }
}