package org.ayachinene.utils;

import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatesTest {

    @Test
    void normalizesRequiredText() {
        assertEquals("商品", Validates.requiredText("  商品  ", "title"));
    }

    @Test
    void rejectsMissingAndOversizedText() {
        assertThrows(
                ValidationException.class,
                () -> Validates.requiredText("  ", "title")
        );
        assertThrows(
                ValidationException.class,
                () -> Validates.requiredText("商品标题", "title", 3)
        );
    }

    @Test
    void requiresUniqueValuesAndKeys() {
        assertThrows(
                ValidationException.class,
                () -> Validates.requireUnique(List.of("A", "A"), "must be unique")
        );
        assertThrows(
                ValidationException.class,
                () -> Validates.requireUnique(
                        List.of("A", "a"),
                        String::toLowerCase,
                        "must be unique"
                )
        );
    }
}
