package org.ayachinene.app.product.domain.sku;

import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;

public record SpecificationSelection(
        SpecificationCode specificationCode,
        SpecificationValueCode specificationValueCode
) {
}
