package com.oss.ossv1.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {
        String url = "jdbc:mysql://69.16.227.64:3306/cp5114_team4_oss?useSSL=false&serverTimezone=UTC";
        String username = "cp5114_team4_oss";
        String password = "Vj!9r)VSVIpP";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ Connection successful!");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed:");
            e.printStackTrace();
        }
    }
}
