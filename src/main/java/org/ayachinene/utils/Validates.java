package org.ayachinene.utils;

import org.ayachinene.app.exception.ValidationException;

public final class Validates {

    private Validates() {
    }

    public static <T> T requireNonNull(T value, String field) {
        if (value == null) {
            throw new ValidationException(field + " must not be null");
        }
        return value;
    }

    public static String requiredText(String value, String field) {
        requireNonNull(value, field);
        var normalized = value.trim();
        require(!normalized.isEmpty(), field + " must not be blank");
        return normalized;
    }

    public static String requiredText(String value, String field, int maxLength) {
        var normalized = requiredText(value, field);
        require(normalized.length() <= maxLength, field + " is too long");
        return normalized;
    }

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
    }
}
