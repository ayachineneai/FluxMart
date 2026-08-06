package org.ayachinene.app.domain.product.specification;

import org.ayachinene.app.uuid7.UUID7;

import java.util.Objects;

public record SpecificationValueId(UUID7 value) {

    public SpecificationValueId {
        Objects.requireNonNull(value, "value must not be null");
    }
}
