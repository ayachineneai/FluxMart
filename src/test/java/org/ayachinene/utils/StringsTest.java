package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringsTest {

    @Test
    void determinesWhetherAStringContainsNoneOfTheCharacters() {
        assertTrue(Strings.notContains("black-shirt.png", '/', '\\'));
        assertFalse(Strings.notContains("product/black-shirt.png", '/', '\\'));
        assertFalse(Strings.notContains("product\\black-shirt.png", '/', '\\'));
    }
}
