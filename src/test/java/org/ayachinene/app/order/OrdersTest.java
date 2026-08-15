package org.ayachinene.app.order;

import org.ayachinene.app.order.creation.CreateOrderRequest;
import org.ayachinene.app.order.creation.OrderQuantity;
import org.ayachinene.app.order.creation.ProductBaseInfo;
import org.ayachinene.app.order.creation.ProductFacts;
import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.app.order.domain.StockReservationStatus;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.exception.ValidationException;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersTest {

    @Test
    void acceptsValidCreateOrderRequest() {
        assertDoesNotThrow(() -> Orders.validate(request(1)));
    }

    @Test
    void rejectsInvalidCreateOrderRequest() {
        assertThrows(
            ValidationException.class,
            () -> Orders.validate(request(0))
        );
    }

    @Test
    void rejectsInvalidSkuCode() {
        var request = new CreateOrderRequest(
            "request-key",
            UUID7s.generate(),
            "invalid-sku-code",
            1
        );

        assertThrows(
            ValidationException.class,
            () -> Orders.validate(request)
        );
    }

    @Test
    void identifiesIdempotentRetry() {
        var quantity = new OrderQuantity(skuCode(), 2);

        assertTrue(Orders.isIdempotentRetry(
            quantity,
            quantity
        ));
    }

    @Test
    void doesNotIdentifyDifferentOrderQuantityAsIdempotentRetry() {
        assertFalse(Orders.isIdempotentRetry(
            new OrderQuantity(skuCode(), 1),
            new OrderQuantity(skuCode(), 2)
        ));
    }

    @Test
    void acceptsOrderableProductFacts() {
        assertDoesNotThrow(() -> Orders.checkOrderable(
            productFacts(ProductStatus.ON_SALE, SkuStatus.ENABLED, 2),
            2
        ));
    }

    @Test
    void rejectsProductThatIsNotOnSale() {
        assertThrows(
            ValidationException.class,
            () -> Orders.checkOrderable(
                productFacts(ProductStatus.OFF_SALE, SkuStatus.ENABLED, 2),
                1
            )
        );
    }

    @Test
    void rejectsDisabledSku() {
        assertThrows(
            ValidationException.class,
            () -> Orders.checkOrderable(
                productFacts(ProductStatus.ON_SALE, SkuStatus.DISABLED, 2),
                1
            )
        );
    }

    @Test
    void rejectsInsufficientStock() {
        assertThrows(
            ValidationException.class,
            () -> Orders.checkOrderable(
                productFacts(ProductStatus.ON_SALE, SkuStatus.ENABLED, 1),
                2
            )
        );
    }

    @Test
    void preparesOrderCreationInformation() {
        var creationInfo = Orders.prepareOrderCreationInfo();

        assertNotEquals(creationInfo.orderId(), creationInfo.orderItemId());
        assertNotEquals(creationInfo.orderId(), creationInfo.stockReservationId());
        assertTrue(creationInfo.orderCode().startsWith("ORD_"));
        assertEquals(24, creationInfo.orderCode().length());
        assertEquals(
            Duration.ofMinutes(15),
            Duration.between(
                creationInfo.createdAt(),
                creationInfo.paymentExpiresAt()
            )
        );
    }

    @Test
    void makesConsistentOrderPersistenceObjects() {
        var request = request(2);
        var productFacts = productFacts(
            ProductStatus.ON_SALE,
            SkuStatus.ENABLED,
            2
        );
        var creationInfo = Orders.prepareOrderCreationInfo();

        var orderPos = Orders.mkOrderPos(
            new ObjectMapper(),
            request,
            productFacts,
            creationInfo
        );

        assertEquals(200L, orderPos.customerOrder().totalAmount());
        assertEquals(200L, orderPos.orderItem().totalAmount());
        assertEquals(creationInfo.orderId(), orderPos.orderItem().orderId());
        assertEquals(
            creationInfo.orderItemId(),
            orderPos.stockReservation().orderItemId()
        );
        assertEquals(
            OrderStatus.PENDING_PAYMENT,
            orderPos.customerOrder().status()
        );
        assertEquals(
            StockReservationStatus.RESERVED,
            orderPos.stockReservation().status()
        );
    }

    private static CreateOrderRequest request(int quantity) {
        return new CreateOrderRequest(
            "request-key",
            UUID7s.generate(),
            skuCode(),
            quantity
        );
    }

    private static ProductFacts productFacts(
        ProductStatus productStatus,
        SkuStatus skuStatus,
        long availableQuantity
    ) {
        return new ProductFacts(
            new ProductBaseInfo(
                UUID7s.generate(),
                skuCode(),
                skuStatus,
                100L,
                UUID7s.generate(),
                availableQuantity,
                "PRD_23456789ABCDEFGHJKMN",
                productStatus,
                "Product",
                UUID7s.generate()
            ),
            List.of()
        );
    }

    private static String skuCode() {
        return SkuCode.generate().value();
    }
}
