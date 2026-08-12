package org.ayachinene.app.code;

import java.security.SecureRandom;

public final class BusinessCodes {

    private static final char[] ALPHABET =
        "23456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
    private static final int CODE_LENGTH = 20;
    private static final SecureRandom RANDOM = new SecureRandom();

    private BusinessCodes() {
    }

    public static String generate() {
        var code = new char[CODE_LENGTH];
        for (var index = 0; index < code.length; index++) {
            code[index] = ALPHABET[RANDOM.nextInt(ALPHABET.length)];
        }
        return new String(code);
    }

    public static void validate(String code) {
        if (code == null) {
            throw new IllegalArgumentException("code must not be null");
        }
        if (code.length() != CODE_LENGTH) {
            throw new IllegalArgumentException(
                "code must contain exactly " + CODE_LENGTH + " characters"
            );
        }
        for (var character : code.toCharArray()) {
            if (!isAllowed(character)) {
                throw new IllegalArgumentException("code contains unsupported characters");
            }
        }
    }

    private static boolean isAllowed(char character) {
        for (var allowed : ALPHABET) {
            if (allowed == character) return true;
        }
        return false;
    }
}
