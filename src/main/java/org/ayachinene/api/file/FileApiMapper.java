package org.ayachinene.api.file;

import org.ayachinene.api.file.data.PrepareProductImageUploadRequest;
import org.ayachinene.api.file.data.PrepareProductImageUploadResponse;
import org.ayachinene.api.file.data.ConfirmFileUploadResponse;
import org.ayachinene.app.file.productimage.PrepareProductImageUploadInput;
import org.ayachinene.app.file.upload.PreparedFileUpload;
import org.ayachinene.app.file.storage.UploadAuthorization;
import org.ayachinene.app.file.upload.ConfirmedFileUpload;
import org.ayachinene.shared.uuid7.UUID7;
import org.springframework.stereotype.Component;

@Component
public class FileApiMapper {

    public PrepareProductImageUploadInput toInput(
            PrepareProductImageUploadRequest request
    ) {
        return new PrepareProductImageUploadInput(
                request.filename(),
                request.contentType(),
                request.sizeInBytes()
        );
    }

    public PrepareProductImageUploadResponse toResponse(
            PreparedFileUpload result
    ) {
        return new PrepareProductImageUploadResponse(
                result.fileId().toString(),
                result.status().name(),
                uploadInstruction(result.authorization()),
                result.expiresAt()
        );
    }

    public UUID7 toFileId(String value) {
        return UUID7.fromString(value, "fileId");
    }

    public ConfirmFileUploadResponse toResponse(ConfirmedFileUpload result) {
        return new ConfirmFileUploadResponse(
                result.fileId().toString(),
                result.status().name()
        );
    }

    private PrepareProductImageUploadResponse.UploadInstruction uploadInstruction(
            UploadAuthorization authorization
    ) {
        return new PrepareProductImageUploadResponse.UploadInstruction(
                authorization.url(),
                new PrepareProductImageUploadResponse.UploadFields(
                        authorization.fields().key(),
                        authorization.fields().contentType(),
                        authorization.fields().successActionStatus(),
                        authorization.fields().forbidOverwrite(),
                        authorization.fields().policy(),
                        authorization.fields().signatureVersion(),
                        authorization.fields().credential(),
                        authorization.fields().date(),
                        authorization.fields().signature()
                )
        );
    }
}
