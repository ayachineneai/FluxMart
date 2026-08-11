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
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductCreationPersistenceConverterTest {

    @Test
    void convertsTheCompleteProductCreationToInsertablePos() {
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

        var pos = converter().toPos(new ProductCreation(
                product,
                List.of(specification),
                List.of(sku)
        ));

        var productPo = pos.product();
        assertNotNull(productPo.getId());
        assertEquals(product.productCode(), productPo.getProductCode());
        assertEquals(0L, productPo.getVersion());

        var firstImagePo = pos.galleryImages().get(0);
        var secondImagePo = pos.galleryImages().get(1);
        assertNotNull(firstImagePo.getId());
        assertNotNull(secondImagePo.getId());
        assertEquals(productPo.getId(), firstImagePo.getProductId());
        assertEquals(firstImage, firstImagePo.getFileId());
        assertEquals(0, firstImagePo.getSortOrder());
        assertEquals(secondImage, secondImagePo.getFileId());
        assertEquals(1, secondImagePo.getSortOrder());

        var specificationPo = pos.specifications().getFirst();
        assertNotNull(specificationPo.getId());
        assertEquals(productPo.getId(), specificationPo.getProductId());
        assertEquals(specificationCode, specificationPo.getSpecificationCode());

        var blackPo = pos.specificationValues().getFirst();
        assertNotNull(blackPo.getId());
        assertEquals(specificationPo.getId(), blackPo.getSpecificationId());
        assertEquals(blackCode, blackPo.getSpecificationValueCode());

        var skuPo = pos.skus().getFirst();
        assertNotNull(skuPo.getId());
        assertEquals(productPo.getId(), skuPo.getProductId());
        assertEquals(sku.skuCode(), skuPo.getSkuCode());

        var selectionPo = pos.selections().getFirst();
        assertNotNull(selectionPo.getId());
        assertEquals(skuPo.getId(), selectionPo.getSkuId());
        assertEquals(specificationPo.getId(), selectionPo.getSpecificationId());
        assertEquals(blackPo.getId(), selectionPo.getSpecificationValueId());
    }

    private ProductCreationPersistenceConverter converter() {
        return new ProductCreationPersistenceConverter(
                Mappers.getMapper(ProductPersistenceConverter.class),
                Mappers.getMapper(SpecificationPersistenceConverter.class),
                Mappers.getMapper(SkuPersistenceConverter.class)
        );
    }
}
