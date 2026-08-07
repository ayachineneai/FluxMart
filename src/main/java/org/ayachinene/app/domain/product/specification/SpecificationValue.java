package org.ayachinene.app.domain.product.specification;

import org.ayachinene.shared.uuid7.UUID7;

import java.util.Objects;

public record SpecificationValue(
        UUID7 specificationValueId,
        String displayName,
        SpecificationStatus status
) {

    public SpecificationValue {
        Objects.requireNonNull(specificationValueId, "specificationValueId must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }
}
