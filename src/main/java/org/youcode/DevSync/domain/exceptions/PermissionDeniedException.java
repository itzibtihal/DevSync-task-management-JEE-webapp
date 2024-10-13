package org.youcode.DevSync.domain.exceptions;

public class PermissionDeniedException extends Exception {
    public PermissionDeniedException(String message) {
        super(message);
    }
}