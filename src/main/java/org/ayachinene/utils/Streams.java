package org.ayachinene.utils;

import org.ayachinene.utils.data.WithIndex;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Streams {

    private Streams() {
    }

    public static <T> Stream<T> of(Iterator<T> values) {
        if (values == null) {
            return Stream.empty();
        }
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(values, Spliterator.ORDERED),
                false
        );
    }

    public static <T> Stream<T> of(Collection<T> values) {
        return values == null ? Stream.empty() : values.stream();
    }

    public static Stream<Character> of(char[] values) {
        if (values == null) return Stream.empty();
        return IntStream.range(0, values.length)
                .mapToObj(index -> values[index]);
    }

    public static <T> Stream<WithIndex<T>> withIndex(java.util.List<T> values) {
        var normalized = Lists.nullToEmpty(values);
        return IntStream.range(0, normalized.size())
                .mapToObj(index -> new WithIndex<>(index, normalized.get(index)));
    }
}
