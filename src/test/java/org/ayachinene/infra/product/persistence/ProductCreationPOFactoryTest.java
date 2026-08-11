package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductCreationPOFactoryTest {

    @Test
    void convertsInputDirectlyToInsertablePos() {
        var primaryImage = UUID7s.generate();
        var galleryImage = UUID7s.generate();
        var input = new CreateProductInput(
                "T-Shirt",
                null,
                "Cotton",
                new CategoryCode("TSHIRT"),
                primaryImage,
                List.of(galleryImage),
                List.of(new CreateProductInput.Specification(
                        "颜色",
                        List.of("黑色")
                )),
                List.of(new CreateProductInput.Sku(
                        "TSHIRT-BLACK",
                        new BigDecimal("99.00"),
                        null,
                        List.of(new CreateProductInput.Selection(
                                "颜色",
                                "黑色"
                        ))
                ))
        );

        var pos = new ProductCreationPOFactory().toPos(input);

        var product = pos.product();
        assertNotNull(product.getId());
        assertNotNull(product.getProductCode());
        assertEquals(ProductStatus.DRAFT, product.getStatus());
        assertEquals(primaryImage, product.getPrimaryImageFileId());

        var gallery = pos.galleryImages().getFirst();
        assertEquals(product.getId(), gallery.getProductId());
        assertEquals(galleryImage, gallery.getFileId());

        var specification = pos.specifications().getFirst();
        var value = pos.specificationValues().getFirst();
        assertEquals(ProductStatus.DRAFT, product.getStatus());
        assertEquals(SpecificationStatus.ENABLED, specification.getStatus());
        assertEquals(specification.getId(), value.getSpecificationId());

        var sku = pos.skus().getFirst();
        var selection = pos.selections().getFirst();
        assertEquals(SkuStatus.ENABLED, sku.getStatus());
        assertEquals(9900L, sku.getPriceAmount());
        assertEquals(sku.getId(), selection.getSkuId());
        assertEquals(specification.getId(), selection.getSpecificationId());
        assertEquals(value.getId(), selection.getSpecificationValueId());
        assertEquals(product.getCreatedAt(), gallery.getCreatedAt());
        assertEquals(product.getCreatedAt(), specification.getCreatedAt());
        assertEquals(product.getCreatedAt(), value.getCreatedAt());
        assertEquals(product.getCreatedAt(), sku.getCreatedAt());
        assertEquals(product.getCreatedAt(), selection.getCreatedAt());
    }

    @Test
    void associatesMultipleSkusWithTheirOwnSpecificationSelections() {
        var input = new CreateProductInput(
                "T-Shirt",
                null,
                "Cotton",
                new CategoryCode("TSHIRT"),
                UUID7s.generate(),
                List.of(),
                List.of(
                        new CreateProductInput.Specification(
                                "颜色",
                                List.of("黑色", "白色")
                        ),
                        new CreateProductInput.Specification(
                                "尺码",
                                List.of("M", "L")
                        )
                ),
                List.of(
                        sku("BLACK-M", "颜色", "黑色", "尺码", "M"),
                        sku("WHITE-L", "尺码", "L", "颜色", "白色")
                )
        );

        var pos = new ProductCreationPOFactory().toPos(input);
        var color = pos.specifications().stream()
                .filter(specification -> specification.getName().equals("颜色"))
                .findFirst()
                .orElseThrow();
        var size = pos.specifications().stream()
                .filter(specification -> specification.getName().equals("尺码"))
                .findFirst()
                .orElseThrow();
        var black = pos.specificationValues().stream()
                .filter(value -> value.getSpecificationId().equals(color.getId()))
                .filter(value -> value.getDisplayName().equals("黑色"))
                .findFirst()
                .orElseThrow();
        var white = pos.specificationValues().stream()
                .filter(value -> value.getSpecificationId().equals(color.getId()))
                .filter(value -> value.getDisplayName().equals("白色"))
                .findFirst()
                .orElseThrow();
        var medium = pos.specificationValues().stream()
                .filter(value -> value.getSpecificationId().equals(size.getId()))
                .filter(value -> value.getDisplayName().equals("M"))
                .findFirst()
                .orElseThrow();
        var large = pos.specificationValues().stream()
                .filter(value -> value.getSpecificationId().equals(size.getId()))
                .filter(value -> value.getDisplayName().equals("L"))
                .findFirst()
                .orElseThrow();
        var blackMedium = pos.skus().stream()
                .filter(sku -> sku.getMerchantSkuCode().equals("BLACK-M"))
                .findFirst()
                .orElseThrow();
        var whiteLarge = pos.skus().stream()
                .filter(sku -> sku.getMerchantSkuCode().equals("WHITE-L"))
                .findFirst()
                .orElseThrow();

        assertSelection(pos, blackMedium.getId(), color.getId(), black.getId());
        assertSelection(pos, blackMedium.getId(), size.getId(), medium.getId());
        assertSelection(pos, whiteLarge.getId(), color.getId(), white.getId());
        assertSelection(pos, whiteLarge.getId(), size.getId(), large.getId());
        assertEquals(4, pos.selections().size());
    }

    @Test
    void createsOneSkuWithoutSpecificationSelections() {
        var input = new CreateProductInput(
                "Gift Card",
                null,
                "Digital gift card",
                new CategoryCode("GIFT_CARD"),
                UUID7s.generate(),
                List.of(),
                List.of(),
                List.of(new CreateProductInput.Sku(
                        "GIFT-CARD",
                        new BigDecimal("100.00"),
                        null,
                        List.of()
                ))
        );

        var pos = new ProductCreationPOFactory().toPos(input);

        assertEquals(1, pos.skus().size());
        assertTrue(pos.specifications().isEmpty());
        assertTrue(pos.specificationValues().isEmpty());
        assertTrue(pos.selections().isEmpty());
    }

    private static CreateProductInput.Sku sku(
            String merchantSkuCode,
            String firstSpecification,
            String firstValue,
            String secondSpecification,
            String secondValue
    ) {
        return new CreateProductInput.Sku(
                merchantSkuCode,
                new BigDecimal("99.00"),
                null,
                List.of(
                        new CreateProductInput.Selection(
                                firstSpecification,
                                firstValue
                        ),
                        new CreateProductInput.Selection(
                                secondSpecification,
                                secondValue
                        )
                )
        );
    }

    private static void assertSelection(
            ProductCreationPOs pos,
            UUID7 skuId,
            UUID7 specificationId,
            UUID7 valueId
    ) {
        assertTrue(pos.selections().stream().anyMatch(selection ->
                selection.getSkuId().equals(skuId)
                        && selection.getSpecificationId().equals(specificationId)
                        && selection.getSpecificationValueId().equals(valueId)
        ));
    }
}
