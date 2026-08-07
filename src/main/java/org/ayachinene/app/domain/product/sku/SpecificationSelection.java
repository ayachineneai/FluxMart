package org.ayachinene.app.domain.product.sku;

import org.ayachinene.shared.uuid7.UUID7;

public record SpecificationSelection(
        UUID7 specificationId,
        UUID7 specificationValueId
) {
}
