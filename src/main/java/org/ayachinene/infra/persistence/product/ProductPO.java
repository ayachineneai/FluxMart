package org.ayachinene.infra.persistence.product;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductStatus;
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
    private CategoryCode categoryCode;
    private UUID7 primaryImageFileId;

    private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() {
        return id;
    }

    public ProductPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public ProductCode getProductCode() {
        return productCode;
    }

    public ProductPO setProductCode(ProductCode productCode) {
        this.productCode = productCode;
        return this;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public ProductPO setStatus(ProductStatus status) {
        this.status = status;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductPO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public ProductPO setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductPO setDescription(String description) {
        this.description = description;
        return this;
    }

    public CategoryCode getCategoryCode() {
        return categoryCode;
    }

    public ProductPO setCategoryCode(CategoryCode categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public UUID7 getPrimaryImageFileId() {
        return primaryImageFileId;
    }

    public ProductPO setPrimaryImageFileId(UUID7 primaryImageFileId) {
        this.primaryImageFileId = primaryImageFileId;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public ProductPO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProductPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ProductPO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
