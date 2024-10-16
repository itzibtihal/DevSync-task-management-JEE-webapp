package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
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
import java.util.UUID;

public class RequestDAOImpl implements RequestDAO {

    private EntityManagerFactory entityManagerFactory;

    public RequestDAOImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public Request save(Request request) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(request);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
        return request;
    }

    @Override
    public Optional<Request> findById(Long requestId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Request request = entityManager.find(Request.class, requestId);
        entityManager.close();
        return Optional.ofNullable(request);
    }

    @Override
    public boolean delete(Request request) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.remove(entityManager.contains(request) ? request : entityManager.merge(request));
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return false;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int countRequestsByUserAndDate(User user, LocalDate date, TokenType tokenType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Request r WHERE r.user = :user AND FUNCTION('DATE', r.timestamp) = :date AND r.tokenType = :tokenType",
                    Long.class);
            query.setParameter("user", user);
            query.setParameter("date", date);
            query.setParameter("tokenType", tokenType);
            long count = query.getSingleResult();
            return (int) count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int countRequestsByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate, TokenType tokenType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Request r WHERE r.user = :user AND r.timestamp BETWEEN :startDate AND :endDate AND r.tokenType = :tokenType",
                    Long.class);
            query.setParameter("user", user);
            query.setParameter("startDate", startDate.atStartOfDay());
            query.setParameter("endDate", endDate.atTime(23, 59, 59));
            long count = query.getSingleResult();
            return (int) count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public RequestStatus updateStatus(Long requestId, RequestStatus status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Request request = entityManager.find(Request.class, requestId);
        if (request != null) {
            entityManager.getTransaction().begin();
            request.setStatus(status);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        return status;
    }

    @Override
    public List<Request> findAllRequestsByUserId(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.user = :user", Request.class);
        query.setParameter("user", user);
        List<Request> requests = query.getResultList();
        entityManager.close();
        return requests;
    }

    @Override
    public List<Request> findRequestsByUserIdAndStatus(User user, RequestStatus status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.user = :user AND r.status = :status", Request.class);
        query.setParameter("user", user);
        query.setParameter("status", status);
        List<Request> requests = query.getResultList();
        entityManager.close();
        return requests;
    }

    @Override
    public void createTokenRequest(User user, Task task, TokenType tokenType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            Request request = new Request();
            request.setUser(user);
            request.setTask(task);
            request.setTokenType(tokenType);
            request.setStatus(RequestStatus.IN_PROGRESS);
            request.setTimestamp(LocalDate.now().atStartOfDay());

            entityManager.persist(request);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Request> findRequestsByStatusNot(User user, RequestStatus status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.user = :user AND r.status != :status", Request.class);
        query.setParameter("user", user);
        query.setParameter("status", status);
        List<Request> requests = query.getResultList();
        entityManager.close();
        return requests;
    }

    @Override
    public List<Request> findRequestsByStatus(RequestStatus status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.status = :status", Request.class);
        query.setParameter("status", status);
        List<Request> requests = query.getResultList();
        entityManager.close();
        return requests;
    }

    @Override
    public List<Request> findRequestsExcludingStatus(RequestStatus status) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Request> query = entityManager.createQuery(
                "SELECT r FROM Request r WHERE r.status <> :status", Request.class);
        query.setParameter("status", status);
        List<Request> requests = query.getResultList();
        entityManager.close();
        return requests;
    }

    @Override
    public void update(Request request) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(request);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Request> findAllPendingRequests() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusHours(12);
            TypedQuery<Request> query = entityManager.createQuery(
                    "SELECT r FROM Request r WHERE r.responseDeadline < :cutoffTime AND r.tokensGranted = false AND r.status = :status", Request.class);
            query.setParameter("cutoffTime", cutoffTime);
            query.setParameter("status", RequestStatus.IN_PROGRESS);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countRequestsCreatedToday(UUID creatorUserId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Request r WHERE r.task.createdBy.id = :creatorUserId AND r.timestamp >= CURRENT_DATE", Long.class);
            query.setParameter("creatorUserId", creatorUserId);
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countDailyRequests(UUID creatorUserId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Request r WHERE r.task.createdBy.id = :creatorUserId AND r.tokenType = :tokenType", Long.class);
            query.setParameter("creatorUserId", creatorUserId);
            query.setParameter("tokenType", TokenType.DAILY);
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countMonthlyRequests(UUID creatorUserId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Request r WHERE r.task.createdBy.id = :creatorUserId AND r.tokenType = :tokenType", Long.class);
            query.setParameter("creatorUserId", creatorUserId);
            query.setParameter("tokenType", TokenType.MONTHLY);
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public long countTotalTokensUsed(UUID creatorUserId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT count(r) FROM Request r WHERE r.task.createdBy.id = :creatorUserId", Long.class);
            query.setParameter("creatorUserId", creatorUserId);
            Long result = query.getSingleResult();
            return result != null ? result : 0;
        } finally {
            entityManager.close();
        }
    }
}
