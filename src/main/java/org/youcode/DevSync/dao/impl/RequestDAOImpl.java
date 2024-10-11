package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.youcode.DevSync.dao.interfaces.RequestDAO;
import org.youcode.DevSync.domain.entities.Request;

import java.util.Optional;

public class RequestDAOImpl implements RequestDAO {

    private EntityManagerFactory entityManagerFactory;

    public RequestDAOImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public Request save(Request request) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(request);
        entityManager.getTransaction().commit();
        entityManager.close();
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
    public void delete(Request request) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(request) ? request : entityManager.merge(request));
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
