package org.ayachinene.app.domain.product.publication;

import org.ayachinene.app.domain.product.ProductCode;

public record PublishProductInput(
        ProductCode productCode,
        long expectedVersion
) {
}
