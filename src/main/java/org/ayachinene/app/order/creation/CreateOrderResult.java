package org.ayachinene.app.order.creation;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.order.domain.OrderStatus;

import java.time.OffsetDateTime;

public record CreateOrderResult(
        OrderCode orderCode,
        OrderStatus status,
        Money totalAmount,
        OffsetDateTime paymentExpiresAt
) {
}
