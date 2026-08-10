package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.order.domain.Order;
import org.ayachinene.app.order.repository.OrderUniquenessConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderRepositoryImplTest {

    @Test
    void translatesAnOrderUniqueKeyConflict() {
        var orderMapper = mock(OrderMapper.class);
        var itemMapper = mock(OrderItemMapper.class);
        var converter = mock(OrderPersistenceConverter.class);
        var order = mock(Order.class);
        when(converter.toOrderPo(order)).thenReturn(new OrderPO());
        doThrow(new DuplicateKeyException("duplicate order"))
                .when(orderMapper).insert(any(OrderPO.class));
        var repository = new OrderRepositoryImpl(
                orderMapper,
                itemMapper,
                converter
        );

        assertThrows(
                OrderUniquenessConflictException.class,
                () -> repository.create(order)
        );
    }
}
