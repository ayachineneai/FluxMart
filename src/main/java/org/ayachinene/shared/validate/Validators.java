package org.ayachinene.shared.validate;

import java.util.function.Function;
import java.util.function.Predicate;

public final class Validators {

    private Validators() {
    }

    public static <T> T notNull(T value, String field) {
        require(value != null, field + " must not be null");
        return value;
    }

    public static <T> Validator<T> whenPresent(Validator<T> validator) {
        return (value, field) -> value == null
            ? null
            : validator.v(value, field);
    }

    public static <T> Validator<T> require(
        Predicate<? super T> predicate,
        String message
    ) {
        return require(predicate, field -> message);
    }

    public static <T> Validator<T> require(
        Predicate<? super T> predicate,
        Function<String, String> message
    ) {
        return (value, field) -> {
            require(predicate.test(value), message.apply(field));
            return value;
        };
    }

    public static void require(boolean condition, String message) {
        if (!condition) throw new ValidationException(message);
    }
}
