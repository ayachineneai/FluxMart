package org.ayachinene.app.order.domain;

import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;

public record SpecificationSnapshotItem(
        SpecificationCode specificationCode,
        String specificationName,
        SpecificationValueCode specificationValueCode,
        String specificationValueName
) {
}
