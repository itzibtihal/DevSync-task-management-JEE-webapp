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


import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        task = new Task(); // Initialize as needed
        requestId = 1L; // Example request ID
    }

    @Test
    void requestDeleteTask_invalidUser() {
        doThrow(new InvalidUserException("Invalid user")).when(validator).validateUser(any(User.class));
        assertThatThrownBy(() -> taskService.requestDeleteTask(user, taskId))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Invalid user");
    }

//    @Test
//    void requestDeleteTask_invalidTaskId() {
//        doThrow(new InvalidTaskIdException("Invalid task ID")).when(validator).validateTaskId(any(UUID.class));
//        assertThatThrownBy(() -> taskService.requestDeleteTask(user, null))
//                .isInstanceOf(InvalidTaskIdException.class)
//                .hasMessage("Invalid task ID");
//    }

    @Test
    void requestDeleteTask_taskNotFound() {
        when(taskDAO.findById(taskId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.requestDeleteTask(user, taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with ID: " + taskId);
    }

    @Test
    void processDeletionRequest_invalidRequestId() {
        doThrow(new InvalidRequestException("Invalid request ID")).when(validator).validateRequestId(any(Long.class));
        assertThatThrownBy(() -> taskService.processDeletionRequest(null))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Invalid request ID");
    }

    @Test
    void processDeletionRequest_requestNotFound() {
        when(requestDAO.findById(requestId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.processDeletionRequest(requestId))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("Request not found with ID: " + requestId);
    }

    @Test
    void processDeletionRequest_taskNotInRequest() {
        Request request = new Request(user, task, TokenType.DAILY);
        when(requestDAO.findById(requestId)).thenReturn(Optional.of(request));
        when(request.getTask()).thenReturn(null);
        assertThatThrownBy(() -> taskService.processDeletionRequest(requestId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found in the request.");
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
        doThrow(new InvalidStatusException("Invalid status")).when(validator).validateStatus(any(StatusTask.class));
        assertThatThrownBy(() -> taskService.updateTaskStatus(task, null))
                .isInstanceOf(InvalidStatusException.class)
                .hasMessage("Invalid status");
    }

//    @Test
//    void updateAssignedUser_invalidTaskId() {
//        doThrow(new InvalidTaskIdException("Invalid task ID")).when(validator).validateTaskId(any(UUID.class));
//        assertThatThrownBy(() -> taskService.updateAssignedUser(null, user))
//                .isInstanceOf(InvalidTaskIdException.class)
//                .hasMessage("Invalid task ID");
//    }

    @Test
    void updateAssignedUser_taskNotFound() {
        when(taskDAO.findById(taskId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.updateAssignedUser(taskId, user))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with ID: " + taskId);
    }


}
