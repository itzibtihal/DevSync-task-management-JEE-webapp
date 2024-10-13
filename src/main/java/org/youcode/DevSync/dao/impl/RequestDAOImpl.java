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
import java.util.List;
import java.util.Optional;

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
            System.out.println("Count for user on date " + date + ": " + count);
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
            entityManager.getTransaction().begin();
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Request r WHERE r.user = :user AND r.timestamp BETWEEN :startDate AND :endDate AND r.tokenType = :tokenType",
                    Long.class);
            query.setParameter("user", user);
            query.setParameter("startDate", startDate.atStartOfDay());
            query.setParameter("endDate", endDate.atTime(23, 59, 59));
            query.setParameter("tokenType", tokenType);
            long count = query.getSingleResult();
            entityManager.getTransaction().commit();
            return (int) count;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return 0;
        } finally {
            entityManager.close();
        }
    }



//    @Override
//    public int countRequestsByUserAndDate(User user, LocalDate date, TokenType tokenType) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            if (user != null) {
//                entityManager.merge(user);
//            }
//            TypedQuery<Long> query = entityManager.createQuery(
//                    "SELECT COUNT(r) FROM Request r WHERE r.user = :user AND FUNCTION('DATE', r.timestamp) = :date AND r.tokenType = :tokenType",
//                    Long.class);
//            query.setParameter("user", user);
//            query.setParameter("date", date);
//            query.setParameter("tokenType", tokenType);
//            long count = query.getSingleResult();
//            entityManager.getTransaction().commit();
//            return (int) count;
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            e.printStackTrace();
//            return 0;
//        } finally {
//            entityManager.close();
//        }
//    }
//
//    @Override
//    public int countRequestsByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate, TokenType tokenType) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            TypedQuery<Long> query = entityManager.createQuery(
//                    "SELECT COUNT(r) FROM Request r WHERE r.user = :user AND r.timestamp BETWEEN :startDate AND :endDate AND r.tokenType = :tokenType",
//                    Long.class);
//            query.setParameter("user", user);
//            query.setParameter("startDate", startDate.atStartOfDay());
//            query.setParameter("endDate", endDate.atTime(23, 59, 59));
//            long count = query.getSingleResult();
//            entityManager.getTransaction().commit();
//            return (int) count;
//        } catch (Exception e) {
//            if (entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().rollback();
//            }
//            e.printStackTrace();
//            return 0;
//        } finally {
//            entityManager.close();
//        }
//    }

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

}
