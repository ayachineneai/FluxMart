package org.ayachinene.app.order.creation;

import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

public record OrderCreationInfo(
    UUID7 orderId,
    UUID7 orderItemId,
    UUID7 stockReservationId,
    String orderCode,
    LocalDateTime createdAt,
    LocalDateTime paymentExpiresAt
) {
}
