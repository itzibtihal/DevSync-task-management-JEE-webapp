package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.enums.Role;
import org.youcode.DevSync.domain.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



public interface UserDAO {

    User save(User user);

    Optional<User> findById(UUID id);

    List<User> findAll();

    boolean update(User user);

    boolean delete(UUID id);

    Optional<User> findByName(String name);

    Optional<User> findByUsernameAndPassword(String username);

    List<User> findByRole(Role role);

    List<User> findRecentUsers(int limit);
}
