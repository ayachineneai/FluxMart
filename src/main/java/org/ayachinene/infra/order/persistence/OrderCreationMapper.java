package org.ayachinene.infra.order.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.order.creation.OrderQuantity;
import org.ayachinene.app.order.creation.ProductBaseInfo;
import org.ayachinene.app.order.creation.SpecificationSelection;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderCreationMapper {

    OrderQuantity findOrderQuantity(
        UUID7 userId,
        String requestKey
    );

    OrderQuantity findOrderQuantityForUpdate(
        UUID7 userId,
        String requestKey
    );

    ProductBaseInfo findProductBaseInfo(String skuCode);

    List<SpecificationSelection> findSpecificationSelections(
        UUID7 skuId
    );

    int insertCustomerOrder(CustomerOrderPO order);

    int insertOrderItem(OrderItemPO item);

    boolean reserveStock(
        UUID7 stockId,
        long quantity,
        LocalDateTime updatedAt
    );

    int insertStockReservation(StockReservationPO reservation);
}
