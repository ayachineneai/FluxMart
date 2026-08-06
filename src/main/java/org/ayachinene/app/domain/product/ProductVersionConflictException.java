package org.ayachinene.app.domain.product;

public class ProductVersionConflictException extends RuntimeException {

    public ProductVersionConflictException(ProductCode productCode, long expectedVersion) {
        super(
                "Product version conflict: productCode=" + productCode.value()
                        + ", expectedVersion=" + expectedVersion
        );
    }
}
