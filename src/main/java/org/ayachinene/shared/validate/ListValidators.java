package org.ayachinene.shared.validate;

import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Streams;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import static org.ayachinene.shared.validate.Validators.require;

public final class ListValidators {

    private ListValidators() {
    }

    public static <T> Validator<List<T>> nullAsEmpty() {
        return (values, field) -> Lists.nullToEmpty(values);
    }

    public static <T> Validator<List<T>> notEmpty() {
        return (values, field) -> {
            require(Lists.notEmpty(values), field + " must not be empty");
            return values;
        };
    }

    public static <T> Validator<List<T>> each(Validator<T> vs) {
        return (values, field) -> Streams.withIndex(values)
            .map(x -> vs.v(x.value(), field + "[" + x.index() + "]"))
            .toList();
    }

    public static <T> Validator<List<T>> unique() {
        return unique(Function.identity());
    }

    public static <T, K> Validator<List<T>> unique(
        Function<? super T, ? extends K> key
    ) {
        return (values, field) -> {
            var keys = new HashSet<K>();
            Streams.withIndex(values).forEach(x -> {
                if (!keys.add(key.apply(x.value())))
                    throw new ValidationException(field + "[" + x.index() + "] is duplicated");
            });
            return values;
        };
    }

}
