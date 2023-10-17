package com.sumalukasz.testing.exception;

public class InvalidParameterValueException extends RuntimeException implements ExceptionWithValueField {
    private final String value;
    public InvalidParameterValueException(String value, String message) {
        super(message);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
