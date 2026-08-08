package org.ayachinene.app.file.upload;

import org.ayachinene.app.file.domain.FileStatus;
import org.ayachinene.shared.uuid7.UUID7;

public record ConfirmedFileUpload(
        UUID7 fileId,
        FileStatus status
) {
}
