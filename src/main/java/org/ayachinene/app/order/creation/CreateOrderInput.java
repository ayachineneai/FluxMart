package org.ayachinene.app.order.creation;

import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record CreateOrderInput(
        UUID7 userId,
        String requestKey,
        List<CreateOrderItemInput> items
) {
}
