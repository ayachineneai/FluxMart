package org.ayachinene.app.product.creation;

import org.ayachinene.infra.product.persistence.ProductGalleryImagePO;
import org.ayachinene.infra.product.persistence.ProductPO;
import org.ayachinene.infra.product.persistence.ProductSpecificationPO;
import org.ayachinene.infra.product.persistence.ProductSpecificationValuePO;
import org.ayachinene.infra.product.persistence.SkuPO;
import org.ayachinene.infra.product.persistence.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.StockPO;

import java.util.List;

public record ProductCreationPos(
    ProductPO product,
    List<ProductGalleryImagePO> productGalleryImages,
    List<ProductSpecificationPO> productSpecifications,
    List<ProductSpecificationValuePO> productSpecificationValues,
    List<SkuPO> skus,
    List<SkuSpecificationSelectionPO> skuSpecificationSelections,
    List<StockPO> stocks
) {
}
