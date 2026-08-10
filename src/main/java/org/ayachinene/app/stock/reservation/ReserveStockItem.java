package org.ayachinene.app.stock.reservation;

import org.ayachinene.app.product.domain.sku.SkuCode;

public record ReserveStockItem(
        SkuCode skuCode,
        int quantity
) {
}
