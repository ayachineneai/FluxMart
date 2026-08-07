package org.ayachinene.utils;

import io.vavr.control.Option;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public final class Values {

    private Values() {
    }

    public static <T> T withDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static <T, R> R map(T value, Function<T, R> mapper) {
        return value == null ? null : mapper.apply(value);
    }

    public static <T> Option<T> filter(T value, Function<T, Boolean> predicate) {
        return Option.of(value).filter(predicate::apply);
    }

    public static <T> boolean notIn(Set<? extends T> values, T value) {
        return values == null || !values.contains(value);
    }

    @SafeVarargs
    public static <T> boolean notNull(T... values) {
        if (values == null) return false;
        return Arrays.stream(values).allMatch(Objects::nonNull);
    }

    @SafeVarargs
    public static <T> boolean anyNull(T ...values) {
        if (values == null) return true;
        return Arrays.stream(values).anyMatch(Objects::isNull);
    }
}
