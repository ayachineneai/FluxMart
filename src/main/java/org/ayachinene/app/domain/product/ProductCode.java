package org.ayachinene.app.domain.product;

import org.ayachinene.app.code.BusinessCodes;

public record ProductCode(String value) {

    private static final String PREFIX = "PRD_";

    public ProductCode {
        value = BusinessCodes.validate(value, PREFIX, "productCode");
    }

    public static ProductCode generate() {
        return new ProductCode(PREFIX + BusinessCodes.generateBody());
    }
}
