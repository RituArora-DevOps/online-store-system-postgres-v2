package com.oss.ossv1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Represents the LoginPage class.
 * Entry point for launching the JavaFX GUI and bootstrapping Spring Boot context.
 *
 * Initializing the full Spring environment (Bootstrapping context), including:
 * All @Component, @Service, @Repository, @Controller, and @RestController beans
 * Dependency Injection container (Spring ApplicationContext)
 * Configuration files (like application.properties)
 * Auto-configurations (e.g., JPA, validation, etc.)
 * Bootstrapping context = "Turning on and wiring up all Spring backend parts before showing the GUI"
 *
 * Design Pattern: None directly, but uses SpringApplicationBuilder to launch backend.
 * SOLID Compliance:
 * - Single Responsibility Principle: Only handles startup (JavaFX + Spring)
 */
public class LoginPage extends Application {

    public static ApplicationContext springContext;

    @Override
/**
 * start method.
 */
    public void start(Stage primaryStage) throws Exception {

        // Debug check
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            System.out.println("JDBC Driver is on the classpath.");
//        } catch (ClassNotFoundException e) {
//            System.err.println(" JDBC Driver NOT found on classpath!");
//            e.printStackTrace();
//        }

//       // Debug check for PostgreSQL
//        try {
//            Class.forName("org.postgresql.Driver");
//            System.out.println("PostgreSQL JDBC Driver is on the classpath.");
//        } catch (ClassNotFoundException e) {
//            System.err.println("PostgreSQL JDBC Driver NOT found on classpath!");
//            e.printStackTrace();
//        }

        //  Use Spring Boot's context bootstrapping
        // Make all backend services (e.g., ProductService, UserRepository) available for injection
        // You manually "bootstrap" the Spring context before loading your GUI,
        // because you are combining JavaFX (GUI) with Spring Boot (backend + DI).
        // JavaFX alone doesn't know how to autowire or inject beans
        springContext = new SpringApplicationBuilder(OssV1Application.class)
                .headless(false) // Important for JavaFX GUI apps
                .run();

        // Clear product registry once at application startup -- please do not remove
        com.oss.ossv1.gui.util.ProductRegistry.clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        primaryStage.setTitle("Online Store System - Login");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
        primaryStage.show();
    }

/**
 * main method.
 */
    public static void main(String[] args) {
        launch(args);
    }
}
