package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.order.domain.Order;
import org.ayachinene.app.order.repository.OrderRepository;
import org.ayachinene.app.order.repository.OrderUniquenessConflictException;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final long INITIAL_VERSION = 0L;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderPersistenceConverter persistenceConverter;

    public OrderRepositoryImpl(
            OrderMapper orderMapper,
            OrderItemMapper orderItemMapper,
            OrderPersistenceConverter persistenceConverter
    ) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.persistenceConverter = persistenceConverter;
    }

    @Override
    public Optional<Order> findByUserIdAndRequestKey(
            UUID7 userId,
            String requestKey
    ) {
        return Optional.ofNullable(
                orderMapper.selectByUserIdAndRequestKey(userId, requestKey)
        ).map(this::withItems);
    }

    private Order withItems(OrderPO orderPo) {
        var itemPos = orderItemMapper.selectByOrderId(orderPo.getId());
        return persistenceConverter.toOrder(orderPo, itemPos);
    }

    @Override
    public void create(Order order) {
        var newOrderId = UUID7s.generate();
        var createdAt = LocalDateTime.now();

        insertOrder(newOrderId, order, createdAt);
        insertOrderItems(newOrderId, order, createdAt);
    }

    private void insertOrder(
            UUID7 newOrderId,
            Order order,
            LocalDateTime createdAt
    ) {
        var orderPo = persistenceConverter.toOrderPo(order)
                .setId(newOrderId)
                .setVersion(INITIAL_VERSION)
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt);
        try {
            orderMapper.insert(orderPo);
        } catch (DuplicateKeyException exception) {
            throw new OrderUniquenessConflictException(exception);
        }
    }

    private void insertOrderItems(
            UUID7 orderId,
            Order order,
            LocalDateTime createdAt
    ) {
        var itemPos = Streams.withIndex(order.items())
                .map(indexed -> persistenceConverter.toOrderItemPo(indexed.value())
                        .setId(UUID7s.generate())
                        .setOrderId(orderId)
                        .setSortOrder(indexed.index())
                        .setCreatedAt(createdAt))
                .toList();
        orderItemMapper.insertBatch(itemPos);
    }
}
