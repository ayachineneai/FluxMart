package org.ayachinene.app.file;

import org.ayachinene.app.file.domain.FilePurpose;
import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.domain.Files;
import org.ayachinene.app.file.upload.FileUploadDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilesTest {

    @Test
    void createsUploadingFileFromDefinition() {
        var file = Files.create(new FileUploadDefinition(
                "product-images/",
                "black-shirt.png",
                "image/png",
                1024L,
                FilePurpose.PRODUCT_IMAGE
        ));

        assertEquals(FilePurpose.PRODUCT_IMAGE, file.purpose());
        assertEquals(FileStatus.UPLOADING, file.status());
        assertEquals("black-shirt.png", file.originalFilename());
        assertEquals("image/png", file.contentType());
        assertEquals(1024L, file.sizeInBytes());
        assertTrue(file.objectKey().startsWith("product-images/"));
        assertTrue(file.objectKey().endsWith(file.fileId().toString()));
    }
}
