package org.ayachinene.app.domain.product.creation;

import java.util.List;

public record CreateProductInput(
        ProductInput product,
        List<SpecificationInput> specifications,
        List<SkuInput> skus
) {
}
