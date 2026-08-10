package org.ayachinene.api.order.data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * <pre>{@code
 * {
 *   "orderCode": "ORD_8KD3M7J2Q9W4R6TXP1ZN",
 *   "status": "PENDING_PAYMENT",
 *   "totalAmount": 199.00,
 *   "paymentExpiresAt": "2026-08-10T12:30:00+08:00"
 * }
 * }</pre>
 */
public record CreateOrderResponse(
        String orderCode,
        String status,
        BigDecimal totalAmount,
        OffsetDateTime paymentExpiresAt
) {
}
