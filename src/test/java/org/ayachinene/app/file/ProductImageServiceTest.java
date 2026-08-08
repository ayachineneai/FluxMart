package org.ayachinene.app.file;

import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.domain.NewFileResource;
import org.ayachinene.app.file.productimage.PrepareProductImageUploadInput;
import org.ayachinene.app.file.productimage.ProductImageService;
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

        var newFileCaptor = ArgumentCaptor.forClass(
                NewFileResource.class
        );
        verify(fileUploadService).prepareUpload(newFileCaptor.capture());
        var newFile = newFileCaptor.getValue();
        assertEquals("product-images/", newFile.objectKeyPrefix());
        assertEquals("black-shirt.png", newFile.originalFilename());
        assertEquals("image/png", newFile.contentType());
        assertEquals(1024L, newFile.sizeInBytes());
        assertEquals(FilePurpose.PRODUCT_IMAGE, newFile.purpose());
    }
}
