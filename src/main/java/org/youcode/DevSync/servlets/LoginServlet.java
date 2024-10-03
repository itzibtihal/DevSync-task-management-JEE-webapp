package org.youcode.DevSync.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.youcode.DevSync.dao.UserDAO;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.modals.User;

import java.io.IOException;
import java.util.Optional;

public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set attributes or any other logic you need for the initial login page
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check username and password
        Optional<User> optionalUser = userDAO.findByUsernameAndPassword(username, password);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect based on role
            switch (user.getRole()) {
                case ADMIN:
                    response.sendRedirect("admin");
                    break;
                case MANAGER:
                    response.sendRedirect("manager");
                    break;
                case USER:
                    response.sendRedirect("user");
                    break;
                default:
                    response.sendRedirect("error");
                    break;
            }
        } else {
            // Invalid credentials
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
