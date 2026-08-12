package org.ayachinene.api.file;

import org.ayachinene.api.file.data.ConfirmFileUploadResponse;
import org.ayachinene.app.file.upload.ConfirmedFileUpload;
import org.ayachinene.shared.uuid7.UUID7;
import org.springframework.stereotype.Component;

@Component
public class FileApiMapper {

    public UUID7 toFileId(String value) {
        return UUID7.fromString(value, "fileId");
    }

    public ConfirmFileUploadResponse toResponse(ConfirmedFileUpload result) {
        return new ConfirmFileUploadResponse(
                result.fileId().toString(),
                result.status().name()
        );
    }
}
