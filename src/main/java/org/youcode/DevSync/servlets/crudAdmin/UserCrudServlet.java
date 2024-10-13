package org.youcode.DevSync.servlets.crudAdmin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.domain.enums.Role;
import org.youcode.DevSync.domain.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/cruduser")
public class UserCrudServlet extends HttpServlet {

    private UserDAOImpl userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("listAll".equals(action)) {
            List<User> managers = userDAO.findByRole(Role.MANAGER);
            request.setAttribute("managers", managers);

            List<User> users = userDAO.findAll();
            System.out.println("Number of users retrieved: " + users.size());
            request.setAttribute("users", users);

            request.getRequestDispatcher("admin/users/userCrud.jsp").forward(request, response);

        } else if ("get".equals(action)) {
            String userIdString = request.getParameter("id");
            UUID userId = UUID.fromString(userIdString);
            Optional<User> optionalUser = userDAO.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                request.setAttribute("user", user);
                request.getRequestDispatcher("update.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } else if ("delete".equals(action)) {
            String userIdString = request.getParameter("id");
            UUID userId = UUID.fromString(userIdString);
            userDAO.delete(userId);

            response.sendRedirect("cruduser?action=listAll");
        } else if ("edit".equals(action)) {
            String userIdString = request.getParameter("id");
            UUID userId = UUID.fromString(userIdString);
            Optional<User> optionalUser = userDAO.findById(userId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                request.setAttribute("user", user);
                request.getRequestDispatcher("admin/users/editUser.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } else if ("add".equals(action)) {
            request.getRequestDispatcher("admin/users/save-user.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        if (method != null && method.equalsIgnoreCase("put")) {
            doPut(request, response);
        } else {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            Role role = Role.valueOf(request.getParameter("role").toUpperCase());

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRole(role);

            userDAO.save(user);

            response.sendRedirect("cruduser?action=listAll");
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdString = request.getParameter("id");
        UUID userId = UUID.fromString(userIdString);

        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String roleString = request.getParameter("role");
        Role role = Role.valueOf(roleString);
        String password = request.getParameter("password");

        Optional<User> optionalUser = userDAO.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Optional<User> userWithSameUsername = userDAO.findByName(username);
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_CONFLICT, "Username already exists");
                return;
            }

            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setRole(role);
            user.setPassword(password);

            boolean result = userDAO.update(user);
            if (result) {
                response.sendRedirect("cruduser?action=listAll");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update user");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
        }
    }

}
