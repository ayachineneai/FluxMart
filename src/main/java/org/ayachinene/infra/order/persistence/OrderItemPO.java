package org.ayachinene.infra.order.persistence;

import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;

public record OrderItemPO(
    UUID7 id,
    UUID7 orderId,
    String productCode,
    String skuCode,
    String productTitle,
    String specificationSnapshot,
    UUID7 imageFileId,
    long unitPriceAmount,
    int quantity,
    long totalAmount,
    int sortOrder,
    LocalDateTime createdAt
) {
}
