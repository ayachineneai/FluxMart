package org.ayachinene.app.file.domain;

import org.ayachinene.shared.uuid7.UUID7;

public class FileUploadCannotBeConfirmedException extends RuntimeException {

    public FileUploadCannotBeConfirmedException(
            UUID7 fileId,
            String reason
    ) {
        super(
                "File upload cannot be confirmed: fileId="
                        + fileId
                        + ", reason="
                        + reason
        );
    }
}
