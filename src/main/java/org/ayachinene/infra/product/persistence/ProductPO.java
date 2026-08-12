package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("product")
public class ProductPO {

    private UUID7 id;
    private ProductCode productCode;
    private ProductStatus status;
    private String title;
    private String subtitle;
    private String description;
    private String categoryCode;
    private UUID7 primaryImageFileId;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() { return id; }
    public ProductPO setId(UUID7 value) { id = value; return this; }
    public ProductCode getProductCode() { return productCode; }
    public ProductPO setProductCode(ProductCode value) { productCode = value; return this; }
    public ProductStatus getStatus() { return status; }
    public ProductPO setStatus(ProductStatus value) { status = value; return this; }
    public String getTitle() { return title; }
    public ProductPO setTitle(String value) { title = value; return this; }
    public String getSubtitle() { return subtitle; }
    public ProductPO setSubtitle(String value) { subtitle = value; return this; }
    public String getDescription() { return description; }
    public ProductPO setDescription(String value) { description = value; return this; }
    public String getCategoryCode() { return categoryCode; }
    public ProductPO setCategoryCode(String value) { categoryCode = value; return this; }
    public UUID7 getPrimaryImageFileId() { return primaryImageFileId; }
    public ProductPO setPrimaryImageFileId(UUID7 value) { primaryImageFileId = value; return this; }
    public Long getVersion() { return version; }
    public ProductPO setVersion(Long value) { version = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public ProductPO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public ProductPO setUpdatedAt(LocalDateTime value) { updatedAt = value; return this; }
}
