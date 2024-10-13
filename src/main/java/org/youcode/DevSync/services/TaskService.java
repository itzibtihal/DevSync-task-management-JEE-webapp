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
import org.youcode.DevSync.domain.exceptions.TokenLimitExceededException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskDAO taskDAO;
    private final TokenManagerDAO tokenManagerDAO;
    private final RequestDAO requestDAO;

    public TaskService(TaskDAO taskDAO, TokenManagerDAO tokenManagerDAO, RequestDAO requestDAO) {
        this.taskDAO = taskDAO;
        this.tokenManagerDAO = tokenManagerDAO;
        this.requestDAO = requestDAO;
    }

    public void requestDeleteTask(User user, UUID taskId) {
        Task task = taskDAO.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getCreatedBy().equals(user)) {
            throw new RuntimeException("Only the creator can request to delete this task.");
        }

        // Create a deletion request using the TokenType enum
        Request deletionRequest = new Request(user, task, TokenType.DAILY); // Assuming a daily token for deletion
        requestDAO.save(deletionRequest);
    }

    public void processDeletionRequest(Long requestId) throws TokenLimitExceededException {
        Request request = requestDAO.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));


        if (!request.getTokenType().equals(TokenType.DAILY)) {
            throw new RuntimeException("Invalid deletion request.");
        }

        Task task = request.getTask();
        if (task == null) {
            throw new RuntimeException("Task not found in request.");
        }

        TokenManager tokenManager = tokenManagerDAO.findByUserId(request.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Token manager not found"));


        task.deleteTask(tokenManager);

        taskDAO.save(task);
        tokenManagerDAO.save(tokenManager);

        requestDAO.delete(request);
    }

    public void updateTaskStatus(Task task, StatusTask newStatus) {
        task.setStatus(newStatus);
        taskDAO.update(task);
    }



    public void updateTaskStatusesForUser(User user) {
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
        LocalDateTime now = LocalDateTime.now();
        return tasks.stream()
                .filter(task -> task.getDueDate().isAfter(now))
                .collect(Collectors.toList());
    }

}
