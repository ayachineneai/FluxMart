package org.ayachinene.infra.persistence.product.specification;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.domain.product.specification.SpecificationId;
import org.ayachinene.app.domain.product.specification.SpecificationStatus;
import org.ayachinene.app.domain.product.specification.SpecificationValueId;

import java.time.LocalDateTime;

@TableName("product_specification_value")
public class SpecificationValuePO {

    private SpecificationValueId id;
    private SpecificationId specificationId;
    private String displayName;
    private SpecificationStatus status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SpecificationValueId getId() {
        return id;
    }

    public SpecificationValuePO setId(SpecificationValueId id) {
        this.id = id;
        return this;
    }

    public SpecificationId getSpecificationId() {
        return specificationId;
    }

    public SpecificationValuePO setSpecificationId(SpecificationId specificationId) {
        this.specificationId = specificationId;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SpecificationValuePO setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public SpecificationStatus getStatus() {
        return status;
    }

    public SpecificationValuePO setStatus(SpecificationStatus status) {
        this.status = status;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public SpecificationValuePO setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SpecificationValuePO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public SpecificationValuePO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
