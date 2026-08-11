package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.product.domain.Product;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.product.domain.sku.SpecificationSelection;
import org.ayachinene.app.product.domain.specification.Specification;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.app.product.domain.specification.SpecificationValue;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.infra.product.persistence.converter.ProductPersistenceConverter;
import org.ayachinene.infra.product.persistence.converter.SkuPersistenceConverter;
import org.ayachinene.infra.product.persistence.converter.SpecificationPersistenceConverter;
import org.ayachinene.infra.product.persistence.sku.SkuMapper;
import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionMapper;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationMapper;
import org.ayachinene.infra.product.persistence.specification.SpecificationPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationValueMapper;
import org.ayachinene.infra.product.persistence.specification.SpecificationValuePO;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductCreationWriterTest {

    @Test
    @SuppressWarnings("unchecked")
    void preparesAndInsertsTheCompleteProductCreation() {
        var productMapper = mock(ProductMapper.class);
        var galleryMapper = mock(ProductGalleryImageMapper.class);
        var specificationMapper = mock(SpecificationMapper.class);
        var specificationValueMapper = mock(SpecificationValueMapper.class);
        var skuMapper = mock(SkuMapper.class);
        var selectionMapper = mock(SkuSpecificationSelectionMapper.class);

        var specificationCode = SpecificationCode.generate();
        var blackCode = SpecificationValueCode.generate();
        var whiteCode = SpecificationValueCode.generate();
        var specification = new Specification(
                specificationCode,
                "颜色",
                SpecificationStatus.ENABLED,
                List.of(
                        new SpecificationValue(
                                blackCode,
                                "黑色",
                                SpecificationStatus.ENABLED
                        ),
                        new SpecificationValue(
                                whiteCode,
                                "白色",
                                SpecificationStatus.ENABLED
                        )
                )
        );
        var sku = new Sku(
                SkuCode.generate(),
                "SKU-BLACK",
                SkuStatus.ENABLED,
                new Money(new BigDecimal("99.00")),
                null,
                List.of(new SpecificationSelection(
                        specificationCode,
                        blackCode
                ))
        );
        var firstImage = UUID7s.generate();
        var secondImage = UUID7s.generate();
        var product = new Product(
                ProductCode.generate(),
                ProductStatus.DRAFT,
                "T-Shirt",
                null,
                "Description",
                new CategoryCode("TSHIRT"),
                UUID7s.generate(),
                List.of(firstImage, secondImage)
        );

        writer(
                productMapper,
                galleryMapper,
                specificationMapper,
                specificationValueMapper,
                skuMapper,
                selectionMapper
        ).insert(new ProductCreation(
                product,
                List.of(specification),
                List.of(sku)
        ));

        var productCaptor = ArgumentCaptor.forClass(ProductPO.class);
        verify(productMapper).insert(productCaptor.capture());
        var savedProduct = productCaptor.getValue();
        assertNotNull(savedProduct.getId());
        assertEquals(product.productCode(), savedProduct.getProductCode());
        assertEquals(0L, savedProduct.getVersion());

        var galleryCaptor = ArgumentCaptor.forClass(List.class);
        verify(galleryMapper).insertBatch(galleryCaptor.capture());
        var savedImages = galleryCaptor.getValue();
        var savedFirstImage = (ProductGalleryImagePO) savedImages.get(0);
        var savedSecondImage = (ProductGalleryImagePO) savedImages.get(1);
        assertNotNull(savedFirstImage.getId());
        assertNotNull(savedSecondImage.getId());
        assertEquals(savedProduct.getId(), savedFirstImage.getProductId());
        assertEquals(firstImage, savedFirstImage.getFileId());
        assertEquals(0, savedFirstImage.getSortOrder());
        assertEquals(secondImage, savedSecondImage.getFileId());
        assertEquals(1, savedSecondImage.getSortOrder());

        var specificationCaptor = ArgumentCaptor.forClass(List.class);
        verify(specificationMapper).insertBatch(specificationCaptor.capture());
        var savedSpecification = (SpecificationPO) specificationCaptor
                .getValue()
                .getFirst();
        assertNotNull(savedSpecification.getId());
        assertEquals(savedProduct.getId(), savedSpecification.getProductId());
        assertEquals(specificationCode, savedSpecification.getSpecificationCode());

        var valueCaptor = ArgumentCaptor.forClass(List.class);
        verify(specificationValueMapper).insertBatch(valueCaptor.capture());
        var savedValues = valueCaptor.getValue();
        var savedBlack = (SpecificationValuePO) savedValues.getFirst();
        assertNotNull(savedBlack.getId());
        assertEquals(savedSpecification.getId(), savedBlack.getSpecificationId());
        assertEquals(blackCode, savedBlack.getSpecificationValueCode());

        var skuCaptor = ArgumentCaptor.forClass(List.class);
        verify(skuMapper).insertBatch(skuCaptor.capture());
        var savedSku = (SkuPO) skuCaptor.getValue().getFirst();
        assertNotNull(savedSku.getId());
        assertEquals(savedProduct.getId(), savedSku.getProductId());
        assertEquals(sku.skuCode(), savedSku.getSkuCode());

        var selectionCaptor = ArgumentCaptor.forClass(List.class);
        verify(selectionMapper).insertBatch(selectionCaptor.capture());
        var savedSelection = (SkuSpecificationSelectionPO) selectionCaptor
                .getValue()
                .getFirst();
        assertNotNull(savedSelection.getId());
        assertEquals(savedSku.getId(), savedSelection.getSkuId());
        assertEquals(savedSpecification.getId(), savedSelection.getSpecificationId());
        assertEquals(savedBlack.getId(), savedSelection.getSpecificationValueId());
    }

    private ProductCreationWriter writer(
            ProductMapper productMapper,
            ProductGalleryImageMapper galleryMapper,
            SpecificationMapper specificationMapper,
            SpecificationValueMapper specificationValueMapper,
            SkuMapper skuMapper,
            SkuSpecificationSelectionMapper selectionMapper
    ) {
        return new ProductCreationWriter(
                productMapper,
                galleryMapper,
                specificationMapper,
                specificationValueMapper,
                skuMapper,
                selectionMapper,
                Mappers.getMapper(ProductPersistenceConverter.class),
                Mappers.getMapper(SpecificationPersistenceConverter.class),
                Mappers.getMapper(SkuPersistenceConverter.class)
        );
    }
}
