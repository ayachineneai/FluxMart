package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamsTest {

    @Test
    void addsZeroBasedIndexesInListOrder() {
        var indexed = Streams.withIndex(List.of("a", "b")).toList();

        assertEquals(0, indexed.get(0).index());
        assertEquals("a", indexed.get(0).value());
        assertEquals(1, indexed.get(1).index());
        assertEquals("b", indexed.get(1).value());
    }

    @Test
    void treatsNullAsAnEmptyStream() {
        assertEquals(0, Streams.withIndex(null).count());
        assertEquals(0, Streams.of((List<Object>) null).count());
        assertEquals(0, Streams.of((char[]) null).count());
    }

    @Test
    void streamsCharactersInArrayOrder() {
        assertEquals(
                List.of('A', 'B', 'C'),
                Streams.of(new char[]{'A', 'B', 'C'}).toList()
        );
    }
}
