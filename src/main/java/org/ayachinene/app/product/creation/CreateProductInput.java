package org.ayachinene.app.product.creation;

import java.util.List;

public record CreateProductInput(
        ProductDetailsInput details,
        List<SpecificationInput> specifications,
        List<SkuInput> skus
) {
}
