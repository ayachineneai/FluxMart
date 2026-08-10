package org.ayachinene.app.order;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderItemInput;
import org.ayachinene.app.order.domain.OrderCannotBeCreatedException;
import org.ayachinene.app.order.domain.OrderIdempotencyConflictException;
import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.app.order.domain.Orders;
import org.ayachinene.app.order.query.PurchasableSku;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrdersTest {

    @Test
    void createsPendingPaymentOrderWithSnapshotsAndAmounts() {
        var sku1 = purchasableSku(new BigDecimal("10.50"));
        var sku2 = purchasableSku(new BigDecimal("20.00"));
        var input = input(
                new CreateOrderItemInput(sku1.skuCode(), 2),
                new CreateOrderItemInput(sku2.skuCode(), 1)
        );
        var expiresAt = OffsetDateTime.parse("2026-08-10T12:30:00+08:00");

        var order = Orders.create(input, List.of(sku1, sku2), expiresAt);

        assertEquals(OrderStatus.PENDING_PAYMENT, order.status());
        assertEquals(new BigDecimal("41.00"), order.totalAmount().amount());
        assertEquals(expiresAt, order.paymentExpiresAt());
        assertEquals(2, order.items().size());
        assertEquals(new BigDecimal("21.00"),
                order.items().getFirst().totalAmount().amount());
        assertEquals(sku1.productTitle(), order.items().getFirst().productTitle());
    }

    @Test
    void rejectsSkuThatIsNotPurchasable() {
        var sku = purchasableSku(new BigDecimal("10.00"));
        var unavailable = new PurchasableSku(
                sku.skuId(),
                sku.productCode(),
                sku.skuCode(),
                ProductStatus.OFF_SALE,
                sku.skuStatus(),
                sku.productTitle(),
                sku.specifications(),
                sku.imageFileId(),
                sku.price()
        );

        assertThrows(
                OrderCannotBeCreatedException.class,
                () -> Orders.create(
                        input(new CreateOrderItemInput(sku.skuCode(), 1)),
                        List.of(unavailable),
                        OffsetDateTime.now().plusMinutes(15)
                )
        );
    }

    @Test
    void acceptsTheSameIdempotentRequest() {
        var sku = purchasableSku(new BigDecimal("10.00"));
        var input = input(new CreateOrderItemInput(sku.skuCode(), 1));
        var order = Orders.create(
                input,
                List.of(sku),
                OffsetDateTime.now().plusMinutes(15)
        );

        Orders.requireConsistent(order, input);
    }

    @Test
    void rejectsDifferentContentForTheSameRequestKey() {
        var sku = purchasableSku(new BigDecimal("10.00"));
        var originalInput = input(new CreateOrderItemInput(sku.skuCode(), 1));
        var order = Orders.create(
                originalInput,
                List.of(sku),
                OffsetDateTime.now().plusMinutes(15)
        );
        var changedInput = new CreateOrderInput(
                originalInput.userId(),
                originalInput.requestKey(),
                List.of(new CreateOrderItemInput(sku.skuCode(), 2))
        );

        assertThrows(
                OrderIdempotencyConflictException.class,
                () -> Orders.requireConsistent(order, changedInput)
        );
    }

    private CreateOrderInput input(CreateOrderItemInput... items) {
        return new CreateOrderInput(
                UUID7s.generate(),
                "request-001",
                List.of(items)
        );
    }

    private PurchasableSku purchasableSku(BigDecimal price) {
        return new PurchasableSku(
                UUID7s.generate(),
                ProductCode.generate(),
                SkuCode.generate(),
                ProductStatus.ON_SALE,
                SkuStatus.ENABLED,
                "FluxMart product",
                List.of(),
                UUID7s.generate(),
                new Money(price)
        );
    }
}
