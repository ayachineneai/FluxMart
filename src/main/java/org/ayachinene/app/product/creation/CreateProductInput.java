package org.ayachinene.app.product.creation;

import java.util.List;

public record CreateProductInput(
        ProductInput product,
        List<SpecificationInput> specifications,
        List<SkuInput> skus
) {
}
