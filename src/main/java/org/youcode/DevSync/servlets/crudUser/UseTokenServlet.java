package org.youcode.DevSync.servlets.crudUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TaskDAOImpl;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.services.RequestService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.UUID;


public class UseTokenServlet extends HttpServlet {
    private RequestDAO requestDAO;
    private TaskDAO taskDAO;
    private RequestService requestService;

    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAOImpl();
        taskDAO = new TaskDAOImpl();
        requestService = new RequestService(requestDAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedInUser = (User) request.getSession().getAttribute("user");
        String taskIdString = request.getParameter("taskId");
        String tokenTypeString = request.getParameter("tokenType");

        System.out.println("Received taskId: " + taskIdString);
        System.out.println("Received tokenType: " + tokenTypeString);

        if (loggedInUser != null && taskIdString != null && tokenTypeString != null) {
            if (!isValidUUID(taskIdString)) {
                response.sendRedirect("/UserTasks?error=" + URLEncoder.encode("Invalid task ID format.", "UTF-8"));
                return;
            }

            try {
                UUID taskId = UUID.fromString(taskIdString);
                TokenType tokenType = TokenType.valueOf(tokenTypeString.toUpperCase());

                Task task = getTaskById(taskId);

                if (task != null) {

                    Request tokenRequest = requestService.createRequest(loggedInUser, task, tokenType);
                    response.sendRedirect("/UserTasks?success=" + URLEncoder.encode("Token used successfully.", "UTF-8"));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
                }
            } catch (IllegalArgumentException e) {
                response.sendRedirect("/UserTasks?error=" + URLEncoder.encode("Invalid task ID or token type format.", "UTF-8"));
            } catch (IllegalStateException e) {
                response.sendRedirect("/UserTasks?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
            } catch (Exception e) {
                response.sendRedirect("/UserTasks?error=" + URLEncoder.encode("An unexpected error occurred: " + e.getMessage(), "UTF-8"));
            }
        } else {
            response.sendRedirect("/UserTasks?error=" + URLEncoder.encode("You must be logged in to use a token.", "UTF-8"));
        }
    }

    private Task getTaskById(UUID taskId) {
        Optional<Task> taskOpt = taskDAO.findById(taskId);
        return taskOpt.orElse(null);
    }
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
