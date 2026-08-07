package org.ayachinene.api.product.data;

/**
 * <pre>{@code
 * {
 *   "expectedVersion": 0
 * }
 * }</pre>
 */
public record PublishProductRequest(
        Long expectedVersion
) {
}
