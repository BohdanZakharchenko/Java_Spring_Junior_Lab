package uaedunung.se.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uaedunung.se.dao.UserDAO;
import uaedunung.se.models.User;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println(" Вхід у doPost()");

        try {
            // Отримання параметрів з форми
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String role = request.getParameter("role");
            Integer idGet = Integer.valueOf(request.getParameter("idGet"));
            Integer idDel = Integer.valueOf(request.getParameter("idDel"));

            // Перевірка на обов'язкові поля
            if (name == null || email == null || password == null || phone == null || role == null
                    || name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || role.isEmpty()) {
                out.println("<p style='color:red;'> Всі поля обов'язкові!</p>");
                return;
            }

            // Створення об'єкта користувача
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setRole(role);

            // Виклик DAO для виконання транзакції
            UserDAO userDAO = new UserDAO();
             User resultUser =  userDAO.performTransaction(user, idGet, idDel);


            if (resultUser != null) {
                out.println("<p style='color:green;'>Користувача додано!</p>");
                out.println("<h3> Отриманий користувач:</h3>");
                out.println("<ul>");
                out.println("<li>ID: " + resultUser.getId() + "</li>");
                out.println("<li>Ім'я: " + resultUser.getName() + "</li>");
                out.println("<li>Email: " + resultUser.getEmail() + "</li>");
                out.println("<li>Телефон: " + resultUser.getPhone() + "</li>");
                out.println("<li>Роль: " + resultUser.getRole() + "</li>");
                out.println("</ul>");
            } else {
                out.println("<p style='color:red;'>Користувача не знайдено!</p>");
            }

        } catch (Exception e) {
            out.println(e);;
        }
    }

//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//
//        try {
//            // Отримуємо ID користувача з параметрів
//            String idParam = request.getParameter("id");
//
//            if (idParam != null) {
//                // Отримуємо одного користувача
//                int id = Integer.parseInt(idParam);
//                User user = UserDAO.getUserById(id);
//                if (user != null) {
//                    out.println("<h1>Користувач</h1>");
//                    out.println("<p>ID: " + user.getId() + "</p>");
//                    out.println("<p>Ім'я: " + user.getName() + "</p>");
//                    out.println("<p>Email: " + user.getEmail() + "</p>");
//                    out.println("<p>Телефон: " + user.getPhone() + "</p>");
//                    out.println("<p>Роль: " + user.getRole() + "</p>");
//                } else {
//                    out.println("<p>Користувача не знайдено!</p>");
//                }
//            } else {
////                // Отримуємо всіх користувачів
////                var users = UserDAO.getAllUsers();
////                out.println("<h1>Список користувачів</h1>");
////                out.println("<table border='1'><tr><th>ID</th><th>Ім'я</th><th>Email</th><th>Роль</th><th>Телефон</th></tr>");
////                for (User user : users) {
////                    out.println("<tr><td>" + user.getId() + "</td><td>" + user.getName() + "</td><td>" + user.getEmail() + "</td><td>" + user.getRole() + "</td><td>" + user.getPhone() + "</td></tr>");
////                }
////                out.println("</table>");
//            }
//        } catch (Exception e) {
//            handleError(out, e);
//        }
//    }


    private void handleError(PrintWriter out, Exception e) {
    }

}
