package org.youcode.DevSync.validators;

import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.enums.TokenType;
import org.youcode.DevSync.domain.exceptions.*;
import org.youcode.DevSync.domain.exceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RequestValidator {

    public void validateUser(User user) {
        if (user == null) {
            throw new InvalidUserException("User cannot be null.");
        }
    }

    public void validateTask(Task task) {
        if (task == null) {
            throw new InvalidTaskException("Task cannot be null.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (task.getStartDate() != null && task.getStartDate().isBefore(now)) {
            throw new InvalidTaskException("Cannot create a request for a task that starts in the past.");
        }
        if (task.getDueDate() != null && task.getDueDate().isBefore(now)) {
            throw new InvalidTaskException("Cannot create a request for a task that is already overdue.");
        }
    }

    public void validateRequestId(Long requestId) {
        if (requestId == null) {
            throw new InvalidRequestException("Invalid request ID");
        }
        // Add any additional validation logic if needed
    }

    public void validateStatus(RequestStatus status) {
        if (status == null) {
            throw new InvalidStatusException("Status cannot be null.");
        }
    }

    public void validateTokenLimit(User user, TokenType tokenType, RequestDAO requestDAO) {
        int[] tokenCounts = countUsedTokens(user, tokenType, requestDAO);
        int tokenCountToday = tokenCounts[0];
        int tokenCountLastMonth = tokenCounts[1];

        boolean hadGrantedRequestYesterday = hasGrantedRequestYesterday(user, requestDAO);

        if (hadGrantedRequestYesterday) {
            if (tokenType == TokenType.DAILY && tokenCountToday >= 4) {
                throw new TokenLimitReachedException("Daily token limit reached. You can only create 4 requests today.");
            }
        } else {
            if (tokenType == TokenType.DAILY && tokenCountToday >= 2) {
                throw new TokenLimitReachedException("Daily token limit reached. You can only create 2 requests today.");
            }
        }

        if (tokenType == TokenType.MONTHLY) {
            if (hasMonthlyRequestInLast30Days(user, requestDAO)) {
                throw new TokenLimitReachedException("You can only create one monthly request every 30 days.");
            }
        } else if (tokenType == null) {
            throw new InvalidStatusException("Invalid token type: " + tokenType);
        }
    }

    private int[] countUsedTokens(User user, TokenType tokenType, RequestDAO requestDAO) {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);

        int tokenCountToday = requestDAO.countRequestsByUserAndDate(user, today, tokenType);
        int tokenCountLastMonth = requestDAO.countRequestsByUserAndDateRange(user, thirtyDaysAgo, today, tokenType);

        return new int[]{tokenCountToday, tokenCountLastMonth};
    }

    private boolean hasMonthlyRequestInLast30Days(User user, RequestDAO requestDAO) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        int count = requestDAO.countRequestsByUserAndDateRange(user, thirtyDaysAgo.toLocalDate(), LocalDate.now(), TokenType.MONTHLY);
        return count > 0;
    }

    private boolean hasGrantedRequestYesterday(User user, RequestDAO requestDAO) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return requestDAO.countRequestsByUserAndDate(user, yesterday, TokenType.DAILY) > 0;
    }
}
