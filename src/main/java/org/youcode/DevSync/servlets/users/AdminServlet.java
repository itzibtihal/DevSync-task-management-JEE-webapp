package org.youcode.DevSync.servlets.users;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.UserDAO;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.modals.User;

import java.io.IOException;
import java.util.List;

public class AdminServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> recentUsers = userDAO.findRecentUsers(4);
        request.setAttribute("recentUsers", recentUsers);
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }
}