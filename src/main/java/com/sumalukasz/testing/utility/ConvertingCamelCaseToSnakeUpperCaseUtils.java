package com.sumalukasz.testing.utility;

public final class ConvertingCamelCaseToSnakeUpperCaseUtils {

    private ConvertingCamelCaseToSnakeUpperCaseUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'ConvertingCamelCaseToSnakeUpperCaseUtils' class");
    }

    public static String convertCamelCaseStringToSnakeUppercase(String camelCaseString) {
        return camelCaseString.replaceAll("[A-Z]", "_$0").toUpperCase();
    }
}
