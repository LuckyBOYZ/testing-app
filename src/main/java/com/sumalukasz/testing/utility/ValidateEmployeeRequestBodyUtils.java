package com.sumalukasz.testing.utility;

import com.sumalukasz.testing.constant.EmployeeColumnViolationConstant;
import com.sumalukasz.testing.exception.InvalidRequestBodyException;
import com.sumalukasz.testing.model.request.EmployeeRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class ValidateEmployeeRequestBodyUtils {

    private ValidateEmployeeRequestBodyUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'ConvertStringToIntegerUtils' class");
    }

    public static void validateRequestBody(EmployeeRequest employeeRequest) {
        Map<String, Object> invalidFields = new HashMap<>();
        checkViolationOnVarcharColumnType(EmployeeColumnViolationConstant.NAME, employeeRequest.getName(), invalidFields);
        checkViolationOnVarcharColumnType(EmployeeColumnViolationConstant.SURNAME, employeeRequest.getSurname(), invalidFields);
        checkViolationOnVarcharColumnType(EmployeeColumnViolationConstant.PESEL, employeeRequest.getPesel(), invalidFields);
        checkViolationOnVarcharColumnType(EmployeeColumnViolationConstant.PHONE_NUMBER, employeeRequest.getPhoneNumber(), invalidFields);
        checkViolationOnDateColumnType(EmployeeColumnViolationConstant.DATE_OF_BIRTH, employeeRequest.getDateOfBirth(), invalidFields);
        checkViolationOnIntColumnType(EmployeeColumnViolationConstant.DEPARTMENT_ID, employeeRequest.getDepartmentId(), invalidFields);
        if (!invalidFields.isEmpty()) {
            throw new InvalidRequestBodyException(invalidFields, "Request body has invalid fields");
        }
    }

    public static void validateRequestBodyByGivenMap(Map<String, Object> employeeRequest) {
        Map<String, Object> invalidFields = new HashMap<>();
        for (Map.Entry<String, Object> employeeProperty : employeeRequest.entrySet()) {
            String key = employeeProperty.getKey();
            Object value = employeeProperty.getValue();
            EmployeeColumnViolationConstant violationsConstant;
            try {
                violationsConstant = EmployeeColumnViolationConstant.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException ignore) {
                invalidFields.put(key, "Unknown property name '%s'".formatted(key));
                continue;
            }
            switch (value) {
                case String val -> checkViolationOnVarcharColumnType(violationsConstant, val, invalidFields);
                case LocalDate val -> checkViolationOnDateColumnType(violationsConstant, val, invalidFields);
                case Long val -> checkViolationOnIntColumnType(violationsConstant, val, invalidFields);
                default -> invalidFields.put(key, "Unknown property type '%s'".formatted(key));
            }
        }
        if (!invalidFields.isEmpty()) {
            throw new InvalidRequestBodyException(invalidFields, "Request body has invalid fields");
        }
    }

    private static void checkViolationOnVarcharColumnType(EmployeeColumnViolationConstant violationsConstant, String value,
                                                          Map<String, Object> invalidFields) {
        String fieldName = violationsConstant.getFieldName();
        Integer columnLength = violationsConstant.getLength();
        if (!violationsConstant.canBeNull() && value == null) {
            invalidFields.put(fieldName, "'%s' cannot be null".formatted(fieldName));
        } else if (columnLength != null && (value != null && value.length() > columnLength)) {
            invalidFields.put(fieldName, "'%s' has invalid length. Maximal length for this property is %s"
                    .formatted(fieldName, columnLength));
        }
    }

    private static void checkViolationOnDateColumnType(EmployeeColumnViolationConstant violationsConstant, LocalDate value,
                                                       Map<String, Object> invalidFields) {
        String fieldName = violationsConstant.getFieldName();
        if (!violationsConstant.canBeNull() && value == null) {
            invalidFields.put(fieldName, "'%s' cannot be null".formatted(fieldName));
        }
    }

    private static void checkViolationOnIntColumnType(EmployeeColumnViolationConstant violationsConstant, Long value,
                                                      Map<String, Object> invalidFields) {
        String fieldName = violationsConstant.getFieldName();
        Integer columnLength = violationsConstant.getLength();
        if (!violationsConstant.canBeNull() && value == null) {
            invalidFields.put(fieldName, "'%s' cannot be null".formatted(fieldName));
        } else if (columnLength != null && (value != null && CheckDigitsINumberUtils.getNumberOfDigitsInNumber(value) > columnLength)) {
            invalidFields.put(fieldName, "'%s' has invalid length. Maximal length for this property is %s"
                    .formatted(fieldName, columnLength));
        }
    }
}
