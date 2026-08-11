package org.ayachinene.app.product.domain.specification;

import java.util.List;

public record Specification(
        SpecificationCode specificationCode,
        String name,
        SpecificationStatus status,
        List<SpecificationValue> values
) {

    public Specification {
        values = List.copyOf(values);
    }
}
