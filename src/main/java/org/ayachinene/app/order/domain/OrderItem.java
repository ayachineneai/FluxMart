package org.ayachinene.app.order.domain;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record OrderItem(
        ProductCode productCode,
        SkuCode skuCode,
        String productTitle,
        List<SpecificationSnapshotItem> specificationSnapshot,
        UUID7 imageFileId,
        Money unitPrice,
        int quantity,
        Money totalAmount
) {

    public OrderItem {
        specificationSnapshot = List.copyOf(specificationSnapshot);
    }
}
