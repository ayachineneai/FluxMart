package org.ayachinene.api.order.data;

/**
 * 创建订单请求。
 *
 * <pre>{@code
 * {
 *   "requestKey": "0195d7d2-6380-7a5c-8b35-3a23b8df1f01",
 *   "skuCode": "SKU_23456789ABCDEFGHJKMN",
 *   "quantity": 2
 * }
 * }</pre>
 */
public record CreateOrderRequest(
    String requestKey,
    String skuCode,
    int quantity
) {
}
