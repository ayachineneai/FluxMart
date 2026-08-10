package org.ayachinene.infra.order.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("order_item")
public class OrderItemPO {

    private UUID7 id;
    private UUID7 orderId;
    private ProductCode productCode;
    private SkuCode skuCode;
    private String productTitle;
    private String specificationSnapshot;
    private UUID7 imageFileId;
    private Long unitPriceAmount;
    private Integer quantity;
    private Long totalAmount;
    private Integer sortOrder;
    private LocalDateTime createdAt;

    public UUID7 getId() {
        return id;
    }

    public OrderItemPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public UUID7 getOrderId() {
        return orderId;
    }

    public OrderItemPO setOrderId(UUID7 orderId) {
        this.orderId = orderId;
        return this;
    }

    public ProductCode getProductCode() {
        return productCode;
    }

    public OrderItemPO setProductCode(ProductCode productCode) {
        this.productCode = productCode;
        return this;
    }

    public SkuCode getSkuCode() {
        return skuCode;
    }

    public OrderItemPO setSkuCode(SkuCode skuCode) {
        this.skuCode = skuCode;
        return this;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public OrderItemPO setProductTitle(String productTitle) {
        this.productTitle = productTitle;
        return this;
    }

    public String getSpecificationSnapshot() {
        return specificationSnapshot;
    }

    public OrderItemPO setSpecificationSnapshot(String specificationSnapshot) {
        this.specificationSnapshot = specificationSnapshot;
        return this;
    }

    public UUID7 getImageFileId() {
        return imageFileId;
    }

    public OrderItemPO setImageFileId(UUID7 imageFileId) {
        this.imageFileId = imageFileId;
        return this;
    }

    public Long getUnitPriceAmount() {
        return unitPriceAmount;
    }

    public OrderItemPO setUnitPriceAmount(Long unitPriceAmount) {
        this.unitPriceAmount = unitPriceAmount;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderItemPO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public OrderItemPO setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public OrderItemPO setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderItemPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
