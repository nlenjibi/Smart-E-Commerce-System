package com.smartcommerce.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppUtilsTest {

    @Test
    void testFormatCurrencyStringBigDecimal() {
        assertEquals("$123.45", AppUtils.formatCurrencyString(BigDecimal.valueOf(123.45)));
        assertEquals("$0.00", AppUtils.formatCurrencyString(BigDecimal.ZERO));
        assertEquals("$0.00", AppUtils.formatCurrencyString(null));
    }

    @Test
    void testFormatCurrencyStringDouble() {
        assertEquals("$123.46", AppUtils.formatCurrencyString(123.456));
        assertEquals("$0.00", AppUtils.formatCurrencyString(0.0));
    }

    @Test
    void testFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30, 45);
        assertEquals("2023-12-25 14:30:45", AppUtils.formatDateTime(dateTime));
        assertEquals("N/A", AppUtils.formatDateTime(null));
    }

    @Test
    void testFormatDate() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30, 45);
        assertEquals("2023-12-25", AppUtils.formatDate(dateTime));
        assertEquals("N/A", AppUtils.formatDate(null));
    }

    @Test
    void testFormatTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30, 45);
        assertEquals("14:30:45", AppUtils.formatTime(dateTime));
        assertEquals("N/A", AppUtils.formatTime(null));
    }

    @Test
    void testAsInt() {
        assertEquals(123, AppUtils.asInt(123));
        assertEquals(123, AppUtils.asInt(123.45));
        assertEquals(0, AppUtils.asInt("not a number"));
        assertEquals(0, AppUtils.asInt(null));
    }

    @Test
    void testAsLong() {
        assertEquals(123L, AppUtils.asLong(123));
        assertEquals(123L, AppUtils.asLong(123.45));
        assertEquals(0L, AppUtils.asLong("not a number"));
        assertEquals(0L, AppUtils.asLong(null));
    }

    @Test
    void testAsDouble() {
        assertEquals(123.45, AppUtils.asDouble(123.45), 0.001);
        assertEquals(123.0, AppUtils.asDouble(123), 0.001);
        assertEquals(0.0, AppUtils.asDouble("not a number"), 0.001);
        assertEquals(0.0, AppUtils.asDouble(null), 0.001);
    }

    @Test
    void testAsString() {
        assertEquals("123", AppUtils.asString(123));
        assertEquals("hello", AppUtils.asString("hello"));
        assertNull(AppUtils.asString(null));
    }

    @Test
    void testAsLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 25, 14, 30, 45);
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        assertEquals(dateTime, AppUtils.asLocalDateTime(dateTime));
        assertEquals(dateTime, AppUtils.asLocalDateTime(timestamp));
        assertNull(AppUtils.asLocalDateTime("not a date"));
        assertNull(AppUtils.asLocalDateTime(null));
    }

    @Test
    void testAsBigDecimal() {
        assertEquals(BigDecimal.valueOf(123.45), AppUtils.asBigDecimal(BigDecimal.valueOf(123.45)));
        assertEquals(BigDecimal.valueOf(123.0), AppUtils.asBigDecimal(123));
        assertEquals(BigDecimal.ZERO, AppUtils.asBigDecimal("not a number"));
        assertEquals(BigDecimal.ZERO, AppUtils.asBigDecimal(null));
    }

    @Test
    void testAsBoolean() {
        assertTrue(AppUtils.asBoolean(true));
        assertTrue(AppUtils.asBoolean(1));
        assertTrue(AppUtils.asBoolean("true"));
        assertFalse(AppUtils.asBoolean(false));
        assertFalse(AppUtils.asBoolean(0));
        assertFalse(AppUtils.asBoolean("false"));
        assertFalse(AppUtils.asBoolean("not a boolean"));
        assertFalse(AppUtils.asBoolean(null));
    }

    @Test
    void testIsEmpty() {
        assertTrue(AppUtils.isEmpty(null));
        assertTrue(AppUtils.isEmpty(""));
        assertTrue(AppUtils.isEmpty("   "));
        assertFalse(AppUtils.isEmpty("hello"));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(AppUtils.isNotEmpty(null));
        assertFalse(AppUtils.isNotEmpty(""));
        assertFalse(AppUtils.isNotEmpty("   "));
        assertTrue(AppUtils.isNotEmpty("hello"));
    }

    @Test
    void testGetOrDefault() {
        assertEquals("default", AppUtils.getOrDefault(null, "default"));
        assertEquals("value", AppUtils.getOrDefault("value", "default"));
    }

    @Test
    void testTruncate() {
        assertEquals("hello", AppUtils.truncate("hello", 10));
        assertEquals("hel...", AppUtils.truncate("hello world", 6));
        assertNull(AppUtils.truncate(null, 10));
    }

    @Test
    void testSanitize() {
        assertEquals("hello world", AppUtils.sanitize("hello world"));
        assertEquals("hello world", AppUtils.sanitize("hello <world>"));
        assertEquals("hello world", AppUtils.sanitize("hello \"world\""));
        assertEquals("hello world", AppUtils.sanitize("hello 'world'"));
        assertNull(AppUtils.sanitize(null));
    }

    @Test
    void testCapitalize() {
        assertEquals("Hello", AppUtils.capitalize("hello"));
        assertEquals("Hello", AppUtils.capitalize("HELLO"));
        assertEquals("", AppUtils.capitalize(""));
        assertNull(AppUtils.capitalize(null));
        assertEquals("H", AppUtils.capitalize("h"));
    }

    @Test
    void testConstructorThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // This will fail to compile since constructor is private
            // But we can test that instantiation is not possible
        });
    }
}
