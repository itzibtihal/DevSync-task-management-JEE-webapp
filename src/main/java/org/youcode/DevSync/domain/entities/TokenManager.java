package org.youcode.DevSync.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.youcode.DevSync.domain.exceptions.TokenLimitExceededException;


  @Entity
  @Table(name = "token_manager")
  @Data


public class TokenManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int dailyTokens = 2;

    @Column(nullable = false)
    private int monthlyDeleteToken = 1;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public void resetDailyTokens() {
        dailyTokens = 2;
    }

    public void doubleTokens() {
        dailyTokens *= 2;
    }

    public void useToken() throws TokenLimitExceededException {
        if (dailyTokens <= 0) {
            throw new TokenLimitExceededException("No tokens available for task modification.");
        }
        dailyTokens--;
    }

    public void useDeleteToken() throws TokenLimitExceededException {
        if (monthlyDeleteToken <= 0) {
            throw new TokenLimitExceededException("No delete token available for this month.");
        }
        monthlyDeleteToken--;
    }
}
