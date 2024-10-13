package org.youcode.DevSync.servlets.crudAdmin;

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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class TaskCrudServlet extends HttpServlet {

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

        switch (action) {
            case "listAll":
                List<Task> tasks = taskDAO.findAll();
                request.setAttribute("tasks", tasks);
                request.getRequestDispatcher("admin/task/task-crud.jsp").forward(request, response);
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
            request.getRequestDispatcher("admin/task/details.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
        }
    }

    private void handleDeleteTask(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String taskIdString = request.getParameter("id");
        UUID taskId = UUID.fromString(taskIdString);
        taskDAO.delete(taskId);
        response.sendRedirect("crudtask?action=listAll");
    }

    private void handleEditTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskIdString = request.getParameter("id");
        UUID taskId = UUID.fromString(taskIdString);
        Optional<Task> optionalTask = taskDAO.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            request.setAttribute("task", task);
            request.getRequestDispatcher("admin/task/edit-task.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Task not found");
        }
    }

    private void handleAddTask(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = userDAO.findAll();
        List<Tag> tags = tagDAO.findAll();
        request.setAttribute("users", users);
        request.setAttribute("tags", tags);
        request.getRequestDispatcher("admin/task/save.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getParameterMap().forEach((key, value) -> System.out.println(key + ": " + Arrays.toString(value)));

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
        response.sendRedirect("crudtask?action=listAll");
    }

}
