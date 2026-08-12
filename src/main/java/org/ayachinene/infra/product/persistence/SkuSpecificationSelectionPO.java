package org.ayachinene.infra.product.persistence;

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

    public UUID7 getId() { return id; }
    public SkuSpecificationSelectionPO setId(UUID7 value) { id = value; return this; }
    public UUID7 getSkuId() { return skuId; }
    public SkuSpecificationSelectionPO setSkuId(UUID7 value) { skuId = value; return this; }
    public UUID7 getSpecificationId() { return specificationId; }
    public SkuSpecificationSelectionPO setSpecificationId(UUID7 value) { specificationId = value; return this; }
    public UUID7 getSpecificationValueId() { return specificationValueId; }
    public SkuSpecificationSelectionPO setSpecificationValueId(UUID7 value) { specificationValueId = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public SkuSpecificationSelectionPO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
}
