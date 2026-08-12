package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("stock")
public class StockPO {

    private UUID7 id;
    private UUID7 skuId;
    private Long availableQuantity;
    private Long reservedQuantity;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() { return id; }
    public StockPO setId(UUID7 value) { id = value; return this; }
    public UUID7 getSkuId() { return skuId; }
    public StockPO setSkuId(UUID7 value) { skuId = value; return this; }
    public Long getAvailableQuantity() { return availableQuantity; }
    public StockPO setAvailableQuantity(Long value) { availableQuantity = value; return this; }
    public Long getReservedQuantity() { return reservedQuantity; }
    public StockPO setReservedQuantity(Long value) { reservedQuantity = value; return this; }
    public Long getVersion() { return version; }
    public StockPO setVersion(Long value) { version = value; return this; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public StockPO setCreatedAt(LocalDateTime value) { createdAt = value; return this; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public StockPO setUpdatedAt(LocalDateTime value) { updatedAt = value; return this; }
}
