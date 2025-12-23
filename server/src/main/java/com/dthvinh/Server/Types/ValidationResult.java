package com.dthvinh.Server.Types;

public class ValidationResult {
    public boolean success;
    public String errorMessage;

    private ValidationResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult error(String errorMessage) {
        return new ValidationResult(false, errorMessage);
    }
}
