package org.ayachinene.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64s {

    private Base64s() {
    }

    public static String encode(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8)
        );
    }
}
