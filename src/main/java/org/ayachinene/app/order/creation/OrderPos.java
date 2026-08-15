package org.ayachinene.app.order.creation;

import org.ayachinene.infra.order.persistence.CustomerOrderPO;
import org.ayachinene.infra.order.persistence.OrderItemPO;
import org.ayachinene.infra.order.persistence.StockReservationPO;
import org.ayachinene.shared.uuid7.UUID7;

public record OrderPos(
    UUID7 stockId,
    CustomerOrderPO customerOrder,
    OrderItemPO orderItem,
    StockReservationPO stockReservation
) {
}
