package org.ayachinene.app.domain.product.specification;

import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;
import java.util.Objects;

public record Specification(
        UUID7 specificationId,
        String name,
        SpecificationStatus status,
        List<SpecificationValue> values
) {

    public Specification {
        Objects.requireNonNull(specificationId, "specificationId must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(status, "status must not be null");
        values = List.copyOf(Objects.requireNonNull(values, "values must not be null"));
    }
}
