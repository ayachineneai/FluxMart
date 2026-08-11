package org.ayachinene.app.product.domain.specification;

import org.ayachinene.app.code.BusinessCodes;

public record SpecificationCode(String value) {

    private static final String PREFIX = "SPC_";

    public SpecificationCode {
        value = BusinessCodes.validate(value, PREFIX, "specificationCode");
    }

    public static SpecificationCode generate() {
        return new SpecificationCode(PREFIX + BusinessCodes.generateBody());
    }
}
