package com.smartecommerce.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * PasswordValidator validates password strength and security requirements
 */
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private static final String SPECIAL_CHAR_PATTERN = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";
    private static final String SEQUENTIAL_PATTERN = ".*(?:012|123|234|345|456|567|678|789|abc|bcd|cde).*";

    /**
     * Validate password against security requirements
     * @param password Password to validate
     * @return ValidationResult with status and error message
     */
    public ValidationResult validate(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password is required");
        }

        // Check minimum length
        if (password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters");
        }

        // Check maximum length
        if (password.length() > MAX_LENGTH) {
            errors.add("Password must not exceed " + MAX_LENGTH + " characters");
        }

        // Check for uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }

        // Check for lowercase letter
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }

        // Check for digit
        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one number");
        }

        // Check for special character (recommended but not required)
        if (!password.matches(SPECIAL_CHAR_PATTERN)) {
            // Optional: could add a warning here
        }

        // Check for common weak passwords
        if (isCommonPassword(password)) {
            errors.add("Password is too common. Please choose a stronger password");
        }

        if (errors.isEmpty()) {
            return new ValidationResult(true, "Password is strong");
        } else {
            return new ValidationResult(false, String.join(", ", errors));
        }
    }

    /**
     * Check password strength level
     * @param password Password to check
     * @return Strength level (WEAK, MEDIUM, STRONG, VERY_STRONG)
     */
    public PasswordStrength checkStrength(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return PasswordStrength.WEAK;
        }

        int score = 0;

        // Length score
        if (password.length() >= 12) score++;
        if (password.length() >= 16) score++;

        // Character variety score
        if (password.matches(".*[A-Z].*")) score++;
        if (password.matches(".*[a-z].*")) score++;
        if (password.matches(".*\\d.*")) score++;
        if (password.matches(SPECIAL_CHAR_PATTERN)) score++;

        // Determine strength
        if (score <= 3) return PasswordStrength.WEAK;
        if (score <= 5) return PasswordStrength.MEDIUM;
        if (score <= 6) return PasswordStrength.STRONG;
        return PasswordStrength.VERY_STRONG;
    }

    /**
     * Check if password is in common password list
     */
    private boolean isCommonPassword(String password) {
        String lowerPassword = password.toLowerCase();

        // List of common weak passwords
        String[] commonPasswords = {
            "password", "123456", "12345678", "qwerty", "abc123",
            "monkey", "1234567", "letmein", "trustno1", "dragon",
            "baseball", "iloveyou", "master", "sunshine", "ashley",
            "bailey", "passw0rd", "shadow", "123123", "654321",
            "superman", "qazwsx", "michael", "football", "welcome"
        };

        for (String common : commonPasswords) {
            if (lowerPassword.equals(common)) {
                return true;
            }
        }

        // Check for sequential characters
        return password.matches(SEQUENTIAL_PATTERN);
    }

    /**
     * Generate password strength description
     */
    public String getStrengthDescription(PasswordStrength strength) {
        return switch (strength) {
            case WEAK -> "Weak - Consider adding more characters and variety";
            case MEDIUM -> "Medium - Good, but could be stronger";
            case STRONG -> "Strong - Well done!";
            case VERY_STRONG -> "Very Strong - Excellent security!";
        };
    }

    /**
     * ValidationResult inner class
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * PasswordStrength enum
     */
    public enum PasswordStrength {
        WEAK,
        MEDIUM,
        STRONG,
        VERY_STRONG
    }
}

