package org.youcode.DevSync.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.dao.interfaces.UserDAO;
import org.youcode.DevSync.dao.impl.RequestDAOImpl; // Import your actual DAO implementation
import org.youcode.DevSync.dao.impl.TokenManagerDAOImpl; // Import your actual DAO implementation
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.Scheduler.TokenGrantScheduler;

import java.io.IOException;
import java.util.Optional;

public class LoginServlet extends HttpServlet {

    private UserDAO userDAO; // Declare UserDAO
    private TokenGrantScheduler tokenGrantScheduler; // Declare TokenGrantScheduler
    private RequestDAOImpl requestDAO; // Declare RequestDAO
    private TokenManagerDAOImpl tokenManagerDAO; // Declare TokenManagerDAO

    @Override
    public void init() throws ServletException {
        this.userDAO = new UserDAOImpl(); // Instantiate UserDAO
        this.requestDAO = new RequestDAOImpl(); // Instantiate RequestDAO
        this.tokenManagerDAO = new TokenManagerDAOImpl(); // Instantiate TokenManagerDAO

        // Instantiate the scheduler with the DAO instances
        this.tokenGrantScheduler = new TokenGrantScheduler(requestDAO,  tokenManagerDAO);
        this.tokenGrantScheduler.startScheduler(); // Start the scheduler
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> optionalUser = userDAO.findByUsernameAndPassword(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (BCrypt.checkpw(password, user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

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
                request.setAttribute("errorMessage", "Invalid username or password");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}
