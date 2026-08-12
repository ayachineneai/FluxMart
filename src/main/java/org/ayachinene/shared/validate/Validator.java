package org.ayachinene.shared.validate;

@FunctionalInterface
public interface Validator<T> {

    T v(T value, String field);

    default Validator<T> c(Validator<T> next) {
        return (value, field) -> next.v(v(value, field), field);
    }
}
