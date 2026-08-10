package org.ayachinene.app.order.domain;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.OffsetDateTime;
import java.util.List;

public record Order(
        OrderCode orderCode,
        UUID7 userId,
        String requestKey,
        OrderStatus status,
        Money totalAmount,
        OffsetDateTime paymentExpiresAt,
        List<OrderItem> items
) {

    public Order {
        items = List.copyOf(items);
    }
}
