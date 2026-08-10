package org.ayachinene.app.order.query;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.domain.SpecificationSnapshotItem;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record PurchasableSku(
        UUID7 skuId,
        ProductCode productCode,
        SkuCode skuCode,
        ProductStatus productStatus,
        SkuStatus skuStatus,
        String productTitle,
        List<SpecificationSnapshotItem> specifications,
        UUID7 imageFileId,
        Money price
) {

    public PurchasableSku {
        specifications = List.copyOf(specifications);
    }
}
