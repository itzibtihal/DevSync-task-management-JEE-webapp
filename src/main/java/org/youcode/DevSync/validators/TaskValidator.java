package org.youcode.DevSync.validators;

import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.StatusTask;
import org.youcode.DevSync.domain.exceptions.InvalidRequestException;
import org.youcode.DevSync.domain.exceptions.InvalidTaskException;
import org.youcode.DevSync.domain.exceptions.InvalidUserException;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskValidator {

    public void validateTask(Task task) {
        if (task == null) {
            throw new InvalidTaskException("Task cannot be null.");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new InvalidTaskException("Task title cannot be null or empty.");
        }
        if (task.getDueDate() == null) {
            throw new InvalidTaskException("Task due date cannot be null.");
        }
        if (task.getDueDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTaskException("Task due date cannot be in the past.");
        }
    }

    public void validateTaskId(UUID taskId) {
        if (taskId == null) {
            throw new InvalidTaskException("Task ID cannot be null.");
        }
    }

    public void validateUser(User user) {
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }
    }

    public void validateUserId(UUID userId) {
        if (userId == null) {
            throw new InvalidUserException("User ID cannot be null.");
        }
    }

    public void validateAssignedUser(User assignedUser) {
        if (assignedUser == null) {
            throw new InvalidUserException("Assigned user cannot be null.");
        }
    }

    public void validateStatus(StatusTask status) {
        if (status == null) {
            throw new InvalidTaskException("Task status cannot be null.");
        }
    }

    public void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidTaskException("Start date and end date cannot be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidTaskException("Start date cannot be after end date.");
        }
    }

    public void validateRequestId(Long requestId) {
        if (requestId == null || requestId <= 0) {
            throw new InvalidRequestException("Invalid request ID.");
        }
    }


}
