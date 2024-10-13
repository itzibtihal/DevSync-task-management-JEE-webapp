package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
        TokenManager tokenManager = entityManager.createQuery("SELECT tm FROM TokenManager tm WHERE tm.user.id = :userId", TokenManager.class)
                .setParameter("userId", userId)
                .getSingleResult();
        entityManager.close();
        return Optional.ofNullable(tokenManager);
    }

    @Override
    public TokenManager save(TokenManager tokenManager) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(tokenManager);
        entityManager.getTransaction().commit();
        entityManager.close();
        return tokenManager;
    }
}