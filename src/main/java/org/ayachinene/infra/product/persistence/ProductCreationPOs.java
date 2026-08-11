package org.ayachinene.infra.product.persistence;

import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationValuePO;

import java.util.List;

public record ProductCreationPOs(
        ProductPO product,
        List<ProductGalleryImagePO> galleryImages,
        List<SpecificationPO> specifications,
        List<SpecificationValuePO> specificationValues,
        List<SkuPO> skus,
        List<SkuSpecificationSelectionPO> selections
) {
}
