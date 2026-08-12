package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("sku")
public class SkuPO {

    private UUID7 id;
    private UUID7 productId;
    private SkuCode skuCode;
    private String merchantSkuCode;
    private SkuStatus status;
    private Long priceAmount;
    private UUID7 imageFileId;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() { return id; }
    public SkuPO setId(UUID7 value) { id = value; return this; }
    public UUID7 getProductId() { return productId; }
    public SkuPO setProductId(UUID7 value) { productId = value; return this; }
    public SkuCode getSkuCode() { return skuCode; }
    public SkuPO setSkuCode(SkuCode value) { skuCode = value; return this; }
    public String getMerchantSkuCode() { return merchantSkuCode; }
    public SkuPO setMerchantSkuCode(String value) { merchantSkuCode = value; return this; }
    public SkuStatus getStatus() { return status; }
    public SkuPO setStatus(SkuStatus value) { status = value; return this; }
    public Long getPriceAmount() { return priceAmount; }
    public SkuPO setPriceAmount(Long value) { priceAmount = value; return this; }
    public UUID7 getImageFileId() { return imageFileId; }
    public SkuPO setImageFileId(UUID7 value) { imageFileId = value; return this; }
    public Long getVersion() { return version; }
    public SkuPO setVersion(Long value) { version = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public SkuPO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public SkuPO setUpdatedAt(LocalDateTime value) { updatedAt = value; return this; }
}
