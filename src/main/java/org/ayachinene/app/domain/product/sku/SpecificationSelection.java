package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationValueId;

import java.util.Objects;

public record SpecificationSelection(
        SpecificationId specificationId,
        SpecificationValueId specificationValueId
) {

    public SpecificationSelection {
        Objects.requireNonNull(specificationId, "specificationId must not be null");
        Objects.requireNonNull(specificationValueId, "specificationValueId must not be null");
    }
}
