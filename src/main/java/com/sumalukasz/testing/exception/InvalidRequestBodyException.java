package com.sumalukasz.testing.exception;

import java.util.Map;

public class InvalidRequestBodyException extends RuntimeException {

    private final Map<String, Object> invalidFields;

    public InvalidRequestBodyException(Map<String, Object> invalidFields, String message) {
        super(message);
        this.invalidFields = Map.copyOf(invalidFields);
    }

    public Map<String, Object> getInvalidFields() {
        return Map.copyOf(invalidFields);
    }
}
