package org.ayachinene.api.order.data;

import java.util.List;

/**
 * <pre>{@code
 * {
 *   "items": [
 *     {
 *       "skuCode": "SKU_8KD3M7J2Q9W4R6TXP1ZN",
 *       "quantity": 2
 *     }
 *   ]
 * }
 * }</pre>
 */
public record CreateOrderRequest(
        List<ItemRequest> items
) {

    public record ItemRequest(
            String skuCode,
            Integer quantity
    ) {
    }
}
