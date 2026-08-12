package org.ayachinene.app.product.domain.sku;

import org.ayachinene.app.code.BusinessCodes;

public record SkuCode(String value) {

    private static final String PREFIX = "SKU_";

    public SkuCode {
        if (value == null || !value.startsWith(PREFIX)) {
            throw new IllegalArgumentException("skuCode must start with " + PREFIX);
        }
        BusinessCodes.validate(value.substring(PREFIX.length()));
    }

    public static SkuCode generate() {
        return new SkuCode(PREFIX + BusinessCodes.generate());
    }
}
