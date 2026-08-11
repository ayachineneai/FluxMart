package org.ayachinene.app.product.creation;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;

import java.util.List;

public record CreatedProduct(
        ProductCode productCode,
        List<SkuCode> skuCodes
) {
}
