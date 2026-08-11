package org.ayachinene.app.product.domain.specification;

import org.ayachinene.app.code.BusinessCodes;

public record SpecificationValueCode(String value) {

    private static final String PREFIX = "SPV_";

    public SpecificationValueCode {
        value = BusinessCodes.validate(value, PREFIX, "specificationValueCode");
    }

    public static SpecificationValueCode generate() {
        return new SpecificationValueCode(PREFIX + BusinessCodes.generateBody());
    }
}
