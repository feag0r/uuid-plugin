package com.th0rn.uuidplugin;

import java.util.UUID;

public enum UUIDFormat {
    STANDARD("Standard"),
    UPPER("Upper Case"),
    NO_DASHES("No Dashes"),
    CURLY_BRACES("Curly Braces"),
    UNDERSCORE("Underscore");

    private final String displayName;

    UUIDFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String generate() {
        String hyphenated = UUID.randomUUID().toString();
        String raw = hyphenated.replace("-", "");

        return switch (this) {
            case STANDARD -> hyphenated;
            case UPPER -> hyphenated.toUpperCase();
            case NO_DASHES -> raw;
            case CURLY_BRACES -> "{" + hyphenated + "}";
            case UNDERSCORE -> hyphenated.replace("-", "_");
        };
    }
}
