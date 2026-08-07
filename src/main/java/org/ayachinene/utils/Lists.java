package org.ayachinene.utils;

import io.vavr.control.Option;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class Lists {

    private Lists() {
    }

    public static <T> List<T> nullToEmpty(List<T> ts) {
        return ts == null ? List.of() : ts;
    }

    public static <T> Option<T> head(List<? extends T> values) {
        return values == null || values.isEmpty()
                ? Option.none()
                : Option.of(values.getFirst());
    }

    public static <T> Option<T> last(List<? extends T> values) {
        return values == null || values.isEmpty()
                ? Option.none()
                : Option.of(values.getLast());
    }

    public static <T> boolean isUnique(List<T> values) {
        return isUnique(values, Function.identity());
    }

    public static <T, K> boolean isUnique(
            List<T> values,
            Function<T, K> key
    ) {
        if (values == null) return false;

        var uniqueKeys = new HashSet<K>();
        for (var value : values) {
            if (!uniqueKeys.add(key.apply(value))) return false;
        }
        return true;
    }
}
