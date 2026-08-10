package org.ayachinene.app.order.creation;

import org.ayachinene.app.product.domain.sku.SkuCode;

public record CreateOrderItemInput(
        SkuCode skuCode,
        Integer quantity
) {
}
