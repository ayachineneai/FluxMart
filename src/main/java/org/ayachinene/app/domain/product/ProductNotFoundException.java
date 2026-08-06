package org.ayachinene.app.domain.product;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(ProductCode productCode) {
        super("Product not found: " + productCode.value());
    }
}
