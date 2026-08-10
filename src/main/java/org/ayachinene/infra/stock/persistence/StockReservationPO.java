package org.ayachinene.infra.stock.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.stock.reservation.StockReservationStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("stock_reservation")
public class StockReservationPO {

    private UUID7 id;
    private UUID7 orderId;
    private UUID7 orderItemId;
    private UUID7 skuId;
    private Long quantity;
    private StockReservationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() {
        return id;
    }

    public StockReservationPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public UUID7 getOrderId() {
        return orderId;
    }

    public StockReservationPO setOrderId(UUID7 orderId) {
        this.orderId = orderId;
        return this;
    }

    public UUID7 getOrderItemId() {
        return orderItemId;
    }

    public StockReservationPO setOrderItemId(UUID7 orderItemId) {
        this.orderItemId = orderItemId;
        return this;
    }

    public UUID7 getSkuId() {
        return skuId;
    }

    public StockReservationPO setSkuId(UUID7 skuId) {
        this.skuId = skuId;
        return this;
    }

    public Long getQuantity() {
        return quantity;
    }

    public StockReservationPO setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public StockReservationStatus getStatus() {
        return status;
    }

    public StockReservationPO setStatus(StockReservationStatus status) {
        this.status = status;
        return this;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public StockReservationPO setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public StockReservationPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public StockReservationPO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
