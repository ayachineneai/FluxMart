package org.ayachinene.app.file;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.file.domain.FileValidator;
import org.ayachinene.app.file.upload.PrepareProductImageUploadInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileValidatorTest {

    @Test
    void validatesAndNormalizesProductImageInput() {
        var input = FileValidator.productImage(
                new PrepareProductImageUploadInput(
                        "  black-shirt.png  ",
                        "  IMAGE/PNG  ",
                        1024L
                )
        );

        assertEquals("black-shirt.png", input.filename());
        assertEquals("image/png", input.contentType());
        assertEquals(1024L, input.sizeInBytes());
    }

    @Test
    void rejectsPathAsFilename() {
        assertThrows(
                ValidationException.class,
                () -> FileValidator.productImage(
                        new PrepareProductImageUploadInput(
                                "product/black-shirt.png",
                                "image/png",
                                1024L
                        )
                )
        );
    }

    @Test
    void rejectsUnsupportedContentType() {
        assertThrows(
                ValidationException.class,
                () -> FileValidator.productImage(
                        new PrepareProductImageUploadInput(
                                "product.gif",
                                "image/gif",
                                1024L
                        )
                )
        );
    }

    @Test
    void rejectsEmptyOrOversizedProductImage() {
        assertThrows(
                ValidationException.class,
                () -> FileValidator.productImage(
                        new PrepareProductImageUploadInput(
                                "product.png",
                                "image/png",
                                0L
                        )
                )
        );
        assertThrows(
                ValidationException.class,
                () -> FileValidator.productImage(
                        new PrepareProductImageUploadInput(
                                "product.png",
                                "image/png",
                                10L * 1024 * 1024 + 1
                        )
                )
        );
    }
}
