package com.sumalukasz.testing.exception;

public class InvalidPageNumberException extends RuntimeException {

    private final String pageNumber;

    public InvalidPageNumberException(String pageNumber, String message) {
        super(message);
        this.pageNumber = pageNumber;
    }

    public String getPageNumber() {
        return pageNumber;
    }
}
