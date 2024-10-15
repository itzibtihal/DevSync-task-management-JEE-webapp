package org.youcode.DevSync.domain.exceptions;

public class InvalidRequestIdException extends RuntimeException {
    public InvalidRequestIdException(String message) {
        super(message);
    }
}
