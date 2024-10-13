package org.youcode.DevSync.servlets.crudUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.TagDAOImpl;
import org.youcode.DevSync.dao.impl.TaskDAOImpl;
import org.youcode.DevSync.dao.impl.UserDAOImpl;
import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.StatusTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserTaskCrudServlet extends HttpServlet {

    private TaskDAOImpl taskDAO;
    private TagDAOImpl tagDAO;
    private UserDAOImpl userDAO;

    @Override
    public void init() throws ServletException {
        taskDAO = new TaskDAOImpl();
        tagDAO = new TagDAOImpl();
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        User loggedInUser = (User) request.getSession().getAttribute("user");
        UUID creatorId = loggedInUser != null ? loggedInUser.getId() : null;

        switch (action) {
            case "listAll":
                if (creatorId != null) {

                    List<Task> tasks = taskDAO.findByCreatedBy(creatorId);
                    request.setAttribute("tasks", tasks);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not logged in");
                    return;
                }
                request.getRequestDispatcher("user/myTasks/taskUser.jsp").forward(request, response);
                break;

            case "get":
                handleGetTask(request, response);
                break;

            case "delete":
                handleDeleteTask(request, response);
                break;

            case "edit":
                handleEditTask(request, response);
                break;

            case "add":
                handleAddTask(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }

    private void handleGetTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskIdString = request.getParameter("id");
        UUID taskId = UUID.fromString(taskIdString);
        Optional<Task> optionalTask = taskDAO.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            List<Tag> tags = tagDAO.findTagsByTaskId(task.getId());
            request.setAttribute("task", task);
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("user/myTasks/details.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
        }
    }

    private void handleDeleteTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String taskIdString = request.getParameter("id");
        UUID taskId = UUID.fromString(taskIdString);
        taskDAO.delete(taskId);
        response.sendRedirect("MyTask?action=listAll");
    }

    private void handleEditTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskIdString = request.getParameter("id");
        UUID taskId = UUID.fromString(taskIdString);
        Optional<Task> optionalTask = taskDAO.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            request.setAttribute("task", task);
            request.getRequestDispatcher("user/myTasks/edit-status.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
        }
    }

    private void handleAddTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        List<Tag> tags = tagDAO.findAll();
        request.setAttribute("users", users);
        request.setAttribute("tags", tags);
        request.getRequestDispatcher("user/myTasks/save.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Log all request parameters for debugging
        request.getParameterMap().forEach((key, value) -> System.out.println(key + ": " + Arrays.toString(value)));

        String action = request.getParameter("action");
        if ("updateStatus".equals(action)) {
            // Handle status update
            String taskIdString = request.getParameter("id");
            String statusString = request.getParameter("status");

            if (taskIdString == null || statusString == null || statusString.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required parameters are missing or empty");
                return;
            }

            UUID taskId = UUID.fromString(taskIdString);
            Optional<Task> optionalTask = taskDAO.findById(taskId);

            if (optionalTask.isPresent()) {
                Task task = optionalTask.get();
                task.setStatus(StatusTask.valueOf(statusString));
                taskDAO.update(task);

                response.sendRedirect("MyTask?action=listAll");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
            }
        } else {

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String[] tags = request.getParameterValues("tags");
            String assignedTo = request.getParameter("assignedUser");
            String startingDateStr = request.getParameter("startDate");
            String endingDateStr = request.getParameter("dueDate");
            String createdBy = request.getParameter("createdBy");


            if (title == null || title.isEmpty() ||
                    description == null || description.isEmpty() ||
                    startingDateStr == null || startingDateStr.isEmpty() ||
                    endingDateStr == null || endingDateStr.isEmpty() ||
                    createdBy == null || createdBy.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required parameters are missing or empty");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime startingDate;
            LocalDateTime endingDate;

            try {
                startingDate = LocalDateTime.parse(startingDateStr, formatter);
                endingDate = LocalDateTime.parse(endingDateStr, formatter);
            } catch (DateTimeParseException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format");
                return;
            }

            User user = userDAO.findById(UUID.fromString(createdBy))
                    .orElseThrow(() -> new ServletException("User not found"));

            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setStartDate(startingDate);
            task.setDueDate(endingDate);
            task.setCreatedBy(user);
            task.setStatus(StatusTask.valueOf(request.getParameter("status")));

            if (assignedTo != null && !assignedTo.isEmpty()) {
                User assignedUser = userDAO.findById(UUID.fromString(assignedTo))
                        .orElseThrow(() -> new ServletException("Assigned user not found"));
                task.setAssignedUser(assignedUser);
            }


            if (tags != null) {
                for (String tagId : tags) {
                    Tag tag = tagDAO.findById(UUID.fromString(tagId)).orElse(null);
                    if (tag != null) {
                        task.getTags().add(tag);
                    }
                }
            }

            taskDAO.save(task);
            response.sendRedirect("MyTask?action=listAll");
        }
    }
}
