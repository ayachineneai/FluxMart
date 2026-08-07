package org.ayachinene.app.product;

import org.ayachinene.app.domain.product.ProductValidator;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.ayachinene.app.exception.ValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidatorTest {

    @Test
    void normalizesProductText() {
        assertEquals("商品标题", ProductValidator.title("  商品标题  "));
        assertEquals("商品副标题", ProductValidator.subtitle("  商品副标题  "));
        assertEquals("商品描述", ProductValidator.description("  商品描述  "));
        assertNull(ProductValidator.subtitle(null));
    }

    @Test
    void rejectsInvalidProductText() {
        assertThrows(ValidationException.class, () -> ProductValidator.title(" "));
        assertThrows(ValidationException.class, () -> ProductValidator.title("a".repeat(51)));
        assertThrows(ValidationException.class, () -> ProductValidator.subtitle("a".repeat(51)));
        assertThrows(ValidationException.class, () -> ProductValidator.description(null));
    }

    @Test
    void validatesImageFileIdsAndGalleryConstraints() {
        var fileId = UUID7s.fromStringUnsafe(
                "018f6b5c-7c00-7000-8000-000000000010"
        );

        assertEquals(fileId, ProductValidator.fileResourceId(fileId, "primaryImageFileId"));
        assertThrows(
                ValidationException.class,
                () -> ProductValidator.fileResourceId(null, "primaryImageFileId")
        );
        assertThrows(
                ValidationException.class,
                () -> ProductValidator.galleryImageFileIds(List.of(fileId, fileId))
        );
    }
}
