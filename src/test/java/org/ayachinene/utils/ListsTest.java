package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListsTest {

    @Test
    void returnsHeadAndLastValues() {
        var values = List.of("first", "middle", "last");

        assertEquals("first", Lists.head(values).get());
        assertEquals("last", Lists.last(values).get());
    }

    @Test
    void returnsNoneForNullOrEmptyLists() {
        assertTrue(Lists.head(null).isEmpty());
        assertTrue(Lists.last(null).isEmpty());
        assertTrue(Lists.head(List.of()).isEmpty());
        assertTrue(Lists.last(List.of()).isEmpty());
    }
}
