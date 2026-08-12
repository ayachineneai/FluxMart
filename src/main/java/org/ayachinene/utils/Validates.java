package org.ayachinene.utils;

import org.apache.commons.lang3.StringUtils;
import org.ayachinene.shared.validate.ValidationException;

import java.util.List;
import java.util.function.Predicate;

public final class Validates {

    private Validates() {
    }

    public static <T> T notNull(T value, String field) {
        if (value == null) throw new ValidationException(field + " must not be null");
        return value;
    }

    public static String text(String value, String field) {
        require(StringUtils.isNotBlank(value), field + " must not be blank");
        return value.strip();
    }

    public static String text(String value, String field, int maxLength) {
        var normalized = text(value, field);
        require(normalized.length() <= maxLength, field + " is too long");
        return normalized;
    }

    public static String optionalText(
        String value,
        String field
    ) {
        return value == null ? null : text(value, field);
    }

    public static String optionalText(
        String value,
        String field,
        int maxLength
    ) {
        return value == null ? null : text(value, field, maxLength);
    }

    public static <T> List<T> optionalList(
        List<T> values,
        Predicate<T> predicate,
        String message
    ) {
        var normalized = Lists.nullToEmpty(values);
        require(Streams.of(values).allMatch(predicate), message);
        return normalized;
    }

    public static <T> List<T> list(List<T> values, String field) {
        require(Lists.notEmpty(values), field + " must not be empty");
        return values;
    }

    public static <T> List<T> list(
        List<T> values,
        Predicate<T> predicate,
        String field
    ) {
        var required = list(values, field);
        require(
            Streams.of(required).allMatch(predicate),
            field + " contains invalid element"
        );
        return required;
    }

    public static <T> List<T> unique(List<T> values, String message) {
        require(Lists.distinct(values).size() == values.size(), message);
        return values;
    }

    public static void require(boolean condition, String message) {
        if (!condition) throw new ValidationException(message);
    }
}
