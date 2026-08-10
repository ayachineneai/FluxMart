package org.ayachinene.infra.stock.persistence;

import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7;

public class StockReservationTarget {

    private UUID7 orderId;
    private UUID7 orderItemId;
    private UUID7 skuId;
    private SkuCode skuCode;

    public UUID7 getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID7 orderId) {
        this.orderId = orderId;
    }

    public UUID7 getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(UUID7 orderItemId) {
        this.orderItemId = orderItemId;
    }

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
