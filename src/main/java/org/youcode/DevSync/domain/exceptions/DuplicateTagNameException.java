package org.youcode.DevSync.domain.exceptions;

public class DuplicateTagNameException extends RuntimeException {
    public DuplicateTagNameException(String message) {
        super(message);
    }
}