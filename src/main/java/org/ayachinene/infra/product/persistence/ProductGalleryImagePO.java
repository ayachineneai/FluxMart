package org.ayachinene.infra.product.persistence;

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

    public UUID7 getId() { return id; }
    public ProductGalleryImagePO setId(UUID7 value) { id = value; return this; }
    public UUID7 getProductId() { return productId; }
    public ProductGalleryImagePO setProductId(UUID7 value) { productId = value; return this; }
    public UUID7 getFileId() { return fileId; }
    public ProductGalleryImagePO setFileId(UUID7 value) { fileId = value; return this; }
    public Integer getSortOrder() { return sortOrder; }
    public ProductGalleryImagePO setSortOrder(Integer value) { sortOrder = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public ProductGalleryImagePO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
}
