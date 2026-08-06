package org.ayachinene.infra.persistence.product.specification;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationStatus;
import org.ayachinene.app.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("product_specification")
public class SpecificationPO {

    private SpecificationId id;
    private UUID7 productId;
    private String name;
    private SpecificationStatus status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SpecificationId getId() {
        return id;
    }

    public SpecificationPO setId(SpecificationId id) {
        this.id = id;
        return this;
    }

    public UUID7 getProductId() {
        return productId;
    }

    public SpecificationPO setProductId(UUID7 productId) {
        this.productId = productId;
        return this;
    }

    public String getName() {
        return name;
    }

    public SpecificationPO setName(String name) {
        this.name = name;
        return this;
    }

    public SpecificationStatus getStatus() {
        return status;
    }

    public SpecificationPO setStatus(SpecificationStatus status) {
        this.status = status;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public SpecificationPO setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SpecificationPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SpecificationPO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
