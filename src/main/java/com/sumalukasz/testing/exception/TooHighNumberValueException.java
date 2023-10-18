package com.sumalukasz.testing.exception;

public class TooHighNumberValueException extends RuntimeException {
    private final String value;
    public TooHighNumberValueException(String value, String message) {
        super(message);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
