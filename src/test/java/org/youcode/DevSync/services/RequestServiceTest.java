package org.youcode.DevSync.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.domain.exceptions.InvalidRequestException;
import org.youcode.DevSync.domain.exceptions.TokenHasAlreadyBeenUsedException;
import org.youcode.DevSync.services.RequestService;
import org.youcode.DevSync.validators.RequestValidator;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    private RequestDAO requestDAO;
    private TokenManagerDAO tokenManagerDAO;
    private RequestValidator validator;
    private RequestService requestService;

    private User user;
    private Task task;
    private Request request;

    @BeforeEach
    void setUp() {
        requestDAO = mock(RequestDAO.class);
        tokenManagerDAO = mock(TokenManagerDAO.class);
        validator = mock(RequestValidator.class);
        requestService = new RequestService(requestDAO, tokenManagerDAO, validator);

        user = new User(); // Create a sample user
        task = new Task(); // Create a sample task
        request = new Request(user, task, TokenType.DAILY);
    }

    @Test
    void createRequest_validInput() {
        when(requestDAO.save(any(Request.class))).thenReturn(request);

        Request createdRequest = requestService.createRequest(user, task, TokenType.DAILY);

        assertThat(createdRequest).isEqualTo(request);
        verify(validator).validateUser(user);
        verify(validator).validateTask(task);
        verify(validator).validateTokenLimit(user, TokenType.DAILY, requestDAO);
    }

    @Test
    void createRequest_tokenAlreadyUsed() {
        task.setTokenUsed(true);

        assertThatThrownBy(() -> requestService.createRequest(user, task, TokenType.DAILY))
                .isInstanceOf(TokenHasAlreadyBeenUsedException.class)
                .hasMessage("A token has already been used for this task. You do not have permission to use a token on it.");
    }

    @Test
    void findRequestById_validId() {
        Long requestId = 1L;
        when(requestDAO.findById(requestId)).thenReturn(Optional.of(request));

        Optional<Request> foundRequest = requestService.findRequestById(requestId);

        assertThat(foundRequest).isPresent().contains(request);
        verify(validator).validateRequestId(requestId);
    }

//    @Test
//    void findRequestById_invalidId() {
//        Long requestId = null;
//        assertThatThrownBy(() -> requestService.findRequestById(requestId))
//                .isInstanceOf(InvalidRequestException.class)
//                .hasMessage("Invalid request ID");
//    }


    @Test
    void updateRequestStatus_validStatus() {
        Long requestId = 1L;
        RequestStatus status = RequestStatus.ACCEPTED;

        when(requestDAO.findById(requestId)).thenReturn(Optional.of(request));

        requestService.updateRequestStatus(requestId, status);

        verify(requestDAO).updateStatus(requestId, status);
    }

    @Test
    void deleteRequest_existingRequest() {
        Long requestId = 1L;
        when(requestDAO.findById(requestId)).thenReturn(Optional.of(request));

        boolean deleted = requestService.deleteRequest(requestId);

        assertThat(deleted).isTrue();
        verify(requestDAO).delete(request);
    }

    @Test
    void deleteRequest_nonExistingRequest() {
        Long requestId = 1L;
        when(requestDAO.findById(requestId)).thenReturn(Optional.empty());

        boolean deleted = requestService.deleteRequest(requestId);

        assertThat(deleted).isFalse();
    }

    @Test
    void grantTokensIfEligible_tokensGranted() {
        Long requestId = 1L;
        request.setTokensGranted(false);
        request.setResponseDeadline(LocalDateTime.now().minusDays(1));
        when(requestDAO.findById(requestId)).thenReturn(Optional.of(request));

        requestService.grantTokensIfEligible(requestId);

        assertThat(request.isTokensGranted()).isTrue();
        verify(requestDAO).update(request);
    }

    @Test
    void grantTokensIfEligible_noTokensGranted() {
        Long requestId = 1L;
        request.setTokensGranted(false);
        request.setResponseDeadline(LocalDateTime.now().plusDays(1));
        when(requestDAO.findById(requestId)).thenReturn(Optional.of(request));

        requestService.grantTokensIfEligible(requestId);

        assertThat(request.isTokensGranted()).isFalse();
        verify(requestDAO, never()).update(request);
    }

    @Test
    void updateRequest_requestNotFound() {
        Long requestId = 1L;
        RequestStatus status = RequestStatus.REFUSED;

        when(requestDAO.findById(requestId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> requestService.updateRequest(requestId, status, task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Request not found with ID: " + requestId);
    }
}
