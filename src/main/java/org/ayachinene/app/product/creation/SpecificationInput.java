package org.ayachinene.app.product.creation;

import java.util.List;

public record SpecificationInput(
        String name,
        List<String> values
) {
}
