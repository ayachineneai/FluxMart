package org.ayachinene.app.domain.product;

import org.ayachinene.app.uuid7.UUID7;

import java.util.Objects;

public record ProductCode(UUID7 value) {
    public ProductCode {
        Objects.requireNonNull(value, "value must not be null");
    }
}
