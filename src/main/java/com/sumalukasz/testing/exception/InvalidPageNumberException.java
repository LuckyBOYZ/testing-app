package com.sumalukasz.testing.exception;

public class InvalidPageNumberException extends RuntimeException {

    private final int pageNumber;

    public InvalidPageNumberException(int pageNumber, String message) {
        super(message);
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
