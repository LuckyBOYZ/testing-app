package com.sumalukasz.testing.exception;

public class InvalidAmountOfAddedRowsToDatabaseException extends RuntimeException {

    private final String rowsNum;

    public InvalidAmountOfAddedRowsToDatabaseException(String rowsNum, String message) {
        super(message);
        this.rowsNum = rowsNum;
    }

    public String getRowsNum() {
        return rowsNum;
    }
}
