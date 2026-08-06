package org.ayachinene.app.product;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.*;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.ayachinene.app.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductsTest {

    @Test
    void createsTheSameDraftForTheSameInputsWithoutMutatingThem() {
        var primaryImageFileId = fileId("018f6b5c-7c00-7000-8000-000000000010");
        var galleryImageFileId = fileId("018f6b5c-7c00-7000-8000-000000000011");
        var gallery = new ArrayList<>(List.of(galleryImageFileId));
        var input = new CreateProductInput(
                "  FluxMart T-Shirt  ",
                "  Soft  ",
                "  Cotton T-Shirt  ",
                new CategoryCode("TSHIRT"),
                primaryImageFileId,
                gallery
        );
        var productCode = new ProductCode(UUID7s.fromStringUnsafe(
                "018f6b5c-7c00-7000-8000-000000000001"
        ));

        var validatedInput = ProductValidator.validate(input);
        var first = Products.mkProduct(productCode, validatedInput);
        var second = Products.mkProduct(productCode, validatedInput);

        assertEquals(first, second);
        assertNotSame(first, second);
        assertEquals(ProductStatus.DRAFT, first.status());
        assertEquals("FluxMart T-Shirt", first.title());
        assertEquals(List.of(galleryImageFileId), first.galleryImageFileIds());

        gallery.add(fileId("018f6b5c-7c00-7000-8000-000000000012"));
        assertEquals(List.of(galleryImageFileId), first.galleryImageFileIds());
    }

    @Test
    void rejectsDuplicateGalleryUrls() {
        var input = new CreateProductInput(
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

    private static FileResourceId fileId(String value) {
        return new FileResourceId(UUID7s.fromStringUnsafe(value));
    }

}
