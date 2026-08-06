package org.ayachinene.app.domain.product.specification;

import java.util.Objects;

public record SpecificationValue(
        SpecificationValueId specificationValueId,
        String displayName,
        SpecificationStatus status
) {

    public SpecificationValue {
        Objects.requireNonNull(specificationValueId, "specificationValueId must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }
}
