package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.entities.Tag;
import org.youcode.DevSync.domain.entities.User;
import org.youcode.DevSync.domain.enums.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagDAO {

    Tag save(Tag tag);

    Optional<Tag> findById(UUID id);

    List<Tag> findAll();

    boolean update(Tag tag);

    boolean delete(UUID id);



}
