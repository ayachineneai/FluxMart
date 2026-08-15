package org.ayachinene.app.order.creation;

import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

public record CreateOrderResult(
    UUID7 orderId,
    String orderCode,
    OrderStatus status,
    long totalAmount,
    LocalDateTime paymentExpiresAt
) {
}
