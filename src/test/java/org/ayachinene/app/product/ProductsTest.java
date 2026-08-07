package org.ayachinene.app.product;

import org.ayachinene.app.product.domain.*;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.ProductInput;
import org.ayachinene.app.product.creation.SelectionInput;
import org.ayachinene.app.product.creation.SkuInput;
import org.ayachinene.app.product.creation.SpecificationInput;
import org.ayachinene.app.product.publication.ProductPublicationState;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.ayachinene.app.exception.ValidationException;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductsTest {

    @Test
    void createsDraftWithoutMutatingInput() {
        var primaryImageFileId = fileId("018f6b5c-7c00-7000-8000-000000000010");
        var galleryImageFileId = fileId("018f6b5c-7c00-7000-8000-000000000011");
        var gallery = new ArrayList<>(List.of(galleryImageFileId));
        var productInput = new ProductInput(
                "  FluxMart T-Shirt  ",
                "  Soft  ",
                "  Cotton T-Shirt  ",
                new CategoryCode("TSHIRT"),
                primaryImageFileId,
                gallery
        );
        var input = new CreateProductInput(
                productInput,
                List.of(new SpecificationInput("颜色", List.of("黑色"))),
                List.of(new SkuInput(
                        "TSHIRT-BLACK",
                        new BigDecimal("99.00"),
                        null,
                        List.of(new SelectionInput("颜色", "黑色"))
                ))
        );

        var product = Products.create(input).product();

        assertTrue(product.productCode().value().startsWith("PRD_"));
        assertEquals(ProductStatus.DRAFT, product.status());
        assertEquals("FluxMart T-Shirt", product.title());
        assertEquals(List.of(galleryImageFileId), product.galleryImageFileIds());

        gallery.add(fileId("018f6b5c-7c00-7000-8000-000000000012"));
        assertEquals(List.of(galleryImageFileId), product.galleryImageFileIds());
    }

    @Test
    void rejectsDuplicateGalleryUrls() {
        var input = new ProductInput(
                "T-Shirt",
                null,
                "Cotton T-Shirt",
                new CategoryCode("TSHIRT"),
                fileId("018f6b5c-7c00-7000-8000-000000000010"),
                List.of(
                        fileId("018f6b5c-7c00-7000-8000-000000000011"),
                        fileId("018f6b5c-7c00-7000-8000-000000000011")
                )
        );

        assertThrows(
                ValidationException.class,
                () -> ProductValidator.validate(input)
        );
    }

    @Test
    void rejectsPublishingWithUnexpectedVersion() {
        var productCode = ProductCode.generate();
        var state = new ProductPublicationState(
                productCode,
                ProductStatus.DRAFT,
                4L
        );

        assertThrows(
                ProductVersionConflictException.class,
                () -> Products.publish(state, 3L, true)
        );
    }

    private static UUID7 fileId(String value) {
        return UUID7s.fromStringUnsafe(value);
    }

}
