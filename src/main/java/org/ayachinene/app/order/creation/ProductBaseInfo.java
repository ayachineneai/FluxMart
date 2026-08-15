package org.ayachinene.app.order.creation;

import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.uuid7.UUID7;

public record ProductBaseInfo(
    UUID7 skuId,
    String skuCode,
    SkuStatus skuStatus,
    long unitPriceAmount,
    UUID7 stockId,
    long availableQuantity,
    String productCode,
    ProductStatus productStatus,
    String productTitle,
    UUID7 snapshotImageFileId
) {
}
