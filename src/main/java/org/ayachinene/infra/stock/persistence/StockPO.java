package org.ayachinene.infra.stock.persistence;

import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

public class StockPO {

    private UUID7 id;
    private UUID7 skuId;
    private Long availableQuantity;
    private Long reservedQuantity;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() {
        return id;
    }

    public StockPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public UUID7 getSkuId() {
        return skuId;
    }

    public StockPO setSkuId(UUID7 skuId) {
        this.skuId = skuId;
        return this;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public StockPO setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
        return this;
    }

    public Long getReservedQuantity() {
        return reservedQuantity;
    }

    public StockPO setReservedQuantity(Long reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public StockPO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public StockPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public StockPO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
