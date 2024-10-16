package org.youcode.DevSync.servlets.crudAdmin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.youcode.DevSync.dao.impl.*;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.services.TaskService;
import org.youcode.DevSync.validators.RequestValidator;
import org.youcode.DevSync.validators.TaskValidator;

import java.io.IOException;

import java.time.LocalDate;
import java.util.*;


public class  UserAchievementServlet extends HttpServlet {

    private TaskService taskService;
    private TagDAOImpl tagDAO;
    private TaskValidator validator;

    @Override
    public void init() throws ServletException {
        TaskDAO taskDAO = new TaskDAOImpl();
        TokenManagerDAO tokenManagerDAO = new TokenManagerDAOImpl();
        RequestDAO requestDAO = new RequestDAOImpl();
        tagDAO = new TagDAOImpl();
        validator = new TaskValidator();
        taskService = new TaskService(taskDAO, tokenManagerDAO, requestDAO,validator);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User userauth = (User) request.getSession().getAttribute("user");
        UUID creatorUserId = userauth.getId();

        if (creatorUserId == null) {
            response.sendRedirect("/login");
            return;
        }

        long requestsToday = taskService.countRequestsCreatedToday(creatorUserId);
        long dailyRequests = taskService.countDailyRequests(creatorUserId);
        long monthlyRequests = taskService.countMonthlyRequests(creatorUserId);
        long totalTokensUsed = taskService.countTotalTokensUsed(creatorUserId);

        // Set statistics as attributes
        request.setAttribute("requestsToday", requestsToday);
        request.setAttribute("dailyRequests", dailyRequests);
        request.setAttribute("monthlyRequests", monthlyRequests);
        request.setAttribute("totalTokensUsed", totalTokensUsed);

        List<User> users = taskService.findUsersForTasksCreatedBy(creatorUserId);
        request.setAttribute("users", users);

        // Create maps to hold the total tasks and achievements for each user
        Map<UUID, Long> userTaskCounts = new HashMap<>();
        Map<UUID, Double> userAchievements = new HashMap<>();

        for (User user : users) {
            long totalTasks = taskService.countAssignedTasksCreatedByUser(user.getId(), creatorUserId);
            userTaskCounts.put(user.getId(), totalTasks);

            long completedTasks = taskService.countCompletedTasksAssignedToUserCreatedBy(user.getId(), creatorUserId);
            double achievement = totalTasks > 0 ? (double) completedTasks / totalTasks * 100 : 0.0;
            userAchievements.put(user.getId(), achievement);
        }

        request.setAttribute("userTaskCounts", userTaskCounts);
        request.setAttribute("userAchievements", userAchievements);



        //tags

        List<Tag> tags =  tagDAO.findAll();
        request.setAttribute("tags", tags);



        request.getRequestDispatcher("/admin/settings/index.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] selectedTags = request.getParameterValues("tags"); // Get selected tag IDs
        String startDateStr = request.getParameter("startDate"); // Get start date
        String endDateStr = request.getParameter("endDate"); // Get end date

        // Convert dates and process the logic as needed
        LocalDate startDateTime = LocalDate.parse(startDateStr);
        LocalDate endDateTime = LocalDate.parse(endDateStr);



        // Convert selected tag IDs to a List<String>
        List<String> tags = selectedTags != null ? Arrays.asList(selectedTags) : Collections.emptyList();

        // Get the creator user ID from the session
        User userauth = (User) request.getSession().getAttribute("user");
        UUID creatorUserId = userauth.getId();

        // Calculate completion percentage
        double completionPercentage = taskService.calculateCompletionPercentage(creatorUserId, tags, startDateTime, endDateTime);

        // Set the completion percentage as a request attribute
        request.setAttribute("completionPercentage", completionPercentage);

        // Forward the request to the JSP page to display the results
        request.getRequestDispatcher("/admin/settings/index.jsp").forward(request, response);
    }


}
