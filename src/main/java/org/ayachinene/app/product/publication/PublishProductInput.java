package org.ayachinene.app.product.publication;

import org.ayachinene.app.product.domain.ProductCode;

public record PublishProductInput(
        ProductCode productCode,
        long expectedVersion
) {
}
