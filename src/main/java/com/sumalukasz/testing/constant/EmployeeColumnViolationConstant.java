package com.sumalukasz.testing.constant;

public enum EmployeeColumnViolationConstant {
    NAME("name", 16, false),
    SURNAME("surname", 45, false),
    PESEL("pesel", 11, false),
    PHONE_NUMBER("phoneNumber", 15, true),
    DATE_OF_BIRTH("dateOfBirth", null, true),
    DEPARTMENT_ID("departmentId", 11, true);

    private final String fieldName;
    private final Integer length;
    private final boolean canBeNull;

    EmployeeColumnViolationConstant(String fieldName, Integer length, boolean canBeNull) {
        this.fieldName = fieldName;
        this.length = length;
        this.canBeNull = canBeNull;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Integer getLength() {
        return length;
    }

    public boolean canBeNull() {
        return canBeNull;
    }
}
