package org.ayachinene.infra.order.persistence;

import com.baomidou.mybatisplus.annotation.TableName;
import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

@TableName("customer_order")
public class OrderPO {

    private UUID7 id;
    private OrderCode orderCode;
    private UUID7 userId;
    private String requestKey;
    private OrderStatus status;
    private Long totalAmount;
    private LocalDateTime paymentExpiresAt;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID7 getId() {
        return id;
    }

    public OrderPO setId(UUID7 id) {
        this.id = id;
        return this;
    }

    public OrderCode getOrderCode() {
        return orderCode;
    }

    public OrderPO setOrderCode(OrderCode orderCode) {
        this.orderCode = orderCode;
        return this;
    }

    public UUID7 getUserId() {
        return userId;
    }

    public OrderPO setUserId(UUID7 userId) {
        this.userId = userId;
        return this;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public OrderPO setRequestKey(String requestKey) {
        this.requestKey = requestKey;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public OrderPO setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public OrderPO setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public LocalDateTime getPaymentExpiresAt() {
        return paymentExpiresAt;
    }

    public OrderPO setPaymentExpiresAt(LocalDateTime paymentExpiresAt) {
        this.paymentExpiresAt = paymentExpiresAt;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public OrderPO setVersion(Long version) {
        this.version = version;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderPO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OrderPO setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
