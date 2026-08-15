package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.order.domain.StockReservationStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

public record StockReservationPO(
    UUID7 id,
    UUID7 orderId,
    UUID7 orderItemId,
    UUID7 skuId,
    long quantity,
    StockReservationStatus status,
    LocalDateTime expiresAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
