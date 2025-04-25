package uaedunung.se.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uaedunung.se.config.DBConnection;
import uaedunung.se.dao.UserDAO;
import uaedunung.se.models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println(">> Сервлет викликано");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Отримання параметрів з форми
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");

            // Перевірка на наявність
            if (name == null || email == null || password == null || phone == null || role == null) {
                out.println("<p style='color:red;'>❌ Всі поля обов’язкові!</p>");
                return;
            }

            // Створення об'єкта користувача
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setRole(role);

            // Виклик DAO
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.addUser(user);
//            if (success) {
//                out.println("<p>✅ Користувача успішно додано!</p>");
//            } else {
//                out.println("<p style='color:red;'>❌ Не вдалося додати користувача! Перевірте дані та спробуйте знову.</p>");
//            }

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>❌ Виникла помилка:</h3>");
            out.println("<pre>" + e.toString() + "</pre>");  // Без escape

            // Виведення стеку помилки
            out.println("<h4>Деталі помилки:</h4>");
            out.println("<pre style='color:gray;'>");
            e.printStackTrace(out);
            out.println("</pre>");

            // Виведення ланцюга причин
            Throwable cause = e.getCause();
            while (cause != null) {
                out.println("<pre style='color:gray;'>Причина: " + cause.toString() + "</pre>");
                cause = cause.getCause();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        UserDAO userDAO = new UserDAO();
        try {
            var users = userDAO.getAllUsers();

            out.println("<html><head><title>Користувачі</title></head><body>");
            out.println("<h1>Список користувачів</h1>");
            out.println("<table border='1' cellpadding='5' cellspacing='0'>");
            out.println("<tr><th>ID</th><th>Ім'я</th><th>Email</th><th>Роль</th><th>Телефон</th></tr>");

            for (var user : users) {
                out.println("<tr>");
                out.println("<td>" + user.getId() + "</td>");
                out.println("<td>" + user.getName() + "</td>");
                out.println("<td>" + user.getEmail() + "</td>");
                out.println("<td>" + user.getRole() + "</td>");
                out.println("<td>" + user.getPhone() + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<h2 style='color:red;'>❌ Сталася помилка при отриманні користувачів:</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace(out);
        }
    }

}
