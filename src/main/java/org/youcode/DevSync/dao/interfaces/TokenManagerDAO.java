package org.youcode.DevSync.dao.interfaces;

import org.youcode.DevSync.domain.entities.TokenManager;

import java.util.Optional;
import java.util.UUID;

public interface TokenManagerDAO {

    Optional<TokenManager> findByUserId(UUID userId);

    TokenManager save(TokenManager tokenManager);
    void update(TokenManager tokenManager);
}