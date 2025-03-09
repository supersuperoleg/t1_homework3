package org.rakhimov.application.exception;

public class InvalidTaskStatusException extends RuntimeException {

    public InvalidTaskStatusException(String message) {
        super(message);
    }
}
