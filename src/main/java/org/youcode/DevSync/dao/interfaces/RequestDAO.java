package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.entities.Request;

import java.util.Optional;

public interface RequestDAO {
    Request save(Request request);
    Optional<Request> findById(Long requestId);
    void delete(Request request);
}
