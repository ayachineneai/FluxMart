package org.ayachinene.shared.validate;

import org.apache.commons.lang3.StringUtils;

public final class StringValidators {

    private StringValidators() {
    }

    public static Validator<String> strip() {
        return (value, field) -> value == null ? null : value.strip();
    }

    public static Validator<String> notBlank() {
        return (value, field) -> {
            Validators.require(
                StringUtils.isNotBlank(value),
                field + " must not be blank"
            );
            return value;
        };
    }

    public static Validator<String> maxLength(int maximum) {
        return (value, field) -> {
            Validators.require(
                value.length() <= maximum,
                field + " must not exceed " + maximum + " characters"
            );
            return value;
        };
    }

    public static Validator<String> text(int maximum) {
        return strip()
            .c(notBlank())
            .c(maxLength(maximum));
    }
}
