package org.youcode.DevSync.domain.exceptions;

public class TokenLimitReachedException extends RuntimeException {
    public TokenLimitReachedException(String message) {
        super(message);
    }
}
