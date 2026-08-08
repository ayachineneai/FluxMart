package org.ayachinene.app.file.productimage;

public record PrepareProductImageUploadInput(
        String filename,
        String contentType,
        Long sizeInBytes
) {
}
