package org.ayachinene.app.product.domain.specification;

import org.ayachinene.app.code.BusinessCodes;

public record SpecificationCode(String value) {

    private static final String PREFIX = "SPC_";

    public SpecificationCode {
        if (value == null || !value.startsWith(PREFIX)) {
            throw new IllegalArgumentException(
                "specificationCode must start with " + PREFIX
            );
        }
        BusinessCodes.validate(value.substring(PREFIX.length()));
    }

    public static SpecificationCode generate() {
        return new SpecificationCode(PREFIX + BusinessCodes.generate());
    }
}
