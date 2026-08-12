package org.ayachinene.shared.uuid7;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.utils.Validates;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class UUID7 implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID value;

    private UUID7(UUID value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        if (value.version() != 7 || value.variant() != 2) {
            throw new IllegalArgumentException("value must be a UUID version 7");
        }
    }

    static UUID7 of(UUID value) {
        return new UUID7(value);
    }

    public static UUID7 fromString(String value, String field) {
        var normalized = Validates.text(value, field);
        var parsed = UUID7s.fromString(normalized);
        if (parsed.isLeft()) {
            throw new ValidationException(field + " must be a UUIDv7");
        }
        return parsed.get();
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof UUID7 other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
