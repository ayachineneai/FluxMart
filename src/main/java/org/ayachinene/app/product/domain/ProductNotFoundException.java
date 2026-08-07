package org.ayachinene.app.product.domain;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(ProductCode productCode) {
        super("Product not found: " + productCode.value());
    }
}
