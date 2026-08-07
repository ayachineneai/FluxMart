package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.code.BusinessCodes;

public record SkuCode(String value) {

    private static final String PREFIX = "SKU_";

    public SkuCode {
        value = BusinessCodes.validate(value, PREFIX, "skuCode");
    }

    public static SkuCode generate() {
        return new SkuCode(PREFIX + BusinessCodes.generateBody());
    }
}
