package org.ayachinene.utils;

import org.ayachinene.app.exception.ValidationException;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;

public final class Validates {

    private Validates() {
    }

    public static <T> T requireNonNull(T value, String field) {
        if (value == null) {
            throw new ValidationException(field + " must not be null");
        }
        return value;
    }

    public static void require(boolean condition, String message) {
        if (!condition) {
            throw new ValidationException(message);
        }
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

    public static <T> void requireUnique(Iterable<T> values, String message) {
        requireUnique(values, Function.identity(), message);
    }

    public static <T, K> void requireUnique(
            Iterable<T> values,
            Function<T, K> key,
            String message
    ) {
        Objects.requireNonNull(values, "values must not be null");
        Objects.requireNonNull(key, "key must not be null");

        var uniqueKeys = new HashSet<K>();
        for (var value : values) {
            require(uniqueKeys.add(key.apply(value)), message);
        }
    }
}
