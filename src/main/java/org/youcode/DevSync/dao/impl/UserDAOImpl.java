package org.youcode.DevSync.dao.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.youcode.DevSync.dao.interfaces.UserDAO;
import org.youcode.DevSync.domain.enums.Role;
import org.youcode.DevSync.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDAOImpl implements UserDAO {

    private final EntityManagerFactory entityManagerFactory;

    public UserDAOImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public User save(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (user.getId() != null) {
                user = em.merge(user);
            } else {
                em.persist(user);
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
        return user;
    }


    @Override
    public Optional<User> findById(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            User user = entityManager.find(User.class, id);
            return Optional.ofNullable(user);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean update(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean delete(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace(); // Log the exception
            return false;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> findByName(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getResultStream().findFirst();
        } finally {
            entityManager.close();
        }
    }


    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
            return em.createQuery(jpql, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getResultList()
                    .stream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findByRole(Role role) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.role = :role ORDER BY u.created_at DESC", User.class);
            query.setParameter("role", role);
            query.setMaxResults(4);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<User> findRecentUsers(int limit) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u ORDER BY u.created_at DESC", User.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }


}
