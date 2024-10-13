package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.entities.Task;
import org.youcode.DevSync.domain.enums.StatusTask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskDAO {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    boolean update(Task task);

    boolean delete(UUID id);

    List<Task> findByStatus(StatusTask status);

    List<Task> findByAssignedUser(UUID userId);

    List<Task> findByCreatedBy(UUID creatorId);
}
