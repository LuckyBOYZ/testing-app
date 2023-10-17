package com.sumalukasz.testing.utility;

import java.util.Map;
import java.util.Objects;

public final class ValidateValuesUtils {

    private ValidateValuesUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'ValidateAllFieldsNonNullUtils' class");
    }

    public static boolean areAllValuesInMapNull(Map<String, Object> objectToCheck) {
        return objectToCheck.values().stream()
                .allMatch(Objects::isNull);
    }
}
