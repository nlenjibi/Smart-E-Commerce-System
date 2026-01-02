package com.smartecommerce.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * AppUtils provides utility methods for common application operations.
 * This includes formatting, type conversion, and string operations.
 */
public class AppUtils {
    private static final Logger logger = LoggerFactory.getLogger(AppUtils.class);

    private AppUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Print object to console
     */
    public static void println(Object text) {
        logger.info("{}", text);
    }

    /**
     * Print formatted string to console
     */
    public static void printf(String format, Object... args) {
        logger.info(String.format(format, args));
    }

    /**
     * Print error message to error stream
     */
    public static void printE(String message) {
        logger.error("{}", message);
    }


    // Formatting utilities
    /**
     * Format currency as string
     */
    public static String formatCurrencyString(BigDecimal amount) {
        return amount != null ? String.format("$%.2f", amount) : "$0.00";
    }

    /**
     * Format currency as string from double
     */
    public static String formatCurrencyString(double amount) {
        return String.format("$%.2f", amount);
    }

    /**
     * Format and print currency value
     */
    public static void formatCurrency(String label, double amount) {
        printf("%s: $%.2f%n", label, amount);
    }

    /**
     * Format and print currency from BigDecimal
     */
    public static void formatCurrency(String label, BigDecimal amount) {
        printf("%s: $%.2f%n", label, amount);
    }

    /**
     * Print key-value pair with alignment
     */
    public static void printKeyValue(String key, String value) {
        printf("%-20s: %s%n", key, value);
    }

    /**
     * Format date and time
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "N/A";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Format date only
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null)
            return "N/A";
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * Format time only
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "N/A";
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    // Type conversion utilities
    /**
     * Convert object to int, returns 0 if null or not a number
     */
    public static int asInt(Object value) {
        return value instanceof Number number ? number.intValue() : 0;
    }

    /**
     * Convert object to long, returns 0 if null or not a number
     */
    public static long asLong(Object value) {
        return value instanceof Number number ? number.longValue() : 0L;
    }

    /**
     * Convert object to double, returns 0.0 if null or not a number
     */
    public static double asDouble(Object value) {
        return value instanceof Number number ? number.doubleValue() : 0.0;
    }

    /**
     * Convert object to String, returns null if value is null
     */
    public static String asString(Object value) {
        return value == null ? null : value.toString();
    }

    /**
     * Convert object to LocalDateTime
     */
    public static LocalDateTime asLocalDateTime(Object value) {
        if (value instanceof Timestamp timestamp) {
            return timestamp.toLocalDateTime();
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime;
        }
        return null;
    }

    /**
     * Convert object to BigDecimal
     */
    public static BigDecimal asBigDecimal(Object value) {
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }

    /**
     * Convert object to boolean, returns false if null
     */
    public static boolean asBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof Number number) {
            return number.intValue() != 0;
        }
        if (value instanceof String string) {
            return Boolean.parseBoolean(string);
        }
        return false;
    }



    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Get value or default if null
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * Truncate string to max length
     */
    public static String truncate(String str, int maxLength) {
        if (str == null)
            return null;
        return str.length() <= maxLength ? str : str.substring(0, maxLength) + "...";
    }

    /**
     * Sanitize string input (remove special characters)
     */
    public static String sanitize(String input) {
        if (input == null)
            return null;
        return input.replaceAll("[<>\"']", "");
    }

    /**
     * Capitalize first letter of string
     */
    public static String capitalize(String str) {
        if (isEmpty(str))
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
