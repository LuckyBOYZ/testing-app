package com.sumalukasz.testing.utility;

public final class CheckDigitsINumberUtils {

    private CheckDigitsINumberUtils() {
        throw new UnsupportedOperationException("Cannot create an instance of 'CheckDigitsINumberUtils' class");
    }

    public static int getNumberOfDigitsInNumber(long number) {
        return String.valueOf(number).length();
    }
}
