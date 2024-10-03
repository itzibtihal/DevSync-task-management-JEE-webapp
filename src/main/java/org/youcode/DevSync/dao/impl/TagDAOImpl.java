package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.youcode.DevSync.dao.interfaces.TagDAO;
import org.youcode.DevSync.domain.entities.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagDAOImpl implements TagDAO {

    private  final EntityManagerFactory entityManagerFactory;

    public TagDAOImpl() {
       this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public Tag save(Tag tag) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (tag.getId() != null) {
                tag = em.merge(tag);
            } else {
                em.persist(tag);
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
        return tag;
    }

    @Override
    public Optional<Tag> findById(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Tag tag = entityManager.find(Tag.class, id);
            return Optional.ofNullable(tag);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Tag> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Tag> query = entityManager.createQuery("SELECT u FROM Tag u", Tag.class);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean update(Tag tag) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(tag);
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
            Tag tag = entityManager.find(Tag.class,id);
            if (tag != null) {
                entityManager.remove(tag);
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
            e.printStackTrace();
            return false;
        } finally {
            entityManager.close();
        }
    }

    public List<Tag> findByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Tag> query = entityManager.createQuery("FROM Tag WHERE name = :name", Tag.class);
            query.setParameter("name", name);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }


}
