package com.sumalukasz.testing.exception;

public class NoValidProfileException extends RuntimeException {

    private final String activeProfile;

    public NoValidProfileException(String activeProfile, String message) {
        super(message);
        this.activeProfile = activeProfile;
    }

    public String getActiveProfile() {
        return activeProfile;
    }
}
