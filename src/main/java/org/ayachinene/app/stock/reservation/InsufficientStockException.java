package org.ayachinene.app.stock.reservation;

import org.ayachinene.app.product.domain.sku.SkuCode;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(SkuCode skuCode) {
        super("Insufficient stock for SKU: " + skuCode.value());
    }
}
