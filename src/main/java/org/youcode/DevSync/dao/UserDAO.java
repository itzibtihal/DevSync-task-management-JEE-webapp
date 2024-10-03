package org.youcode.DevSync.dao;

import org.youcode.DevSync.modals.Role;
import org.youcode.DevSync.modals.User;

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

    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findByRole(Role role);

    List<User> findRecentUsers(int limit);
}
