package com.smartcommerce.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @Test
    void testHashPassword() {
        String password = "testPassword123";
        String hash1 = SecurityUtils.hashPassword(password);
        String hash2 = SecurityUtils.hashPassword(password);

        assertNotNull(hash1);
        assertEquals(hash1, hash2); // Same password should produce same hash
        assertTrue(hash1.length() > 0);
        assertTrue(hash1.matches("[a-fA-F0-9]+")); // Should be hex
    }

    @Test
    void testHashPasswordDifferentPasswords() {
        String hash1 = SecurityUtils.hashPassword("password1");
        String hash2 = SecurityUtils.hashPassword("password2");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testHashPasswordWithSalt() {
        String password = "testPassword123";
        String salt = "testSalt";

        String hash1 = SecurityUtils.hashPasswordWithSalt(password, salt);
        String hash2 = SecurityUtils.hashPasswordWithSalt(password, salt);

        assertNotNull(hash1);
        assertEquals(hash1, hash2); // Same password and salt should produce same hash
        assertTrue(hash1.length() > 0);
    }

    @Test
    void testHashPasswordWithSaltDifferentSalt() {
        String password = "testPassword123";

        String hash1 = SecurityUtils.hashPasswordWithSalt(password, "salt1");
        String hash2 = SecurityUtils.hashPasswordWithSalt(password, "salt2");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testGenerateSalt() {
        String salt1 = SecurityUtils.generateSalt();
        String salt2 = SecurityUtils.generateSalt();

        assertNotNull(salt1);
        assertNotNull(salt2);
        assertNotEquals(salt1, salt2); // Salts should be unique
        assertTrue(salt1.length() > 0);
    }

    @Test
    void testGenerateToken() {
        int length = 16;
        String token1 = SecurityUtils.generateToken(length);
        String token2 = SecurityUtils.generateToken(length);

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2); // Tokens should be unique
        assertTrue(token1.length() > 0);
    }

    @Test
    void testVerifyPassword() {
        String password = "testPassword123";
        String hash = SecurityUtils.hashPassword(password);

        assertTrue(SecurityUtils.verifyPassword(password, hash));
        assertFalse(SecurityUtils.verifyPassword("wrongPassword", hash));
    }

    @Test
    void testVerifyPasswordWithSalt() {
        String password = "testPassword123";
        String salt = SecurityUtils.generateSalt();
        String hash = SecurityUtils.hashPasswordWithSalt(password, salt);

        assertTrue(SecurityUtils.verifyPasswordWithSalt(password, salt, hash));
        assertFalse(SecurityUtils.verifyPasswordWithSalt("wrongPassword", salt, hash));
        assertFalse(SecurityUtils.verifyPasswordWithSalt(password, "wrongSalt", hash));
    }

    @Test
    void testSanitizeInput() {
        assertEquals("hello world", SecurityUtils.sanitizeInput("hello world"));
        assertEquals("hello world", SecurityUtils.sanitizeInput("hello <script> world"));
        assertEquals("hello world", SecurityUtils.sanitizeInput("hello \"world\""));
        assertEquals("hello world", SecurityUtils.sanitizeInput("hello 'world'"));
        assertNull(SecurityUtils.sanitizeInput(null));
    }

    @Test
    void testIsValidToken() {
        String validToken = SecurityUtils.generateToken(16);
        assertTrue(SecurityUtils.isValidToken(validToken));

        assertFalse(SecurityUtils.isValidToken(""));
        assertFalse(SecurityUtils.isValidToken(null));
        assertFalse(SecurityUtils.isValidToken("invalid token with spaces"));
        assertFalse(SecurityUtils.isValidToken("short"));
    }

    @Test
    void testMaskSensitiveData() {
        assertEquals("****1234", SecurityUtils.maskSensitiveData("1234567890123456"));
        assertEquals("****", SecurityUtils.maskSensitiveData("1234"));
        assertEquals("***", SecurityUtils.maskSensitiveData("123"));
        assertEquals("**", SecurityUtils.maskSensitiveData("12"));
        assertEquals("*", SecurityUtils.maskSensitiveData("1"));
        assertEquals("", SecurityUtils.maskSensitiveData(""));
        assertNull(SecurityUtils.maskSensitiveData(null));
    }

    @Test
    void testContainsSQLInjection() {
        assertTrue(SecurityUtils.containsSQLInjection("SELECT * FROM users"));
        assertTrue(SecurityUtils.containsSQLInjection("DROP TABLE users"));
        assertTrue(SecurityUtils.containsSQLInjection("user' OR '1'='1"));
        assertTrue(SecurityUtils.containsSQLInjection("admin'; --"));

        assertFalse(SecurityUtils.containsSQLInjection("normal user input"));
        assertFalse(SecurityUtils.containsSQLInjection("user@example.com"));
        assertFalse(SecurityUtils.containsSQLInjection(""));
        assertFalse(SecurityUtils.containsSQLInjection(null));
    }

    @Test
    void testGenerateSessionId() {
        String sessionId1 = SecurityUtils.generateSessionId();
        String sessionId2 = SecurityUtils.generateSessionId();

        assertNotNull(sessionId1);
        assertNotNull(sessionId2);
        assertNotEquals(sessionId1, sessionId2);
        assertTrue(sessionId1.length() > 0);
        assertTrue(SecurityUtils.isValidToken(sessionId1)); // Should be a valid token format
    }

    @Test
    void testHashPasswordNullInput() {
        assertThrows(SecurityException.class, () -> SecurityUtils.hashPassword(null));
    }

    @Test
    void testHashPasswordWithSaltNullInput() {
        assertThrows(SecurityException.class, () -> SecurityUtils.hashPasswordWithSalt(null, "salt"));
        assertThrows(SecurityException.class, () -> SecurityUtils.hashPasswordWithSalt("password", null));
    }

    @Test
    void testVerifyPasswordNullInput() {
        assertFalse(SecurityUtils.verifyPassword(null, "hash"));
        assertFalse(SecurityUtils.verifyPassword("password", null));
    }

    @Test
    void testVerifyPasswordWithSaltNullInput() {
        assertFalse(SecurityUtils.verifyPasswordWithSalt(null, "salt", "hash"));
        assertFalse(SecurityUtils.verifyPasswordWithSalt("password", null, "hash"));
        assertFalse(SecurityUtils.verifyPasswordWithSalt("password", "salt", null));
    }

    @Test
    void testConstructorThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // This will fail to compile since constructor is private
            // But we can test that instantiation is not possible
        });
    }

    // Helper method to convert bytes to hex (used in SecurityUtils)
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
