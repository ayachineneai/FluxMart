package org.ayachinene.app.order.creation;

import org.ayachinene.shared.uuid7.UUID7;

public record CreateOrderRequest(String requestKey, UUID7 userId, String skuCode, int quantity) {
}
