package com.sumalukasz.testing.exception;

public class InvalidUuidException extends RuntimeException {

    private final String value;

    public InvalidUuidException(String value, String message) {
        super(message);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
