package com.sumalukasz.testing.exception;

public class InvalidParameterValueException extends RuntimeException {
    private final String value;
    public InvalidParameterValueException(String value, String message) {
        super(message);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
