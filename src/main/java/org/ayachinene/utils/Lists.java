package org.ayachinene.utils;

import io.vavr.control.Option;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class Lists {

    private Lists() {
    }

    public static <T> boolean notEmpty(List<T> ts) {
        return ts != null && !ts.isEmpty();
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

    public static <T> Option<T> find(List<T> values, Predicate<T> predicate) {
        return Option.ofOptional(
            Streams.of(values)
                .filter(predicate)
                .findFirst()
        );
    }

    public static <T> List<T> distinct(List<T> values) {
        return Streams.of(values)
            .distinct()
            .toList();
    }

    public static <T> List<T> filterNull(List<T> values) {
        return Streams.of(values)
            .filter(Objects::nonNull)
            .toList();
    }

    public static <T> List<T> uniqueNonNull(List<T> values) {
        return Streams.of(values)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    }

}
