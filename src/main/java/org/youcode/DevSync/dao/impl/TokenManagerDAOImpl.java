package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import org.youcode.DevSync.dao.interfaces.TokenManagerDAO;
import org.youcode.DevSync.domain.entities.TokenManager;

import java.util.Optional;
import java.util.UUID;

public class TokenManagerDAOImpl implements TokenManagerDAO {

    private EntityManagerFactory entityManagerFactory;

    public TokenManagerDAOImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public Optional<TokenManager> findByUserId(UUID userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TokenManager tokenManager = null;
        try {
            tokenManager = entityManager.createQuery("SELECT tm FROM TokenManager tm WHERE tm.user.id = :userId", TokenManager.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Handle case where no result is found
            System.out.println("No TokenManager found for user ID: " + userId);
        } finally {
            entityManager.close();
        }
        return Optional.ofNullable(tokenManager);
    }

    @Override
    public TokenManager save(TokenManager tokenManager) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(tokenManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            // Handle transaction rollback in case of error
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e; // Rethrow or handle the exception appropriately
        } finally {
            entityManager.close();
        }
        return tokenManager;
    }

    // New update method

    @Override
    public void update(TokenManager tokenManager) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(tokenManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e; // Rethrow or handle the exception appropriately
        } finally {
            entityManager.close();
        }
    }
}
