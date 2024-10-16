package org.youcode.DevSync.servlets.crudUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.TagDAOImpl;
import org.youcode.DevSync.dao.impl.TaskDAOImpl;
import org.youcode.DevSync.dao.impl.RequestDAOImpl;
import org.youcode.DevSync.dao.impl.TokenManagerDAOImpl;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.StatusTask;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.domain.exceptions.TaskNotFoundException;
import org.youcode.DevSync.services.TaskService;
import org.youcode.DevSync.services.RequestService;
import org.youcode.DevSync.validators.RequestValidator;
import org.youcode.DevSync.validators.TaskValidator;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// Ensure you have a correct servlet mapping
public class TaskServlet extends HttpServlet {
    private TaskDAOImpl taskDAO;
    private TagDAOImpl tagDAO;
    private RequestDAO requestDAO;
    private TokenManagerDAO tokenManagerDAO;
    private TaskService taskService;
    private RequestService requestService;
    private RequestValidator validator;
    private TaskValidator taskvalidator;


    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAOImpl();
        tagDAO = new TagDAOImpl();
        requestDAO = new RequestDAOImpl();
        tokenManagerDAO = new TokenManagerDAOImpl();

        validator = new RequestValidator();

        taskvalidator = new TaskValidator();
        taskService = new TaskService(taskDAO, tokenManagerDAO, requestDAO,taskvalidator);
        requestService = new RequestService(requestDAO, tokenManagerDAO, validator);// Pass TokenManagerDAO to RequestService
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listTasks(request, response);
                break;
            case "details":
                showTaskDetails(request, response);
                break;
            case "delete":
                deleteTask(request, response);
                break;
            default:
                listTasks(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "updateStatus":
                updateTaskStatus(request, response);
                break;
            case "useToken":
                useToken(request, response);
                break;

            default:
                listTasks(request, response);
                break;
        }
    }

    private void listTasks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedInUser = (User) request.getSession().getAttribute("user");

        if (loggedInUser != null) {
            taskService.updateTaskStatusesForUser(loggedInUser);

            List<Task> allTasks = taskDAO.findByAssignedUser(loggedInUser.getId());

            List<Task> tasks = allTasks.stream()
                    .filter(task -> !task.getCreatedBy().equals(loggedInUser.getId()))
                    .collect(Collectors.toList());

            long notStartedCount = tasks.stream()
                    .filter(task -> task.getStatus() == StatusTask.NOT_STARTED)
                    .count();

            request.setAttribute("tasks", tasks);
            request.setAttribute("notStartedCount", notStartedCount);
        }

        request.getRequestDispatcher("/user/tasks/tasks.jsp").forward(request, response);
    }

    private void showTaskDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskIdString = request.getParameter("taskId");
        try {
            UUID taskId = UUID.fromString(taskIdString);
            Optional<Task> optionalTask = taskDAO.findById(taskId);

            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                List<Tag> tags = tagDAO.findTagsByTaskId(task.getId());
                request.setAttribute("task", task);
                request.setAttribute("tags", tags);
                request.getRequestDispatcher("/user/tasks/taskDetails.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
            }
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid task ID format.");
        }
    }

    private void deleteTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedInUser = (User) request.getSession().getAttribute("user");
        String taskIdString = request.getParameter("taskId");

        if (loggedInUser != null && taskIdString != null) {
            try {
                UUID taskId = UUID.fromString(taskIdString);
                taskService.requestDeleteTask(loggedInUser, taskId);
                request.setAttribute("message", "Deletion request has been submitted successfully.");
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid task ID format.");
            } catch (RuntimeException e) {
                request.setAttribute("error", e.getMessage());
            } catch (TaskNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            request.setAttribute("error", "You must be logged in to request deletion.");
        }

        listTasks(request, response);
    }

    private void updateTaskStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedInUser = (User) request.getSession().getAttribute("user");
        String taskIdString = request.getParameter("taskId");
        String statusString = request.getParameter("status");


        if (loggedInUser != null && taskIdString != null && statusString != null) {
            try {
                UUID taskId = UUID.fromString(taskIdString);
                StatusTask status = StatusTask.valueOf(statusString);
                Optional<Task> optionalTask = taskDAO.findById(taskId);

                if (optionalTask.isPresent()) {
                    Task task = optionalTask.get();
                    taskService.updateTaskStatus(task, status);
                    request.setAttribute("message", "Task status updated successfully.");
                } else {
                    request.setAttribute("error", "Task not found.");
                }
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid task ID or status.");
            }
        } else {
            request.setAttribute("error", "You must be logged in to update the task.");
        }


        showTaskDetails(request, response);
    }

    private void useToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User loggedInUser = (User) request.getSession().getAttribute("user");
        String taskIdString = request.getParameter("taskId");
        String tokenTypeString = request.getParameter("tokenType");

        if (loggedInUser != null && taskIdString != null && tokenTypeString != null) {
            if (!isValidUUID(taskIdString)) {
                response.sendRedirect("/DevSync/UserTasks?error=" + URLEncoder.encode("Invalid task ID format.", "UTF-8"));
                return;
            }

            try {
                UUID taskId = UUID.fromString(taskIdString);
                TokenType tokenType = TokenType.valueOf(tokenTypeString.toUpperCase());

                Task task = getTaskById(taskId);
                if (task != null) {
                    // Use the DAO
                    //requestDAO.createTokenRequest(loggedInUser, task, tokenType);
                    // Use the RequestService
                    Request requestResult = requestService.createRequest(loggedInUser, task, tokenType);



                    response.sendRedirect("/DevSync/UserTasks?success=" + URLEncoder.encode("Token used successfully.", "UTF-8"));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found.");
                }
            } catch (IllegalArgumentException e) {
                response.sendRedirect("/DevSync/UserTasks?error=" + URLEncoder.encode("Invalid task ID or token type format.", "UTF-8"));
            } catch (Exception e) {
                response.sendRedirect("/DevSync/UserTasks?error=" + URLEncoder.encode("An unexpected error occurred: " + e.getMessage(), "UTF-8"));
            }
        } else {
            response.sendRedirect("/DevSync/UserTasks?error=" + URLEncoder.encode("You must be logged in to use a token.", "UTF-8"));
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
