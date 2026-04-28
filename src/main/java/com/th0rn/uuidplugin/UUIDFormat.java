package com.th0rn.uuidplugin;

import java.util.UUID;

public final class UUIDFormat {

    private UUIDFormat() {
    }

    public static String generate(boolean uppercase, String delimiter, String braces) {
        String hyphenated = UUID.randomUUID().toString();
        String withDelimiter = delimiter.isEmpty() ? hyphenated.replace("-", "") : hyphenated.replace("-", delimiter);
        String cased = uppercase ? withDelimiter.toUpperCase() : withDelimiter;
        if (braces.isEmpty()) {
            return cased;
        }
        char left = braces.charAt(0);
        char right = braces.length() >= 2 ? braces.charAt(1) : left;
        return left + cased + right;
    }
}
