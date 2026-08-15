package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

public record CustomerOrderPO(
    UUID7 id,
    String orderCode,
    UUID7 userId,
    String requestKey,
    OrderStatus status,
    long totalAmount,
    LocalDateTime paymentExpiresAt,
    long version,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
