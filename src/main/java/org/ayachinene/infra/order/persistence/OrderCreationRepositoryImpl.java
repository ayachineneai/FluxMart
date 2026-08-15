package org.ayachinene.infra.order.persistence;

import io.vavr.control.Option;
import org.ayachinene.app.order.OrderCreationRepository;
import org.ayachinene.app.order.creation.OrderPos;
import org.ayachinene.app.order.creation.OrderQuantity;
import org.ayachinene.app.order.creation.ProductFacts;
import org.ayachinene.app.order.Orders;
import org.ayachinene.shared.exception.ValidationException;
import org.ayachinene.shared.uuid7.UUID7;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCreationRepositoryImpl implements OrderCreationRepository {

    private final OrderCreationMapper mapper;

    public OrderCreationRepositoryImpl(OrderCreationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Option<OrderQuantity> findOrderQuantity(
        UUID7 userId,
        String requestKey
    ) {
        return Option.of(mapper.findOrderQuantity(userId, requestKey));
    }

    @Override
    public Option<ProductFacts> findProductFacts(String skuCode) {
        var baseInfo = mapper.findProductBaseInfo(skuCode);
        if (baseInfo == null) return Option.none();

        var selections = mapper.findSpecificationSelections(baseInfo.skuId());

        return Option.of(new ProductFacts(
            baseInfo,
            selections
        ));
    }

    @Override
    public void create(OrderPos orderPos) {
        try {
            mapper.insertCustomerOrder(orderPos.customerOrder());
        } catch (DuplicateKeyException exception) {
            handleDuplicateOrder(orderPos, exception);
            return;
        }

        mapper.insertOrderItem(orderPos.orderItem());
        var reserved = mapper.reserveStock(
            orderPos.stockId(),
            orderPos.stockReservation().quantity(),
            orderPos.customerOrder().updatedAt()
        );
        if (!reserved) {
            throw new ValidationException("insufficient stock");
        }
        mapper.insertStockReservation(orderPos.stockReservation());
    }

    private void handleDuplicateOrder(
        OrderPos orderPos,
        DuplicateKeyException exception
    ) {
        var order = orderPos.customerOrder();
        var existing = findOrderQuantityAfterConflict(
            order.userId(),
            order.requestKey()
        );

        // 其他冲突
        if (existing.isEmpty()) throw exception;

        var item = orderPos.orderItem();
        if (!Orders.isIdempotentRetry(
            existing,
            new OrderQuantity(item.skuCode(), item.quantity())
        )) {
            throw new ValidationException(
                "requestKey has been used with different order parameters"
            );
        }
    }

    private Option<OrderQuantity> findOrderQuantityAfterConflict(
        UUID7 userId,
        String requestKey
    ) {
        return Option.of(mapper.findOrderQuantityForUpdate(userId, requestKey));
    }
}
