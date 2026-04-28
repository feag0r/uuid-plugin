package com.th0rn.uuidplugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class UUIDFormatTest {

    @Test
    public void testStandardFormat() {
        String result = UUIDFormat.STANDARD.generate();
        assertNotNull(result);
        assertEquals(36, result.length());
        assertEquals('-', result.charAt(8));
        assertEquals('-', result.charAt(13));
        assertEquals('-', result.charAt(18));
        assertEquals('-', result.charAt(23));
    }

    @Test
    public void testUpperFormat() {
        String result = UUIDFormat.UPPER.generate();
        assertNotNull(result);
        assertEquals(36, result.length());
        assertEquals(result, result.toUpperCase());
    }

    @Test
    public void testNoDashesFormat() {
        String result = UUIDFormat.NO_DASHES.generate();
        assertNotNull(result);
        assertEquals(32, result.length());
        assertFalse(result.contains("-"));
    }

    @Test
    public void testCurlyBracesFormat() {
        String result = UUIDFormat.CURLY_BRACES.generate();
        assertNotNull(result);
        assertEquals(38, result.length());
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    public void testUnderscoreFormat() {
        String result = UUIDFormat.UNDERSCORE.generate();
        assertNotNull(result);
        assertEquals(36, result.length());
        assertFalse(result.contains("-"));
        assertTrue(result.contains("_"));
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Standard", UUIDFormat.STANDARD.getDisplayName());
        assertEquals("Upper Case", UUIDFormat.UPPER.getDisplayName());
        assertEquals("No Dashes", UUIDFormat.NO_DASHES.getDisplayName());
        assertEquals("Curly Braces", UUIDFormat.CURLY_BRACES.getDisplayName());
        assertEquals("Underscore", UUIDFormat.UNDERSCORE.getDisplayName());
    }

    @Test
    public void testAllValues() {
        UUIDFormat[] values = UUIDFormat.values();
        assertEquals(5, values.length);
    }

    @Test
    public void testStandardIsValidUuid() {
        String result = UUIDFormat.STANDARD.generate();
        UUID.fromString(result);
    }

    @Test
    public void testUpperIsValidUuid() {
        String result = UUIDFormat.UPPER.generate();
        UUID.fromString(result.toLowerCase());
    }

    @Test
    public void testNoDashesIsValidUuid() {
        String result = UUIDFormat.NO_DASHES.generate();
        String withDashes = result.substring(0, 8) + "-" + result.substring(8, 12) + "-"
            + result.substring(12, 16) + "-" + result.substring(16, 20) + "-" + result.substring(20);
        UUID.fromString(withDashes);
    }

    @Test
    public void testCurlyBracesIsValidUuid() {
        String result = UUIDFormat.CURLY_BRACES.generate();
        String withoutBraces = result.substring(1, result.length() - 1);
        UUID.fromString(withoutBraces);
    }

    @Test
    public void testUnderscoreIsValidUuid() {
        String result = UUIDFormat.UNDERSCORE.generate();
        String withDashes = result.replace('_', '-');
        UUID.fromString(withDashes);
    }

    @Test
    public void testMultipleGenerationsAreUnique() {
        String first = UUIDFormat.STANDARD.generate();
        String second = UUIDFormat.STANDARD.generate();
        assertNotEquals(first, second);
    }
}
