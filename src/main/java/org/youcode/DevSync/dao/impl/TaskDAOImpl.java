package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.StatusTask;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskDAOImpl implements TaskDAO {

    private final EntityManagerFactory entityManagerFactory;

    public TaskDAOImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public Task save(Task task) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (task.getId() != null) {
                task = em.merge(task);
            } else {
                em.persist(task);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return task;
    }

    @Override
    public Optional<Task> findById(UUID id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Task task = em.find(Task.class, id);
            return Optional.ofNullable(task);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Task> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t", Task.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Task task) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(task);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(UUID id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Task task = em.find(Task.class, id);
            if (task != null) {
                em.remove(task);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Task> findByStatus(StatusTask status) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery("FROM Task WHERE status = :status", Task.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Task> findByAssignedUser(UUID userId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery("FROM Task WHERE assignedUser.id = :userId", Task.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Task> findByCreatedBy(UUID creatorId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery("FROM Task WHERE createdBy.id = :creatorId", Task.class);
            query.setParameter("creatorId", creatorId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public long countTasksByStatusForUser(UUID userId, StatusTask status) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Task t WHERE t.assignedUser.id = :userId AND t.status = :status", Long.class);
            query.setParameter("userId", userId);
            query.setParameter("status", status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }


    public List<Task> findTasksAssignedToUserNotCreatedBy(UUID assignedUserId, UUID creatorId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "SELECT t FROM Task t WHERE t.assignedUser.id = :assignedUserId AND t.createdBy.id != :creatorId", Task.class);
            query.setParameter("assignedUserId", assignedUserId);
            query.setParameter("creatorId", creatorId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public boolean updateAssignedUser(UUID taskId, User newAssignedUser) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Task task = em.find(Task.class, taskId);
            if (task != null) {
                task.setAssignedUser(newAssignedUser);
                task.setTokenUsed(true);
                em.merge(task);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public long countAssignedTasksCreatedByUser(UUID assignedUserId, UUID creatorUserId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Task t WHERE t.assignedUser.id = :assignedUserId AND t.createdBy.id = :creatorUserId", Long.class);
            query.setParameter("assignedUserId", assignedUserId);
            query.setParameter("creatorUserId", creatorUserId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countCompletedTasksAssignedToUserCreatedBy(UUID assignedUserId, UUID creatorUserId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(t) FROM Task t WHERE t.assignedUser.id = :assignedUserId AND t.createdBy.id = :creatorUserId AND t.status = :status", Long.class);
            query.setParameter("assignedUserId", assignedUserId);
            query.setParameter("creatorUserId", creatorUserId);
            query.setParameter("status", StatusTask.COMPLETED);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findUsersForTasksCreatedBy(UUID creatorUserId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT DISTINCT t.assignedUser FROM Task t WHERE t.createdBy.id = :creatorUserId", User.class);
            query.setParameter("creatorUserId", creatorUserId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countTotalTasksByTags(UUID creatorUserId, List<String> tags, LocalDate startDate, LocalDate endDate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Task t JOIN t.tags tag WHERE t.createdBy.id = :creatorUserId AND tag.name IN :tags AND t.createdAt BETWEEN :startDate AND :endDate", Long.class);
            query.setParameter("creatorUserId", creatorUserId);
            query.setParameter("tags", tags);
            query.setParameter("startDate", startDate.atStartOfDay()); // Convert LocalDate to LocalDateTime
            query.setParameter("endDate", endDate.plusDays(1).atStartOfDay()); // Convert LocalDate to LocalDateTime
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }


    @Override
    public long countCompletedTasksByTags(UUID creatorUserId, List<String> tags, LocalDate startDate, LocalDate endDate) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(t) FROM Task t JOIN t.tags tag WHERE t.createdBy.id = :creatorUserId AND tag.name IN :tags AND t.createdAt BETWEEN :startDate AND :endDate AND t.status = :completedStatus", Long.class);
            query.setParameter("creatorUserId", creatorUserId);
            query.setParameter("tags", tags);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setParameter("completedStatus", StatusTask.COMPLETED);
            return query.getSingleResult();
        } finally {
            entityManager.close();
        }
    }
}
