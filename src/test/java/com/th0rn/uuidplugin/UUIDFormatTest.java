package com.th0rn.uuidplugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UUIDFormatTest {

    private static final String DELIMITER_DASH = "-";
    private static final String NO_BRACES = "";

    @Test
    public void testStandardFormat() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, NO_BRACES);
        assertNotNull(result);
        assertEquals(36, result.length());
        assertEquals('-', result.charAt(8));
        assertEquals('-', result.charAt(13));
        assertEquals('-', result.charAt(18));
        assertEquals('-', result.charAt(23));
    }

    @Test
    public void testUpperCaseFormat() {
        String result = UUIDFormat.generate(true, DELIMITER_DASH, NO_BRACES);
        assertNotNull(result);
        assertEquals(36, result.length());
        assertEquals(result, result.toUpperCase());
    }

    @Test
    public void testNoDashesFormat() {
        String result = UUIDFormat.generate(false, "", NO_BRACES);
        assertNotNull(result);
        assertEquals(32, result.length());
        assertFalse(result.contains("-"));
    }

    @Test
    public void testCurlyBracesFormat() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, "{}");
        assertNotNull(result);
        assertEquals(38, result.length());
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    public void testUnderscoreFormat() {
        String result = UUIDFormat.generate(false, "_", NO_BRACES);
        assertNotNull(result);
        assertEquals(36, result.length());
        assertFalse(result.contains("-"));
        assertTrue(result.contains("_"));
    }

    @Test
    public void testUpperCaseWithCurlyBraces() {
        String result = UUIDFormat.generate(true, DELIMITER_DASH, "{}");
        assertNotNull(result);
        assertEquals(result, result.toUpperCase());
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    public void testUpperCaseNoDashes() {
        String result = UUIDFormat.generate(true, "", NO_BRACES);
        assertNotNull(result);
        assertEquals(32, result.length());
        assertEquals(result, result.toUpperCase());
    }

    @Test
    public void testSingleCharBraces() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, "|");
        assertNotNull(result);
        assertTrue(result.startsWith("|"));
        assertTrue(result.endsWith("|"));
    }

    @Test
    public void testMultipleGenerationsAreUnique() {
        String first = UUIDFormat.generate(false, DELIMITER_DASH, NO_BRACES);
        String second = UUIDFormat.generate(false, DELIMITER_DASH, NO_BRACES);
        assertNotEquals(first, second);
    }

    @Test
    public void testConstructorIsPrivate() throws Exception {
        var constructor = UUIDFormat.class.getDeclaredConstructor();
        assertFalse(constructor.canAccess(null));
    }
}
