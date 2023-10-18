package com.sumalukasz.testing.utility;

import com.sumalukasz.testing.constant.AddressColumnViolationConstant;
import com.sumalukasz.testing.exception.InvalidRequestBodyException;
import com.sumalukasz.testing.model.request.AddressRequest;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("DuplicatedCode")
public final class ValidateAddressRequestBodyUtils {

    private ValidateAddressRequestBodyUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'ValidateAddressRequestBodyUtils' class");
    }

    public static void validateRequestBody(AddressRequest addressRequest) {
        Map<String, Object> invalidFields = new HashMap<>();
        checkViolationOnVarcharColumnType(AddressColumnViolationConstant.STREET, addressRequest.getStreet(), invalidFields);
        checkViolationOnVarcharColumnType(AddressColumnViolationConstant.HOUSE_NUMBER, addressRequest.getHouseNumber(), invalidFields);
        checkViolationOnVarcharColumnType(AddressColumnViolationConstant.PREMISES_NUMBER, addressRequest.getPremisesNumber(), invalidFields);
        checkViolationOnVarcharColumnType(AddressColumnViolationConstant.POSTCODE, addressRequest.getPostcode(), invalidFields);
        checkViolationOnVarcharColumnType(AddressColumnViolationConstant.CITY, addressRequest.getCity(), invalidFields);
        if (!invalidFields.isEmpty()) {
            throw new InvalidRequestBodyException(invalidFields, "Request body has invalid fields");
        }
    }

    public static void validateRequestBodyByGivenMap(Map<String, Object> addressRequestBody) {
        Map<String, Object> invalidFields = new HashMap<>();
        for (Map.Entry<String, Object> employeeProperty : addressRequestBody.entrySet()) {
            String key = employeeProperty.getKey();
            Object value = employeeProperty.getValue();
            AddressColumnViolationConstant violationsConstant;
            try {
                violationsConstant = AddressColumnViolationConstant.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException ignore) {
                invalidFields.put(key, "Unknown property name '%s'".formatted(key));
                continue;
            }

            if (value instanceof String val) {
                checkViolationOnVarcharColumnType(violationsConstant, val, invalidFields);
            } else {
                invalidFields.put(key, "Unknown property type '%s'".formatted(key));
            }
        }
        if (!invalidFields.isEmpty()) {
            throw new InvalidRequestBodyException(invalidFields, "Request body has invalid fields");
        }
    }

    private static void checkViolationOnVarcharColumnType(AddressColumnViolationConstant violationsConstant, String value,
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

}
