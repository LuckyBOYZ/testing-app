package com.sumalukasz.testing.utility;

import com.sumalukasz.testing.constant.NumberPropertyNameConstant;
import com.sumalukasz.testing.exception.InvalidParameterValueException;
import com.sumalukasz.testing.exception.TooHighNumberValueException;

public final class ConvertStringToIntegerUtils {

    private ConvertStringToIntegerUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'ConvertStringToIntegerUtils' class");
    }

    public static int convertStringToInt(String value, NumberPropertyNameConstant propertyNameConstant) {
        try {
            long longVal = Long.parseLong(value);
            if (longVal > Integer.MAX_VALUE) {
                throw new TooHighNumberValueException(value, "'%s' parameter has too high value".formatted(
                        propertyNameConstant.name().toLowerCase()));
            }
            return (int) longVal;
        } catch (NumberFormatException ignore) {
            throw new InvalidParameterValueException(value, "'%s' parameter must be the number"
                    .formatted(propertyNameConstant.name().toLowerCase()));
        }
    }

}
