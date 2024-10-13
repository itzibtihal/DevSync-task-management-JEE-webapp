package org.youcode.DevSync.domain.enums;

import java.time.LocalDateTime;

public enum StatusTask {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    OVERDUE;

    public static StatusTask determineStatus(LocalDateTime dueDate, boolean isCompleted) {
        if (isCompleted) {
            return COMPLETED;
        } else if (LocalDateTime.now().isAfter(dueDate)) {
            return OVERDUE;
        } else {
            return IN_PROGRESS;
        }
    }
}
