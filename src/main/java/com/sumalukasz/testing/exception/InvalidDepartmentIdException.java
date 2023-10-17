package com.sumalukasz.testing.exception;

public class InvalidDepartmentIdException extends RuntimeException {

    private final String departmentId;
    public InvalidDepartmentIdException(String departmentId, String errorMessage) {
        super(errorMessage);
        this.departmentId = departmentId;
    }

    public String getDepartmentId() {
        return departmentId;
    }
}
