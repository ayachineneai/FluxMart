package org.ayachinene.app.product.publication;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;

public record ProductPublication(
        ProductCode productCode,
        ProductStatus status
) {
}
