package uaedunung.se.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/base?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "bofg2";
    private static final String PASSWORD = "1111";

    public static Connection getConnection() throws SQLException {
        try {
            // Реєстрація драйвера для MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Підключення до бази даних...");

            // Підключення до бази даних
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Підключення успішне!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Помилка при підключенні до бази даних: " + e.getMessage());
            throw e; // Пробрасываем исключение дальше
        } catch (ClassNotFoundException e) {
            System.err.println("Не знайдено драйвер для MySQL: " + e.getMessage());
            throw new SQLException("Не знайдено драйвер для MySQL", e);
        }
    }
}
