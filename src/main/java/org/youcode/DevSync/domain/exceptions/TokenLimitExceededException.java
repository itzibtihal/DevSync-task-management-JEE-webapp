package org.youcode.DevSync.domain.exceptions;

public class TokenLimitExceededException extends Exception {
    public TokenLimitExceededException(String message) {
        super(message);
    }
}
