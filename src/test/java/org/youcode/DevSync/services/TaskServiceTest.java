package org.youcode.DevSync.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.StatusTask;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.domain.exceptions.*;
import org.youcode.DevSync.validators.TaskValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private TokenManagerDAO tokenManagerDAO;

    @Mock
    private RequestDAO requestDAO;

    @Mock
    private TaskValidator validator;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private UUID taskId;
    private Long requestId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(); // Initialize as needed
        taskId = UUID.randomUUID();
        task = new Task();
        requestId = 1L;
    }

    @Test
    void requestDeleteTask_invalidUser() {
        doThrow(new InvalidUserException("Invalid user")).when(validator).validateUser(any(User.class));
        assertThatThrownBy(() -> taskService.requestDeleteTask(user, taskId))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Invalid user");
    }

    @Test
    void requestDeleteTask_taskNotFound() {
        when(taskDAO.findById(taskId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.requestDeleteTask(user, taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with ID: " + taskId);
    }




    @Test
    void processDeletionRequest_requestNotFound() {
        when(requestDAO.findById(requestId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.processDeletionRequest(requestId))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Request not found with ID: " + requestId);
    }

    @Test
    void updateTaskStatus_invalidTask() {
        doThrow(new InvalidTaskException("Invalid task")).when(validator).validateTask(any(Task.class));
        assertThatThrownBy(() -> taskService.updateTaskStatus(task, StatusTask.IN_PROGRESS))
                .isInstanceOf(InvalidTaskException.class)
                .hasMessage("Invalid task");
    }

    @Test
    void updateTaskStatus_invalidStatus() {
        doThrow(new InvalidStatusException("Invalid status"))
                .when(validator).validateStatus(null); // Use null explicitly

        assertThatThrownBy(() -> taskService.updateTaskStatus(task, null))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessage("Invalid status");
    }


    @Test
    void updateAssignedUser_taskNotFound() {
        when(taskDAO.findById(taskId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.updateAssignedUser(taskId, user))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with ID: " + taskId);
    }

    @Test
    void countAssignedTasksCreatedByUser_validInput() {
        UUID assignedUserId = UUID.randomUUID();
        UUID creatorUserId = UUID.randomUUID();
        when(taskDAO.countAssignedTasksCreatedByUser(assignedUserId, creatorUserId)).thenReturn(5L);

        long count = taskService.countAssignedTasksCreatedByUser(assignedUserId, creatorUserId);

        assertEquals(5L, count);
    }

    @Test
    void countCompletedTasksAssignedToUserCreatedBy_validInput() {
        UUID assignedUserId = UUID.randomUUID();
        UUID creatorUserId = UUID.randomUUID();
        when(taskDAO.countCompletedTasksAssignedToUserCreatedBy(assignedUserId, creatorUserId)).thenReturn(3L);

        long count = taskService.countCompletedTasksAssignedToUserCreatedBy(assignedUserId, creatorUserId);

        assertEquals(3L, count);
    }

    @Test
    void findUsersForTasksCreatedBy_validInput() {
        UUID creatorUserId = UUID.randomUUID();
        List<User> users = List.of(new User(), new User());
        when(taskDAO.findUsersForTasksCreatedBy(creatorUserId)).thenReturn(users);

        List<User> result = taskService.findUsersForTasksCreatedBy(creatorUserId);

        assertEquals(users, result);
    }

    @Test
    void calculateUserAchievement_validInput() {
        UUID userId = UUID.randomUUID();
        User assignedUser = new User(); // Initialize as needed
        when(taskDAO.findUsersForTasksCreatedBy(userId)).thenReturn(List.of(assignedUser));
        when(taskDAO.countAssignedTasksCreatedByUser(assignedUser.getId(), userId)).thenReturn(10L);
        when(taskDAO.countCompletedTasksAssignedToUserCreatedBy(assignedUser.getId(), userId)).thenReturn(5L);

        double achievement = taskService.calculateUserAchievement(userId);

        assertEquals(50.0, achievement);
    }

    @Test
    void updateTaskStatusesForUser_validInput() {
        List<Task> tasks = List.of(new Task(), new Task());
        tasks.get(0).setDueDate(LocalDateTime.now().minusDays(1));
        tasks.get(1).setDueDate(LocalDateTime.now().plusDays(1));
        when(taskDAO.findByAssignedUser(user.getId())).thenReturn(tasks);

        taskService.updateTaskStatusesForUser(user);

        assertEquals(StatusTask.OVERDUE, tasks.get(0).getStatus());
        //assertEquals(StatusTask.NOT_OVERDUE, tasks.get(1).getStatus());
    }

    @Test
    void filterOverdueTasks_validInput() {
        List<Task> tasks = List.of(new Task(), new Task());
        tasks.get(0).setDueDate(LocalDateTime.now().minusDays(1));
        tasks.get(1).setDueDate(LocalDateTime.now().plusDays(1));

        List<Task> overdueTasks = taskService.filterOverdueTasks(tasks);

        assertEquals(1, overdueTasks.size());
        assertEquals(tasks.get(0), overdueTasks.get(0));
    }

    @Test
    void updateAssignedUser_validInput() throws TaskNotFoundException {
        User newAssignedUser = new User();
        newAssignedUser.setId(UUID.randomUUID());

        Task task = new Task();
        task.setId(taskId);
        task.setAssignedUser(null);


        when(taskDAO.findById(taskId)).thenReturn(Optional.of(task));

        when(taskDAO.update(task)).thenReturn(true);

        boolean result = taskService.updateAssignedUser(taskId, newAssignedUser);


        assertTrue(result);
        assertEquals(newAssignedUser, task.getAssignedUser());
        assertTrue(task.isTokenUsed());
    }

}
