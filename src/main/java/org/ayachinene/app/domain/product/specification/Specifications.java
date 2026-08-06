package org.ayachinene.app.domain.product.specification;

import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.app.uuid7.UUID7s;

import java.util.List;

public final class Specifications {

    private Specifications() {
    }

    public static List<Specification> mkSpecifications(
            List<SpecificationInput> inputs
    ) {
        return inputs.stream()
                .map(Specifications::mkSpecification)
                .toList();
    }

    private static Specification mkSpecification(SpecificationInput input) {
        return new Specification(
                new SpecificationId(UUID7s.generate()),
                input.name(),
                SpecificationStatus.ENABLED,
                input.values().stream()
                        .map(Specifications::mkSpecificationValue)
                        .toList()
        );
    }

    private static SpecificationValue mkSpecificationValue(String displayName) {
        return new SpecificationValue(
                new SpecificationValueId(UUID7s.generate()),
                displayName,
                SpecificationStatus.ENABLED
        );
    }
}
