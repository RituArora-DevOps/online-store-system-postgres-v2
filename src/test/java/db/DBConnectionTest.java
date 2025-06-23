package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {
//        String url = "jdbc:mysql://69.16.227.64:3306/cp5114_team4_oss?useSSL=false&serverTimezone=UTC";
//        String username = "cp5114_team4_oss";
//        String password = "Vj!9r)VSVIpP";
//
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            System.out.println(" Connection successful!");
//        } catch (SQLException e) {
//            System.err.println(" Connection failed:");
//            e.printStackTrace();
//        }

        String url = "jdbc:postgresql://172.24.98.77:5432/ossdb"; // Your PostgreSQL test DB
        String username = "ossuser";
        String password = "osspass";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ PostgreSQL connection successful!");
        } catch (SQLException e) {
            System.err.println("❌ PostgreSQL connection failed:");
            e.printStackTrace();
        }

    }
}
