package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("product_specification")
public class ProductSpecificationPO {

    private UUID7 id;
    private UUID7 productId;
    private SpecificationCode specificationCode;
    private String name;
    private SpecificationStatus status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() { return id; }
    public ProductSpecificationPO setId(UUID7 value) { id = value; return this; }
    public UUID7 getProductId() { return productId; }
    public ProductSpecificationPO setProductId(UUID7 value) { productId = value; return this; }
    public SpecificationCode getSpecificationCode() { return specificationCode; }
    public ProductSpecificationPO setSpecificationCode(SpecificationCode value) { specificationCode = value; return this; }
    public String getName() { return name; }
    public ProductSpecificationPO setName(String value) { name = value; return this; }
    public SpecificationStatus getStatus() { return status; }
    public ProductSpecificationPO setStatus(SpecificationStatus value) { status = value; return this; }
    public Integer getSortOrder() { return sortOrder; }
    public ProductSpecificationPO setSortOrder(Integer value) { sortOrder = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public ProductSpecificationPO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public ProductSpecificationPO setUpdatedAt(LocalDateTime value) { updatedAt = value; return this; }
}
