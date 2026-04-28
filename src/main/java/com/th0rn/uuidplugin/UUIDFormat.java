package com.th0rn.uuidplugin;

import java.util.UUID;

public final class UUIDFormat {

    private UUIDFormat() {
    }

    public static String generate(boolean uppercase, String delimiter, String leftBrace, String rightBrace) {
        String hyphenated = UUID.randomUUID().toString();
        String withDelimiter = delimiter.isEmpty() ? hyphenated.replace("-", "") : hyphenated.replace("-", delimiter);
        String cased = uppercase ? withDelimiter.toUpperCase() : withDelimiter;
        return leftBrace + cased + rightBrace;
    }
}