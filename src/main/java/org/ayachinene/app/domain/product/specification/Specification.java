package org.ayachinene.app.domain.product.specification;

import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

public record Specification(
        UUID7 specificationId,
        String name,
        SpecificationStatus status,
        List<SpecificationValue> values
) {

    public Specification {
        values = List.copyOf(values);
    }
}
