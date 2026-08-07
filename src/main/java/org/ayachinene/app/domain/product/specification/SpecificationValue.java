package org.ayachinene.app.domain.product.specification;

import org.ayachinene.shared.uuid7.UUID7;

public record SpecificationValue(
        UUID7 specificationValueId,
        String displayName,
        SpecificationStatus status
) {
}
