package org.ayachinene.infra.persistence.product;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("product_gallery_image")
public class ProductGalleryImagePO {

    private UUID7 id;
    private UUID7 productId;
    private UUID7 fileId;
    private Integer sortOrder;
    private LocalDateTime createdAt;

    public UUID7 getId() {
        return id;
    }

    public ProductGalleryImagePO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public UUID7 getProductId() {
        return productId;
    }

    public ProductGalleryImagePO setProductId(UUID7 productId) {
        this.productId = productId;
        return this;
    }

    public UUID7 getFileId() {
        return fileId;
    }

    public ProductGalleryImagePO setFileId(UUID7 fileId) {
        this.fileId = fileId;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public ProductGalleryImagePO setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProductGalleryImagePO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
