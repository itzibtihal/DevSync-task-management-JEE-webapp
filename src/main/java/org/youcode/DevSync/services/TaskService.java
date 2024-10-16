package org.youcode.DevSync.services;

import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.TokenManager;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.StatusTask;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.domain.exceptions.*;
import org.youcode.DevSync.validators.TaskValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskDAO taskDAO;
    private final TokenManagerDAO tokenManagerDAO;
    private final RequestDAO requestDAO;
    private final TaskValidator validator;

    public TaskService(TaskDAO taskDAO, TokenManagerDAO tokenManagerDAO, RequestDAO requestDAO, TaskValidator validator) {
        this.taskDAO = taskDAO;
        this.tokenManagerDAO = tokenManagerDAO;
        this.requestDAO = requestDAO;
        this.validator = validator;
    }

    public void requestDeleteTask(User user, UUID taskId) throws TaskNotFoundException {
        validator.validateUser(user);
        validator.validateTaskId(taskId);

        Task task = taskDAO.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        if (!task.getCreatedBy().equals(user)) {
            throw new InvalidUserException("Only the creator can request to delete this task.");
        }

        Request deletionRequest = new Request(user, task, TokenType.DAILY);
        requestDAO.save(deletionRequest);
    }

    public void processDeletionRequest(Long requestId) throws TokenLimitExceededException, TaskNotFoundException {
        validator.validateRequestId(requestId);

        Request request = requestDAO.findById(requestId)
                .orElseThrow(() -> new InvalidRequestException("Request not found with ID: " + requestId));

        if (!request.getTokenType().equals(TokenType.DAILY)) {
            throw new InvalidRequestException("Invalid deletion request.");
        }

        Task task = request.getTask();
        if (task == null) {
            throw new TaskNotFoundException("Task not found in the request.");
        }

        TokenManager tokenManager = tokenManagerDAO.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Token manager not found for user ID: " + request.getUser().getId()));

        task.deleteTask(tokenManager);

        taskDAO.save(task);
        tokenManagerDAO.save(tokenManager);

        requestDAO.delete(request);
    }

    public void updateTaskStatus(Task task, StatusTask newStatus) {
        validator.validateTask(task);
        validator.validateStatus(newStatus);

        task.setStatus(newStatus);
        taskDAO.update(task);
    }

    public void updateTaskStatusesForUser(User user) {
        validator.validateUser(user);

        List<Task> tasks = taskDAO.findByAssignedUser(user.getId());
        LocalDateTime now = LocalDateTime.now();

        for (Task task : tasks) {
            if (task.getDueDate().isBefore(now) && task.getStatus() != StatusTask.OVERDUE) {
                task.setStatus(StatusTask.OVERDUE);
                taskDAO.update(task);
            }
        }
    }

    public List<Task> filterOverdueTasks(List<Task> tasks) {
        if (tasks == null) {
            throw new InvalidTaskException("Task list cannot be null.");
        }

        LocalDateTime now = LocalDateTime.now();
        return tasks.stream()
                .filter(task -> task.getDueDate().isBefore(now))
                .collect(Collectors.toList());
    }

    public boolean updateAssignedUser(UUID taskId, User newAssignedUser) throws TaskNotFoundException {
        validator.validateTaskId(taskId);
        validator.validateAssignedUser(newAssignedUser);

        Task task = taskDAO.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        task.setAssignedUser(newAssignedUser);
        task.setTokenUsed(true);

        return taskDAO.update(task);
    }

    public long countAssignedTasksCreatedByUser(UUID assignedUserId, UUID creatorUserId) {
        validator.validateUserId(assignedUserId);
        validator.validateUserId(creatorUserId);

        long count = taskDAO.countAssignedTasksCreatedByUser(assignedUserId, creatorUserId);

        if (count < 0) {
            throw new InvalidTaskException("Count of tasks cannot be negative.");
        }

        return count;
    }

    public long countCompletedTasksAssignedToUserCreatedBy(UUID assignedUserId, UUID creatorUserId) {
        validator.validateUserId(assignedUserId);
        validator.validateUserId(creatorUserId);

        long count = taskDAO.countCompletedTasksAssignedToUserCreatedBy(assignedUserId, creatorUserId);

        if (count < 0) {
            throw new InvalidTaskException("Count of completed tasks cannot be negative.");
        }

        return count;
    }

    public List<User> findUsersForTasksCreatedBy(UUID creatorUserId) {
        validator.validateUserId(creatorUserId);
        return taskDAO.findUsersForTasksCreatedBy(creatorUserId);
    }

    public double calculateUserAchievement(UUID userId) {
        validator.validateUserId(userId);

        List<User> assignedUsers = taskDAO.findUsersForTasksCreatedBy(userId);
        double totalAchievement = 0.0;

        for (User assignedUser : assignedUsers) {
            long assignedTaskCount = countAssignedTasksCreatedByUser(assignedUser.getId(), userId);
            long completedTaskCount = countCompletedTasksAssignedToUserCreatedBy(assignedUser.getId(), userId);

            if (assignedTaskCount > 0) {
                double achievement = (double) completedTaskCount / assignedTaskCount * 100;
                totalAchievement += achievement;
            }
        }

        return assignedUsers.isEmpty() ? 0.0 : totalAchievement / assignedUsers.size();
    }

    public long countRequestsCreatedToday(UUID creatorUserId) {
        validator.validateUserId(creatorUserId);
        return requestDAO.countRequestsCreatedToday(creatorUserId);
    }

    public long countDailyRequests(UUID creatorUserId) {
        validator.validateUserId(creatorUserId);
        return requestDAO.countDailyRequests(creatorUserId);
    }

    public long countMonthlyRequests(UUID creatorUserId) {
        validator.validateUserId(creatorUserId);
        return requestDAO.countMonthlyRequests(creatorUserId);
    }

    public long countTotalTokensUsed(UUID creatorUserId) {
        validator.validateUserId(creatorUserId);
        return requestDAO.countTotalTokensUsed(creatorUserId);
    }

    public long countTotalTasksByTags(UUID creatorUserId, List<String> tags, LocalDate startDate, LocalDate endDate) {
        validator.validateUserId(creatorUserId);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        validator.validateDateRange(startDateTime, endDateTime);

        return taskDAO.countTotalTasksByTags(creatorUserId, tags, startDate, endDate);
    }

    public long countCompletedTasksByTags(UUID creatorUserId, List<String> tags, LocalDate startDate, LocalDate endDate) {
        validator.validateUserId(creatorUserId);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        validator.validateDateRange(startDateTime, endDateTime);

        return taskDAO.countCompletedTasksByTags(creatorUserId, tags, startDate, endDate);
    }

    public double calculateCompletionPercentage(UUID creatorUserId, List<String> tags, LocalDate startDate, LocalDate endDate) {
        long totalTasks = countTotalTasksByTags(creatorUserId, tags, startDate, endDate);
        long completedTasks = countCompletedTasksByTags(creatorUserId, tags, startDate, endDate);

        if (totalTasks == 0) {
            return 0.0;
        }

        return ((double) completedTasks / totalTasks) * 100;
    }
}
