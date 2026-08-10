package org.ayachinene.infra.stock.persistence;

import org.ayachinene.app.product.domain.sku.SkuCode;

import java.time.LocalDateTime;

public record ReserveStockCommand(
        SkuCode skuCode,
        int quantity,
        LocalDateTime updatedAt
) {
}
