package org.youcode.DevSync.services;

import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.enums.TokenType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RequestService {
    private final RequestDAO requestDAO;

    public RequestService(RequestDAO requestDAO) {
        this.requestDAO = requestDAO;
    }

    public Request createRequest(User user, Task task, TokenType tokenType) {
        validateUser(user);
        validateTask(task);

        if (task.isTokenUsed()) {
            throw new IllegalStateException("A token has already been used for this task. You do not have permission to use a token on it.");
        }

        validateTokenLimit(user, tokenType);

        Request request = new Request(user, task, tokenType);
        return requestDAO.save(request);
    }

    public Optional<Request> findRequestById(Long requestId) {
        validateRequestId(requestId);
        return requestDAO.findById(requestId);
    }

    public void updateRequestStatus(Long requestId, RequestStatus status) {
        validateRequestId(requestId);
        validateStatus(status);
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
        validateUser(user);
        System.out.println("Fetching all requests for user: " + user.getId());
        List<Request> requests = requestDAO.findAllRequestsByUserId(user);
        System.out.println("Requests fetched: " + requests.size());
        return requests;
    }

    public List<Request> getRequestsByUserAndStatus(User user, RequestStatus status) {
        validateUser(user);
        validateStatus(status);
        return requestDAO.findRequestsByUserIdAndStatus(user, status);
    }





    // Validation Methods
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        LocalDateTime now = LocalDateTime.now();
        if (task.getStartDate() != null && task.getStartDate().isBefore(now)) {
            throw new IllegalArgumentException("Cannot create a request for a task that starts in the past.");
        }
        if (task.getDueDate() != null && task.getDueDate().isBefore(now)) {
            throw new IllegalArgumentException("Cannot create a request for a task that is already overdue.");
        }
    }

    private void validateRequestId(Long requestId) {
        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException("Invalid request ID.");
        }
    }

    private void validateStatus(RequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
    }

    private void validateTokenLimit(User user, TokenType tokenType) {
        int[] tokenCounts = countUsedTokens(user, tokenType);
        int tokenCountToday = tokenCounts[0];
        int tokenCountLastMonth = tokenCounts[1];
        System.out.println(tokenCountLastMonth);

        if (tokenType == TokenType.DAILY && tokenCountToday >= 2) {
            throw new IllegalStateException("Daily token limit reached. You can only create 2 requests today.");
        } else if (tokenType == TokenType.MONTHLY) {
            if (hasMonthlyRequestInLast30Days(user)) {
                throw new IllegalStateException("You can only create one monthly request every 30 days.");
            }
//            if (tokenCountLastMonth > 0) {
//                throw new IllegalStateException("Monthly token limit reached. You cannot create a new monthly request.");
//            }
        } else if (tokenType == null) {
            throw new IllegalArgumentException("Invalid token type: " + tokenType);
        }
    }




    private int[] countUsedTokens(User user, TokenType tokenType) {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);

        int tokenCountToday = requestDAO.countRequestsByUserAndDate(user, today, tokenType);
        int tokenCountLastMonth = requestDAO.countRequestsByUserAndDateRange(user, thirtyDaysAgo, today, tokenType);

        System.out.println("Token Count Today: " + tokenCountToday);
        System.out.println("Token Count Last Month: " + tokenCountLastMonth);

        return new int[]{tokenCountToday, tokenCountLastMonth};
    }


    private boolean hasMonthlyRequestInLast30Days(User user) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        int count = requestDAO.countRequestsByUserAndDateRange(user, thirtyDaysAgo.toLocalDate(), LocalDate.now(), TokenType.MONTHLY);
        return count > 0;
    }





}
