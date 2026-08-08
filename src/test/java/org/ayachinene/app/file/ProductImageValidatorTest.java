package org.ayachinene.app.file;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.file.productimage.PrepareProductImageUploadInput;
import org.ayachinene.app.file.productimage.ProductImageValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductImageValidatorTest {

    @Test
    void validatesAndNormalizesProductImageInput() {
        var input = ProductImageValidator.validate(input(
                "  black-shirt.png  ",
                "  IMAGE/PNG  ",
                1024L
        ));

        assertEquals("black-shirt.png", input.filename());
        assertEquals("image/png", input.contentType());
        assertEquals(1024L, input.sizeInBytes());
    }

    @Test
    void rejectsPathAsFilename() {
        assertThrows(
                ValidationException.class,
                () -> ProductImageValidator.validate(input(
                        "product/black-shirt.png",
                        "image/png",
                        1024L
                ))
        );
    }

    @Test
    void rejectsUnsupportedContentType() {
        assertThrows(
                ValidationException.class,
                () -> ProductImageValidator.validate(input(
                        "product.gif",
                        "image/gif",
                        1024L
                ))
        );
    }

    @Test
    void rejectsEmptyOrOversizedProductImage() {
        assertThrows(
                ValidationException.class,
                () -> ProductImageValidator.validate(input(
                        "product.png",
                        "image/png",
                        0L
                ))
        );
        assertThrows(
                ValidationException.class,
                () -> ProductImageValidator.validate(input(
                        "product.png",
                        "image/png",
                        10L * 1024 * 1024 + 1
                ))
        );
    }

    private PrepareProductImageUploadInput input(
            String filename,
            String contentType,
            Long sizeInBytes
    ) {
        return new PrepareProductImageUploadInput(
                filename,
                contentType,
                sizeInBytes
        );
    }
}
