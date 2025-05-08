package com.oss.ossv1.gui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss.ossv1.OnlineStoreApplication;
import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Represents the LoginController class.
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
/**
 * handleLogin method.
 */
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            URL url = new URL("http://localhost:8080/api/users/login");
            String postData = "username=" + username + "&password=" + password;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = reader.readLine();

                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(response, User.class);
                UserSession.getInstance().setUser(user);

                messageLabel.setText("✅ Welcome " + user.getUsername());

                Stage loginStage = (Stage) usernameField.getScene().getWindow();
                loginStage.close();
                new OnlineStoreApplication().start(new Stage());

            } else if (responseCode == 401) {
                // Handle unauthorized (invalid credentials)
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorMsg = errorReader.readLine();
                messageLabel.setText("❌ " + errorMsg);
            } else {
                messageLabel.setText("❌ Unexpected server response: " + responseCode);
            }

        } catch (IOException e) {
            messageLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            messageLabel.setText("Failed to open dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
/**
 * switchToRegister method.
 */
    private void switchToRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/RegisterView.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            messageLabel.setText("Failed to switch view.");
            e.printStackTrace();
        }
    }
}
