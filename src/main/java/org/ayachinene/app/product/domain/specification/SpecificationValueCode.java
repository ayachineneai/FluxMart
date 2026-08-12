package org.ayachinene.app.product.domain.specification;

import org.ayachinene.app.code.BusinessCodes;

public record SpecificationValueCode(String value) {

    private static final String PREFIX = "SPV_";

    public SpecificationValueCode {
        if (value == null || !value.startsWith(PREFIX)) {
            throw new IllegalArgumentException(
                "specificationValueCode must start with " + PREFIX
            );
        }
        BusinessCodes.validate(value.substring(PREFIX.length()));
    }

    public static SpecificationValueCode generate() {
        return new SpecificationValueCode(PREFIX + BusinessCodes.generate());
    }
}
