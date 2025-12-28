package com.smartecommerce.utils;

import java.math.BigDecimal;
import java.net.URI;
import java.util.regex.Pattern;

/**
 * Utility class for validation operations.
 * Provides methods for validating various input formats.
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^\\w{3,20}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s'-]{2,50}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$"); // Basic international phone format
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile(
            "^[0-9]{5}(-[0-9]{4})?$");

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            Pattern.CASE_INSENSITIVE);

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }


    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }


    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim().replaceAll("[\\s\\-()]", "")).matches();
    }


    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }


    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }


    public static boolean isPositive(int value) {
        return value > 0;
    }


    public static boolean isPositive(double value) {
        return value > 0;
    }


    public static boolean isNotNegative(int value) {
        return value >= 0;
    }


    public static boolean isNotNegative(double value) {
        return value >= 0;
    }

    /**
     * Validate string length within range
     */
    public static boolean isStringLengthValid(String str, int minLength, int maxLength) {
        if (str == null) {
            return minLength <= 0;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validate URL format
     */
    public static boolean isValidUrl(String url) {
      if (url == null || url.trim().isEmpty() || !URL_PATTERN.matcher(url.trim()).matches()) {
          return false;
      }
        
        try {
            URI.create(url).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validate rating value (1-5)
     */
    public static void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }



    public static boolean isValidZipCode(String zipCode) {
        return zipCode != null && ZIP_CODE_PATTERN.matcher(zipCode.trim()).matches();
    }

    /**
     * Validate URL format
     */

    /**
     * Validate string length
     */
    public static boolean hasValidLength(String value, int minLength, int maxLength) {
        if (value == null) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Validate password strength
     * Must contain: uppercase, lowercase, digit, special char, min 8 chars
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Validate positive number
     */
    public static boolean isPositiveNumber(String value) {
        try {
            double num = Double.parseDouble(value.trim());
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate non-negative number
     */
    public static boolean isNonNegativeNumber(String value) {
        try {
            double num = Double.parseDouble(value.trim());
            return num >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate integer in range
     */
    public static boolean isIntegerInRange(String value, int min, int max) {
        try {
            int num = Integer.parseInt(value.trim());
            return num >= min && num <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate BigDecimal price
     */
    public static boolean isValidPrice(String value) {
        try {
            BigDecimal price = new BigDecimal(value.trim());
            return price.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate price with max value
     */
    public static boolean isValidPrice(String value, BigDecimal maxPrice) {
        try {
            BigDecimal price = new BigDecimal(value.trim());
            return price.compareTo(BigDecimal.ZERO) > 0 &&
                    price.compareTo(maxPrice) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate alphanumeric string
     */
    public static boolean isAlphanumeric(String value) {
        return value != null && value.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Validate alphabetic string
     */
    public static boolean isAlphabetic(String value) {
        return value != null && value.matches("^[a-zA-Z]+$");
    }

    /**
     * Validate numeric string
     */
    public static boolean isNumeric(String value) {
        return value != null && value.matches("^[0-9]+$");
    }

    /**
     * Sanitize string (remove special characters)
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"';]", "").trim();
    }

    /**
     * Sanitize for SQL (prevent SQL injection)
     */
    public static String sanitizeForSQL(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("'", "''").replace("\\", "\\\\").trim();
    }

    /**
     * Validate credit card number (Luhn algorithm)
     */
    public static boolean isValidCreditCard(String cardNumber) {
        if (cardNumber == null || !cardNumber.matches("^[0-9]{13,19}$")) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    /**
     * Validate date format (yyyy-MM-dd)
     */
    public static boolean isValidDateFormat(String date) {
        return date != null && date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }

    /**
     * Get validation error message
     */
    public static String getValidationMessage(String fieldName, String validationType) {
        return switch (validationType.toLowerCase()) {
            case "required" -> fieldName + " is required";
            case "email" -> "Invalid email format";
            case "phone" -> "Invalid phone number format";
            case "url" -> "Invalid URL format";
            case "price" -> fieldName + " must be a positive number";
            case "password" ->
                    "Password must be at least 8 characters with uppercase, lowercase, digit, and special character";
            case "length" -> fieldName + " length is invalid";
            case "numeric" -> fieldName + " must be numeric";
            default -> fieldName + " is invalid";
        };
    }

    /**
     * Validation result holder
     */
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, "");
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }

}

