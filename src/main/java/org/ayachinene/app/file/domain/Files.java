package org.ayachinene.app.file.domain;

import org.ayachinene.app.file.upload.PrepareProductImageUploadInput;
import org.ayachinene.app.file.storage.StoredObject;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;

public final class Files {

    private static final String PRODUCT_IMAGE_PREFIX = "product-images/";

    private Files() {
    }

    public static FileResource createProductImageResource(
            PrepareProductImageUploadInput input
    ) {
        var validatedInput = FileValidator.productImage(input);
        var fileId = UUID7s.generate();
        return new FileResource(
                fileId,
                productImageObjectKey(fileId),
                validatedInput.filename(),
                validatedInput.contentType(),
                validatedInput.sizeInBytes(),
                FilePurpose.PRODUCT_IMAGE,
                FileStatus.UPLOADING
        );
    }

    private static String productImageObjectKey(UUID7 fileId) {
        return PRODUCT_IMAGE_PREFIX + fileId;
    }

    public static FileResource confirmUpload(
            FileResource file,
            StoredObject storedObject
    ) {
        if (file.status() != FileStatus.UPLOADING) {
            throw cannotConfirm(file, "status must be UPLOADING");
        }
        if (file.sizeInBytes() != storedObject.sizeInBytes()) {
            throw cannotConfirm(file, "uploaded object size does not match");
        }
        if (!file.contentType().equals(storedObject.contentType())) {
            throw cannotConfirm(file, "uploaded object content type does not match");
        }
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

    private static FileUploadCannotBeConfirmedException cannotConfirm(
            FileResource file,
            String reason
    ) {
        return new FileUploadCannotBeConfirmedException(file.fileId(), reason);
    }
}
