package org.ayachinene.app.product.domain.specification;

public record SpecificationValue(
        SpecificationValueCode specificationValueCode,
        String displayName,
        SpecificationStatus status
) {
}
