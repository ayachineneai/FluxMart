package org.ayachinene.app.domain.product.sku;

import org.ayachinene.shared.uuid7.UUID7;

import java.util.Objects;

public record SpecificationSelection(
        UUID7 specificationId,
        UUID7 specificationValueId
) {

    public SpecificationSelection {
        Objects.requireNonNull(specificationId, "specificationId must not be null");
        Objects.requireNonNull(specificationValueId, "specificationValueId must not be null");
    }
}
