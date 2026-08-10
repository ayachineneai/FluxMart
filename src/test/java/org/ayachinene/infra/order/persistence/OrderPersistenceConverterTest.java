package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.domain.Order;
import org.ayachinene.app.order.domain.OrderCode;
import org.ayachinene.app.order.domain.OrderItem;
import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.app.order.domain.SpecificationSnapshotItem;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPersistenceConverterTest {

    private final OrderPersistenceConverter converter =
            new OrderPersistenceConverter(JsonMapper.builder().build());

    @Test
    void convertsMoneyTimeAndSpecificationSnapshotRoundTrip() {
        var specification = new SpecificationSnapshotItem(
                UUID7s.generate(),
                "颜色",
                UUID7s.generate(),
                "黑色"
        );
        var item = new OrderItem(
                ProductCode.generate(),
                SkuCode.generate(),
                "FluxMart 商品",
                List.of(specification),
                UUID7s.generate(),
                new Money(new BigDecimal("19.90")),
                2,
                new Money(new BigDecimal("39.80"))
        );
        var order = new Order(
                OrderCode.generate(),
                UUID7s.generate(),
                "request-001",
                OrderStatus.PENDING_PAYMENT,
                item.totalAmount(),
                OffsetDateTime.now(ZoneId.systemDefault()).withNano(123_000_000),
                List.of(item)
        );

        var orderPo = converter.toOrderPo(order).setId(UUID7s.generate());
        var itemPo = converter.toOrderItemPo(item);
        var restored = converter.toOrder(orderPo, List.of(itemPo));

        assertEquals(order.orderCode(), restored.orderCode());
        assertEquals(order.totalAmount(), restored.totalAmount());
        assertEquals(order.paymentExpiresAt(), restored.paymentExpiresAt());
        assertEquals(order.items(), restored.items());
    }
}
