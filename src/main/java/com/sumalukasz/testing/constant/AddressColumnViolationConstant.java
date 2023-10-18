package com.sumalukasz.testing.constant;

public enum AddressColumnViolationConstant {
    EMPLOYEE_ID("employeeId", 11, false),
    STREET("street", 100, false),
    HOUSE_NUMBER("houseNumber", 6, false),
    PREMISES_NUMBER("premisesNumber", 6, true),
    POSTCODE("postcode", 6, false),
    CITY("city", 45, false);

    private final String fieldName;
    private final Integer length;
    private final boolean canBeNull;

    AddressColumnViolationConstant(String fieldName, Integer length, boolean canBeNull) {
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
