package org.youcode.DevSync.services;

import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.TokenManager;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.domain.exceptions.TokenHasAlreadyBeenUsedException;
import org.youcode.DevSync.validators.RequestValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RequestService {

    private final RequestDAO requestDAO;
    private final TokenManagerDAO tokenManagerDAO;
    private  final RequestValidator validator; // New validator class

    public RequestService(RequestDAO requestDAO, TokenManagerDAO tokenManagerDAO ,RequestValidator validator) {
        this.requestDAO = requestDAO;
        this.tokenManagerDAO = tokenManagerDAO;
        this.validator = validator;
    }

    public Request createRequest(User user, Task task, TokenType tokenType) {
        validator.validateUser(user);
        validator.validateTask(task);

        if (task.isTokenUsed()) {
            throw new TokenHasAlreadyBeenUsedException("A token has already been used for this task. You do not have permission to use a token on it.");
        }

        validator.validateTokenLimit(user, tokenType, requestDAO);
        Request request = new Request(user, task, tokenType);
        return requestDAO.save(request);
    }

    public Optional<Request> findRequestById(Long requestId) {
        validator.validateRequestId(requestId);
        return requestDAO.findById(requestId);
    }

    public void updateRequestStatus(Long requestId, RequestStatus status) {
        validator.validateRequestId(requestId);
        validator.validateStatus(status);
        requestDAO.updateStatus(requestId, status);
    }

    public boolean deleteRequest(Long requestId) {
        Optional<Request> requestOpt = requestDAO.findById(requestId);
        if (requestOpt.isPresent()) {
            requestDAO.delete(requestOpt.get());
            return true;
        }
        return false;
    }

    public List<Request> getAllRequestsByUser(User user) {
        validator.validateUser(user);
        return requestDAO.findAllRequestsByUserId(user);
    }

    public List<Request> getRequestsByUserAndStatus(User user, RequestStatus status) {
        validator.validateUser(user);
        validator.validateStatus(status);
        return requestDAO.findRequestsByUserIdAndStatus(user, status);
    }

    public List<Request> getAllRequestsByStatus(RequestStatus status) {
        validator.validateStatus(status);
        return requestDAO.findRequestsByStatus(status);
    }

    public List<Request> getRequestsExcludingStatus(RequestStatus status) {
        validator.validateStatus(status);
        return requestDAO.findRequestsExcludingStatus(status);
    }

    public void updateRequest(Long requestId, RequestStatus status, Task task) {
        validator.validateRequestId(requestId);
        validator.validateStatus(status);
        validator.validateTask(task);

        Optional<Request> requestOpt = requestDAO.findById(requestId);
        if (requestOpt.isPresent()) {
            Request request = requestOpt.get();
            request.setStatus(status);
            request.setTask(task);
            requestDAO.update(request);
        } else {
            throw new IllegalArgumentException("Request not found with ID: " + requestId);
        }
    }

    public void grantTokensIfEligible(Long requestId) {
        Request request = requestDAO.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (LocalDateTime.now().isAfter(request.getResponseDeadline()) && !request.isTokensGranted()) {
            Optional<TokenManager> tokenManagerOpt = tokenManagerDAO.findByUserId(request.getUser().getId());

            if (!tokenManagerOpt.isPresent()) {
                TokenManager newTokenManager = new TokenManager();
                newTokenManager.setUser(request.getUser());
                newTokenManager.setDailyTokens(4);
                tokenManagerDAO.save(newTokenManager);
            }

            request.setTokensGranted(true);
            requestDAO.update(request);
        }
    }
}
