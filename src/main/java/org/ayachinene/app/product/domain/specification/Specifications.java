package org.ayachinene.app.product.domain.specification;

import org.ayachinene.app.product.creation.SpecificationInput;
import java.util.List;

public final class Specifications {

    private Specifications() {
    }

    public static List<Specification> create(
            List<SpecificationInput> inputs
    ) {
        return inputs.stream()
                .map(Specifications::createSpecification)
                .toList();
    }

    private static Specification createSpecification(SpecificationInput input) {
        return new Specification(
                SpecificationCode.generate(),
                input.name(),
                SpecificationStatus.ENABLED,
                input.values().stream()
                        .map(Specifications::createSpecificationValue)
                        .toList()
        );
    }

    private static SpecificationValue createSpecificationValue(String displayName) {
        return new SpecificationValue(
                SpecificationValueCode.generate(),
                displayName,
                SpecificationStatus.ENABLED
        );
    }
}
