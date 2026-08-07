package org.ayachinene.app.code;

import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Validates;

import java.security.SecureRandom;

public final class BusinessCodes {

    private static final char[] ALPHABET =
            "23456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
    private static final int CODE_LENGTH = 20;
    private static final SecureRandom RANDOM = new SecureRandom();

    private BusinessCodes() {
    }

    public static String generateBody() {
        var code = new char[CODE_LENGTH];
        for (var index = 0; index < code.length; index++) {
            code[index] = ALPHABET[RANDOM.nextInt(ALPHABET.length)];
        }
        return new String(code);
    }

    public static String validate(String value, String prefix, String field) {
        var normalized = Validates.requiredText(value, field);
        Validates.require(
                normalized.startsWith(prefix),
                field + " must start with " + prefix
        );
        var body = normalized.substring(prefix.length());
        Validates.require(
                body.length() == CODE_LENGTH,
                field + " body must contain exactly " + CODE_LENGTH + " characters"
        );
        Validates.require(
                body.chars().allMatch(BusinessCodes::isAllowed),
                field + " contains unsupported characters"
        );
        return normalized;
    }

    private static boolean isAllowed(int character) {
        return Streams.of(ALPHABET)
                .anyMatch(allowed -> allowed == character);
    }
}
