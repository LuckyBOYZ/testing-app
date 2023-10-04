package com.sumalukasz.testing.exception;

public class InvalidOffsetNumberException extends RuntimeException {

    private final int offset;

    public InvalidOffsetNumberException(int offset, String message) {
        super(message);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }
}
