package org.ayachinene.app.product.domain;

import org.ayachinene.app.code.BusinessCodes;

public record ProductCode(String value) {

    private static final String PREFIX = "PRD_";

    public ProductCode {
        if (value == null || !value.startsWith(PREFIX)) {
            throw new IllegalArgumentException("productCode must start with " + PREFIX);
        }
        BusinessCodes.validate(value.substring(PREFIX.length()));
    }

    public static ProductCode generate() {
        return new ProductCode(PREFIX + BusinessCodes.generate());
    }
}
