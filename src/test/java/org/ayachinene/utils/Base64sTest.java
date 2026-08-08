package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64sTest {

    @Test
    void encodesUtf8String() {
        assertEquals("Rmx1eE1hcnTllYblk4E=", Base64s.encode("FluxMart商品"));
    }
}
