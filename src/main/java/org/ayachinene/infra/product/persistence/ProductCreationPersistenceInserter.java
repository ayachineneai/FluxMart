package org.ayachinene.infra.product.persistence;

import org.ayachinene.infra.product.persistence.sku.SkuMapper;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionMapper;
import org.ayachinene.infra.product.persistence.specification.SpecificationMapper;
import org.ayachinene.infra.product.persistence.specification.SpecificationValueMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductCreationPersistenceInserter {

    private final ProductMapper productMapper;
    private final ProductGalleryImageMapper galleryImageMapper;
    private final SpecificationMapper specificationMapper;
    private final SpecificationValueMapper specificationValueMapper;
    private final SkuMapper skuMapper;
    private final SkuSpecificationSelectionMapper selectionMapper;

    public ProductCreationPersistenceInserter(
            ProductMapper productMapper,
            ProductGalleryImageMapper galleryImageMapper,
            SpecificationMapper specificationMapper,
            SpecificationValueMapper specificationValueMapper,
            SkuMapper skuMapper,
            SkuSpecificationSelectionMapper selectionMapper
    ) {
        this.productMapper = productMapper;
        this.galleryImageMapper = galleryImageMapper;
        this.specificationMapper = specificationMapper;
        this.specificationValueMapper = specificationValueMapper;
        this.skuMapper = skuMapper;
        this.selectionMapper = selectionMapper;
    }

    public void insert(ProductCreationPOs pos) {
        productMapper.insert(pos.product());
        if (!pos.galleryImages().isEmpty()) {
            galleryImageMapper.insertBatch(pos.galleryImages());
        }
        if (!pos.specifications().isEmpty()) {
            specificationMapper.insertBatch(pos.specifications());
        }
        if (!pos.specificationValues().isEmpty()) {
            specificationValueMapper.insertBatch(pos.specificationValues());
        }
        if (!pos.skus().isEmpty()) {
            skuMapper.insertBatch(pos.skus());
        }
        if (!pos.selections().isEmpty()) {
            selectionMapper.insertBatch(pos.selections());
        }
    }
}
