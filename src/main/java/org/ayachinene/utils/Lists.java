package org.ayachinene.utils;

import io.vavr.control.Option;

import java.util.List;

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
}
