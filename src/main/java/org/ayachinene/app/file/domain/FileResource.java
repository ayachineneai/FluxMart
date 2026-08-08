package org.ayachinene.app.file.domain;

import org.ayachinene.shared.uuid7.UUID7;

public record FileResource(
        UUID7 fileId,
        String objectKey,
        String originalFilename,
        String contentType,
        long sizeInBytes,
        FilePurpose purpose,
        FileStatus status
) {
}
