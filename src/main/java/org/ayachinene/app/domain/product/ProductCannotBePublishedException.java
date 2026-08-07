package org.ayachinene.app.domain.product;

public class ProductCannotBePublishedException extends RuntimeException {

    public ProductCannotBePublishedException(
        ProductCode productCode,
        String reason
    ) {
        super(
            "Product cannot be published: productCode="
                + productCode.value()
                + ", reason="
                + reason
        );
    }
}
