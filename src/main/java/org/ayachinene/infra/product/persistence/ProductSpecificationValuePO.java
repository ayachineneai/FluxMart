package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("product_specification_value")
public class ProductSpecificationValuePO {

    private UUID7 id;
    private UUID7 specificationId;
    private SpecificationValueCode specificationValueCode;
    private String displayName;
    private SpecificationStatus status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() { return id; }
    public ProductSpecificationValuePO setId(UUID7 value) { id = value; return this; }
    public UUID7 getSpecificationId() { return specificationId; }
    public ProductSpecificationValuePO setSpecificationId(UUID7 value) { specificationId = value; return this; }
    public SpecificationValueCode getSpecificationValueCode() { return specificationValueCode; }
    public ProductSpecificationValuePO setSpecificationValueCode(SpecificationValueCode value) { specificationValueCode = value; return this; }
    public String getDisplayName() { return displayName; }
    public ProductSpecificationValuePO setDisplayName(String value) { displayName = value; return this; }
    public SpecificationStatus getStatus() { return status; }
    public ProductSpecificationValuePO setStatus(SpecificationStatus value) { status = value; return this; }
    public Integer getSortOrder() { return sortOrder; }
    public ProductSpecificationValuePO setSortOrder(Integer value) { sortOrder = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public ProductSpecificationValuePO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public ProductSpecificationValuePO setUpdatedAt(LocalDateTime value) { updatedAt = value; return this; }
}
