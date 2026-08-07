package org.ayachinene.api.product.data;

/**
 * <pre>{@code
 * {
 *   "productCode": "PRD_8KD3M7J2Q9W4R6TXP1ZN",
 *   "status": "ON_SALE",
 *   "version": 1
 * }
 * }</pre>
 */
public record PublishProductResponse(
        String productCode,
        String status,
        long version
) {
}
