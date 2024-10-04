package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskDAO {
    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    boolean update(Task task);

    boolean delete(UUID id);
}
