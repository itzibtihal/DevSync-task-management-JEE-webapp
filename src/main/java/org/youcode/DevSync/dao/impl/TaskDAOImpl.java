package org.youcode.DevSync.dao.impl;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.enums.StatusTask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskDAOImpl implements TaskDAO {

    private  final EntityManagerFactory entityManagerFactory;

    public TaskDAOImpl() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }

    @Override
    public boolean update(Task task) {
        return false;
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    @Override
    public List<Task> findByStatus(StatusTask status) {
        return List.of();
    }

    @Override
    public List<Task> findByAssignedUser(UUID userId) {
        return List.of();
    }

    @Override
    public List<Task> findByCreatedBy(UUID creatorId) {
        return List.of();
    }
}
