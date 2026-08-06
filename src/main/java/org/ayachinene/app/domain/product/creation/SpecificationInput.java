package org.ayachinene.app.domain.product.creation;

import java.util.List;

public record SpecificationInput(
        String name,
        List<String> values
) {
}
