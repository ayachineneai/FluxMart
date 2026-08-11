package org.ayachinene.infra.stock.persistence;

import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7;

public class SkuCodeWithId {

    private UUID7 skuId;
    private SkuCode skuCode;

    public UUID7 getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID7 skuId) {
        this.skuId = skuId;
    }

    public SkuCode getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(SkuCode skuCode) {
        this.skuCode = skuCode;
    }
}
