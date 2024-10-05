package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.enums.StatusTask;

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
}
