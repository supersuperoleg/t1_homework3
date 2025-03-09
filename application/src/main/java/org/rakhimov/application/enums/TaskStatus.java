package org.rakhimov.application.enums;

public enum TaskStatus {
    NEW,
    IN_WORK,
    CANCELLED,
    CLOSED,
    UNDEFINED;

    public static TaskStatus fromString(String status) {
        try {
            return TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown status: " + status, e);
        }
    }
}