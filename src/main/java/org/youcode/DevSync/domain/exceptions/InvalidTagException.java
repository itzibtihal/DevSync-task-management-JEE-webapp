package org.youcode.DevSync.domain.exceptions;

public class InvalidTagException extends RuntimeException {
    public InvalidTagException(String message) {
        super(message);
    }
}