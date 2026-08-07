package org.ayachinene.app.domain.product.publication;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductStatus;

public record PublishProductResult(
        ProductCode productCode,
        ProductStatus status,
        long version
) {
}
