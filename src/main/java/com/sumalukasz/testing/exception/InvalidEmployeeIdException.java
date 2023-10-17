package com.sumalukasz.testing.exception;

public class InvalidEmployeeIdException extends RuntimeException {

    private final String id;

    public InvalidEmployeeIdException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
