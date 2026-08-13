package org.ayachinene.app.order.creation;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.math.BigDecimal;
import java.util.List;

public record OrderProductData(
    UUID7 skuId,
    SkuCode skuCode,
    SkuStatus skuStatus,
    BigDecimal price,
    UUID7 stockId,
    ProductCode productCode,
    ProductStatus productStatus,
    String productTitle,
    UUID7 snapshotImageFileId,
    List<SpecificationSelection> specificationSelections
) {

    public record SpecificationSelection(
        SpecificationCode specificationCode,
        String specificationName,
        SpecificationValueCode specificationValueCode,
        String specificationValue
    ) {
    }
}
