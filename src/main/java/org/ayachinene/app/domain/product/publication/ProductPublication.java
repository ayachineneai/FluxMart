package org.ayachinene.app.domain.product.publication;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductStatus;

public record ProductPublication(
        ProductCode productCode,
        ProductStatus status
) {
}
