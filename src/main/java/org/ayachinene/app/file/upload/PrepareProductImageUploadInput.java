package org.ayachinene.app.file.upload;

public record PrepareProductImageUploadInput(
        String filename,
        String contentType,
        Long sizeInBytes
) {
}
