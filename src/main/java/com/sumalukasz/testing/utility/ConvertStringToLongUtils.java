package com.sumalukasz.testing.utility;

import com.sumalukasz.testing.constant.NumberPropertyNameConstant;
import com.sumalukasz.testing.exception.InvalidParameterValueException;
import com.sumalukasz.testing.exception.TooHighNumberValueException;

public final class ConvertStringToLongUtils {

    private ConvertStringToLongUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'ConvertStringToLongUtils' class");
    }

    public static long convertStringToLong(String value, NumberPropertyNameConstant propertyNameConstant) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignore) {
            throw new InvalidParameterValueException(value, "'%s' parameter must be the number"
                    .formatted(propertyNameConstant.name().toLowerCase()));
        }
    }

}
