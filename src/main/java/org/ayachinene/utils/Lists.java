package org.ayachinene.utils;

import io.vavr.control.Option;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Lists {

    private Lists() {
    }

    public <T> int size(List<T> ts) {
        return ts == null ? 0 : ts.size();
    }

    public static <T> boolean isEmpty(List<T> ts) {
        return ts == null || ts.isEmpty();
    }

    public static <T> boolean notEmpty(List<T> ts) {
        return ts != null && !ts.isEmpty();
    }

    public static <T> List<T> nullToEmpty(List<T> ts) {
        return ts == null ? new ArrayList<>() : ts;
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
        if (values == null) return null;
        return Streams.of(values)
            .distinct()
            .toList();
    }

    public static <T> boolean isUnique(List<T> values) {
        return isUniqueBy(values, Function.identity());
    }

    public static <T, K> boolean isUniqueBy(
        List<T> values,
        Function<? super T, ? extends K> key
    ) {
        if (values == null) return false;
        var keys = new HashSet<K>(values.size());
        for (var value : values) {
            if (!keys.add(key.apply(value))) return false;
        }
        return true;
    }

    public static <T> List<T> filterNull(List<T> values) {
        if (values == null) return null;
        return Streams.of(values)
            .filter(Objects::nonNull)
            .toList();
    }
}
