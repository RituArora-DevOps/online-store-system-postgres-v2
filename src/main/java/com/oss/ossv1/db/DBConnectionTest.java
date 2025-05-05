package com.oss.ossv1.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://online-store-project.database.windows.net:1433;" +
                "database=OnlineStoreDB;" +
                "user=sqladmin@online-store-project;" +
                "password=mQ_//}66M49b;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "loginTimeout=30;";

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("✅ Connection successful!");
        } catch (SQLException e) {
            System.err.println("❌ Connection failed:");
            e.printStackTrace();
        }
    }
}
