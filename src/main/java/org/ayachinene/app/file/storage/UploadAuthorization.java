package org.ayachinene.app.file.storage;

public record UploadAuthorization(
        String url,
        UploadFormFields fields
) {
}
