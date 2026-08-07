package org.ayachinene.app.product.publication;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;

public record ProductPublicationState(
        ProductCode productCode,
        ProductStatus status,
        long version
) {
}
