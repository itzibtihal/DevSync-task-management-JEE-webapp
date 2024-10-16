package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.entities.Request;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.enums.RequestStatus;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.TokenType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestDAO {

    Request save(Request request);

    Optional<Request> findById(Long requestId);

    boolean delete(Request request);

    int countRequestsByUserAndDate(User user, LocalDate date, TokenType tokenType);

    int countRequestsByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate, TokenType tokenType);

    RequestStatus updateStatus(Long requestId, RequestStatus status);

    List<Request> findAllRequestsByUserId(User user);

    List<Request> findRequestsByUserIdAndStatus(User user, RequestStatus status);

    void createTokenRequest(User user, Task task, TokenType tokenType);

    List<Request> findRequestsByStatusNot(User user, RequestStatus status);

    List<Request> findRequestsByStatus(RequestStatus status);

    List<Request> findRequestsExcludingStatus(RequestStatus status);

    void update(Request request);

    List<Request> findAllPendingRequests();

    long countRequestsCreatedToday(UUID creatorUserId);
    long countDailyRequests(UUID creatorUserId);
    long countMonthlyRequests(UUID creatorUserId);
    long countTotalTokensUsed(UUID creatorUserId);



}
