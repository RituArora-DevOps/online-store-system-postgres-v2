package com.oss.ossv1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LoginPage extends Application {

    public static ApplicationContext springContext;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Debug check
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✅ JDBC Driver is on the classpath.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver NOT found on classpath!");
            e.printStackTrace();
        }

        // ✅ Use Spring Boot's context bootstrapping
        springContext = new SpringApplicationBuilder(AppConfig.class)
                .headless(false) // Important for JavaFX GUI apps
                .run();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        primaryStage.setTitle("Online Store System - Login");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
