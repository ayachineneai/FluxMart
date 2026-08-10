package org.ayachinene.app.file.domain;

import org.ayachinene.app.file.storage.StoredObject;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;

public final class Files {

    private Files() {
    }

    public static FileResource createForUpload(NewFileResource newFile) {
        var fileId = UUID7s.generate();
        return new FileResource(
                fileId,
                objectKey(newFile.objectKeyPrefix(), fileId),
                newFile.originalFilename(),
                newFile.contentType(),
                newFile.sizeInBytes(),
                newFile.purpose(),
                FileStatus.UPLOADING
        );
    }

    private static String objectKey(String prefix, UUID7 fileId) {
        return prefix + fileId;
    }

    public static FileResource confirmUpload(
            FileResource file,
            StoredObject storedObject
    ) {
        checkForConfirm(file);
        if (file.sizeInBytes() != storedObject.sizeInBytes()) {
            throw cannotConfirm(file, "uploaded object size does not match");
        }
        if (!file.contentType().equals(storedObject.contentType())) {
            throw cannotConfirm(file, "uploaded object content type does not match");
        }
        return markAvailable(file);
    }

    private static FileResource markAvailable(FileResource file) {
        return new FileResource(
                file.fileId(),
                file.objectKey(),
                file.originalFilename(),
                file.contentType(),
                file.sizeInBytes(),
                file.purpose(),
                FileStatus.AVAILABLE
        );
    }

    public static void checkForConfirm(FileResource file) {
        if (file.status() != FileStatus.UPLOADING) {
            throw cannotConfirm(file, "status must be UPLOADING");
        }
    }

    private static FileUploadCannotBeConfirmedException cannotConfirm(
            FileResource file,
            String reason
    ) {
        return new FileUploadCannotBeConfirmedException(file.fileId(), reason);
    }
}
