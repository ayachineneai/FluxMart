package org.ayachinene.app.file.upload;

import org.ayachinene.app.file.domain.FilePurpose;

public record FileUploadDefinition(
        String objectKeyPrefix,
        String originalFilename,
        String contentType,
        long sizeInBytes,
        FilePurpose purpose
) {
}
