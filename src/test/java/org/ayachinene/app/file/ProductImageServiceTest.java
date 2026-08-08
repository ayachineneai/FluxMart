package org.ayachinene.app.file;

import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.productimage.PrepareProductImageUploadInput;
import org.ayachinene.app.file.productimage.ProductImageService;
import org.ayachinene.app.file.upload.FileUploadDefinition;
import org.ayachinene.app.file.upload.FileUploadService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductImageServiceTest {

    @Test
    void convertsProductImageInputToGenericUploadDefinition() {
        var fileUploadService = mock(FileUploadService.class);
        var service = new ProductImageService(fileUploadService);

        service.prepareUpload(new PrepareProductImageUploadInput(
                "  black-shirt.png  ",
                "  IMAGE/PNG  ",
                1024L
        ));

        var definitionCaptor = ArgumentCaptor.forClass(
                FileUploadDefinition.class
        );
        verify(fileUploadService).prepareUpload(definitionCaptor.capture());
        var definition = definitionCaptor.getValue();
        assertEquals("product-images/", definition.objectKeyPrefix());
        assertEquals("black-shirt.png", definition.originalFilename());
        assertEquals("image/png", definition.contentType());
        assertEquals(1024L, definition.sizeInBytes());
        assertEquals(FilePurpose.PRODUCT_IMAGE, definition.purpose());
    }
}
