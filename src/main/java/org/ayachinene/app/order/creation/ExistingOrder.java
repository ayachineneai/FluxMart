package org.ayachinene.app.order.creation;

public record ExistingOrder(
    OrderQuantity quantity,
    CreateOrderResult result
) {
}
