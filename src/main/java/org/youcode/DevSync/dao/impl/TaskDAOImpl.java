package org.youcode.DevSync.dao.impl;

import org.youcode.DevSync.dao.interfaces.TaskDAO;
import org.youcode.DevSync.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TaskDAOImpl implements TaskDAO {



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
}
