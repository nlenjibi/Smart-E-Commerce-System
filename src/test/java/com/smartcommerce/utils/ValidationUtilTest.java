package com.smartcommerce.utils;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name+tag@example.co.uk"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    void testIsValidUsername() {
        assertTrue(ValidationUtil.isValidUsername("user123"));
        assertTrue(ValidationUtil.isValidUsername("abc"));
        assertFalse(ValidationUtil.isValidUsername("ab"));
        assertFalse(ValidationUtil.isValidUsername("user name"));
        assertFalse(ValidationUtil.isValidUsername(""));
        assertFalse(ValidationUtil.isValidUsername(null));
    }

    @Test
    void testIsValidName() {
        assertTrue(ValidationUtil.isValidName("John Doe"));
        assertTrue(ValidationUtil.isValidName("Mary-Jane O'Connor"));
        assertFalse(ValidationUtil.isValidName("John123"));
        assertFalse(ValidationUtil.isValidName("A"));
        assertFalse(ValidationUtil.isValidName(""));
        assertFalse(ValidationUtil.isValidName(null));
    }

    @Test
    void testIsValidPhone() {
        assertTrue(ValidationUtil.isValidPhone("+1234567890"));
        assertTrue(ValidationUtil.isValidPhone("1234567890"));
        assertFalse(ValidationUtil.isValidPhone("123"));
        assertFalse(ValidationUtil.isValidPhone("invalid"));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone(null));
    }

    @Test
    void testIsNotEmpty() {
        assertTrue(ValidationUtil.isNotEmpty("hello"));
        assertTrue(ValidationUtil.isNotEmpty("  hello  "));
        assertFalse(ValidationUtil.isNotEmpty(""));
        assertFalse(ValidationUtil.isNotEmpty("   "));
        assertFalse(ValidationUtil.isNotEmpty(null));
    }

    @Test
    void testIsInRangeInt() {
        assertTrue(ValidationUtil.isInRange(5, 1, 10));
        assertTrue(ValidationUtil.isInRange(1, 1, 10));
        assertTrue(ValidationUtil.isInRange(10, 1, 10));
        assertFalse(ValidationUtil.isInRange(0, 1, 10));
        assertFalse(ValidationUtil.isInRange(11, 1, 10));
    }

    @Test
    void testIsInRangeDouble() {
        assertTrue(ValidationUtil.isInRange(5.5, 1.0, 10.0));
        assertTrue(ValidationUtil.isInRange(1.0, 1.0, 10.0));
        assertTrue(ValidationUtil.isInRange(10.0, 1.0, 10.0));
        assertFalse(ValidationUtil.isInRange(0.5, 1.0, 10.0));
        assertFalse(ValidationUtil.isInRange(10.5, 1.0, 10.0));
    }

    @Test
    void testIsPositiveInt() {
        assertTrue(ValidationUtil.isPositive(1));
        assertTrue(ValidationUtil.isPositive(100));
        assertFalse(ValidationUtil.isPositive(0));
        assertFalse(ValidationUtil.isPositive(-1));
    }

    @Test
    void testIsPositiveDouble() {
        assertTrue(ValidationUtil.isPositive(1.0));
        assertTrue(ValidationUtil.isPositive(0.1));
        assertFalse(ValidationUtil.isPositive(0.0));
        assertFalse(ValidationUtil.isPositive(-1.0));
    }

    @Test
    void testIsNotNegativeInt() {
        assertTrue(ValidationUtil.isNotNegative(0));
        assertTrue(ValidationUtil.isNotNegative(1));
        assertFalse(ValidationUtil.isNotNegative(-1));
    }

    @Test
    void testIsNotNegativeDouble() {
        assertTrue(ValidationUtil.isNotNegative(0.0));
        assertTrue(ValidationUtil.isNotNegative(1.0));
        assertFalse(ValidationUtil.isNotNegative(-0.1));
    }

    @Test
    void testIsStringLengthValid() {
        assertTrue(ValidationUtil.isStringLengthValid("hello", 1, 10));
        assertTrue(ValidationUtil.isStringLengthValid("hi", 1, 10));
        assertTrue(ValidationUtil.isStringLengthValid(null, 0, 10));
        assertFalse(ValidationUtil.isStringLengthValid("hello", 10, 20));
        assertFalse(ValidationUtil.isStringLengthValid(null, 1, 10));
    }

    @Test
    void testIsValidUrl() {
        assertTrue(ValidationUtil.isValidUrl("http://example.com"));
        assertTrue(ValidationUtil.isValidUrl("https://example.com/path"));
        assertTrue(ValidationUtil.isValidUrl("ftp://ftp.example.com"));
        assertFalse(ValidationUtil.isValidUrl("invalid-url"));
        assertFalse(ValidationUtil.isValidUrl(""));
        assertFalse(ValidationUtil.isValidUrl(null));
    }

    @Test
    void testValidateRating() {
        assertDoesNotThrow(() -> ValidationUtil.validateRating(1));
        assertDoesNotThrow(() -> ValidationUtil.validateRating(3));
        assertDoesNotThrow(() -> ValidationUtil.validateRating(5));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateRating(0));
        assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateRating(6));
    }

    @Test
    void testIsValidZipCode() {
        assertTrue(ValidationUtil.isValidZipCode("12345"));
        assertTrue(ValidationUtil.isValidZipCode("12345-6789"));
        assertFalse(ValidationUtil.isValidZipCode("1234"));
        assertFalse(ValidationUtil.isValidZipCode("123456"));
        assertFalse(ValidationUtil.isValidZipCode("abcde"));
        assertFalse(ValidationUtil.isValidZipCode(null));
    }

    @Test
    void testHasValidLength() {
        assertTrue(ValidationUtil.hasValidLength("hello", 1, 10));
        assertTrue(ValidationUtil.hasValidLength("  hello  ", 1, 10));
        assertFalse(ValidationUtil.hasValidLength("hi", 5, 10));
        assertFalse(ValidationUtil.hasValidLength("this is a very long string", 1, 10));
        assertFalse(ValidationUtil.hasValidLength(null, 1, 10));
    }

    @Test
    void testIsStrongPassword() {
        assertTrue(ValidationUtil.isStrongPassword("StrongPass123!"));
        assertTrue(ValidationUtil.isStrongPassword("Password123!@#"));
        assertFalse(ValidationUtil.isStrongPassword("weak"));
        assertFalse(ValidationUtil.isStrongPassword("password"));
        assertFalse(ValidationUtil.isStrongPassword("PASSWORD123"));
        assertFalse(ValidationUtil.isStrongPassword("Password!"));
        assertFalse(ValidationUtil.isStrongPassword(null));
    }

    @Test
    void testIsPositiveNumber() {
        assertTrue(ValidationUtil.isPositiveNumber("123"));
        assertTrue(ValidationUtil.isPositiveNumber("123.45"));
        assertFalse(ValidationUtil.isPositiveNumber("0"));
        assertFalse(ValidationUtil.isPositiveNumber("-123"));
        assertFalse(ValidationUtil.isPositiveNumber("abc"));
        assertFalse(ValidationUtil.isPositiveNumber(null));
    }

    @Test
    void testIsNonNegativeNumber() {
        assertTrue(ValidationUtil.isNonNegativeNumber("0"));
        assertTrue(ValidationUtil.isNonNegativeNumber("123"));
        assertTrue(ValidationUtil.isNonNegativeNumber("123.45"));
        assertFalse(ValidationUtil.isNonNegativeNumber("-123"));
        assertFalse(ValidationUtil.isNonNegativeNumber("abc"));
        assertFalse(ValidationUtil.isNonNegativeNumber(null));
    }

    @Test
    void testIsIntegerInRange() {
        assertTrue(ValidationUtil.isIntegerInRange("5", 1, 10));
        assertTrue(ValidationUtil.isIntegerInRange("1", 1, 10));
        assertTrue(ValidationUtil.isIntegerInRange("10", 1, 10));
        assertFalse(ValidationUtil.isIntegerInRange("0", 1, 10));
        assertFalse(ValidationUtil.isIntegerInRange("11", 1, 10));
        assertFalse(ValidationUtil.isIntegerInRange("abc", 1, 10));
        assertFalse(ValidationUtil.isIntegerInRange(null, 1, 10));
    }

    @Test
    void testConstructorThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // This will fail to compile since constructor is private
            // But we can test that instantiation is not possible
        });
    }
}
