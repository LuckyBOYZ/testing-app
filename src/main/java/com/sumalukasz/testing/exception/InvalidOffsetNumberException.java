package com.sumalukasz.testing.exception;

public class InvalidOffsetNumberException extends RuntimeException {

    private final String offset;

    public InvalidOffsetNumberException(String offset, String message) {
        super(message);
        this.offset = offset;
    }

    public String getOffset() {
        return offset;
    }
}
