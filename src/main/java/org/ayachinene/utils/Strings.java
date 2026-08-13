package org.ayachinene.utils;

public final class Strings {

    private Strings() {
    }

    public static boolean notContains(String s, char... characters) {
        return Streams.of(characters)
                .noneMatch(character -> s.indexOf(character) >= 0);
    }

    public static String strip(String s) {
        return s == null ? null : s.strip();
    }

}
