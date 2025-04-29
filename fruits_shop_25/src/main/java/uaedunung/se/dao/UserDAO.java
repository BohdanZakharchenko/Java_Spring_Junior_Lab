package uaedunung.se.dao;

import uaedunung.se.models.User;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

public class UserDAO {

    static DataSource ds;

    static {
        try {
            ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/MyDB");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public UserDAO() throws NamingException {
    }

    public void createUser(Connection conn, User user) throws SQLException, NamingException {

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, email, password, role, phone) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, user.getName());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPassword());
        stmt.setString(4, user.getRole());
        stmt.setString(5, user.getPhone());
        stmt.executeUpdate();
    }
    // Отримання користувача за ID
    public static User getUserById(Connection conn, int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    user.setPhone(rs.getString("phone"));
                    return user;
                } else {
                    throw new SQLException("Користувача з ID " + id + " не знайдено.");
                }
            }
        }
    }

    // Оновлення користувача
    public void updateUser(User user) throws SQLException {
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ?, email = ?, password = ?, role = ?, phone = ? WHERE id = ?")) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getPhone());
            stmt.setInt(6, user.getId());
            stmt.executeUpdate();
        }
    }

    // Видалення користувача
    public void deleteUser(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Метод для виконання всіх операцій у транзакції
    public User performTransaction(User user, int userIdToRetrieve, int userIdToDelete) throws SQLException {

        Connection conn = null;
        try {
            // Отримання з'єднання
            conn = ds.getConnection();
            conn.setAutoCommit(false); // Вимикаємо автозбереження для транзакції

            // Додавання нового користувача
            createUser(conn, user);

            // Виведення користувача за ID
            User retrievedUser = getUserById(conn, userIdToRetrieve);

            // Видалення користувача за ID
            deleteUser(conn, userIdToDelete);


            // Підтвердення транзакцію
            conn.commit();
            return retrievedUser;

        } catch (SQLException e) {
            // В разі помилки, повертаємо всі зміни
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Не вдалося виконати відкат транзакції", rollbackEx);
                }
            }
            throw e;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Поверненя автозбереження в нормальний режим
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Не вдалося закрити з'єднання", e);
                }
            }
        }
    }
}

//public class UserDAO {
//
//    public List<User> getAllUsers() {
//        List<User> users = new ArrayList<>();
//        String query = "SELECT id, name, email, password, role, phone FROM users"; // Явно вказуємо стовпці
//
//        try (Connection conn = DBConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String password = rs.getString("password");
//                String role = rs.getString("role");
//                String phone = rs.getString("phone");
//                User user = new User(id, name, email, password, role, phone);
//                users.add(user);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//
//        return users;
//    }
//
//    public boolean addUser(User user) {
//        String query = "INSERT INTO users (name, email, password, role, phone) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//
//            pstmt.setString(1, user.getName());
//            pstmt.setString(2, user.getEmail());
//            pstmt.setString(3, user.getPassword());
//            pstmt.setString(4, user.getRole());
//            pstmt.setString(5, user.getPhone());
//            return pstmt.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//
//        return false;
//    }
//}

