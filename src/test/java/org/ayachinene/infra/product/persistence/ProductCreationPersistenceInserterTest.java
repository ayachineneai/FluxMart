package org.ayachinene.infra.product.persistence;

import org.ayachinene.infra.product.persistence.sku.SkuMapper;
import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionMapper;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationMapper;
import org.ayachinene.infra.product.persistence.specification.SpecificationPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationValueMapper;
import org.ayachinene.infra.product.persistence.specification.SpecificationValuePO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductCreationPersistenceInserterTest {

    @Test
    void insertsEveryPreparedPo() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        var specificationMapper = mock(SpecificationMapper.class);
        var specificationValueMapper = mock(SpecificationValueMapper.class);
        var skuMapper = mock(SkuMapper.class);
        var selectionMapper = mock(SkuSpecificationSelectionMapper.class);

        var product = new ProductPO();
        var galleryImages = List.of(new ProductGalleryImagePO());
        var specifications = List.of(new SpecificationPO());
        var specificationValues = List.of(new SpecificationValuePO());
        var skus = List.of(new SkuPO());
        var selections = List.of(new SkuSpecificationSelectionPO());

        new ProductCreationPersistenceInserter(
                productMapper,
                galleryMapper,
                specificationMapper,
                specificationValueMapper,
                skuMapper,
                selectionMapper
        ).insert(new ProductCreationPOs(
                product,
                galleryImages,
                specifications,
                specificationValues,
                skus,
                selections
        ));

        verify(productMapper).insert(product);
        verify(galleryMapper).insertBatch(galleryImages);
        verify(specificationMapper).insertBatch(specifications);
        verify(specificationValueMapper).insertBatch(specificationValues);
        verify(skuMapper).insertBatch(skus);
        verify(selectionMapper).insertBatch(selections);
    }
}
