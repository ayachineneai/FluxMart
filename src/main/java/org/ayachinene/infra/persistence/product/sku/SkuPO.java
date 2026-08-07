package org.ayachinene.infra.persistence.product.sku;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.domain.product.sku.SkuCode;
import org.ayachinene.app.domain.product.sku.SkuStatus;
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

    public UUID7 getId() {
        return id;
    }

    public SkuPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public UUID7 getProductId() {
        return productId;
    }

    public SkuPO setProductId(UUID7 productId) {
        this.productId = productId;
        return this;
    }

    public SkuCode getSkuCode() {
        return skuCode;
    }

    public SkuPO setSkuCode(SkuCode skuCode) {
        this.skuCode = skuCode;
        return this;
    }

    public String getMerchantSkuCode() {
        return merchantSkuCode;
    }

    public SkuPO setMerchantSkuCode(String merchantSkuCode) {
        this.merchantSkuCode = merchantSkuCode;
        return this;
    }

    public SkuStatus getStatus() {
        return status;
    }

    public SkuPO setStatus(SkuStatus status) {
        this.status = status;
        return this;
    }

    public Long getPriceAmount() {
        return priceAmount;
    }

    public SkuPO setPriceAmount(Long priceAmount) {
        this.priceAmount = priceAmount;
        return this;
    }

    public UUID7 getImageFileId() {
        return imageFileId;
    }

    public SkuPO setImageFileId(UUID7 imageFileId) {
        this.imageFileId = imageFileId;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public SkuPO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SkuPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SkuPO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
