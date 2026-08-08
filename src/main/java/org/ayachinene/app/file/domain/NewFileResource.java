package org.ayachinene.app.file.domain;

public record NewFileResource(
        String objectKeyPrefix,
        String originalFilename,
        String contentType,
        long sizeInBytes,
        FilePurpose purpose
) {
}
