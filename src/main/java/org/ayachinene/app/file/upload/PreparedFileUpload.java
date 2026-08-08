package org.ayachinene.app.file.upload;

import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.app.file.storage.UploadAuthorization;
import org.ayachinene.shared.uuid7.UUID7;

import java.time.OffsetDateTime;

public record PreparedFileUpload(
        UUID7 fileId,
        FileStatus status,
        UploadAuthorization authorization,
        OffsetDateTime expiresAt
) {
}
