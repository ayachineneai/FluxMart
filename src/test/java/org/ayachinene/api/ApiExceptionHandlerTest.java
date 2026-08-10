package org.ayachinene.api;

import org.ayachinene.app.order.domain.OrderIdempotencyConflictException;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.stock.reservation.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void mapsInsufficientStock() {
        var error = handler.handleInsufficientStock(
                new InsufficientStockException(SkuCode.generate())
        );

        assertEquals("INSUFFICIENT_STOCK", error.code());
    }

    @Test
    void mapsOrderIdempotencyConflict() {
        var error = handler.handleOrderIdempotencyConflict(
                new OrderIdempotencyConflictException("request-001")
        );

        assertEquals("ORDER_IDEMPOTENCY_CONFLICT", error.code());
    }
}
