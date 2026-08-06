package org.ayachinene.infra.persistence.product.sku;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationValueId;
import org.ayachinene.app.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("sku_specification_selection")
public class SkuSpecificationSelectionPO {

    private UUID7 id;
    private UUID7 skuId;
    private SpecificationId specificationId;
    private SpecificationValueId specificationValueId;
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

    public SpecificationId getSpecificationId() {
        return specificationId;
    }

    public SkuSpecificationSelectionPO setSpecificationId(
            SpecificationId specificationId
    ) {
        this.specificationId = specificationId;
        return this;
    }

    public SpecificationValueId getSpecificationValueId() {
        return specificationValueId;
    }

    public SkuSpecificationSelectionPO setSpecificationValueId(
            SpecificationValueId specificationValueId
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
