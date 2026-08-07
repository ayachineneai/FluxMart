package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValuesTest {

    @Test
    void mapsOnlyNonNullValues() {
        assertEquals(3, Values.map("abc", String::length));
        assertNull(Values.map(null, String::length));
    }

    @Test
    void suppliesDefaultsAndChecksNulls() {
        assertEquals("value", Values.withDefault("value", "default"));
        assertEquals("default", Values.withDefault(null, "default"));
        assertTrue(Values.notNull("a", "b"));
        assertFalse(Values.notNull("a", null));

        String[] values = null;
        assertFalse(Values.notNull(values));
        assertTrue(Values.anyNull(values));
    }

    @Test
    void checksWhetherAValueIsOutsideASet() {
        var values = Set.of("a", "b");

        assertFalse(Values.notIn(values, "a"));
        assertTrue(Values.notIn(values, "c"));
    }
}
