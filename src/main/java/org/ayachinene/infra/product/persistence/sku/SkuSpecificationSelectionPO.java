package org.ayachinene.infra.product.persistence.sku;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("sku_specification_selection")
public class SkuSpecificationSelectionPO {

    private UUID7 id;
    private UUID7 skuId;
    private UUID7 specificationId;
    private UUID7 specificationValueId;
    private LocalDateTime createdAt;

    public UUID7 getId() {
        return id;
    }

    public SkuSpecificationSelectionPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public UUID7 getSkuId() {
        return skuId;
    }

    public SkuSpecificationSelectionPO setSkuId(UUID7 skuId) {
        this.skuId = skuId;
        return this;
    }

    public UUID7 getSpecificationId() {
        return specificationId;
    }

    public SkuSpecificationSelectionPO setSpecificationId(
            UUID7 specificationId
    ) {
        this.specificationId = specificationId;
        return this;
    }

    public UUID7 getSpecificationValueId() {
        return specificationValueId;
    }

    public SkuSpecificationSelectionPO setSpecificationValueId(
            UUID7 specificationValueId
    ) {
        this.specificationValueId = specificationValueId;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SkuSpecificationSelectionPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
