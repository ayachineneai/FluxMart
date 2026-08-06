package org.ayachinene.app.domain.product.specification;

import org.ayachinene.app.uuid7.UUID7;

import java.util.Objects;

public record SpecificationId(UUID7 value) {

    public SpecificationId {
        Objects.requireNonNull(value, "value must not be null");
    }
}
