package org.ayachinene.utils;

import org.ayachinene.shared.exception.ValidationException;

public final class Validates {

    private Validates() {
    }

    public static <T> T notNull(T value, String field) {
        if (value == null) throw new ValidationException(field + " must not be null");
        return value;
    }

    public static void text(String value, String field) {
        require(value != null && !value.isBlank(), field + " must not be blank");
    }

    public static void text(String value, String field, int maxLength) {
        text(value, field);
        require(value.length() <= maxLength, field + " is too long");
    }

    public static void optionalText(
        String value,
        String field
    ) {
        if (value != null) text(value, field);
    }

    public static void optionalText(
        String value,
        String field,
        int maxLength
    ) {
        if (value != null) text(value, field, maxLength);
    }

    public static void require(boolean condition, String message) {
        if (!condition) throw new ValidationException(message);
    }
}
