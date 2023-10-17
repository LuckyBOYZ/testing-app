package com.sumalukasz.testing.exception;

public class TooHighNumberValueException extends RuntimeException implements ExceptionWithValueField {
    private final String value;
    public TooHighNumberValueException(String value, String message) {
        super(message);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
