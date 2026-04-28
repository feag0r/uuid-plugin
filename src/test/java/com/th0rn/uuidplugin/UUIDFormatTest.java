package com.th0rn.uuidplugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UUIDFormatTest {

    private static final String DELIMITER_DASH = "-";
    private static final String NO_BRACE = "";

    @Test
    public void testStandardFormat() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, NO_BRACE, NO_BRACE);
        assertNotNull(result);
        assertEquals(36, result.length());
        assertEquals('-', result.charAt(8));
        assertEquals('-', result.charAt(13));
        assertEquals('-', result.charAt(18));
        assertEquals('-', result.charAt(23));
    }

    @Test
    public void testUpperCaseFormat() {
        String result = UUIDFormat.generate(true, DELIMITER_DASH, NO_BRACE, NO_BRACE);
        assertNotNull(result);
        assertEquals(36, result.length());
        assertEquals(result, result.toUpperCase());
    }

    @Test
    public void testNoDashesFormat() {
        String result = UUIDFormat.generate(false, "", NO_BRACE, NO_BRACE);
        assertNotNull(result);
        assertEquals(32, result.length());
        assertFalse(result.contains("-"));
    }

    @Test
    public void testCurlyBracesFormat() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, "{", "}");
        assertNotNull(result);
        assertEquals(38, result.length());
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    public void testUnderscoreFormat() {
        String result = UUIDFormat.generate(false, "_", NO_BRACE, NO_BRACE);
        assertNotNull(result);
        assertEquals(36, result.length());
        assertFalse(result.contains("-"));
        assertTrue(result.contains("_"));
    }

    @Test
    public void testUpperCaseWithCurlyBraces() {
        String result = UUIDFormat.generate(true, DELIMITER_DASH, "{", "}");
        assertNotNull(result);
        assertEquals(result, result.toUpperCase());
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
    }

    @Test
    public void testUpperCaseNoDashes() {
        String result = UUIDFormat.generate(true, "", NO_BRACE, NO_BRACE);
        assertNotNull(result);
        assertEquals(32, result.length());
        assertEquals(result, result.toUpperCase());
    }

    @Test
    public void testDifferentBraces() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, "[", "]");
        assertNotNull(result);
        assertTrue(result.startsWith("["));
        assertTrue(result.endsWith("]"));
    }

    @Test
    public void testLeftBraceOnly() {
        String result = UUIDFormat.generate(false, DELIMITER_DASH, "{", NO_BRACE);
        assertNotNull(result);
        assertTrue(result.startsWith("{"));
        assertFalse(result.endsWith("}"));
        assertEquals(37, result.length());
    }

    @Test
    public void testMultipleGenerationsAreUnique() {
        String first = UUIDFormat.generate(false, DELIMITER_DASH, NO_BRACE, NO_BRACE);
        String second = UUIDFormat.generate(false, DELIMITER_DASH, NO_BRACE, NO_BRACE);
        assertNotEquals(first, second);
    }

    @Test
    public void testConstructorIsPrivate() throws Exception {
        var constructor = UUIDFormat.class.getDeclaredConstructor();
        assertFalse(constructor.canAccess(null));
    }
}