package org.youcode.DevSync.servlets.crudAdmin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TaskDAOImpl;
import org.youcode.DevSync.dao.impl.TokenManagerDAOImpl;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.dao.interfaces.UserDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.exceptions.TaskNotFoundException;
import org.youcode.DevSync.services.RequestService;
import org.youcode.DevSync.services.TaskService;
import org.youcode.DevSync.validators.RequestValidator;
import org.youcode.DevSync.validators.TaskValidator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersRequestServlet extends HttpServlet {

    private RequestService requestService;
    private UserDAO userDAO;
    private TaskService taskService;
    private RequestValidator validator; // Moved the validator declaration here
    private TaskValidator taskvalidator;

    @Override
    public void init() throws ServletException {
        // Initialize DAO implementations
        RequestDAO requestDAO = new RequestDAOImpl();
        userDAO = new UserDAOImpl();
        TaskDAO taskDAO = new TaskDAOImpl();
        TokenManagerDAO tokenManagerDAO = new TokenManagerDAOImpl();

        // Initialize the validator
        validator = new RequestValidator();
        taskvalidator = new TaskValidator();

        // Initialize the services with the appropriate dependencies
        requestService = new RequestService(requestDAO, tokenManagerDAO, validator); // Pass validator here
        taskService = new TaskService(taskDAO, tokenManagerDAO, requestDAO,taskvalidator);
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                Long requestId = Long.valueOf(idParam);
                Request requestDetails = requestService.findRequestById(requestId)
                        .orElseThrow(() -> new IllegalArgumentException("Request not found"));

                request.setAttribute("requestDetails", requestDetails);

                List<User> users = userDAO.findAll();
                request.setAttribute("users", users);

                request.getRequestDispatcher("/admin/requests/requestDetails.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID.");
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        } else {
            String action = request.getParameter("action");

            if ("not-accepted".equals(action)) {
                try {
                    RequestStatus excludedStatus = RequestStatus.REFUSED;
                    List<Request> requests = requestService.getRequestsExcludingStatus(excludedStatus);
                    request.setAttribute("requests", requests);
                    request.getRequestDispatcher("/admin/requests/usersrequest.jsp").forward(request, response);
                } catch (IllegalArgumentException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
            } else if ("refused".equals(action)) {
                try {
                    List<Request> refusedRequests = requestService.getAllRequestsByStatus(RequestStatus.REFUSED);
                    request.setAttribute("requests", refusedRequests);
                    request.getRequestDispatcher("/admin/requests/refusedreq.jsp").forward(request, response);
                } catch (IllegalArgumentException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
                }
            } else {
                // Handle other scenarios or default behavior
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestIdParam = request.getParameter("id");
        String statusParam = request.getParameter("status");
        String assignedUserIdParam = request.getParameter("assignedUser");

        if (requestIdParam != null && statusParam != null) {
            Long requestId = Long.parseLong(requestIdParam);
            RequestStatus status;

            try {
                status = RequestStatus.valueOf(statusParam.toUpperCase());
                requestService.updateRequestStatus(requestId, status);

                // If the status is ACCEPTED, also update the assigned user if provided
                if (assignedUserIdParam != null && status == RequestStatus.ACCEPTED) {
                    UUID assignedUserId = UUID.fromString(assignedUserIdParam);
                    User newAssignedUser = userDAO.findById(assignedUserId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found for ID: " + assignedUserId));

                    // Retrieve the task associated with the request
                    Request requestDetails = requestService.findRequestById(requestId)
                            .orElseThrow(() -> new IllegalArgumentException("Request not found for ID: " + requestId));

                    UUID taskId = requestDetails.getTask().getId(); // Assuming getTask() returns a Task object

                    // Update the assigned user for the task
                    taskService.updateAssignedUser(taskId, newAssignedUser);
                }

                response.sendRedirect(request.getContextPath() + " /UsersRequest");
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request status or user ID.");
            } catch (TaskNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID and status must be provided.");
        }
    }

}
