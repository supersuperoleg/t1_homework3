package org.rakhimov.application.exception;

public class TaskCreationException extends RuntimeException {

    public TaskCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
